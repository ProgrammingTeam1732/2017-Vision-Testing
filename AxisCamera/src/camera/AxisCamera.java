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

	public enum Resolution {
		r800x600("800x600"), r640x480("640x480"), r480x360("480x360"), r320x240("320x240"), r640x360(
				"640x360"), r640x400("640x400"), r352x240("352x240");

		public String value;

		private Resolution(String s) {
			value = s;
		}
	}

	private Resolution resolution = Resolution.r800x600;

	private int compression = 30;
	private int fps = 10;

	public enum WhiteBalance {
		auto, hold, fixed_outdoor, fixed_outdoor1, fixed_outdoor2, fixed_indoor, fixed_fluor, fixed_fluor1, fixed_fluor2;
		public static final String property = "root.ImageSource.I0.Sensor.WhiteBalance";
	}

	private WhiteBalance whiteBalance;

	private String USER = "root";
	private String PASS = "root";

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
		return fps * 1;
	}

	public WhiteBalance getWhiteBalance() {
		return whiteBalance;
	}

	public String setWhiteBalance(WhiteBalance whiteBalance) {
		return setParameter("root.ImageSource.I0.Sensor.WhiteBalance", whiteBalance.toString());
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

	public String listParameters(String group) {
		try {
			return sendGet(relativeURL(cgiURL, "/admin/param.cgi?action=list&group=" + group));
		} catch (IOException e) {
			System.err.println("Could not check parameters, sendGet() method failed");
			e.printStackTrace();
		}
		return null;
	}

	public String setParameter(String parameter, String value) {
		try {
			return sendGet(relativeURL(cgiURL, "/admin/param.cgi?action=update&" + parameter + "=" + value));
		} catch (IOException e) {
			System.err.println("Could not check parameters, sendGet() method failed");
			e.printStackTrace();
		}
		return null;
	}

	public String getUSER() {
		return USER;
	}

	public void setUSER(String uSER) {
		USER = uSER;
	}

	public String getPASS() {
		return PASS;
	}

	public void setPASS(String pASS) {
		PASS = pASS;
	}

	private String sendGet(URL url) throws IOException {
		HttpURLConnection con = (HttpURLConnection) url.openConnection();

		con.setRequestMethod("GET");
		String userCredentials = USER + ":" + PASS;
		String basicAuth = "Basic " + new String(Base64.getEncoder().encodeToString(userCredentials.getBytes()));
		con.setRequestProperty("Authorization", basicAuth);

		StringBuffer response = new StringBuffer();
		response.append(String.format("Sending 'GET' request to URL : %s%n", url.toString()));
		response.append(String.format("Response Code : %s%n", con.getResponseCode()));

		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String inputLine;

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();

		return response.toString();
	}

}