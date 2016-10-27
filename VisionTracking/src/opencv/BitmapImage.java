package opencv;

import java.awt.image.BufferedImage;

import org.opencv.core.Mat;

public class BitmapImage {
	private int[][] bitmap;
	private BufferedImage bufferedImage;
	private int[] targetColor;
	private int tolerance;

	public BitmapImage(Mat mat, int[] targetColor, int tolerance) {
		this.tolerance = tolerance;
		this.targetColor = targetColor;
		bitmap = new int[mat.height()][mat.width()];
		bufferedImage = new BufferedImage(mat.width(), mat.height(), BufferedImage.TYPE_INT_RGB);
		for (int row = 0; row < mat.height(); row++) {
			for (int col = 0; col < mat.width(); col++) {
				bitmap[row][col] = getDistance(targetColor, mat.get(row, col)) < tolerance ? 1 : 0;
			}
		}
	}

	public void updateBitmap(Mat mat) {
		for (int row = 0; row < mat.height(); row++) {
			for (int col = 0; col < mat.width(); col++) {
				bitmap[row][col] = getDistance(targetColor, mat.get(row, col)) < tolerance ? 1 : 0;
			}
		}
	}

	public BufferedImage getBufferedImage() {
		for (int row = 0; row < bitmap.length; row++) {
			for (int col = 0; col < bitmap[row].length; col++) {
				bufferedImage.setRGB(col, row, bitmap[row][col] == 0 ? 0 : 0xFFFFFF);
			}
		}
		return bufferedImage;
	}

	private double getDistance(int[] array1, double[] array2) {
		return Math.sqrt(
				(array1[0] - array2[2]) * (array1[0] - array2[2]) + (array1[1] - array2[1]) * (array1[1] - array2[1])
						+ (array1[2] - array2[0]) * (array1[2] - array2[0]));
	}
}
