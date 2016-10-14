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
import org.opencv.videoio.VideoCapture;

public class LiveVideoFeed {

	public static void main(String[] args) {
		boolean blue = false;
		String opencvpath = System.getProperty("user.dir") + "\\files\\";
		System.load(opencvpath + Core.NATIVE_LIBRARY_NAME + ".dll");
		//System.load(opencvpath + "opencv_ffmpeg2413_64.dll");
		VideoCapture camera = new VideoCapture(0);
		// http://169.254.148.78/axis-media/media.amp
		// http://169.254.148.78/mjpg/video.mjpg
		if (!camera.isOpened(/*"http://169.254.148.78/mjpg/video.mjpg"*/)) {
			System.out.println("Error");
		}
		Mat mat = new Mat();
		camera.read(mat);
		// System.out.println(camera.grab());
		// System.out.println(camera.retrieve(mat));
		JFrame frame = new JFrame("IMG");
		// frame.addWindowListener( new WindowAdapter() {
		// @Override
		// public void windowClosing(WindowEvent we) {
		// System.exit(1);
		// }
		// });
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		frame.setResizable(false);
		frame.setLocation(200, 100);
		BufferedImage image = createAwtImage(mat);
		ImageIcon icon = new ImageIcon(image);
		frame.setSize(icon.getIconWidth() + 100, icon.getIconHeight() + 100);
		JLabel label1 = new JLabel("", icon, JLabel.CENTER);
		frame.getContentPane().add(label1);
		frame.validate();
		frame.setVisible(true);
		MatImage m = new MatImage(mat);
		long start = System.currentTimeMillis();
		while (true) {
			start = System.currentTimeMillis();
			camera.read(mat);
			m.updateMat(mat);
			m.pencilDrawing(5);
			image = m.getBufferedImage();
			//image = new MatImage(mat).getBufferedImage();
			if (blue) blue(image);
			icon.setImage(image);
			frame.repaint();
			// frame.getContentPane().add(new JLabel("", icon, JLabel.CENTER));
			// rounding to make it easier to read the fps on the jframe
			frame.setTitle("IMG framerate: " + Math.round((1000.0 / (System.currentTimeMillis() - start))/5) * 5 + "fps");
			frame.validate();
		}

	}

	public static void blue(BufferedImage b) {
		for (int j = 0; j < b.getHeight(); j++) {
			for (int i = 0; i < b.getWidth(); i++) {
				b.setRGB(i, j, new Color(b.getRGB(i, j)).getBlue());
			}
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
