package opencv;

import java.awt.Color;

public class RGBPixel {
	private int red;
	private int green;
	private int blue;

	public RGBPixel(Color c) {
		red = c.getRed();
		green = c.getGreen();
		blue = c.getBlue();
	}

	public RGBPixel(int red, int green, int blue) {
		this.red = red;
		this.green = green;
		this.blue = blue;
	}

	public double[] getArray() {
		return new double[] { red, green, blue };
	}

	public int getRGB() {
		return new Color(red, green, blue).getRGB();
	}

	public Color getColor() {
		return new Color(red, green, blue);
	}

	public int getRed() {
		return red;
	}

	public void setRed(int red) {
		this.red = red;
	}

	public int getBlue() {
		return blue;
	}

	public void setBlue(int blue) {
		this.blue = blue;
	}

	public int getGreen() {
		return green;
	}

	public void setGreen(int green) {
		this.green = green;
	}

}