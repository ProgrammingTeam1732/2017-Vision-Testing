package opencv;

import java.awt.Color;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;

public class LiveVideoFeed {


	public static void main(String[] args) {

		String opencvpath = System.getProperty("user.dir") + "\\files\\";
		System.load(opencvpath + Core.NATIVE_LIBRARY_NAME + ".dll");
		// needed in order to run the axis camera
		if (Core.VERSION.equals("2.4.13.0"))
			System.load(opencvpath + "opencv_ffmpeg2413_64.dll");

		VideoCapture camera = new VideoCapture(0);

		// http://169.254.148.78/axis-media/media.amp
		// http://169.254.148.78/mjpg/video.mjpg

		if (!camera.open(0)) { // "http://169.254.148.78/mjpg/video.mjpg")) {
			System.out.println("Error");
		}

		Mat mat = new Mat();
		camera.read(mat);
		MatImage matImage = new MatImage(mat);

		JFrame frame = new JFrame("IMG");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		ImageIcon icon = new ImageIcon(matImage.getBufferedImage());
		frame.setResizable(false);
		frame.setLocation(200, 100);
		frame.setSize(icon.getIconWidth(), icon.getIconHeight());
		JLabel label1 = new JLabel("", icon, JLabel.CENTER);
		JSlider tol = new JSlider(0, 255);
		JPanel panel = new JPanel();
		panel.add(label1);
		panel.add(tol);
		frame.add(panel);
		frame.setSize(frame.getWidth(), frame.getHeight()+63);
		frame.validate();
		frame.setVisible(true);

		long start = System.currentTimeMillis();
		long FPS = 1;
		long prevFPS = 1;
		long beforePrevFPS = 1;
		while (true) {
			start = System.currentTimeMillis();
			camera.read(mat);
			matImage.updatePixelArray(mat);
			matImage.highlightCustom(new Color(255, 128, 0), tol.getValue());
			icon.setImage(matImage.getBufferedImage());
			frame.setTitle("FPS: " + (FPS + prevFPS + beforePrevFPS) / 3);
			frame.repaint();
			beforePrevFPS = prevFPS;
			prevFPS = FPS;
			FPS = Math.round((1000.0 / (System.currentTimeMillis() - start)) / 5) * 5;
		}

	}
}