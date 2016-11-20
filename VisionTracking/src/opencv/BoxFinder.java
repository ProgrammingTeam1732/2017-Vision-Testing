package opencv;

import java.util.ArrayList;

public class BoxFinder {

	private int[][] bitmap;
	// public ArrayList<BoundingBox> allBoundingBoxes = new ArrayList<>();
	public ArrayList<BoundingBox>	goodBoundingBoxes	= new ArrayList<>();
	private static int				MIN_AREA =40;
	private static final int		BLACK				= 0;
	private static final int		WHITE				= 0xFFFFFF;
	private static final int		PRECISION			= 4;

	public BoxFinder(int[][] aBitmap) {
		bitmap = aBitmap;
		clearEdges();
		findBoxes();
	}

	public BoundingBox getLargestBox() {
		return goodBoundingBoxes.size() == 0 ? null : goodBoundingBoxes.get(0);
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
			for (int p = precision; p > 0; p--) {
				if (!(rowMin - p < 0 || rowMax + p >= bitmap.length || colMin - p < 0
						|| colMax + p >= bitmap[bitmap.length - 1].length)) {
					if (bitmap[rowMin - p][colMin - p] == WHITE) {// top
																					// left
						rowMin -= p;
						colMin -= p;
						return false;
					}
					if (bitmap[rowMin - p][colMax + p] == WHITE) { // top
																					// right
						rowMin -= p;
						colMax += p;
						return false;
					}
					if (bitmap[rowMax + p][colMin - p] == WHITE) { // bottom
																					// left
						rowMax += p;
						colMin -= p;
						return false;
					}
					if (bitmap[rowMax + p][colMax + p] == WHITE) { // bottom
																					// right
						rowMax += p;
						colMax += p;
						return false;
					}
					// checks left and right
					for (int i = rowMin; i <= rowMax; i++) {
						if (bitmap[i][colMin - p] == WHITE) {
							colMin -= p; // expand box
							return false;
						}
						if (bitmap[i][colMax + p] == WHITE) {
							colMax += p; // expand box
							return false;
						}
					}
					// checks top and bottom
					for (int j = colMin; j <= colMax; j++) {
						if (bitmap[rowMin - p][j] == WHITE) {
							rowMin -= p;
							return false;
						}
						if (bitmap[rowMax + p][j] == WHITE) {
							rowMax += p;
							return false;
						}
					}
				}
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