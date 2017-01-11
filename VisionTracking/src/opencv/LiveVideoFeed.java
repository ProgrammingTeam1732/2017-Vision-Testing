package opencv;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextArea;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.highgui.VideoCapture;

public class LiveVideoFeed extends JFrame implements MouseListener {

	private static final long serialVersionUID = -2222565840007699833L;

	MatImage	matImage;
	JSlider		redSlider, greenSlider, blueSlider;

	public LiveVideoFeed() {
		String opencvpath = System.getProperty("user.dir") + "\\files\\";
		System.load(opencvpath + Core.NATIVE_LIBRARY_NAME + ".dll");
		// needed in order to run the axis camera
		if (Core.VERSION.equals("2.4.13.0"))
			System.load(opencvpath + "opencv_ffmpeg2413_64.dll");
		VideoCapture camera = new VideoCapture(1);
		// AxisCamera axisCamera = new AxisCamera("http://169.254.148.78/");
		if (!camera.open(1)) {
			System.out.println("Error");
		}
		// System.out.println(axisCamera.getMjpgURL());
		// axisCamera.setCompression(70);
		Mat mat = new Mat();
		camera.read(mat);
		matImage = new MatImage(mat);
		camera.set(38, 3); // 38 = CV_CAP_PROP_BUFFERSIZE

		ImageIcon colorIcon = new ImageIcon(matImage.getBufferedImage());
		JLabel colorLabel = new JLabel("", colorIcon, JLabel.LEFT);

		matImage.updateBitmapArray(new int[] { 255, 255, 255 }, 10);
		matImage.detectBlobs();
		matImage.drawBoxes();
		ImageIcon bitmapIcon = new ImageIcon(matImage.getBitmapImage());
		JLabel bitmapLabel = new JLabel("", bitmapIcon, JLabel.LEFT);

		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(true);
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

		long start = System.currentTimeMillis();
		long FPS = 1;
		long prevFPS = 1;
		long beforePrevFPS = 1;
		while (true) {
			start = System.currentTimeMillis();

			int tolerance = toleranceSlider.getValue();
			int[] targetColor = new int[] { redSlider.getValue(), greenSlider.getValue(), blueSlider.getValue() };
			field1.setText(String.format(	"Red: %d Green: %d%nBlue: %d Tolerance:%3d", targetColor[0], targetColor[1],
											targetColor[2], tolerance));

			camera.read(mat);
			matImage.updateBufferedArray(mat);
			matImage.updateBitmapArray(targetColor, tolerance);

			matImage.detectBlobs();
			matImage.drawBoxes();
			// b = resize(matImage.getBufferedImage(), 800, 600);
			colorIcon.setImage(matImage.getBufferedImage());// matImage.getBufferedImage())//resize(matImage.getBufferedImage(),
															// 640, 480));
			bitmapIcon.setImage(matImage.getBitmapImage());// resize(matImage.getBitmapImage(),
															// 640, 480));

			this.setTitle("FPS: " + (FPS + prevFPS + beforePrevFPS) / 3);
			this.repaint();

			beforePrevFPS = prevFPS;
			prevFPS = FPS;
			FPS = Math.round((1000.0 / (System.currentTimeMillis() - start)));
		}
	}

	public static BufferedImage resize(BufferedImage img, int newW, int newH) {
		Image tmp = img.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
		BufferedImage dimg = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_ARGB);

		Graphics2D g2d = dimg.createGraphics();
		g2d.drawImage(tmp, 0, 0, null);
		g2d.dispose();

		return dimg;
	}

	public static void main(String[] args) {
		new LiveVideoFeed();
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		int[] pixel = new int[3];
		matImage.getBufferedImage().getRaster().getPixel(e.getX(), e.getY(), pixel);
		redSlider.setValue(pixel[0]);
		greenSlider.setValue(pixel[1]);
		blueSlider.setValue(pixel[2]);
	}

	@Override
	public void mouseEntered(MouseEvent e) {}

	@Override
	public void mouseExited(MouseEvent e) {}

	@Override
	public void mousePressed(MouseEvent e) {}

	@Override
	public void mouseReleased(MouseEvent e) {}
}