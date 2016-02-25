package edu.ucla.cs.cs144;

/* 
 * SearchRegion object represents a rectangular geographic region.
 * (lx, ly): (latitude, longitude) of the lower left corner of the rectangle
 * (rx, ry): (latitude, longitude) of the upper right corner of the rectangle
 */

public class SearchRegion {	
	private double lx, ly, rx, ry;
	
	public SearchRegion() {}
	
	public SearchRegion(double lx, double ly, double rx, double ry) {
		this.lx = lx;
		this.ly = ly;
		this.rx = rx;
		this.ry = ry;
	}

	public double getLx() {
		return lx;
	}

	public double getLy() {
		return ly;
	}

	public double getRx() {
		return rx;
	}

	public double getRy() {
		return ry;
	}

	public void setLx(double lx) {
		this.lx = lx;
	}

	public void setLy(double ly) {
		this.ly = ly;
	}

	public void setRx(double rx) {
		this.rx = rx;
	}

	public void setRy(double ry) {
		this.ry = ry;
	}
}
