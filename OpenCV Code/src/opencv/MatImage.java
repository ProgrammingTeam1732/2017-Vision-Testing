package opencv;

import java.awt.image.BufferedImage;

import org.opencv.core.Mat;

public class MatImage {
	private Pixel[][] image;
	private BufferedImage total;
	public MatImage(Mat image) {
		double[] pixel = new double[3];
		/*this.image = new Pixel[image.height()][image.width()];
		for (int i = 0; i < image.height(); i++) {
			for (int j = 0; j < image.width(); j++) {
				pixel = image.get(i, j);
				this.image[i][j] = new Pixel((int) pixel[2], (int) pixel[0], (int) pixel[1]);
			}
		}*/
		this.image = new Pixel[image.width()][image.height()];
		for (int i = 0; i < image.height(); i++) {
			for (int j = 1; j <= image.width(); j++) {
				pixel = image.get(i, image.width()-j);
				this.image[j-1][i] = new Pixel((int) pixel[0], (int) pixel[1], (int) pixel[2]);
			}
		}
	}
	public BufferedImage getBufferedImage(){
		total = new BufferedImage(image.length, image[0].length, BufferedImage.TYPE_INT_BGR);
		for(int i = 0; i < total.getHeight(); i++){
			for(int j = 0; j < total.getWidth(); j++){
				total.setRGB(j, i, image[j][i].getRGB());
			}
		}
		return total;
	}
}