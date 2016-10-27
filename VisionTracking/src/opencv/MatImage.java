
package opencv;

import java.awt.Color;
import java.awt.image.BufferedImage;

import org.opencv.core.Mat;

public class MatImage {
	private RGBPixel[][] pixelArray;
	private BufferedImage bufferedImage;

	public MatImage(Mat mat) {
		pixelArray = new RGBPixel[mat.height()][mat.width()];
		double[] pixel;
		for (int row = 0; row < mat.height(); row++) {
			for (int col = 0; col < mat.width(); col++) {
				pixel = mat.get(row, col);
				pixelArray[row][col] = new RGBPixel(pixel[2], pixel[1], pixel[0]);
			}
		}
		bufferedImage = new BufferedImage(mat.width(), mat.height(), BufferedImage.TYPE_INT_RGB);
	}

	public void updatePixelArray(Mat mat) {
		double[] pixel;
		for (int row = 0; row < mat.height(); row++) {
			for (int col = 0; col < mat.width(); col++) {
				pixel = mat.get(row, col);
				pixelArray[row][col].setPixel(pixel[2], pixel[1], pixel[0]);
			}
		}
	}

	public BufferedImage getBufferedImage() {
		for (int row = 0; row < pixelArray.length; row++) {
			for (int col = 0; col < pixelArray[row].length; col++) {
				bufferedImage.setRGB(col, row, pixelArray[row][col].getRGBValue());
			}
		}
		return bufferedImage;
	}

	public void negate() {
		for (int row = 0; row < pixelArray.length; row++) {
			for (int col = 0; col < pixelArray[0].length; col++) {
				int[] pixel = pixelArray[row][col].getPixel();
				pixelArray[row][col].setPixel(255 - pixel[0], 255 - pixel[1], 255 - pixel[2]);
			}
		}
	}

	public void mirrorVertical(boolean negative) {
		if (!negative) {
			for (int row = 0; row < pixelArray.length; row++) {
				for (int col = 0; col < pixelArray[0].length / 2; col++) {
					pixelArray[row][pixelArray[0].length - col - 1].setRed(pixelArray[row][col].getRed());
					pixelArray[row][pixelArray[0].length - col - 1].setGreen(pixelArray[row][col].getGreen());
					pixelArray[row][pixelArray[0].length - col - 1].setBlue(pixelArray[row][col].getBlue());
				}
			}
		} else {
			for (int row = 0; row < pixelArray.length; row++) {
				for (int col = pixelArray[0].length - 1; col > pixelArray[0].length / 2; col--) {
					pixelArray[row][pixelArray[0].length - col].setRed(pixelArray[row][col].getRed());
					pixelArray[row][pixelArray[0].length - col].setGreen(pixelArray[row][col].getGreen());
					pixelArray[row][pixelArray[0].length - col].setBlue(pixelArray[row][col].getBlue());
				}
			}
		}
	}

	public void mirrorHorizontal(boolean negative) {
		if (!negative) {
			for (int row = 0; row < pixelArray.length / 2; row++) {
				for (int col = 0; col < pixelArray[0].length; col++) {
					pixelArray[pixelArray.length - 1 - row][col].setRed(pixelArray[row][col].getRed());
					pixelArray[pixelArray.length - 1 - row][col].setGreen(pixelArray[row][col].getGreen());
					pixelArray[pixelArray.length - 1 - row][col].setBlue(pixelArray[row][col].getBlue());
				}
			}
		} else {
			for (int row = pixelArray.length - 1; row > pixelArray.length / 2; row--) {
				for (int col = 0; col < pixelArray[0].length; col++) {
					pixelArray[pixelArray.length - row][col].setRed(pixelArray[row][col].getRed());
					pixelArray[pixelArray.length - row][col].setGreen(pixelArray[row][col].getGreen());
					pixelArray[pixelArray.length - row][col].setBlue(pixelArray[row][col].getBlue());
				}
			}
		}
	}

	public void pencilDrawing(int tolerance) {
		for (int row = 0; row < pixelArray.length - 1; row++) {
			for (int col = 0; col < pixelArray[0].length - 1; col++) {
				if (pixelArray[row][col].getRed() - pixelArray[row + 1][col].getRed() >= tolerance)
					pixelArray[row][col].setPixel(0, 0, 0);
				else if (pixelArray[row][col].getGreen() - pixelArray[row + 1][col].getGreen() >= tolerance)
					pixelArray[row][col].setPixel(0, 0, 0);
				else if (pixelArray[row][col].getBlue() - pixelArray[row + 1][col].getBlue() >= tolerance)
					pixelArray[row][col].setPixel(0, 0, 0);
				else if (pixelArray[row][col].getRed() - pixelArray[row][col + 1].getRed() >= tolerance)
					pixelArray[row][col].setPixel(0, 0, 0);
				else if (pixelArray[row][col].getGreen() - pixelArray[row][col + 1].getGreen() >= tolerance)
					pixelArray[row][col].setPixel(0, 0, 0);
				else if (pixelArray[row][col].getBlue() - pixelArray[row][col + 1].getBlue() >= tolerance)
					pixelArray[row][col].setPixel(0, 0, 0);
				else
					pixelArray[row][col].setPixel(255, 255, 255);
			}
		}
	}

	public void pencilDrawing(int tolerance, Color pencil, Color background) {
		for (int row = 0; row < pixelArray.length - 1; row++) {
			for (int col = 0; col < pixelArray[0].length - 1; col++) {
				if (pixelArray[row][col].getRed() - pixelArray[row + 1][col].getRed() >= tolerance)
					pixelArray[row][col].setPixel(pencil);
				else if (pixelArray[row][col].getGreen() - pixelArray[row + 1][col].getGreen() >= tolerance)
					pixelArray[row][col].setPixel(pencil);
				else if (pixelArray[row][col].getBlue() - pixelArray[row + 1][col].getBlue() >= tolerance)
					pixelArray[row][col].setPixel(pencil);
				else if (pixelArray[row][col].getRed() - pixelArray[row][col + 1].getRed() >= tolerance)
					pixelArray[row][col].setPixel(pencil);
				else if (pixelArray[row][col].getGreen() - pixelArray[row][col + 1].getGreen() >= tolerance)
					pixelArray[row][col].setPixel(pencil);
				else if (pixelArray[row][col].getBlue() - pixelArray[row][col + 1].getBlue() >= tolerance)
					pixelArray[row][col].setPixel(pencil);
				else
					pixelArray[row][col].setPixel(background);
			}
		}
	}

	public void increseEdgeSharpness(int tolerance) {
		for (int row = 0; row < pixelArray.length - 1; row++) {
			for (int col = 0; col < pixelArray[0].length - 1; col++) {
				if (pixelArray[row][col].getRed() - pixelArray[row + 1][col].getRed() >= tolerance)
					pixelArray[row][col].setPixel(0, 0, 0);
				else if (pixelArray[row][col].getGreen() - pixelArray[row + 1][col].getGreen() >= tolerance)
					pixelArray[row][col].setPixel(0, 0, 0);
				else if (pixelArray[row][col].getBlue() - pixelArray[row + 1][col].getBlue() >= tolerance)
					pixelArray[row][col].setPixel(0, 0, 0);
				else if (pixelArray[row][col].getRed() - pixelArray[row][col + 1].getRed() >= tolerance)
					pixelArray[row][col].setPixel(0, 0, 0);
				else if (pixelArray[row][col].getGreen() - pixelArray[row][col + 1].getGreen() >= tolerance)
					pixelArray[row][col].setPixel(0, 0, 0);
				else if (pixelArray[row][col].getBlue() - pixelArray[row][col + 1].getBlue() >= tolerance)
					pixelArray[row][col].setPixel(0, 0, 0);
				// else image[row][col].setPixel(255, 255, 255);
			}
		}
	}

	public void keepRed() {
		for (int row = 0; row < pixelArray.length; row++) {
			for (int col = 0; col < pixelArray[0].length; col++) {
				pixelArray[row][col].setPixel(pixelArray[row][col].getRed(), 0, 0);
			}
		}
	}

	public void keepBlue() {
		for (int row = 0; row < pixelArray.length; row++) {
			for (int col = 0; col < pixelArray[0].length; col++) {
				pixelArray[row][col].setPixel(0, 0, pixelArray[row][col].getBlue());
			}
		}
	}

	public void keepGreen() {
		for (int row = 0; row < pixelArray.length; row++) {
			for (int col = 0; col < pixelArray[0].length; col++) {
				pixelArray[row][col].setPixel(0, pixelArray[row][col].getGreen(), 0);
			}
		}
	}

	public void grayscale() {
		int avg = 0;
		for (int row = 0; row < pixelArray.length; row++) {
			for (int col = 0; col < pixelArray[0].length; col++) {
				avg = (pixelArray[row][col].getGreen() + pixelArray[row][col].getRed() + pixelArray[row][col].getBlue())
						/ 3;
				pixelArray[row][col].setPixel(avg, avg, avg);
			}
		}
	}

	public void zeroGreen() {
		for (int row = 0; row < pixelArray.length; row++) {
			for (int col = 0; col < pixelArray[0].length; col++) {
				pixelArray[row][col].setPixel(pixelArray[row][col].getRed(), 0, pixelArray[row][col].getBlue());
			}
		}
	}

	public void zeroRed() {
		for (int row = 0; row < pixelArray.length; row++) {
			for (int col = 0; col < pixelArray[0].length; col++) {
				pixelArray[row][col].setPixel(0, pixelArray[row][col].getGreen(), pixelArray[row][col].getBlue());
			}
		}
	}

	public void zeroBlue() {
		for (int row = 0; row < pixelArray.length; row++) {
			for (int col = 0; col < pixelArray[0].length; col++) {
				pixelArray[row][col].setPixel(pixelArray[row][col].getRed(), pixelArray[row][col].getGreen(), 0);
			}
		}
	}

	public void switchBlueRedGreen() {
		for (int row = 0; row < pixelArray.length; row++) {
			for (int col = 0; col < pixelArray[0].length; col++) {
				pixelArray[row][col].setPixel(pixelArray[row][col].getBlue(), pixelArray[row][col].getRed(),
						pixelArray[row][col].getGreen());
			}
		}
	}

	public void switchGreenBlueRed() {
		for (int row = 0; row < pixelArray.length; row++) {
			for (int col = 0; col < pixelArray[0].length; col++) {
				pixelArray[row][col].setPixel(pixelArray[row][col].getGreen(), pixelArray[row][col].getBlue(),
						pixelArray[row][col].getRed());
			}
		}
	}

	public void switchBlueGreenRed() {
		for (int row = 0; row < pixelArray.length; row++) {
			for (int col = 0; col < pixelArray[0].length; col++) {
				pixelArray[row][col].setPixel(pixelArray[row][col].getBlue(), pixelArray[row][col].getGreen(),
						pixelArray[row][col].getRed());
			}
		}
	}

	public void sepia() {
		for (int row = 0; row < pixelArray.length; row++) {
			for (int col = 0; col < pixelArray[0].length; col++) {
				double red = (pixelArray[row][col].getPixel()[0] * .393) + (pixelArray[row][col].getPixel()[1] * .769)
						+ (pixelArray[row][col].getPixel()[2] * .189);
				double green = (pixelArray[row][col].getPixel()[0] * .349) + (pixelArray[row][col].getPixel()[1] * .686)
						+ (pixelArray[row][col].getPixel()[2] * .168);
				double blue = (pixelArray[row][col].getPixel()[0] * .272) + (pixelArray[row][col].getPixel()[1] * .534)
						+ (pixelArray[row][col].getPixel()[2] * .132);
				if (red > 255)
					red = 255;
				if (green > 255)
					green = 255;
				if (blue > 255)
					blue = 255;
				pixelArray[row][col].setPixel((int) red, (int) green, (int) blue);
			}
		}
	}

	public void highlightRed(int tolerance) {
		for (int row = 0; row < pixelArray.length; row++) {
			for (int col = 0; col < pixelArray[0].length; col++) {
				if (pixelArray[row][col].getRed() - pixelArray[row][col].getBlue() > tolerance
						&& pixelArray[row][col].getRed() - pixelArray[row][col].getGreen() > tolerance)
					pixelArray[row][col].setWhite();
				else
					pixelArray[row][col].setBlack();
			}
		}
	}

	public void highlightGreen(int tolerance) {
		for (int row = 0; row < pixelArray.length - 1; row++) {
			for (int col = 0; col < pixelArray[0].length; col++) {
				if (pixelArray[row][col].getGreen() - pixelArray[row][col].getBlue() > tolerance
						&& pixelArray[row][col].getGreen() - pixelArray[row][col].getBlue() > tolerance)
					pixelArray[row][col].setWhite();
				else
					pixelArray[row][col].setBlack();
			}
		}
	}

	public void highlightBlue(int tolerance) {
		for (int row = 0; row < pixelArray.length - 1; row++) {
			for (int col = 0; col < pixelArray[0].length; col++) {
				if (pixelArray[row][col].getBlue() - pixelArray[row][col].getRed() > tolerance
						&& pixelArray[row][col].getBlue() - pixelArray[row][col].getGreen() > tolerance)
					pixelArray[row][col].setWhite();
				else
					pixelArray[row][col].setBlack();
			}
		}
	}
}