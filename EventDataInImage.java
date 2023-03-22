package com.videonetics.media.event;

import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.videonetics.model.output.VRectangle;

public class EventDataInImage implements Serializable {

	private static final Logger LOGGER = Logger.getLogger(EventDataInImage.class.getName());
	
	private short topLeftCol;
	private short topLeftRow;
	private short buttomRightCol;
	private short buttomRightRow;

	private long evidenceTimeStamp_1;
	private short topLeftCol_1;
	private short topLeftRow_1;
	private short buttomRightCol_1;
	private short buttomRightRow_1;

	private long evidenceTimeStamp_2;
	private short topLeftCol_2;
	private short topLeftRow_2;
	private short buttomRightCol_2;
	private short buttomRightRow_2;

	private long evidenceTimeStamp_3;
	private short topLeftCol_3;
	private short topLeftRow_3;
	private short buttomRightCol_3;
	private short buttomRightRow_3;

	private int rgbWidth;
	private int rgbHeight;

	public VRectangle getLpRectangle() {
		VRectangle rectangle = null;
		try {
			rectangle = new VRectangle(topLeftCol, topLeftRow, buttomRightCol - topLeftCol + 2, buttomRightRow - topLeftRow + 2);
		} catch (Exception e) {
			Object[] params = new Object[] {e.getMessage()};
			LOGGER.log(Level.WARNING, "error: {}", params);
		}

		return rectangle;
	}

	public short getTopLeftCol() {
		return topLeftCol;
	}
	public void setTopLeftCol(short topLeftCol) {
		this.topLeftCol = topLeftCol;
	}
	public short getTopLeftRow() {
		return topLeftRow;
	}
	public void setTopLeftRow(short topLeftRow) {
		this.topLeftRow = topLeftRow;
	}
	public short getButtomRightCol() {
		return buttomRightCol;
	}
	public void setButtomRightCol(short buttomRightCol) {
		this.buttomRightCol = buttomRightCol;
	}
	public short getButtomRightRow() {
		return buttomRightRow;
	}
	public void setButtomRightRow(short buttomRightRow) {
		this.buttomRightRow = buttomRightRow;
	}
	public long getEvidenceTimeStamp_1() {
		return evidenceTimeStamp_1;
	}
	public void setEvidenceTimeStamp_1(long evidenceTimeStamp_1) {
		this.evidenceTimeStamp_1 = evidenceTimeStamp_1;
	}
	public short getTopLeftCol_1() {
		return topLeftCol_1;
	}
	public void setTopLeftCol_1(short topLeftCol_1) {
		this.topLeftCol_1 = topLeftCol_1;
	}
	public short getTopLeftRow_1() {
		return topLeftRow_1;
	}
	public void setTopLeftRow_1(short topLeftRow_1) {
		this.topLeftRow_1 = topLeftRow_1;
	}
	public short getButtomRightCol_1() {
		return buttomRightCol_1;
	}
	public void setButtomRightCol_1(short buttomRightCol_1) {
		this.buttomRightCol_1 = buttomRightCol_1;
	}
	public short getButtomRightRow_1() {
		return buttomRightRow_1;
	}
	public void setButtomRightRow_1(short buttomRightRow_1) {
		this.buttomRightRow_1 = buttomRightRow_1;
	}
	public long getEvidenceTimeStamp_2() {
		return evidenceTimeStamp_2;
	}
	public void setEvidenceTimeStamp_2(long evidenceTimeStamp_2) {
		this.evidenceTimeStamp_2 = evidenceTimeStamp_2;
	}
	public short getTopLeftCol_2() {
		return topLeftCol_2;
	}
	public void setTopLeftCol_2(short topLeftCol_2) {
		this.topLeftCol_2 = topLeftCol_2;
	}
	public short getTopLeftRow_2() {
		return topLeftRow_2;
	}
	public void setTopLeftRow_2(short topLeftRow_2) {
		this.topLeftRow_2 = topLeftRow_2;
	}
	public short getButtomRightCol_2() {
		return buttomRightCol_2;
	}
	public void setButtomRightCol_2(short buttomRightCol_2) {
		this.buttomRightCol_2 = buttomRightCol_2;
	}
	public short getButtomRightRow_2() {
		return buttomRightRow_2;
	}
	public void setButtomRightRow_2(short buttomRightRow_2) {
		this.buttomRightRow_2 = buttomRightRow_2;
	}
	public long getEvidenceTimeStamp_3() {
		return evidenceTimeStamp_3;
	}
	public void setEvidenceTimeStamp_3(long evidenceTimeStamp_3) {
		this.evidenceTimeStamp_3 = evidenceTimeStamp_3;
	}
	public short getTopLeftCol_3() {
		return topLeftCol_3;
	}
	public void setTopLeftCol_3(short topLeftCol_3) {
		this.topLeftCol_3 = topLeftCol_3;
	}
	public short getTopLeftRow_3() {
		return topLeftRow_3;
	}
	public void setTopLeftRow_3(short topLeftRow_3) {
		this.topLeftRow_3 = topLeftRow_3;
	}
	public short getButtomRightCol_3() {
		return buttomRightCol_3;
	}
	public void setButtomRightCol_3(short buttomRightCol_3) {
		this.buttomRightCol_3 = buttomRightCol_3;
	}
	public short getButtomRightRow_3() {
		return buttomRightRow_3;
	}
	public void setButtomRightRow_3(short buttomRightRow_3) {
		this.buttomRightRow_3 = buttomRightRow_3;
	}
	public int getRgbWidth() {
		return rgbWidth;
	}
	public void setRgbWidth(int rgbWidth) {
		this.rgbWidth = rgbWidth;
	}
	public int getRgbHeight() {
		return rgbHeight;
	}
	public void setRgbHeight(int rgbHeight) {
		this.rgbHeight = rgbHeight;
	}

	@Override
	public String toString() {
		return "EventDataInImage [lpRect=" + getLpRectangle()
				+ ", topLeftCol="+ topLeftCol + ", topLeftRow="+ topLeftRow + ", buttomRightCol=" + buttomRightCol
				+ ", buttomRightRow=" + buttomRightRow
				+ ", evidenceTimeStamp_1=" + evidenceTimeStamp_1
				+ ", topLeftCol_1=" + topLeftCol_1 + ", topLeftRow_1="
				+ topLeftRow_1 + ", buttomRightCol_1=" + buttomRightCol_1
				+ ", buttomRightRow_1=" + buttomRightRow_1
				+ ", evidenceTimeStamp_2=" + evidenceTimeStamp_2
				+ ", topLeftCol_2=" + topLeftCol_2 + ", topLeftRow_2="
				+ topLeftRow_2 + ", buttomRightCol_2=" + buttomRightCol_2
				+ ", buttomRightRow_2=" + buttomRightRow_2
				+ ", evidenceTimeStamp_3=" + evidenceTimeStamp_3
				+ ", topLeftCol_3=" + topLeftCol_3 + ", topLeftRow_3="
				+ topLeftRow_3 + ", buttomRightCol_3=" + buttomRightCol_3
				+ ", buttomRightRow_3=" + buttomRightRow_3 + ", rgbWidth="
				+ rgbWidth + ", rgbHeight=" + rgbHeight + "]";
	}


}
