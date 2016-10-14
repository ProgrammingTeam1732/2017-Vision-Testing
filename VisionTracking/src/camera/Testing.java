package camera;

import java.awt.FlowLayout;
import java.awt.event.WindowEvent;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import camera.AxisCamera.Resolution;
import camera.AxisCamera.WhiteBalance;

public class Testing {

	public static void main(String[] args) throws Exception {
		AxisCamera camera = new AxisCamera("169.254.148.78");
		System.out.println(camera.listParameters("ImageSource").replaceAll("root", "\nroot") + "\n");
		System.out.println(camera.setBrightness(50));
		System.out.println(camera.listParameters(AxisCamera.BRIGHTNESS_PROPERTY));
		System.out.println(camera.setColorLevel(50));
		System.out.println(camera.listParameters(AxisCamera.COLOR_LEVEL_PROPERTY));
		System.out.println(camera.setContrast(50));
		System.out.println(camera.listParameters(AxisCamera.CONTRAST_PROPERTY));
		System.out.println(camera.setExposureValue(50));
		System.out.println(camera.listParameters(AxisCamera.EXPOSURE_VALUE_PROPERTY));
		System.out.println(camera.setSharpness(50));
		System.out.println(camera.listParameters(AxisCamera.SHARPNESS_PROPERTY));
		System.out.println(camera.setWhiteBalance(WhiteBalance.fixed_outdoor));
		System.out.println(camera.listParameters(AxisCamera.WhiteBalance.property));

		camera.setResolution(Resolution.r480x360);
		ImageIcon icon = new ImageIcon(camera.getBufferedImage());
		JFrame frame = new JFrame();
		initializeJFrame(frame, icon);

		Timer timer = new Timer();
		timer.setTimerLength(10 * 1000);
		timer.reset();
		while (!timer.isFinished()) {
			icon.setImage(camera.getBufferedImage());
			frame.repaint();
		}
		frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
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