package opencv;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import org.opencv.core.Core;
import org.opencv.core.Mat;

//import org.opencv.videoio.VideoCapture;

// when using 2.4.13:
//import org.opencv.highgui.VideoCapture;
// when using 3.1.0:
import org.opencv.videoio.VideoCapture;

public class LiveVideoFeedHighFPS {
	private volatile static MatImage image;
	private volatile static boolean updated;
	public static void main(String[] args) {
		boolean blue = false;
		String opencvpath = System.getProperty("user.dir") + "\\files\\";
		System.load(opencvpath + Core.NATIVE_LIBRARY_NAME + ".dll");

		//System.load(opencvpath + "opencv_ffmpeg2413_64.dll");
		VideoCapture camera = new VideoCapture(0);

		if (Core.VERSION.equals("2.4.13.0"))
			System.load(opencvpath + "opencv_ffmpeg2413_64.dll");
		else
			System.load(opencvpath + "opencv_ffmpeg310_64.dll");
		//VideoCapture camera = new VideoCapture();

		// http://169.254.148.78/axis-media/media.amp
		// http://169.254.148.78/mjpg/video.mjpg

		//if (!camera.isOpened(/*"http://169.254.148.78/mjpg/video.mjpg"*/)) {

		if (!camera.open(0)) { // "http://169.254.148.78/mjpg/video.mjpg")) {

			System.out.println("Error");
		}
		Mat mat = new Mat();
		camera.read(mat);
		JFrame frame = new JFrame("IMG");
		// frame.addWindowListener( new WindowAdapter() {
		// @Override
		// public void windowClosing(WindowEvent we) {
		// System.exit(1);
		// }
		// });
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		image = new MatImage(mat);
		ImageIcon icon = new ImageIcon(image.getBufferedImage());
		frame.setResizable(false);
		frame.setLocation(200, 100);
		frame.setSize(icon.getIconWidth() + 100, icon.getIconHeight() + 100);
		JLabel label1 = new JLabel("", icon, JLabel.CENTER);
		frame.getContentPane().add(label1);
		frame.validate();
		frame.setVisible(true);
		new Thread(() -> {
			//image = new MatImage(mat);
			updated = false;
			while(true){
				updated = false;
				camera.read(mat);
				image = new MatImage(mat);
				image.mirrorHorizontal(true);
				image.pencilDrawing(5);
				updated = true;
			}
		}).start();
		long start = System.currentTimeMillis();
		while (true) {
			start = System.currentTimeMillis();
			//camera.read(mat);
			//image.updateMat(mat);
			//image = new MatImage(mat).getBufferedImage();
			//if (blue) blue(image);
			while(!updated){}
			icon.setImage(image.getBufferedImage());
			frame.setTitle("IMG framerate: " + Math.round((1000.0 / (System.currentTimeMillis() - start))/5) * 5 + "fps");
			//frame.setTitle("IMG Time between frames:" + Math.round((System.currentTimeMillis() - start)/5)*5 + "milliseconds");
			frame.repaint();
			// frame.getContentPane().add(new JLabel("", icon, JLabel.CENTER));
			// rounding to make it easier to read the fps on the jframe
			//frame.validate();
		}

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
		mat.get(0, 0, ((DataBufferByte) image.getRaster().getDataBuffer()).getData());

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