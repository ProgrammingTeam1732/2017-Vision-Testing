
package opencv;

import java.awt.Color;
import java.awt.image.BufferedImage;

import org.opencv.core.Mat;

public class MatImage {
	private RGBPixel[][] image;
	private BufferedImage total;

	public MatImage(Mat image) {
		double[] pixel = new double[3];
		/*
		 * this.image = new Pixel[image.height()][image.width()]; for (int i =
		 * 0; i < image.height(); i++) { for (int j = 0; j < image.width(); j++)
		 * { pixel = image.get(i, j); this.image[i][j] = new Pixel((int)
		 * pixel[2], (int) pixel[0], (int) pixel[1]); } }
		 */
		this.image = new RGBPixel[image.width()][image.height()];
		for (int i = 0; i < image.height(); i++) {
			for (int j = 1; j <= image.width(); j++) {
				pixel = image.get(i, image.width() - j);
				this.image[j - 1][i] = new RGBPixel((int) pixel[2], (int) pixel[1], (int) pixel[0]);
			}
		}
	}
	public void updateMat(Mat image){
		double[] pixel = new double[3];
		this.image = new RGBPixel[image.width()][image.height()];
		for (int i = 0; i < image.height(); i++) {
			for (int j = 1; j <= image.width(); j++) {
				pixel = image.get(i, image.width() - j);
				this.image[j - 1][i] = new RGBPixel((int) pixel[2], (int) pixel[1], (int) pixel[0]);
			}
		}
	}
	public BufferedImage getBufferedImage() {
		total = new BufferedImage(image.length, image[0].length, BufferedImage.TYPE_INT_RGB);
		for (int i = 0; i < total.getHeight(); i++) {
			for (int j = 0; j < total.getWidth(); j++) {
				total.setRGB(j, i, image[j][i].getRGB());
			}
		}
		return total;
	}
	public void negate(){
		for(int row = 0; row < image.length; row++){
			for(int col = 0; col < image[0].length; col++){
			    image[row][col].setRed(255 - image[row][col].getRed());
		        image[row][col].setGreen(255 - image[row][col].getGreen());
		        image[row][col].setBlue(255 - image[row][col].getBlue());
			}
		}
	}
	public void mirrorVertical(boolean negative){
		if(!negative){
			for(int row = 0; row < image.length; row++){
				for(int col = 0; col < image[0].length/2; col++){
				    image[row][image[0].length - col-1].setRed(image[row][col].getRed());
			        image[row][image[0].length - col-1].setGreen(image[row][col].getGreen());
			        image[row][image[0].length - col-1].setBlue(image[row][col].getBlue());
				}
			}
		}
		else{
			for(int row = 0; row < image.length; row++){
				for(int col = image[0].length-1; col > image[0].length/2; col--){
				    image[row][image[0].length - col].setRed(image[row][col].getRed());
			        image[row][image[0].length - col].setGreen(image[row][col].getGreen());
			        image[row][image[0].length - col].setBlue(image[row][col].getBlue());
				}
			}
		}
	}
	public void mirrorHorizontal(boolean negative){
		if(!negative){
			for(int row = 0; row < image.length/2; row++){
				for(int col = 0; col < image[0].length; col++){
				    image[image.length - 1 -row][col].setRed(image[row][col].getRed());
			        image[image.length - 1 -row][col].setGreen(image[row][col].getGreen());
			        image[image.length - 1 -row][col].setBlue(image[row][col].getBlue());
				}
			}
		}
		else{
			for(int row = image.length-1; row > image.length/2; row--){
				for(int col = 0; col < image[0].length; col++){
				    image[image.length -row][col].setRed(image[row][col].getRed());
			        image[image.length -row][col].setGreen(image[row][col].getGreen());
			        image[image.length -row][col].setBlue(image[row][col].getBlue());
				}
			}
		}
	}
	public void pencilDrawing(int tolerance){
		for(int row = 0; row < image.length-1; row++){
			for(int col = 0; col < image[0].length-1; col++){
				if(image[row][col].getRed() - image[row + 1][col].getRed() >= tolerance)image[row][col].setPixel(0, 0, 0);
				else if(image[row][col].getGreen() - image[row + 1][col].getGreen() >= tolerance)image[row][col].setPixel(0, 0, 0);
				else if(image[row][col].getBlue() - image[row + 1][col].getBlue() >= tolerance)image[row][col].setPixel(0, 0, 0);
				else if(image[row][col].getRed() - image[row][col+1].getRed() >= tolerance)image[row][col].setPixel(0, 0, 0);
				else if(image[row][col].getGreen() - image[row][col + 1].getGreen() >= tolerance)image[row][col].setPixel(0, 0, 0);
				else if(image[row][col].getBlue() - image[row][col + 1].getBlue() >= tolerance)image[row][col].setPixel(0, 0, 0);
				else image[row][col].setPixel(255, 255, 255);	
			}
		}
	}
	public void pencilDrawing(int tolerance, Color pencil, Color background){
		for(int row = 0; row < image.length-1; row++){
			for(int col = 0; col < image[0].length-1; col++){
				if(image[row][col].getRed() - image[row + 1][col].getRed() >= tolerance)image[row][col].setPixel(pencil);
				else if(image[row][col].getGreen() - image[row + 1][col].getGreen() >= tolerance)image[row][col].setPixel(pencil);
				else if(image[row][col].getBlue() - image[row + 1][col].getBlue() >= tolerance)image[row][col].setPixel(pencil);
				else if(image[row][col].getRed() - image[row][col+1].getRed() >= tolerance)image[row][col].setPixel(pencil);
				else if(image[row][col].getGreen() - image[row][col + 1].getGreen() >= tolerance)image[row][col].setPixel(pencil);
				else if(image[row][col].getBlue() - image[row][col + 1].getBlue() >= tolerance)image[row][col].setPixel(pencil);
				else image[row][col].setPixel(background);	
			}
		}
	}
	public void increseEdgeSharpness(int tolerance){
		for(int row = 0; row < image.length-1; row++){
			for(int col = 0; col < image[0].length-1; col++){
				if(image[row][col].getRed() - image[row + 1][col].getRed() >= tolerance)image[row][col].setPixel(0, 0, 0);
				else if(image[row][col].getGreen() - image[row + 1][col].getGreen() >= tolerance)image[row][col].setPixel(0, 0, 0);
				else if(image[row][col].getBlue() - image[row + 1][col].getBlue() >= tolerance)image[row][col].setPixel(0, 0, 0);
				else if(image[row][col].getRed() - image[row][col+1].getRed() >= tolerance)image[row][col].setPixel(0, 0, 0);
				else if(image[row][col].getGreen() - image[row][col + 1].getGreen() >= tolerance)image[row][col].setPixel(0, 0, 0);
				else if(image[row][col].getBlue() - image[row][col + 1].getBlue() >= tolerance)image[row][col].setPixel(0, 0, 0);
				//else image[row][col].setPixel(255, 255, 255);	
			}
		}
	}
	public void keepRed(){
		for(int row = 0; row < image.length-1; row++){
			for(int col = 0; col < image[0].length; col++){
				image[row][col].setPixel(image[row][col].getRed(), 0, 0);
			}
		}
	}
	public void keepBlue(){
		for(int row = 0; row < image.length-1; row++){
			for(int col = 0; col < image[0].length; col++){
				image[row][col].setPixel(0, 0, image[row][col].getBlue());
			}
		}
	}
	public void keepGreen(){
		for(int row = 0; row < image.length-1; row++){
			for(int col = 0; col < image[0].length; col++){
				image[row][col].setPixel(0, image[row][col].getGreen(), 0);
			}
		}
	}
	public void grayscale(){
		int avg = 0;
		for(int row = 0; row < image.length-1; row++){
			for(int col = 0; col < image[0].length; col++){
				avg = (image[row][col].getGreen() + image[row][col].getRed() + image[row][col].getBlue())/3;
				image[row][col].setPixel(avg, avg, avg);
			}
		}
	}
	public void zeroGreen(){
		for(int row = 0; row < image.length-1; row++){
			for(int col = 0; col < image[0].length; col++){
				image[row][col].setPixel(image[row][col].getRed(), 0,  image[row][col].getBlue());
			}
		}
	}
	public void zeroRed(){
		for(int row = 0; row < image.length-1; row++){
			for(int col = 0; col < image[0].length; col++){
				image[row][col].setPixel(0, image[row][col].getGreen(),  image[row][col].getBlue());
			}
		}
	}
	public void zeroBlue(){
		for(int row = 0; row < image.length-1; row++){
			for(int col = 0; col < image[0].length; col++){
				image[row][col].setPixel(image[row][col].getRed(), image[row][col].getGreen(), 0);
			}
		}
	}
	public void switchBlueRedGreen(){
		for(int row = 0; row < image.length-1; row++){
			for(int col = 0; col < image[0].length; col++){
				image[row][col].setPixel(image[row][col].getBlue(), image[row][col].getRed() ,image[row][col].getGreen() );
			}
		}
	}
	public void switchGreenBlueRed(){
		for(int row = 0; row < image.length-1; row++){
			for(int col = 0; col < image[0].length; col++){
				image[row][col].setPixel(image[row][col].getGreen() , image[row][col].getBlue() ,image[row][col].getRed());
			}
		}
	}
	public void switchBlueGreenRed(){
		for(int row = 0; row < image.length-1; row++){
			for(int col = 0; col < image[0].length; col++){
				image[row][col].setPixel( image[row][col].getBlue(), image[row][col].getGreen()  ,image[row][col].getRed());
			}
		}
	}
	public void sepia(){
		for(int row = 0; row < image.length-1; row++){
			for(int col = 0; col < image[0].length; col++){
				double red = (image[row][col].getPixel()[0] * .393) + (image[row][col].getPixel()[1] * .769) + (image[row][col].getPixel()[2] * .189);
				double green = (image[row][col].getPixel()[0] * .349) + (image[row][col].getPixel()[1] * .686) + (image[row][col].getPixel()[2] * .168);
				double blue = (image[row][col].getPixel()[0] * .272) + (image[row][col].getPixel()[1] * .534) + (image[row][col].getPixel()[2] * .132);
				if(red > 255) red = 255;
				if(green > 255) green = 255;
				if(blue > 255) blue = 255;
				image[row][col].setPixel((int) red, (int) green, (int) blue);
			}
		}
	}
}