package opencv;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

// class to screw around with the openCV classes

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;

public class Testing {

	public static void main(String[] args) {
		String opencvpath = System.getProperty("user.dir") + "\\files\\";
		System.load(opencvpath + Core.NATIVE_LIBRARY_NAME + ".dll");
		Mat mat = Highgui.imread("files\\Test.bmp", 0);
		// Do stuff to mat
		List<MatOfPoint> list = detectBlobs(mat);
		for (int i = 0; i < list.size(); i++) {
			System.out.printf("%d: %s%n", i, list.get(i));
		}
		// ???
		// Imgproc.drawContours(mat, list, -1, new Scalar(0, 0, 255));

		// display the mat
		ImageIcon colorIcon = new ImageIcon(toBufferedImage(mat));
		JLabel colorLabel = new JLabel("", colorIcon, JLabel.LEFT);

		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setSize(mat.width(), mat.height());
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		frame.setLocation(dim.width / 2 - frame.getWidth() / 2, dim.height / 2 - frame.getHeight() / 2);
		frame.add(colorLabel);
		frame.validate();
		frame.setVisible(true);

	}

	public static List<MatOfPoint> detectBlobs(Mat mat) {
		// FeatureDetector detector =
		// FeatureDetector.create(FeatureDetector.SIMPLEBLOB);
		// detector.detect(mat, keyPoints);
		Mat contours;
		List<MatOfPoint> list = new ArrayList<>();
		// look at documentation for method to understand arguments
		Mat heirachy = new Mat();
		Imgproc.findContours(mat, list, heirachy, 1, 1);
		return list;
	}

	public static BufferedImage toBufferedImage(Mat m) {
		BufferedImage image = new BufferedImage(m.cols(), m.rows(), BufferedImage.TYPE_3BYTE_BGR);
		for (int i = 0; i < m.height(); i++) {
			for (int j = 0; j < m.width(); j++) {
				image.setRGB(j, i, m.get(i, j)[0] == 0 ? 0 : 0xFFFFFF);
			}
		}
		return image;
	}
}