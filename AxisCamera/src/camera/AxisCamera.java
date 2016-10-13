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

	// The following settings are communicated to the camera when getting the
	// image/stream or whatever
	// They could also be directly changed and saved on the camera
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

	// The following settings must be sent and saved on the camera
	public enum WhiteBalance {
		auto, hold, fixed_outdoor, fixed_outdoor1, fixed_outdoor2, fixed_indoor, fixed_fluor, fixed_fluor1, fixed_fluor2;
		public static final String property = "root.ImageSource.I0.Sensor.WhiteBalance";
	}

	private WhiteBalance whiteBalance;

	private int brightness = 50;
	public static final String BRIGHTNESS_PROPERTY = "root.ImageSource.I0.Sensor.Brightness";

	private int colorLevel = 50;
	public static final String COLOR_LEVEL_PROPERTY = "root.ImageSource.I0.Sensor.ColorLevel";

	private int contrast = 50;
	public static final String CONTRAST_PROPERTY = "root.ImageSource.I0.Sensor.Contrast";

	private int exposureValue = 50;
	public static final String EXPOSURE_VALUE_PROPERTY = "root.ImageSource.I0.Sensor.ExposureValue";

	private int sharpness = 50;
	public static final String SHARPNESS_PROPERTY = "root.ImageSource.I0.Sensor.Sharpness";

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
		return relativeURL(cgiURL, String.format("/mjpg/video.cgi%s&fps=%d", getImageArguements(), fps));
	}

	private String getImageArguements() {
		return String.format("?resolution=%s&compression=%s", resolution.value, compression);
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

	public String setWhiteBalance(WhiteBalance aWhiteBalance) {
		whiteBalance = aWhiteBalance;
		return setParameter(WhiteBalance.property, aWhiteBalance.toString());
	}

	public int getBrightness() {
		return brightness;
	}

	public String setBrightness(int aBrightness) {
		brightness = aBrightness < 0 ? 0 : aBrightness > 100 ? 100 : aBrightness;
		return setParameter(BRIGHTNESS_PROPERTY, Integer.toString(brightness));
	}

	public int getColorLevel() {
		return colorLevel;
	}

	public String setColorLevel(int aColorLevel) {
		colorLevel = aColorLevel < 0 ? 0 : aColorLevel > 100 ? 100 : aColorLevel;
		return setParameter(COLOR_LEVEL_PROPERTY, Integer.toString(colorLevel));
	}

	public int getContrast() {
		return contrast;
	}

	public String setContrast(int aContrast) {
		contrast = aContrast < 0 ? 0 : aContrast > 100 ? 100 : aContrast;
		return setParameter(CONTRAST_PROPERTY, Integer.toString(contrast));
	}

	public int getExposureValue() {
		return exposureValue;
	}

	public String setExposureValue(int aExposureValue) {
		exposureValue = aExposureValue < 0 ? 0 : aExposureValue > 100 ? 100 : aExposureValue;
		return setParameter(EXPOSURE_VALUE_PROPERTY, Integer.toString(exposureValue));
	}

	public int getSharpness() {
		return sharpness;
	}

	public String setSharpness(int aSharpness) {
		sharpness = aSharpness < 0 ? 0 : aSharpness > 100 ? 100 : aSharpness;
		return setParameter("root.ImageSource.I0.Sensor.ExposureValue", Integer.toString(sharpness));
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
			return sendGet(
					relativeURL(cgiURL, String.format("/admin/param.cgi?action=update&%s=%s", parameter, value)));
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