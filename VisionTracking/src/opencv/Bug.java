package opencv;

import java.util.ArrayList;

public class Bug {
	// Uses moore neighborhood tracing algorithm

	private int[][] tesselation;
	private static int minArea = 1;

	public ArrayList<BoundingBox> boundingBoxes = new ArrayList<>();

	public Bug(int[][] aTesselation) {
		tesselation = aTesselation;
		clearEdges();
		findBoxes();
	}

	public static void setMinArea(int aMinArea) {
		minArea = aMinArea;
	}

	public BoundingBox getLargestBox() {
		BoundingBox largest = boundingBoxes.get(0);
		for (BoundingBox box : boundingBoxes) {
			if (box.compareTo(largest) == 1) {
				largest = box;
			}
		}
		return largest;
	}

	private void clearEdges() {
		for (int i = 0; i < tesselation[0].length; i++)
			tesselation[0][i] = 0;
		for (int i = 0; i < tesselation[tesselation.length - 1].length; i++)
			tesselation[0][i] = 0;
		for (int i = 0; i < tesselation.length; i++)
			tesselation[i][0] = 0;
		for (int i = 0; i < tesselation.length; i++)
			tesselation[i][tesselation[i].length - 1] = 0;
	}

	private void findBoxes() {
		int startColumn = 1;
		int startRow = 1;
		loop: for (int row = 1; row < tesselation.length - 1; row++) {
			for (int col = 1; col < tesselation[row].length - 1; col++) {
				if (tesselation[row][col] == 1) {
					startColumn = col;
					startRow = row;
					break loop;
				}
			}
		}
		BoundingBox aBox;
		for (int row = startRow; row < tesselation.length; row++) {
			for (int col = startColumn; col < tesselation[row].length; col++) {
				boolean contained = false;
				for (BoundingBox box : boundingBoxes) {
					if (box.contains(row, col)) {
						contained = true;
						break;
					}
				}
				if (!contained) {
					aBox = findABox(col, row);
					if (aBox.getArea() >= minArea)
						boundingBoxes.add(findABox(startColumn, startRow));
				}
			}
		}
	}

	private static final int[][] directions = { { 0, 1 }, { -1, 0 }, { 0, -1 }, { 1, 0 } };

	// uses the moore tracing algorithm
	private BoundingBox findABox(int startColumn, int startRow) {

		BoundingBox box = new BoundingBox(startColumn, startRow, startColumn, startRow);

		int startDirection = 0;
		int direction = startDirection;

		int row = startRow;
		int column = startColumn;

		// assume that you start on a white pixel with a black pixel to the
		// left
		do {
			box.updateBounds(row, column);
			// backtrack to the pixel previously entered (should always be
			// black)
			row -= directions[direction][0];
			column -= directions[direction][1];
			// start checking for white pixels around the current pixels
			// turn left
			direction = getDirectionIndex(direction - 1, directions);
			// move forward
			row += directions[direction][0];
			column = directions[direction][1];
			if (tesselation[row][column] == 1)
				continue;
			for (int i = 0; i < 3; i++) {
				// turn right
				direction = getDirectionIndex(direction + 1, directions);
				// move forward
				row += directions[direction][0];
				column = directions[direction][1];
				if (tesselation[row][column] == 1)
					continue;
				// move forward
				row += directions[direction][0];
				column = directions[direction][1];
				if (tesselation[row][column] == 1)
					continue;
			}
			// if the loop makes it to here there are no white pixels
			// surrounding the initial white pixel(pRow, pColumn)
			// turn right
			direction = getDirectionIndex(direction + 1, directions);
			// move forward
			row += directions[direction][0];
			column = directions[direction][1];
			// turn right
			direction = getDirectionIndex(direction + 1, directions);
			// move forward
			row += directions[direction][0];
			column = directions[direction][1];

		} while (direction != startDirection && column != startColumn && row != startRow);

		return box;

	}

	private static int getDirectionIndex(int index, int[][] array) {
		return (index + array.length) % array.length;
	}

	private class BoundingBox {
		public int left;
		public int top;
		public int right;
		public int bottom;

		public BoundingBox(int left, int top, int right, int bottom) {
			this.left = left;
			this.top = top;
			this.right = right;
			this.bottom = bottom;
		}

		public void updateBounds(int row, int col) {
			this.left = Math.min(this.left, col);
			this.top = Math.min(this.top, row);
			this.right = Math.max(this.right, col);
			this.bottom = Math.max(this.bottom, row);
		}

		public boolean contains(int row, int col) {
			return left <= col && right >= col && top <= row && bottom >= row;
		}

		public int getWidth() {
			return right - left;
		}

		public int getHeight() {
			return bottom - top;
		}

		public int getArea() {
			return getWidth() * getHeight();
		}

		public int compareTo(BoundingBox box) {
			int area1 = this.getArea();
			int area2 = box.getArea();
			return area1 > area2 ? -1 : area1 < area2 ? 1 : 0;
		}
	}

}