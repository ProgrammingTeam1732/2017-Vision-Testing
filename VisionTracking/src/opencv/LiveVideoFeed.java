package opencv;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextArea;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;

public class LiveVideoFeed extends JFrame implements MouseListener {

	private static final long serialVersionUID = -2222565840007699833L;

	MatImage matImage;
	JSlider red, green, blue;

	public LiveVideoFeed() {
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
		matImage = new MatImage(mat);

		Toolkit tk = Toolkit.getDefaultToolkit();
		Dimension dim = tk.getScreenSize();

		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		ImageIcon icon = new ImageIcon(matImage.getBufferedImage());
		this.setResizable(false);
		this.setSize(icon.getIconWidth(), icon.getIconHeight() + 123);
		this.setLocation(dim.width / 2 - this.getWidth() / 2, dim.height / 2 - this.getHeight() / 2);
		JLabel label1 = new JLabel("", icon, JLabel.CENTER);
		JTextArea field1 = new JTextArea(2, 10);
		JSlider tolerance = new JSlider(0, 255);
		tolerance.setToolTipText("TOLERANCE");
		tolerance.setValue(0);
		red = new JSlider(0, 255);
		red.setToolTipText("RED");
		green = new JSlider(0, 255);
		green.setToolTipText("GREEN");
		blue = new JSlider(0, 255);
		blue.setToolTipText("BLUE");
		JPanel panel = new JPanel();
		panel.add(label1);
		panel.add(red);
		panel.add(green);
		panel.add(blue);
		panel.add(tolerance);
		panel.add(field1);
		this.add(panel);
		panel.addMouseListener(this);
		this.validate();
		this.setVisible(true);

		long start = System.currentTimeMillis();
		long FPS = 1;
		long prevFPS = 1;
		long beforePrevFPS = 1;
		while (true) {
			start = System.currentTimeMillis();
			camera.read(mat);
			matImage.updatePixelArray(mat);
			matImage.highlightCustom(new int[] { red.getValue(), green.getValue(), blue.getValue() },
					tolerance.getValue());
			icon.setImage(matImage.getBufferedImage());
			this.setTitle("FPS: " + (FPS + prevFPS + beforePrevFPS) / 3);
			this.repaint();
			field1.setText(String.format("Red: %3d  Green: %3d%nBlue: %3d Tolerance: %3d", red.getValue(),
					green.getValue(), blue.getValue(), tolerance.getValue()));
			beforePrevFPS = prevFPS;
			prevFPS = FPS;
			FPS = Math.round((1000.0 / (System.currentTimeMillis() - start)) / 5) * 5;
		}
	}

	public static void main(String[] args) {
		new LiveVideoFeed();
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		int[] selectedColor = matImage.getPixelArray()[e.getY()][e.getX()].rgb;
		red.setValue(selectedColor[0]);
		green.setValue(selectedColor[1]);
		blue.setValue(selectedColor[2]);
	}
	public void mouseEntered(MouseEvent e) {
	}
	public void mouseExited(MouseEvent e) {
	}
	public void mousePressed(MouseEvent e) {
	}
	public void mouseReleased(MouseEvent e) {
	}
}