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
	// not recommended to get a single image at a time, if we use openCV or
	// something I am pretty sure we can open a video stream and use that, much
	// better speed/etc.
	private URL bmpURL;
	private URL rtspURL;
	private URL mjpgURL;
	
	public AxisCamera(String ip) throws MalformedURLException {
		// cannot make a relative url from another relative url, must use
		// absolute url for base or use the private relativeURL() method
		mainURL = new URL("http://" + ip);
		cgiURL = relativeURL(mainURL, "/axis-cgi");
		// backslashes at the end of URLs are automatically removed, don't try
		// to shorten code by doing that
		mediaURL = relativeURL(mainURL, "/axis-media");
		// would be okay to use the class URL's relative URL constructor in the
		// above case
		imageURL = relativeURL(cgiURL, "/jpg/image.cgi");
		System.out.println(imageURL);
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
	
	// Not recomended we use this for image processing
	public BufferedImage getBufferedImage() {
		try {
			return ImageIO.read(imageURL);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	private String sendGet(URL url) throws IOException {
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		
		// optional default is GET
		con.setRequestMethod("GET");
		
		int responseCode = con.getResponseCode();
		System.out.println("\nSending 'GET' request to URL : " + url);
		System.out.println("Response Code : " + responseCode);
		
		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();
		
		while ( (inputLine = in.readLine()) != null ) {
			response.append(inputLine);
		}
		in.close();
		
		// return result
		return response.toString();
		
	}
	
}