package testing;

import java.awt.FlowLayout;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import camera.AxisCamera;
import camera.AxisCamera.Resolution;

public class Testing {

	public static void main(String[] args) throws Exception {
		AxisCamera camera = new AxisCamera("169.254.148.78");
		ImageIcon icon = new ImageIcon(camera.getBufferedImage());
		JFrame frame = new JFrame();
		frame.getContentPane().setLayout(new FlowLayout());
		frame.getContentPane().add(new JLabel(icon));
		frame.pack();
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Timer timer = new Timer();
		while (timer.getElapsedTime() < 10 * 1000) {
			icon.setImage(camera.getBufferedImage());
			frame.repaint();
		}
		camera.setResolution(Resolution.r320x240);
		timer.reset();
		while (timer.getElapsedTime() < 10 * 1000) {
			icon.setImage(camera.getBufferedImage());
			frame.repaint();
		}
	}
}

class Timer {
	private long time;

	public Timer() {
		time = System.currentTimeMillis();
	}

	public long getElapsedTime() {
		return System.currentTimeMillis() - time;
	}

	public void reset() {
		time = System.currentTimeMillis();
	}
}