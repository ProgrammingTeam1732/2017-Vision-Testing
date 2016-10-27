package opencv;

import java.awt.image.BufferedImage;

import org.opencv.core.Mat;

public class BitmapImage {
	private int[][] bitmap;
	private BufferedImage bufferedImage;
	int[] redRange;
	int[] greenRange;
	int[] blueRange;

	public BitmapImage(Mat mat, int[] redRange, int[] greenRange, int[] blueRange) {
		this.redRange = redRange;
		this.greenRange = greenRange;
		this.blueRange = blueRange;

		bitmap = new int[mat.height()][mat.width()];

		for (int row = 0; row < mat.height(); row++) {
			for (int col = 0; col < mat.width(); col++) {
				double[] pixel = mat.get(row, col);
				if (pixel[2] < redRange[0] || pixel[2] > redRange[1]) {
					bitmap[row][col] = 0;
				} else if (pixel[1] < greenRange[0] || pixel[1] > greenRange[1]) {
					bitmap[row][col] = 0;
				} else if (pixel[0] < blueRange[0] || pixel[0] > blueRange[1]) {
					bitmap[row][col] = 0;
				} else {
					bitmap[row][col] = 1;
				}
			}
		}
		bufferedImage = new BufferedImage(mat.width(), mat.height(), BufferedImage.TYPE_INT_RGB);
	}

	public BufferedImage getBufferedImage() {
		for (int row = 0; row < bitmap.length; row++) {
			for (int col = 0; col < bitmap[row].length; col++) {
				bufferedImage.setRGB(col, row, bitmap[row][col] == 0 ? 0 : 0xFFFFFF);
			}
		}
		return bufferedImage;
	}

	public void updateBitmap(Mat mat) {
		for (int row = 0; row < mat.height(); row++) {
			for (int col = 0; col < mat.width(); col++) {
				double[] pixel = mat.get(row, col);
				if (pixel[2] < redRange[0] || pixel[2] > redRange[1]) {
					bitmap[row][col] = 0;
				} else if (pixel[1] < greenRange[0] || pixel[1] > greenRange[1]) {
					bitmap[row][col] = 0;
				} else if (pixel[0] < blueRange[0] || pixel[0] > blueRange[1]) {
					bitmap[row][col] = 0;
				} else {
					bitmap[row][col] = 1;
				}
			}
		}
	}

	public void updateBitmapRed(Mat mat) {
		for (int row = 0; row < mat.height(); row++) {
			for (int col = 0; col < mat.width(); col++) {
				double[] pixel = mat.get(row, col);
				if (pixel[2] < redRange[0] || pixel[2] > redRange[1]) {
					bitmap[row][col] = 0;
				} else {
					bitmap[row][col] = 1;
				}
			}
		}
	}

	public void updateBitmapGreen(Mat mat) {
		for (int row = 0; row < mat.height(); row++) {
			for (int col = 0; col < mat.width(); col++) {
				double[] pixel = mat.get(row, col);
				if (pixel[1] < greenRange[0] || pixel[1] > greenRange[1]) {
					bitmap[row][col] = 0;
				} else {
					bitmap[row][col] = 1;
				}
			}
		}
	}

	public void updateBitmapBlue(Mat mat) {
		for (int row = 0; row < mat.height(); row++) {
			for (int col = 0; col < mat.width(); col++) {
				double[] pixel = mat.get(row, col);
				if (pixel[0] < blueRange[0] || pixel[0] > blueRange[1]) {
					bitmap[row][col] = 0;
				} else {
					bitmap[row][col] = 1;
				}
			}
		}
	}
}
