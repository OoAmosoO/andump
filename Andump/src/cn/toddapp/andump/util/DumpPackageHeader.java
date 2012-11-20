package cn.toddapp.andump.util;

public class DumpPackageHeader {

	private int position;
	private int gmtTime;
	private int microTime;
	private int dumpLength;
	private int actualLength;

	public DumpPackageHeader(int position, int gmtTime, int microTime,
			int dumpLength, int actualLength) {
		this.position = position;
		this.gmtTime = gmtTime;
		this.microTime = microTime;
		this.dumpLength = dumpLength;
		this.actualLength = actualLength;
	}

	public int getPosition() {
		return position;
	}

	public int getGmtTime() {
		return gmtTime;
	}

	public int getMicroTime() {
		return microTime;
	}

	public int getDumpLength() {
		return dumpLength;
	}

	public int getActualLength() {
		return actualLength;
	}

}
