
package opencv;

import java.awt.Color;

public class RGBPixel {

	int[] rgb = new int[3];

	public RGBPixel(int red, int green, int blue) {
		rgb[0] = red;
		rgb[1] = green;
		rgb[2] = blue;
	}

	public RGBPixel(double red, double green, double blue) {
		rgb[0] = (int) red;
		rgb[1] = (int) green;
		rgb[2] = (int) blue;
	}

	public void setPixel(int red, int green, int blue) {
		rgb[0] = red;
		rgb[1] = green;
		rgb[2] = blue;
	}

	public void setPixel(double red, double green, double blue) {
		rgb[0] = (int) red;
		rgb[1] = (int) green;
		rgb[2] = (int) blue;
	}

	public void setPixel(Color c) {
		rgb[0] = c.getRed();
		rgb[1] = c.getGreen();
		rgb[2] = c.getBlue();
	}

	public int[] getPixel() {
		return new int[] { rgb[0], rgb[1], rgb[2] };
	}

	public int getRGBValue() {
		int rgbValue = rgb[0];
		rgbValue = (rgbValue << 8) + rgb[1];
		rgbValue = (rgbValue << 8) + rgb[2];
		return rgbValue;
	}

	public int getRed() {
		return rgb[0];
	}

	public void setRed(int red) {
		rgb[0] = red;
	}

	public int getGreen() {
		return rgb[1];
	}

	public void setGreen(int green) {
		rgb[1] = green;
	}

	public int getBlue() {
		return rgb[2];
	}

	public void setBlue(int blue) {
		rgb[2] = blue;
	}

	public void setBlack() {
		rgb = new int[] { 0, 0, 0 };
	}

	public void setWhite() {
		rgb = new int[] { 255, 255, 255 };
	}

}