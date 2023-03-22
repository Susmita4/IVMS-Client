package com.videonetics.model.output;

import java.awt.Point;
import java.io.Serializable;

public class VRectangle implements Serializable {

	private int rectX;
	private int rectY;
	private int rectW;
	private int rectH;

	private Point bottomLeft;
	private Point topRight;

	public VRectangle(int rectX, int rectY, int rectW, int rectH) {
		this.rectX = rectX;
		this.rectY = rectY;
		this.rectW = rectW;
		this.rectH = rectH;

		bottomLeft = new Point(rectX, rectY + rectH);
		topRight = new Point(rectX + rectW, rectY);
	}

	public VRectangle(Point bottomLeft, Point topRight) {
		this.bottomLeft = bottomLeft;
		this.topRight = topRight;

		this.rectX = bottomLeft.x;
		this.rectY = topRight.y;
		this.rectW = topRight.x - bottomLeft.x;
		this.rectH = bottomLeft.y - topRight.y;
	}
	
	public int getX1() {
		return rectX;
	}
	
	public int getY1() {
		return rectY;
	}
	
	public int getX2() {
		return rectX + rectW;
	}
	
	public int getY2() {
		return rectY;
	}
	
	public int getX3() {
		return rectX + rectW;
	}
	
	public int getY3() {
		return rectY + rectH;
	}
	
	public int getX4() {
		return rectX;
	}
	
	public int getY4() {
		return rectY + rectH;
	}

	/**
	 * @return the rectX
	 */
	public int getRectX() {
		return rectX;
	}
	/**
	 * @param rectX the rectX to set
	 */
	public void setRectX(int rectX) {
		this.rectX = rectX;
	}
	/**
	 * @return the rectY
	 */
	public int getRectY() {
		return rectY;
	}
	/**
	 * @param rectY the rectY to set
	 */
	public void setRectY(int rectY) {
		this.rectY = rectY;
	}
	/**
	 * @return the rectW
	 */
	public int getRectW() {
		return rectW;
	}
	/**
	 * @param rectW the rectW to set
	 */
	public void setRectW(int rectW) {
		this.rectW = rectW;
	}
	/**
	 * @return the rectH
	 */
	public int getRectH() {
		return rectH;
	}
	/**
	 * @param rectH the rectH to set
	 */
	public void setRectH(int rectH) {
		this.rectH = rectH;
	}

	public Point getBottomLeft() {
		return bottomLeft;
	}

	public void setBottomLeft(Point bottomLeft) {
		this.bottomLeft = bottomLeft;
	}

	public Point getTopRight() {
		return topRight;
	}

	public void setTopRight(Point topRight) {
		this.topRight = topRight;
	}

	@Override
	public String toString() {
		return "VRectangle [rectX=" + rectX + ", rectY=" + rectY + ", rectW=" + rectW + ", rectH=" + rectH
				+ ", bottomLeft=" + bottomLeft + ", topRight=" + topRight + "]";
	}
}
