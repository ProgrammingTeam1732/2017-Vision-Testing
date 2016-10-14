package opencv;

import java.awt.Color;

public class RGBPixel {
	private int red;
	private int blue;
	private int green;

	public RGBPixel(Color c) {
		red = c.getRed();
		blue = c.getBlue();
		green = c.getGreen();
	}

	public RGBPixel(int red, int blue, int green) {
		this.red = red;
		this.blue = blue;
		this.green = green;
	}

	public double[] getArray() {
		return new double[] { red, blue, green };
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