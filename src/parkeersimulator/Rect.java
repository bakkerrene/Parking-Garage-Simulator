package parkeersimulator;

/**
 * This class is used in CarparkView
 * It calculates the parkingspots rectangles
 * 
 * @author Rick Zwaneveld
 * @version 07/02/2017
 */


public class Rect {

	public int x, y;
	public int w, h;

	public Rect() {
		x = y = 0;
		w = h = 0;
	}
	public Rect(int x, int y, int w, int h) {
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
	}
}