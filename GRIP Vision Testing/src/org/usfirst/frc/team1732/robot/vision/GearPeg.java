package org.usfirst.frc.team1732.robot.vision;

import org.opencv.core.MatOfPoint;
import org.opencv.core.Rect;

public class GearPeg {

	public final MatOfPoint		left;
	public final MatOfPoint		right;
	public final Rect			leftRect;
	public final Rect			rightRect;
	private static final double	GEAR_TARGET_WIDTH_INCHES	= 10.15;

	public GearPeg(MatOfPoint a, Rect aRect, MatOfPoint b, Rect bRect) {
		if (aRect.x < bRect.x) {
			leftRect = aRect;
			left = a;
			rightRect = bRect;
			right = b;
		} else {
			leftRect = bRect;
			left = b;
			rightRect = aRect;
			right = a;
		}
	}

	/**
	 * @return a score (-infinite to 500) of how likely this pair is the gear
	 *         peg (500 = very likely)
	 */
	public double getScore() {
		// all doubles to avoid floor rounding
		double totalWidth = getTotalWidth();
		// left width should be 2/10.25 = 8/41 of total width
		double leftWidthScore = scaleScore(leftRect.width * (totalWidth * 2.0 / 10.25));
		// Difference between the left edges of the contours should be
		// 8/10.25 of total width
		double dLeft = rightRect.x - leftRect.x;
		double dLeftScore = scaleScore(dLeft / (totalWidth * 8.0 / 10.25));
		// Difference between the tops should be close to 0 relative to
		// height
		double dTop = leftRect.y - rightRect.y;
		double dTopScore = scaleScore(dTop / leftRect.height + 1);
		// Widths and heights should be about the same
		double widthRatioScore = scaleScore((double) leftRect.width / rightRect.width);
		double heightRatioScore = scaleScore((double) leftRect.height / rightRect.height);
		return leftWidthScore + dLeftScore + dTopScore + widthRatioScore + heightRatioScore;
	}

	private double scaleScore(double score) {
		return 100 - (100 * Math.abs(1 - score));
	}

	public int getCenterX() {
		// average the x cordinate
		return ((rightRect.x + rightRect.width) + leftRect.x) / 2;
	}

	public int getCenterY() {
		// subtract one half the height from the y value
		return (int) (((rightRect.y - rightRect.height * 0.5) + (leftRect.y - leftRect.height * 0.5)) / 2.0);
	}

	public int getTotalWidth() {
		return (rightRect.x + rightRect.width) - leftRect.x;
	}

	public double getHorizontalDistance(double horizontalViewAngle, int imageWidth) {
		double inchesFOV = GEAR_TARGET_WIDTH_INCHES * imageWidth / getTotalWidth();
		return inchesFOV / (2 * Math.tan(Math.toRadians(horizontalViewAngle / 2)));
	}

	public double getHorizontalAngle(double horizontalViewAngle, int imageWidth) {
		double percentage = (getCenterX() - (imageWidth / 2.0)) / imageWidth;
		return percentage * horizontalViewAngle;
	}

}