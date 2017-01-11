package opencv;

import java.util.ArrayList;

public class BlobFinder {

	private int[][]				bitmap;
	private ArrayList<Blob>		goodBlobs	= new ArrayList<>();
	private static int			MIN_AREA	= 40;
	private static final int	BLACK		= 0;
	private static final int	WHITE		= 0xFFFFFF;
	private static final int	PRECISION	= 4;

	public BlobFinder(int[][] aBitmap) {
		bitmap = aBitmap;
	}

	public ArrayList<Blob> findBlobs() {
		clearEdges();
		// code
		return goodBlobs;
	}

	public class Blob {

		private BoundingBox	boundingBox;
		private int			startRow, startColumn;

		public Blob(int row, int column) {
			boundingBox = new BoundingBox(row, column, row, column);
			startRow = row;
			startColumn = column;
		}

		public void expandBlob(int[][] bitmap) {
			if (bitmap[startRow - 1][startColumn] == WHITE) {
				expand(new EdgePoint(startRow, startColumn, true, -1), bitmap);
			}
			if (bitmap[startRow + 1][startColumn] == WHITE) {
				expand(new EdgePoint(startRow, startColumn, true, 1), bitmap);
			}
			if (bitmap[startRow][startColumn - 1] == WHITE) {
				expand(new EdgePoint(startRow, startColumn, false, -1), bitmap);
			}
			if (bitmap[startRow][startColumn + 1] == WHITE) {
				expand(new EdgePoint(startRow, startColumn, false, 1), bitmap);
			}
		}

		public void expand(EdgePoint source, int[][] bitmap) {

		}

	}

	private void clearEdges() {
		for (int j = 0; j < bitmap[0].length; j++) {
			bitmap[0][j] = BLACK;
			bitmap[bitmap.length - 1][j] = BLACK;
		}
		for (int i = 0; i < bitmap.length; i++) {
			bitmap[i][0] = BLACK;
			bitmap[i][bitmap[0].length - 1] = BLACK;
		}
	}

	public class EdgePoint {

		private final int		row, column;
		private final boolean	horizontal;
		private final int		direction;

		public EdgePoint(int aRow, int aColumn, boolean isHorizontal, int aDirection) {
			row = aRow;
			column = aColumn;
			horizontal = isHorizontal;
			direction = aDirection;
		}

		public int getRowDecreaseChange() {
			if (horizontal)
				return direction == -1 ? direction : 0;
			else
				return -1;
		}

		public int getRowIncreaseChange() {
			if (horizontal)
				return direction == 1 ? direction : 0;
			else
				return 1;
		}

		public int getColumnDecrease() {
			if (horizontal)
				return -1;
			else
				return direction == -1 ? direction : 0;
		}

		public int getColumnIncrease() {
			if (horizontal)
				return 1;
			else
				return direction == 1 ? direction : 0;
		}

		public boolean isHorizontal() {
			return horizontal;
		}

		public int getDirection() {
			return direction;
		}
	}

	public class BoundingBox {

		private int minRow, minColumn, maxRow, maxColumn;

		public BoundingBox(int aMinRow, int aMinColumn, int aMaRowRow, int aMaRowColumn) {
			minRow = aMinRow;
			minColumn = aMinColumn;
			maxRow = aMaRowRow;
			maxColumn = aMaRowColumn;
		}

		public int getWidth() {
			return maxRow - minRow;
		}

		public int getHeight() {
			return maxColumn - minColumn;
		}

		public int getArea() {
			return getWidth() * getHeight();
		}

		public void expandBounds(int row, int column) {
			if (row < minRow)
				minRow = row;
			else if (row > maxRow)
				maxRow = row;
			if (column < minColumn)
				minColumn = column;
			else if (column > maxColumn)
				maxColumn = column;
		}

		public boolean contains(int row, int column) {
			return minRow <= row && row <= maxRow && minColumn <= column && column <= maxColumn;
		}

	}

}