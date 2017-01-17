package org.usfirst.frc.team1732.robot.vision;

import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfInt;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import edu.wpi.first.wpilibj.vision.VisionPipeline;

/**
 * GripVisionTest class.
 *
 * <p>
 * An OpenCV pipeline generated by GRIP.
 *
 * @author GRIP
 */
public class GearVisionPipeline implements VisionPipeline {

	// Outputs
	// (might want to instead have only two mats - input and output - that get
	// reused)
	private Mat						hsvThresholdOutput		= new Mat();
	private Mat						cvErodeOutput			= new Mat();
	private ArrayList<MatOfPoint>	findContoursOutput		= new ArrayList<MatOfPoint>();
	private ArrayList<MatOfPoint>	filterContoursOutput	= new ArrayList<MatOfPoint>();
	private GearPeg					gearPair				= null;

	private double[]	hsvThresholdHue			= { 65, 99 };
	private double[]	hsvThresholdSaturation	= { 174, 255 };
	private double[]	hsvThresholdValue		= { 88, 208 };

	private double cvErodeIterations = 1.0;

	private double		filterContoursMinArea		= 400.0;
	private double		filterContoursMinPerimeter	= 0.0;
	private double		filterContoursMinWidth		= 0.0;
	private double		filterContoursMaxWidth		= 1000.0;
	private double		filterContoursMinHeight		= 0.0;
	private double		filterContoursMaxHeight		= 1000.0;
	private double[]	filterContoursSolidity		= { 0, 100.0 };
	private double		filterContoursMaxVertices	= 1000000.0;
	private double		filterContoursMinVertices	= 0.0;
	private double		filterContoursMinRatio		= 0.3;
	private double		filterContoursMaxRatio		= 0.5;

	static {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	}

	/**
	 * This is the primary method that runs the entire pipeline and updates the
	 * outputs.
	 */
	@Override
	public void process(Mat source0) {
		// Step HSV_Threshold0:
		Mat hsvThresholdInput = source0;
		hsvThreshold(hsvThresholdInput, hsvThresholdHue, hsvThresholdSaturation, hsvThresholdValue, hsvThresholdOutput);

		// Step CV_erode0:
		Mat cvErodeSrc = hsvThresholdOutput;
		Mat cvErodeKernel = new Mat();
		Point cvErodeAnchor = new Point(-1, -1);
		int cvErodeBordertype = Core.BORDER_CONSTANT;
		Scalar cvErodeBordervalue = new Scalar(-1);
		cvErode(cvErodeSrc, cvErodeKernel, cvErodeAnchor, cvErodeIterations, cvErodeBordertype, cvErodeBordervalue,
				cvErodeOutput);

		// Step Find_Contours0:
		Mat findContoursInput = cvErodeOutput;
		boolean findContoursExternalOnly = false;
		findContours(findContoursInput, findContoursExternalOnly, findContoursOutput);

		// Step Filter_Contours0:
		ArrayList<MatOfPoint> filterContoursContours = findContoursOutput;
		filterContours(	filterContoursContours, filterContoursMinArea, filterContoursMinPerimeter,
						filterContoursMinWidth, filterContoursMaxWidth, filterContoursMinHeight,
						filterContoursMaxHeight, filterContoursSolidity, filterContoursMaxVertices,
						filterContoursMinVertices, filterContoursMinRatio, filterContoursMaxRatio,
						filterContoursOutput);
		// Determine which of the contours
		gearPair = processFilteredContours(filterContoursOutput);
	}

	/**
	 * This method is a generated getter for the output of a HSV_Threshold.
	 * 
	 * @return Mat output from HSV_Threshold.
	 */
	public Mat hsvThresholdOutput() {
		return hsvThresholdOutput;
	}

	/**
	 * This method is a generated getter for the output of a CV_erode.
	 * 
	 * @return Mat output from CV_erode.
	 */
	public Mat cvErodeOutput() {
		return cvErodeOutput;
	}

	/**
	 * This method is a generated getter for the output of a Find_Contours.
	 * 
	 * @return ArrayList<MatOfPoint> output from Find_Contours.
	 */
	public ArrayList<MatOfPoint> findContoursOutput() {
		return findContoursOutput;
	}

	/**
	 * This method is a generated getter for the output of a Filter_Contours.
	 * 
	 * @return ArrayList<MatOfPoint> output from Filter_Contours.
	 */
	public ArrayList<MatOfPoint> filterContoursOutput() {
		return filterContoursOutput;
	}

	/**
	 * 
	 * @return the most likely gear pair, null if no gear pair found
	 */
	public GearPeg getProbableGearPair() {
		return gearPair;
	}

	/**
	 * Segment an image based on hue, saturation, and value ranges.
	 *
	 * @param input
	 *            The image on which to perform the HSL threshold.
	 * @param hue
	 *            The min and max hue
	 * @param sat
	 *            The min and max saturation
	 * @param val
	 *            The min and max value
	 * @param output
	 *            The image in which to store the output.
	 */
	private void hsvThreshold(Mat input, double[] hue, double[] sat, double[] val, Mat out) {
		Imgproc.cvtColor(input, out, Imgproc.COLOR_BGR2HSV);
		Core.inRange(out, new Scalar(hue[0], sat[0], val[0]), new Scalar(hue[1], sat[1], val[1]), out);
	}

	/**
	 * Expands area of lower value in an image.
	 * 
	 * @param src
	 *            the Image to erode.
	 * @param kernel
	 *            the kernel for erosion.
	 * @param anchor
	 *            the center of the kernel.
	 * @param iterations
	 *            the number of times to perform the erosion.
	 * @param borderType
	 *            pixel extrapolation method.
	 * @param borderValue
	 *            value to be used for a constant border.
	 * @param dst
	 *            Output Image.
	 */
	private void cvErode(Mat src, Mat kernel, Point anchor, double iterations, int borderType, Scalar borderValue,
			Mat dst) {
		if (kernel == null) {
			kernel = new Mat();
		}
		if (anchor == null) {
			anchor = new Point(-1, -1);
		}
		if (borderValue == null) {
			borderValue = new Scalar(-1);
		}
		Imgproc.erode(src, dst, kernel, anchor, (int) iterations, borderType, borderValue);
	}

	/**
	 * Sets the values of pixels in a binary image to their distance to the
	 * nearest black pixel.
	 * 
	 * @param input
	 *            The image on which to perform the Distance Transform.
	 * @param type
	 *            The Transform.
	 * @param maskSize
	 *            the size of the mask.
	 * @param output
	 *            The image in which to store the output.
	 */
	private void findContours(Mat input, boolean externalOnly, List<MatOfPoint> contours) {
		Mat hierarchy = new Mat();
		contours.clear();
		int mode;
		if (externalOnly) {
			mode = Imgproc.RETR_EXTERNAL;
		} else {
			mode = Imgproc.RETR_LIST;
		}
		int method = Imgproc.CHAIN_APPROX_SIMPLE;
		Imgproc.findContours(input, contours, hierarchy, mode, method);
	}

	/**
	 * Filters out contours that do not meet certain criteria.
	 * 
	 * @param inputContours
	 *            is the input list of contours
	 * @param output
	 *            is the the output list of contours
	 * @param minArea
	 *            is the minimum area of a contour that will be kept
	 * @param minPerimeter
	 *            is the minimum perimeter of a contour that will be kept
	 * @param minWidth
	 *            minimum width of a contour
	 * @param maxWidth
	 *            maximum width
	 * @param minHeight
	 *            minimum height
	 * @param maxHeight
	 *            maximimum height
	 * @param Solidity
	 *            the minimum and maximum solidity of a contour
	 * @param minVertexCount
	 *            minimum vertex Count of the contours
	 * @param maxVertexCount
	 *            maximum vertex Count
	 * @param minRatio
	 *            minimum ratio of width to height
	 * @param maxRatio
	 *            maximum ratio of width to height
	 */
	private void filterContours(List<MatOfPoint> inputContours, double minArea, double minPerimeter, double minWidth,
			double maxWidth, double minHeight, double maxHeight, double[] solidity, double maxVertexCount,
			double minVertexCount, double minRatio, double maxRatio, List<MatOfPoint> output) {
		final MatOfInt hull = new MatOfInt();
		output.clear();
		// operation
		for (int i = 0; i < inputContours.size(); i++) {
			final MatOfPoint contour = inputContours.get(i);
			final Rect bb = Imgproc.boundingRect(contour);
			if (bb.width < minWidth || bb.width > maxWidth)
				continue;
			if (bb.height < minHeight || bb.height > maxHeight)
				continue;
			final double area = Imgproc.contourArea(contour);
			if (area < minArea)
				continue;
			if (Imgproc.arcLength(new MatOfPoint2f(contour.toArray()), true) < minPerimeter)
				continue;
			Imgproc.convexHull(contour, hull);
			MatOfPoint mopHull = new MatOfPoint();
			mopHull.create((int) hull.size().height, 1, CvType.CV_32SC2);
			for (int j = 0; j < hull.size().height; j++) {
				int index = (int) hull.get(j, 0)[0];
				double[] point = new double[] { contour.get(index, 0)[0], contour.get(index, 0)[1] };
				mopHull.put(j, 0, point);
			}
			final double solid = 100 * area / Imgproc.contourArea(mopHull);
			if (solid < solidity[0] || solid > solidity[1])
				continue;
			if (contour.rows() < minVertexCount || contour.rows() > maxVertexCount)
				continue;
			final double ratio = bb.width / (double) bb.height;
			if (ratio < minRatio || ratio > maxRatio)
				continue;
			// If the contour passes all the restrictions, add it to the output
			output.add(contour);
		}
	}

	private GearPeg processFilteredContours(ArrayList<MatOfPoint> contours) {
		if (contours.size() < 2)
			return null;
		// determine which two of the counours actually are the correct two
		ArrayList<Rect> rectangles = new ArrayList<>(contours.size());
		for (MatOfPoint mop : contours) {
			rectangles.add(Imgproc.boundingRect(mop));
		}
		GearPeg bestPair = new GearPeg(contours.get(0), rectangles.get(0), contours.get(1), rectangles.get(1));
		GearPeg pair;
		for (int i = 0; i < contours.size() - 1; i++) {
			for (int j = i + 1; j < contours.size(); j++) {
				pair = new GearPeg(contours.get(i), rectangles.get(i), contours.get(j), rectangles.get(j));
				if (pair.getScore() > bestPair.getScore())
					bestPair = pair;
			}
		}
		return bestPair;
	}

}