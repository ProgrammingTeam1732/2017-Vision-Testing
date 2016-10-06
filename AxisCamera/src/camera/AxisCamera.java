package camera;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.imageio.ImageIO;

public class AxisCamera {
	
	private URL mainURL;
	private URL cgiURL;
	private URL mediaURL;
	private URL imageURL;
	// We should not directly get the jpg for image processing, it is much
	// slower than a video stream. I think openCV and probably other CV
	// libraries support using video streams for input.
	private URL bmpURL;
	private URL rtspURL;
	private URL mjpgURL;
	
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
	
	public AxisCamera(String ip) throws MalformedURLException {
		mainURL = new URL("http://" + ip);
		cgiURL = relativeURL(mainURL, "/axis-cgi");
		mediaURL = relativeURL(mainURL, "/axis-media");
		imageURL = relativeURL(cgiURL, "/jpg/image.cgi");
		rtspURL = relativeURL(mediaURL, "/media.amp");
		mjpgURL = relativeURL(cgiURL, "/mjpg/video.cgi");
		bmpURL = relativeURL(cgiURL, "/bitmap/image.bmp");
	}
	
	private URL relativeURL(URL base, String ending) throws MalformedURLException {
		return new URL(base.toString() + ending);
	}
	
	public URL getMainURL() {
		return mainURL;
	}
	
	public URL getCgiURL() {
		return cgiURL;
	}
	
	public URL getMediaURL() {
		return mediaURL;
	}
	
	public URL getImageURL() {
		return imageURL;
	}
	
	public URL getRtspURL() {
		return rtspURL;
	}
	
	public URL getMjpgURL() {
		return mjpgURL;
	}
	
	public URL getBmpURL() {
		return bmpURL;
	}
	
	// We should not directly get the jpg for image processing, it is much
	// slower than a video stream.
	public BufferedImage getBufferedImage() {
		try {
			return ImageIO.read(imageURL);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private String sendGet(URL url) throws IOException {
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		
		con.setRequestMethod("GET");
		
		int responseCode = con.getResponseCode();
		System.out.printf("Sending 'GET' request to URL : %s%n", url.toString());
		System.out.printf("Response Code : %d%n", responseCode);
		
		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();
		
		while ( (inputLine = in.readLine()) != null ) {
			System.out.println(inputLine);
			response.append(inputLine);
		}
		in.close();
		
		return response.toString();
	}
	
}