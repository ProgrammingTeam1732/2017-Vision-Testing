package opencv;

import java.awt.image.BufferedImage;

import org.opencv.core.Mat;

import opencv.BoxFinder.BoundingBox;

public class MatImage {
	private BufferedImage	bufferedImage;
	private int[][][]		bufferedArray;
	private BufferedImage	bitmapImage;
	private int[][]			bitmapArray;
	private BoxFinder		finder;

	public MatImage(Mat mat) {
		bufferedImage = new BufferedImage(mat.width(), mat.height(), BufferedImage.TYPE_INT_RGB);
		bufferedArray = new int[mat.height()][mat.width()][3];
		bitmapImage = new BufferedImage(mat.width(), mat.height(), BufferedImage.TYPE_INT_RGB);
		bitmapArray = new int[mat.height()][mat.width()];
		double[] pixel;
		for (int row = 0; row < bufferedArray.length; row++) {
			for (int col = 0; col < bufferedArray[row].length; col++) {
				pixel = mat.get(row, col);
				setRGB(bufferedArray[row][col], (int) pixel[2], (int) pixel[1], (int) pixel[0]);
			}
		}

	}

	public BufferedImage getBufferedImage() {
		for (int row = 0; row < bufferedArray.length; row++) {
			for (int col = 0; col < bufferedArray[row].length; col++) {
				bufferedImage.setRGB(col, row, getRGBValue(bufferedArray[row][col]));
			}
		}
		return bufferedImage;
	}

	public BufferedImage getBitmapImage() {
		for (int row = 0; row < bitmapArray.length; row++) {
			for (int col = 0; col < bitmapArray[row].length; col++) {
				bitmapImage.setRGB(col, row, bitmapArray[row][col]);
			}
		}
		return bitmapImage;
	}

	public void updateBufferedArray(Mat mat) {
		double[] pixel;
		for (int row = 0; row < bufferedArray.length; row++) {
			for (int col = 0; col < bufferedArray[row].length; col++) {
				pixel = mat.get(row, col);
				setRGB(bufferedArray[row][col], (int) pixel[2], (int) pixel[1], (int) pixel[0]);
			}
		}
	}

	public void updateBitmapArray(int[] target, int tolerance) {
		// Scalar lower = new Scalar(target[2] - tolerance, target[1] -
		// tolerance, target[0] - tolerance);
		// Scalar upper = new Scalar(target[2] + tolerance, target[1] +
		// tolerance, target[0] + tolerance);
		// Core.inRange(mat, lower, upper, matBitmap);
		// Core.bitwise_not(matBitmap, matBitmap);
		for (int row = 0; row < bitmapArray.length; row++) {
			for (int col = 0; col < bitmapArray[row].length; col++) {
				bitmapArray[row][col] = getDistanceSquared(bufferedArray[row][col], target) < tolerance * tolerance
						? 0xFFFFFF : 0;
			}
		}
	}

	public void detectBlobs() {
		// detector.detect(matBitmap, keyPoints);
		// System.out.println(keyPoints.size());
		finder = new BoxFinder(bitmapArray);
	}

	private void drawRectange(int rowStart, int colStart, int rowEnd, int colEnd, int size, int[] color) {
		int value = getRGBValue(color);
		for (int col = colStart; col <= colEnd; col++) {
			for (int row = 0; row < size && rowEnd + row < bufferedArray.length; row++) {
				// bufferedArray[rowStart + row][col] = color;
				// bufferedArray[rowEnd + row][col] = color;
				bitmapArray[rowStart + row][col] = value;
				bitmapArray[rowEnd + row][col] = value;
			}
		}
		for (int row = rowStart; row <= rowEnd; row++) {
			for (int col = 0; col < size && colEnd + col < bufferedArray[0].length; col++) {
				// bufferedArray[row][colStart + col] = color;
				// bufferedArray[row][colEnd + col] = color;
				bitmapArray[row][colStart + col] = value;
				bitmapArray[row][colEnd + col] = value;
			}
		}
	}

	public void drawBoxes(int[] color) {
		for (BoundingBox box : finder.goodBoundingBoxes) {
			drawRectange(box.rowMin, box.colMin, box.rowMax, box.colMax, 1, color);
			// System.out.println(box);
		}
	}

	public void drawLargest(int[] color) {
		BoundingBox largest = finder.getLargestBox();
		if (largest != null)
			drawRectange(largest.rowMin, largest.colMin, largest.rowMax, largest.colMax, 1, color);
	}

	public void negate() {
		int[] pixel = new int[3];
		for (int row = 0; row < bufferedArray.length; row++) {
			for (int col = 0; col < bufferedArray[0].length; col++) {
				pixel = bufferedArray[row][col];
				setRGB(bufferedArray[row][col], 255 - pixel[0], 255 - pixel[1], 255 - pixel[2]);
			}
		}
	}

	public void mirrorVertical(boolean negative) {
		if (!negative) {
			for (int row = 0; row < bufferedArray.length; row++) {
				for (int col = 0; col < bufferedArray[row].length / 2; col++) {
					bufferedArray[row][bufferedArray[row].length - col - 1] = bufferedArray[row][col];
				}
			}
		} else {
			for (int row = 0; row < bufferedArray.length; row++) {
				for (int col = bufferedArray[row].length - 1; col > bufferedArray[row].length / 2; col--) {
					bufferedArray[row][bufferedArray[row].length - col] = bufferedArray[row][col];
				}
			}
		}
	}

	public void mirrorHorizontal(boolean negative) {
		if (!negative) {
			for (int row = 0; row < bufferedArray.length / 2; row++) {
				for (int col = 0; col < bufferedArray[row].length; col++) {
					bufferedArray[bufferedArray.length - row - 1][col] = bufferedArray[row][col];
				}
			}
		} else {
			for (int row = bufferedArray.length - 1; row > bufferedArray.length / 2; row--) {
				for (int col = 0; col < bufferedArray[row].length; col++) {
					bufferedArray[bufferedArray.length - row][col] = bufferedArray[row][col];
				}
			}
		}
	}

	public void pencilDrawing(int tolerance) {
		int[] pixelA = new int[3];
		int[] pixelB = new int[3];
		int[] pixelC = new int[3];
		for (int row = 0; row < bufferedArray.length - 1; row++) {
			for (int col = 0; col < bufferedArray[row].length - 1; col++) {
				pixelA = bufferedArray[row][col];
				pixelB = bufferedArray[row + 1][col];
				pixelC = bufferedArray[row][col + 1];
				if (Math.abs(pixelA[0] - pixelB[0]) >= tolerance)
					setRGB(bufferedArray[row][col], 0, 0, 0);
				else if (Math.abs(pixelA[1] - pixelB[1]) >= tolerance)
					setRGB(bufferedArray[row][col], 0, 0, 0);
				else if (Math.abs(pixelA[2] - pixelB[2]) >= tolerance)
					setRGB(bufferedArray[row][col], 0, 0, 0);
				else if (Math.abs(pixelA[0] - pixelC[0]) >= tolerance)
					setRGB(bufferedArray[row][col], 0, 0, 0);
				else if (Math.abs(pixelA[1] - pixelC[1]) >= tolerance)
					setRGB(bufferedArray[row][col], 0, 0, 0);
				else if (Math.abs(pixelA[2] - pixelC[2]) >= tolerance)
					setRGB(bufferedArray[row][col], 0, 0, 0);
				else
					setRGB(bufferedArray[row][col], 255, 255, 255);
			}
		}
	}

	public void pencilDrawing(int tolerance, int[] pencil, int[] background) {
		int[] pixelA = new int[3];
		int[] pixelB = new int[3];
		int[] pixelC = new int[3];
		for (int row = 0; row < bufferedArray.length - 1; row++) {
			for (int col = 0; col < bufferedArray[row].length - 1; col++) {
				pixelA = bufferedArray[row][col];
				pixelB = bufferedArray[row + 1][col];
				pixelC = bufferedArray[row][col + 1];
				if (Math.abs(pixelA[0] - pixelB[0]) >= tolerance)
					setRGB(bufferedArray[row][col], pencil);
				else if (Math.abs(pixelA[1] - pixelB[1]) >= tolerance)
					setRGB(bufferedArray[row][col], pencil);
				else if (Math.abs(pixelA[2] - pixelB[2]) >= tolerance)
					setRGB(bufferedArray[row][col], pencil);
				else if (Math.abs(pixelA[0] - pixelC[0]) >= tolerance)
					setRGB(bufferedArray[row][col], pencil);
				else if (Math.abs(pixelA[1] - pixelC[1]) >= tolerance)
					setRGB(bufferedArray[row][col], pencil);
				else if (Math.abs(pixelA[2] - pixelC[2]) >= tolerance)
					setRGB(bufferedArray[row][col], pencil);
				else
					setRGB(bufferedArray[row][col], background);
			}
		}
	}

	public void increseEdgeSharpness(int tolerance) {
		int[] pixelA = new int[3];
		int[] pixelB = new int[3];
		int[] pixelC = new int[3];
		for (int row = 0; row < bufferedArray.length - 1; row++) {
			for (int col = 0; col < bufferedArray[row].length - 1; col++) {
				pixelA = bufferedArray[row][col];
				pixelB = bufferedArray[row + 1][col];
				pixelC = bufferedArray[row][col + 1];
				if (Math.abs(pixelA[0] - pixelB[0]) >= tolerance)
					setRGB(bufferedArray[row][col], 0, 0, 0);
				else if (Math.abs(pixelA[1] - pixelB[1]) >= tolerance)
					setRGB(bufferedArray[row][col], 0, 0, 0);
				else if (Math.abs(pixelA[2] - pixelB[2]) >= tolerance)
					setRGB(bufferedArray[row][col], 0, 0, 0);
				else if (Math.abs(pixelA[0] - pixelC[0]) >= tolerance)
					setRGB(bufferedArray[row][col], 0, 0, 0);
				else if (Math.abs(pixelA[1] - pixelC[1]) >= tolerance)
					setRGB(bufferedArray[row][col], 0, 0, 0);
				else if (Math.abs(pixelA[2] - pixelC[2]) >= tolerance)
					setRGB(bufferedArray[row][col], 0, 0, 0);
			}
		}
	}

	public void keepRed() {
		int[] pixel = new int[3];
		for (int row = 0; row < bufferedArray.length; row++) {
			for (int col = 0; col < bufferedArray[row].length; col++) {
				pixel = bufferedArray[row][col];
				setRGB(bufferedArray[row][col], pixel[0], 0, 0);
			}
		}
	}

	public void keepGreen() {
		int[] pixel = new int[3];
		for (int row = 0; row < bufferedArray.length; row++) {
			for (int col = 0; col < bufferedArray[row].length; col++) {
				pixel = bufferedArray[row][col];
				setRGB(bufferedArray[row][col], 0, pixel[1], 0);
			}
		}
	}

	public void keepBlue() {
		int[] pixel = new int[3];
		for (int row = 0; row < bufferedArray.length; row++) {
			for (int col = 0; col < bufferedArray[row].length; col++) {
				pixel = bufferedArray[row][col];
				setRGB(bufferedArray[row][col], 0, 0, pixel[2]);
			}
		}
	}

	public void grayscale() {
		int avg = 0;
		int[] pixel = new int[3];
		for (int row = 0; row < bufferedArray.length; row++) {
			for (int col = 0; col < bufferedArray[row].length; col++) {
				pixel = bufferedArray[row][col];
				avg = (pixel[0] + pixel[1] + pixel[2]) / 3;
				setRGB(bufferedArray[row][col], avg, avg, avg);
			}
		}
	}

	public void zeroGreen() {
		int[] pixel = new int[3];
		for (int row = 0; row < bufferedArray.length; row++) {
			for (int col = 0; col < bufferedArray[row].length; col++) {
				pixel = bufferedArray[row][col];
				setRGB(bufferedArray[row][col], pixel[0], 0, pixel[2]);

			}
		}
	}

	public void zeroRed() {
		int[] pixel = new int[3];
		for (int row = 0; row < bufferedArray.length; row++) {
			for (int col = 0; col < bufferedArray[row].length; col++) {
				pixel = bufferedArray[row][col];
				setRGB(bufferedArray[row][col], 0, pixel[1], pixel[2]);
			}
		}
	}

	public void zeroBlue() {
		int[] pixel = new int[3];
		for (int row = 0; row < bufferedArray.length; row++) {
			for (int col = 0; col < bufferedArray[row].length; col++) {
				pixel = bufferedArray[row][col];
				setRGB(bufferedArray[row][col], pixel[0], pixel[1], 0);
			}
		}
	}

	public void switchBlueRedGreen() {
		int[] pixel = new int[3];
		for (int row = 0; row < bufferedArray.length; row++) {
			for (int col = 0; col < bufferedArray[row].length; col++) {
				pixel = bufferedArray[row][col];
				setRGB(bufferedArray[row][col], pixel[2], pixel[0], pixel[1]);
			}
		}
	}

	public void switchGreenBlueRed() {
		int[] pixel = new int[3];
		for (int row = 0; row < bufferedArray.length; row++) {
			for (int col = 0; col < bufferedArray[row].length; col++) {
				pixel = bufferedArray[row][col];
				setRGB(bufferedArray[row][col], pixel[1], pixel[2], pixel[0]);
			}
		}
	}

	public void switchBlueGreenRed() {
		int[] pixel = new int[3];
		for (int row = 0; row < bufferedArray.length; row++) {
			for (int col = 0; col < bufferedArray[row].length; col++) {
				pixel = bufferedArray[row][col];
				setRGB(bufferedArray[row][col], pixel[1], pixel[2], pixel[0]);
			}
		}
	}

	public void sepia() {
		int[] pixel = new int[3];
		for (int row = 0; row < bufferedArray.length; row++) {
			for (int col = 0; col < bufferedArray[row].length; col++) {
				pixel = bufferedArray[row][col];
				double red = (pixel[0] * .393) + (pixel[1] * .769) + (pixel[2] * .189);
				double green = (pixel[0] * .349) + (pixel[1] * .686) + (pixel[2] * .168);
				double blue = (pixel[0] * .272) + (pixel[1] * .534) + (pixel[2] * .132);
				if (red > 255)
					red = 255;
				if (green > 255)
					green = 255;
				if (blue > 255)
					blue = 255;
				setRGB(bufferedArray[row][col], (int) red, (int) green, (int) blue);
			}
		}
	}

	public void highlightRed(int tolerance) {
		int[] pixel = new int[3];
		for (int row = 0; row < bufferedArray.length; row++) {
			for (int col = 0; col < bufferedArray[row].length; col++) {
				pixel = bufferedArray[row][col];
				if (Math.abs(pixel[0] - pixel[2]) > tolerance && Math.abs(pixel[0] - pixel[1]) > tolerance)
					setRGB(bufferedArray[row][col], 255, 255, 255);
				else
					setRGB(bufferedArray[row][col], 0, 0, 0);
			}
		}
	}

	public void highlightGreen(int tolerance) {
		int[] pixel = new int[3];
		for (int row = 0; row < bufferedArray.length; row++) {
			for (int col = 0; col < bufferedArray[row].length; col++) {
				pixel = bufferedArray[row][col];
				if (Math.abs(pixel[1] - pixel[2]) > tolerance && Math.abs(pixel[1] - pixel[0]) > tolerance)
					setRGB(bufferedArray[row][col], 255, 255, 255);
				else
					setRGB(bufferedArray[row][col], 0, 0, 0);
			}
		}
	}

	public void highlightBlue(int tolerance) {
		int[] pixel = new int[3];
		for (int row = 0; row < bufferedArray.length; row++) {
			for (int col = 0; col < bufferedArray[row].length; col++) {
				pixel = bufferedArray[row][col];
				if (Math.abs(pixel[2] - pixel[1]) > tolerance && Math.abs(pixel[2] - pixel[0]) > tolerance)
					setRGB(bufferedArray[row][col], 255, 255, 255);
				else
					setRGB(bufferedArray[row][col], 0, 0, 0);
			}
		}
	}

	public void highlightCustom(int[] rgb, int tolerance) {
		for (int row = 0; row < bufferedArray.length; row++) {
			for (int col = 0; col < bufferedArray[row].length; col++) {
				if (getDistanceSquared(bufferedArray[row][col], rgb) < tolerance * tolerance)
					setRGB(bufferedArray[row][col], 255, 255, 255);
			}
		}
	}

	public static int getRGBValue(int red, int green, int blue) {
		int rgbValue = red;
		rgbValue = (rgbValue << 8) + green;
		rgbValue = (rgbValue << 8) + blue;
		return rgbValue;
	}

	public static int getRGBValue(int[] rgb) {
		int rgbValue = rgb[0];
		rgbValue = (rgbValue << 8) + rgb[1];
		rgbValue = (rgbValue << 8) + rgb[2];
		return rgbValue;
	}

	public static int getDistanceSquared(int[] a, int[] b) {
		int dr = b[0] - a[0], dg = b[1] - a[1], db = b[2] - a[2];
		return (dr * dr) + (db * db) + (dg * dg);
	}

	public static boolean isWithinTolerance(int[] a, int[] b, int tolerance) {
		return Math.abs(b[0] - a[0]) < tolerance && Math.abs(b[1] - a[1]) < tolerance
				&& Math.abs(b[2] - a[2]) < tolerance;
	}

	public static void setRGB(int[] array, int[] rgb) {
		array[0] = rgb[0];
		array[1] = rgb[1];
		array[2] = rgb[2];
	}

	public static void setRGB(int[] array, int red, int green, int blue) {
		array[0] = red;
		array[1] = green;
		array[2] = blue;
	}

}