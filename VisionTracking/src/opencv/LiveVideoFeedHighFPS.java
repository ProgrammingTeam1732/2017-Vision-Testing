package opencv;

import java.awt.Dimension;
import java.awt.GridLayout;
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
import org.opencv.highgui.VideoCapture;

public class LiveVideoFeedHighFPS extends JFrame implements MouseListener {

	private static final long serialVersionUID = 1L;
	// we don't actually use this ^
	private MatImage			matImage;
	private volatile boolean	update;
	private JSlider				redSlider, greenSlider, blueSlider;

	public LiveVideoFeedHighFPS() {
		String opencvpath = System.getProperty("user.dir") + "\\files\\";
		System.load(opencvpath + Core.NATIVE_LIBRARY_NAME + ".dll");
		// needed in order to run the axis camera
		if (Core.VERSION.equals("2.4.13.0"))
			System.load(opencvpath + "opencv_ffmpeg2413_64.dll");
		VideoCapture camera = new VideoCapture(0);

		if (!camera.open("http://169.254.148.78/mjpg/video.mjpg")) {
			System.out.println("Error");
		}
		camera.set(38, 3); // 38 = CV_CAP_PROP_BUFFERSIZE
		Mat mat = new Mat();
		camera.read(mat);
		matImage = new MatImage(mat);

		ImageIcon colorIcon = new ImageIcon(matImage.getBufferedImage());
		JLabel colorLabel = new JLabel("", colorIcon, JLabel.LEFT);

		matImage.updateBitmapArray(new int[] { 255, 255, 255 }, 10);
		matImage.detectBlobs();
		matImage.drawBoxes();
		ImageIcon bitmapIcon = new ImageIcon(matImage.getBitmapImage());
		JLabel bitmapLabel = new JLabel("", bitmapIcon, JLabel.LEFT);

		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
		this.setSize(colorIcon.getIconWidth() * 2, colorIcon.getIconHeight() + 123);
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation(dim.width / 2 - this.getWidth() / 2, dim.height / 2 - this.getHeight() / 2);

		JTextArea field1 = new JTextArea(2, 10);

		JSlider toleranceSlider = new JSlider(0, 255);
		toleranceSlider.setToolTipText("TOLERANCE");
		toleranceSlider.setValue(10);
		redSlider = new JSlider(0, 255);
		redSlider.setToolTipText("RED");
		redSlider.setValue(255);
		greenSlider = new JSlider(0, 255);
		greenSlider.setToolTipText("GREEN");
		greenSlider.setValue(255);
		blueSlider = new JSlider(0, 255);
		blueSlider.setToolTipText("BLUE");
		blueSlider.setValue(255);

		JPanel container = new JPanel();
		container.setLayout(new GridLayout(1, 2));

		JPanel panel1 = new JPanel();
		panel1.add(colorLabel);
		panel1.add(redSlider);
		panel1.add(greenSlider);
		panel1.add(blueSlider);
		panel1.add(toleranceSlider);
		panel1.add(field1);
		panel1.addMouseListener(this);

		JPanel panel2 = new JPanel();
		panel2.add(bitmapLabel);

		container.add(panel1);
		container.add(panel2);
		this.add(container);
		this.validate();
		this.setVisible(true);

		new Thread(() -> {
			while (true) {
				if (update) {
					camera.grab();
					update = false;
				}
			}
		}).start();

		long start = System.currentTimeMillis();
		long FPS = 1;
		long prevFPS = 1;
		long beforePrevFPS = 1;
		while (true) {
			start = System.currentTimeMillis();

			int tolerance = toleranceSlider.getValue();
			int[] targetColor = new int[] { redSlider.getValue(), greenSlider.getValue(), blueSlider.getValue() };
			field1.setText(String.format("Red: %d Green: %d%nBlue: %d Tolerance:%3d", targetColor[0], targetColor[1],
					targetColor[2], tolerance));
			camera.retrieve(mat);
			update = true;
			matImage.updateBufferedArray(mat);
			matImage.updateBitmapArray(targetColor, tolerance);

			matImage.detectBlobs();
			matImage.drawBoxes();

			colorIcon.setImage(matImage.getBufferedImage());
			bitmapIcon.setImage(matImage.getBitmapImage());

			this.setTitle("FPS: " + (FPS + prevFPS + beforePrevFPS) / 3);
			this.repaint();

			beforePrevFPS = prevFPS;
			prevFPS = FPS;
			FPS = Math.round((1000.0 / (System.currentTimeMillis() - start)));
		}

	}

	public static void main(String[] args) {
		new LiveVideoFeedHighFPS();
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		int[] pixel = new int[3];
		matImage.getBufferedImage().getRaster().getPixel(e.getX(), e.getY(), pixel);
		redSlider.setValue(pixel[0]);
		greenSlider.setValue(pixel[1]);
		blueSlider.setValue(pixel[2]);
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}
}
