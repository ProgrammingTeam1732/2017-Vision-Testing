import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;

public class Main {

	public static void main(String[] args) {
		String opencvpath = System.getProperty("user.dir") + "\\files\\";
		System.load(opencvpath + Core.NATIVE_LIBRARY_NAME + ".dll");
		System.load(opencvpath + "opencv_ffmpeg310_64.dll");
		System.out.printf("%s%n", Core.VERSION);
		VideoCapture vc = new VideoCapture("http://169.254.148.78/mjpg/video.mjpg");
		if (vc.open(0)) {
			System.out.println("Success");
		}
		Mat mat = new Mat();
		vc.read(mat);
		JFrame j = new JFrame();
		JPanel jp = new JPanel();
		JLabel jl = new JLabel(new ImageIcon(findBlobs(mat2Img(mat), 0xFFFFFFFF, 30)));
		JLabel jl2 = new JLabel(new ImageIcon(mat2Img(mat)));
		jp.add(jl2);
		jp.add(jl);
		j.add(jp);
		j.pack();
		j.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		j.setVisible(true);
		Timer t = new Timer(1000 / 30, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				vc.read(mat);
				jl.setIcon(new ImageIcon(findBlobs(mat2Img(mat), 0xFFFFFFFF, 30)));
				jl2.setIcon(new ImageIcon(mat2Img(mat)));
				// j.repaint();
			}
		});
		t.start();
	}
	public static BufferedImage findBlobs(BufferedImage bf, int color, int tolerance) {
		// int r = c.getRed(), g = c.getGreen(), b = c.getBlue();
		for (int i = 0; i < bf.getWidth(); i++) {
			for (int j = 0; j < bf.getHeight(); j++) {
				int cc = bf.getRGB(i, j);
				int dx = (cc >> 24) - (color >> 24);
				int dy = (cc >> 16) - (color >> 16);
				int dz = (cc >> 8) - (color >> 8);
				if (Math.sqrt(dx * dx + dy * dy + dz * dz) < tolerance) {
					bf.setRGB(i, j, 0xFFFFFFFF);
				} else
					bf.setRGB(i, j, 0x00000000);
			}
		}
		return bf;
	}
	public static BufferedImage mat2Img(Mat in) {
		BufferedImage bf = new BufferedImage(in.width(), in.height(), BufferedImage.TYPE_INT_RGB);
		for (int i = 0; i < bf.getWidth(); i++) {
			for (int j = 0; j < bf.getHeight(); j++) {
				double[] pixel = in.get(j, i);
				bf.setRGB(i, j, new Color((int) pixel[2], (int) pixel[1], (int) pixel[0]).getRGB());
			}
		}
		return bf;
	}
}