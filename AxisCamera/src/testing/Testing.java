package testing;

import java.awt.FlowLayout;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import camera.AxisCamera;
import camera.AxisCamera.Resolution;
import camera.AxisCamera.WhiteBalance;

public class Testing {

	public static void main(String[] args) throws Exception {
		AxisCamera camera = new AxisCamera("169.254.148.78");
		System.out.println(camera.listParameters("ImageSource") + "\n");
		System.out.println(camera.listParameters(WhiteBalance.property) + "\n");
		System.out.println(camera.setWhiteBalance(WhiteBalance.auto) + "\n");
		System.out.println(camera.listParameters(WhiteBalance.property) + "\n");

		ImageIcon icon = new ImageIcon(camera.getBufferedImage());
		JFrame frame = new JFrame();
		initializeJFrame(frame, icon);

		Timer timer = new Timer();
		timer.setTimerLength(5 * 1000);
		while (!timer.isFinished()) {
			icon.setImage(camera.getBufferedImage());
			frame.repaint();
		}
		camera.setResolution(Resolution.r320x240);
		timer.reset();
		while (!timer.isFinished()) {
			icon.setImage(camera.getBufferedImage());
			frame.repaint();
		}
		camera.setCompression(100);
		timer.reset();
		while (!timer.isFinished()) {
			icon.setImage(camera.getBufferedImage());
			frame.repaint();
		}
	}

	private static void initializeJFrame(JFrame frame, ImageIcon icon) {
		frame.getContentPane().setLayout(new FlowLayout());
		frame.getContentPane().add(new JLabel(icon));
		frame.pack();
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

}

class Timer {

	private long time;
	private int setTime;

	public Timer() {
		time = System.currentTimeMillis();
	}

	public void setTimerLength(int aSetTime) {
		setTime = aSetTime;
	}

	public boolean isFinished() {
		return getElapsedTime() > setTime;
	}

	public long getElapsedTime() {
		return System.currentTimeMillis() - time;
	}

	public void reset() {
		time = System.currentTimeMillis();
	}

}