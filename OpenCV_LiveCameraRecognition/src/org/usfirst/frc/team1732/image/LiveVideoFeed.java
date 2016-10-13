package org.usfirst.frc.team1732.image;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import org.opencv.core.*;
import org.opencv.videoio.VideoCapture;

public class LiveVideoFeed {

	public static void main(String[] args) {
		String opencvpath = System.getProperty("user.dir") + "\\files\\";
		System.load(opencvpath + Core.NATIVE_LIBRARY_NAME + ".dll");
		VideoCapture camera = new VideoCapture(0);
		Mat mat = new Mat();
		camera.read(mat);
		if (!camera.isOpened()) {
			System.out.println("Error");
		}
		JFrame frame = new JFrame("IMG");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(true);
		frame.setLocationRelativeTo(null);
        ImageIcon image = new ImageIcon(createAwtImage(mat));
		frame.setSize(image.getIconWidth()+10,image.getIconHeight()+35);
    	JLabel label1 = new JLabel(" ", image, JLabel.CENTER);
		frame.getContentPane().add(label1);
        frame.validate();
	    frame.setVisible(true);
	    long start = System.currentTimeMillis();
	    while(true){
	    	start = System.currentTimeMillis();
	    	camera.read(mat);
	    	image.setImage(createAwtImage(mat));
	    	label1 = new JLabel(" ", image, JLabel.CENTER);
	    	frame.getContentPane().add(label1);
	    	frame.validate();
	    	System.out.println(System.currentTimeMillis() - start);
		}
	    
		
		//ImageIcon image = new ImageIcon(createAwtImage(mat));
		
		//camera.release();
	}
	public static Image blackAndWhite(BufferedImage b){
		return null;
	}
	public static BufferedImage createAwtImage(Mat mat) {

		int type = 0;
		if (mat.channels() == 1) {
			type = BufferedImage.TYPE_BYTE_GRAY;
		} else if (mat.channels() == 3) {
			type = BufferedImage.TYPE_3BYTE_BGR;
		} else {
			return null;
		}

		BufferedImage image = new BufferedImage(mat.width(), mat.height(), type);
		WritableRaster raster = image.getRaster();
		DataBufferByte dataBuffer = (DataBufferByte) raster.getDataBuffer();
		byte[] data = dataBuffer.getData();
		mat.get(0, 0, data);

		return image;
	}

	public static BufferedImage toBufferedImage(Image img) {
		if (img instanceof BufferedImage) {
			return (BufferedImage) img;
		}
		BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);
		Graphics2D bGr = bimage.createGraphics();
		bGr.drawImage(img, 0, 0, null);
		bGr.dispose();
		return bimage;
	}
}
