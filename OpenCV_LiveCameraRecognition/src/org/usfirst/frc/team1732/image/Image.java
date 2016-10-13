package org.usfirst.frc.team1732.image;

import org.opencv.core.Mat;

public class Image {
	private Pixel[][] image;
	public Image(Mat image){
		double[] pixel = new double[3];
		for(int i = 0; i < image.height(); i++){
			for(int j = 0; j < image.width(); j++){
				image.get(i, j, pixel);
				this.image[i][j] = new Pixel((int) pixel[0], (int) pixel[1], (int) pixel[2]);
			}
		}
	}
}
