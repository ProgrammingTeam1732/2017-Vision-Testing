package org.usfirst.frc.team1732.robot.vision;

import org.opencv.core.Mat;

import edu.wpi.cscore.AxisCamera;
import edu.wpi.cscore.CvSink;
import edu.wpi.cscore.CvSource;
import edu.wpi.cscore.VideoProperty;
import edu.wpi.first.wpilibj.CameraServer;

public class GearVision implements Runnable {

	// Center x and y of the gear peg in image

	private GearVisionPipeline	gearPipeline;
	private AxisCamera			camera;
	private static final String	AXIS_IP						= "axis-camera-1732.local";
	private static final String	CAMERA_NAME					= "Gear Camera";
	public static final int		WIDTH						= 320;
	public static final int		HEIGHT						= 240;
	public static final int		HORIZONTAL_FIELD_OF_VIEW	= 67;
	private CvSink				cvSink;
	private CvSource			outputStream;
	private Mat					mat;

	public GearPeg gearPeg = gearPipeline.getProbableGearPair();

	public GearVision() {
		gearPipeline = new GearVisionPipeline();
		camera = CameraServer.getInstance().addAxisCamera(AXIS_IP, CAMERA_NAME);
		// Set the resolution
		camera.setResolution(WIDTH, HEIGHT);
		camera.setFPS(15);

		// Get a CvSink. This will capture Mats from the camera
		cvSink = CameraServer.getInstance().getVideo();
		// Setup a CvSource. This will send images back to the Dashboard
		outputStream = CameraServer.getInstance().putVideo("Gear Vision", WIDTH, HEIGHT);

		// Mats are very memory expensive. Lets reuse this Mat.
		mat = new Mat();

		// axisCamera.setWhiteBalanceManual(50);
		// axisCamera.setBrightness(0);
		// axisCamera.setExposureManual(0);
		// axisCamera.setPixelFormat(VideoMode.PixelFormat.);
		// pixel mode, width, height, fps:
		// axisCamera.setVideoMode(new VideoMode(VideoMode.PixelFormat.kBGR, 0,
		// 0, 0))
		VideoProperty[] props = camera.enumerateProperties();
		for (VideoProperty p : props) {
			System.out.println(p.getString());
		}
	}

	@Override
	public void run() {
		// Creates the pipeline object
		// This cannot be 'true'. The program will never exit if it is. This
		// lets the robot stop this thread when restarting robot code or
		// deploying.
		while (!Thread.interrupted()) {
			// Tell the CvSink to grab a frame from the camera and put it
			// in the source mat. If there is an error notify the output.
			if (cvSink.grabFrame(mat) == 0) {
				// Send the output the error.
				outputStream.notifyError(cvSink.getError());
				// skip the rest of the current iteration
				continue;
			}
			// Process the image
			gearPipeline.process(mat);
			gearPeg = gearPipeline.getProbableGearPair();
			// Give the output stream a new image to display
			outputStream.putFrame(mat);
		}
	}

}