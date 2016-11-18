package opencv;

import java.util.ArrayList;

public class BoxFinder {

	private int[][] bitmap;
	// public ArrayList<BoundingBox> allBoundingBoxes = new ArrayList<>();
	public ArrayList<BoundingBox>	goodBoundingBoxes	= new ArrayList<>();
	private static int				MIN_AREA			= 40;
	private static final int		BLACK				= 0;
	private static final int		WHITE				= 0xFFFFFF;
	private static final int		PRECISION			= 10;

	public BoxFinder(int[][] aBitmap) {
		bitmap = aBitmap;
		clearEdges();
		findBoxes();
	}

	public BoundingBox getLargestBox() {
		return goodBoundingBoxes.size() == 0 ? null : goodBoundingBoxes.get(0);
	}

	public static void setMinArea(int aMinArea) {
		MIN_AREA = aMinArea;
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

	private void findBoxes() {
		int startColumn = 1;
		int startRow = 1;
		findStart: for (int row = startRow; row < bitmap.length - 1; row++) {
			for (int col = startColumn; col < bitmap[0].length - 1; col++) {
				if (bitmap[row][col] == WHITE) {
					startColumn = col;
					startRow = row;
					break findStart;
				}
			}
		}
		for (int row = startRow; row < bitmap.length - 1; row++) {
			for (int col = startColumn; col < bitmap[0].length - 1; col++) {
				if (bitmap[row][col] == WHITE) {// && !containedInABox(row,
												// col)) {
					BoundingBox box = findABox(row, col);
					// allBoundingBoxes.add(box);
					box.fillBox(bitmap);
					if (box.getArea() > MIN_AREA) {
						int i = 0;
						for (; i < goodBoundingBoxes.size(); i++) {
							if (box.getArea() >= goodBoundingBoxes.get(i).getArea()) {
								break;
							}
						}
						goodBoundingBoxes.add(i, box);
					}
				}
			}
		}

	}

	// private boolean containedInABox(int row, int col) {
	// for (BoundingBox box : allBoundingBoxes) {
	// if (box.contains(row, col)) {
	// return true;
	// }
	// }
	// return false;
	// }

	private BoundingBox findABox(int startRow, int startColumn) {
		BoundingBox box = new BoundingBox(startColumn, startRow, startColumn, startRow);
		while (!box.checkBounds(bitmap, PRECISION)) {
		}
		return box;
	}

	public class BoundingBox {
		public int	colMin;
		public int	rowMin;
		public int	colMax;
		public int	rowMax;

		public BoundingBox(int aColMin, int aRowMin, int aColMax, int aRowMax) {
			this.colMin = aColMin;
			this.rowMin = aRowMin;
			this.colMax = aColMax;
			this.rowMax = aRowMax;
		}

		public boolean contains(int row, int col) {
			return (row >= rowMin && row <= rowMax && col >= colMin && col <= colMax);
		}

		public boolean checkBounds(int[][] bitmap, int precision) {
			// check corners
			for (int count = 0; count < 2; count++) {
				if (!(rowMin - precision < 0 || rowMax + precision >= bitmap.length || colMin - precision < 0
						|| colMax + precision >= bitmap[bitmap.length - 1].length)) {
					if (bitmap[rowMin - precision][colMin - precision] == WHITE) {// top
																					// left
						rowMin -= precision;
						colMin -= precision;
						return false;
					}
					if (bitmap[rowMin - precision][colMax + precision] == WHITE) { // top
																					// right
						rowMin -= precision;
						colMax += precision;
						return false;
					}
					if (bitmap[rowMax + precision][colMin - precision] == WHITE) { // bottom
																					// left
						rowMax += precision;
						colMin -= precision;
						return false;
					}
					if (bitmap[rowMax + precision][colMax + precision] == WHITE) { // bottom
																					// right
						rowMax += precision;
						colMax += precision;
						return false;
					}
					// checks left and right
					for (int i = rowMin; i <= rowMax; i++) {
						if (bitmap[i][colMin - precision] == WHITE) {
							colMin -= precision; // expand box
							return false;
						}
						if (bitmap[i][colMax + precision] == WHITE) {
							colMax += precision; // expand box
							return false;
						}
					}
					// checks top and bottom
					for (int j = colMin; j <= colMax; j++) {
						if (bitmap[rowMin - precision][j] == WHITE) {
							rowMin -= precision;
							return false;
						}
						if (bitmap[rowMax + precision][j] == WHITE) {
							rowMax += precision;
							return false;
						}
					}
				}
				precision = 1;
			}
			return true;
		}

		public void fillBox(int[][] bitmap) {
			for (int i = rowMin; i <= rowMax; i++) {
				for (int j = colMin; j <= colMax; j++) {
					bitmap[i][j] = bitmap[i][j] == WHITE ? WHITE - 1 : 0;
				}
			}
		}

		public int getWidth() {
			return colMax - colMin;
		}

		public int getHeight() {
			return rowMax - rowMin;
		}

		public int getArea() {
			return getWidth() * getHeight();
		}

		@Override
		public String toString() {
			return String.format("left: %d, top: %d, right: %d, bottom: %d", colMin, rowMin, colMax, rowMax);
		}

	}

}