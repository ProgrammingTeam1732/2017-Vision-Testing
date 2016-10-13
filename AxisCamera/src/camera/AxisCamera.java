package camera;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Base64;

import javax.imageio.ImageIO;

public class AxisCamera {

	private URL mainURL;
	private URL cgiURL;
	private URL mediaURL;

	// A note about the URL(URL context, String spec) constructor:
	// This will be easier to show than explain
	// mainURL = new URL("http://" + ip); // mainURL is "http://ip"
	// cgiURL = new URL(mainURL, "/axis-cgi"); // cgiURL is "http://ip/axis-cgi"
	// imageURL = relativeURL(cgiURL, "/jpg/image.cgi"); // imageURL is
	// "http://ip/jpg/image.cgi"
	// TLDR: making a relative URL from a relative URL using the URL class's
	// constructor doesn't work
	// I know that seems strange, but it is the way it works, so if you want to
	// do this instead use the relative URL method

	// Another note about any URL constructor:
	// It seems that it will remove any forward slashes at the end, like this:
	// URL url = new URL("http://" + ip + "/"); // url is "http://ip"
	// Do not try to shorten the code by adding backslashes at the end of a URL
	// (making it shorter by saving backslashes in future relative URLs), they
	// will be removed

	private Resolution resolution = Resolution.r800x600;

	public enum Resolution {
		r800x600("800x600"), r640x480("640x480"), r480x360("480x360"), r320x240("320x240"), r640x360(
				"640x360"), r640x400("640x400"), r352x240("352x240");

		public String value;

		private Resolution(String s) {
			value = s;
		}
	}

	private int compression = 30;
	private int fps = 10;

	private static final String user = "root";
	private static final String pass = "root";

	public AxisCamera(String ip) throws MalformedURLException {
		mainURL = new URL("http://" + ip);
		cgiURL = relativeURL(mainURL, "/axis-cgi");
		mediaURL = relativeURL(mainURL, "/axis-media");
	}

	private URL relativeURL(URL base, String ending) {
		try {
			return new URL(base.toString() + ending);
		} catch (MalformedURLException e) {
			System.err.println("Cannot form relative URL, programmer screwed up the spelling of the ending");
			System.err.println("Ending: " + ending);
			e.printStackTrace();
			return null;
		}
	}

	public URL getBmpURL() {
		return relativeURL(cgiURL, "/bitmap/image.bmp" + getImageArguements());
	}

	public URL getImageURL() {
		return relativeURL(cgiURL, "/jpg/image.cgi" + getImageArguements());
	}

	public URL getMjpgURL() {
		return relativeURL(cgiURL, "/mjpg/video.cgi" + getImageArguements() + "&fps=" + fps);
	}

	private String getImageArguements() {
		return "?resolution=" + resolution.value + "&compression=" + compression;
	}

	public URL getRtspURL() {
		return relativeURL(mediaURL, "/media.amp");
	}

	public void setResolution(Resolution r) {
		resolution = r;
	}

	public Resolution getResolution() {
		return resolution;
	}

	public void setCompression(int aCompression) {
		// sets to 0 if less than 0, if greater than 0 checks if greater than
		// 100 then sets to 100, otherwise sets to aCompression
		compression = aCompression < 0 ? 0 : aCompression > 100 ? 100 : aCompression;
	}

	public int getCompression() {
		return compression;
	}

	public void setFPS(int aFPS) {
		// if less than 0 sets it to 0
		fps = aFPS < 0 ? 0 : aFPS;
	}

	public int getFPS() {
		return fps;
	}

	// We should not directly get the jpg for image processing, it is much
	// slower than a video stream.
	public BufferedImage getBufferedImage() {
		try {
			return ImageIO.read(getImageURL());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public String VAPIXVersion() {
		try {
			return sendGet(relativeURL(cgiURL, "/param.cgi?action=list&group=Properties.API.HTTP.Version"));
		} catch (IOException e) {
			System.err.println("Could not check VAPIX version, sendGet() method failed");
			e.printStackTrace();
		}
		return null;
	}

	public String supportedResolutions() {
		try {
			return sendGet(relativeURL(cgiURL, "/param.cgi?action=list&group=Properties.Image.Resolution"));
		} catch (IOException e) {
			System.err.println("Could not check supported resolutions, sendGet() method failed");
			e.printStackTrace();
		}
		return null;
	}

	public String supportedImageFormats() {
		try {
			return sendGet(relativeURL(cgiURL, "/param.cgi?action=list&group=Properties.Image.Format"));
		} catch (IOException e) {
			System.err.println("Could not check supported image formats, sendGet() method failed");
			e.printStackTrace();
		}
		return null;
	}

	public String sendGet(URL url) throws IOException {
		HttpURLConnection con = (HttpURLConnection) url.openConnection();

		con.setRequestMethod("GET");
		String userCredentials = user + ":" + pass;
		String basicAuth = "Basic " + new String(Base64.getEncoder().encodeToString(userCredentials.getBytes()));
		con.setRequestProperty("Authorization", basicAuth);

		int responseCode = con.getResponseCode();
		System.out.println("Sending 'GET' request to URL : " + url.toString());
		System.out.println("Response Code : " + responseCode);

		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();

		return response.toString();
	}

}