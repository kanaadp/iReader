package net.parvate.iReader.imageproc;

import java.util.ArrayList;

public class Line {

	private int width;
	private int height;
	private int x1;
	private int x2;
	private int y1;
	private int y2;

	ArrayList<Integer> coords = new ArrayList<Integer>();

	public Line(int leftmostpixel, int rightmostpixel, int topmostpixel,
			int bottommostpixel) {

		width = rightmostpixel - leftmostpixel;
		height = bottommostpixel - topmostpixel;
		x1 = leftmostpixel;
		x2 = rightmostpixel;
		y1 = topmostpixel;
		y2 = bottommostpixel;
		coords.add(x1);
		coords.add(x2);
		coords.add(y1);
		coords.add(y2);
	}

	public int getHeight() {
		return height;
	}

	public int getWidth() {
		return width;
	}

	public int getTopMostPixel() {
		return y1;
	}

	public int getBottomMostPixel() {
		return y2;
	}

	public int getLeftMostPixel() {
		return x1;
	}

	public int getRightMostPixel() {
		return x2;
	}

	public ArrayList<Integer> getCoords() {

		return coords;
	}

	public static Line mergeLine(Line line1, Line line2) {

		System.out.println("Merging");
		int newx1 = 0;
		int newx2 = 0;
		int newy1 = 0;
		int newy2 = 0;

		if (line1.y1 > line2.y1) {
			newy1 = line2.y1;
		} else if (line1.y1 <= line2.y1) {
			newy1 = line1.y1;

		}
		if (line1.y2 < line2.y2) {
			newy2 = line2.y2;
		} else if (line1.y2 >= line2.y2) {
			newy2 = line1.y2;
		}

		if (line1.x1 > line2.x1) {
			newx1 = line2.x1;
		} else if (line1.x1 <= line2.x1) {
			newx1 = line1.x1;
		}

		if (line1.x2 > line2.x2) {
			newx2 = line1.x2;
		} else if (line1.x2 <= line2.x2) {
			newx2 = line2.x2;
		}

		Line newline = null;

		newline = new Line(newx1, newx2, newy1, newy2);

		return newline;
	}

	public String toString() {
		return x1 + ":" + x2 + ":" + y1 + ":" + y2;
	}

}
