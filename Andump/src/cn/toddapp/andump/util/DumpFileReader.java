package cn.toddapp.andump.util;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

public class DumpFileReader {

	private String dumpFilePath;
	private int currentPosition;
	private DataInputStream stream;

	public DumpFileReader(String dumpFilePath) throws IOException {
		this.dumpFilePath = dumpFilePath;
		this.stream = new DataInputStream(new BufferedInputStream(
				new FileInputStream(this.dumpFilePath)));
	}
	
	public DumpFileReader(DataInputStream stream){
		this.stream = stream;
	}

	public ArrayList<DumpPackageHeader> getDumpPackageHeaders(int position,
			int numbers) throws IOException {
		ArrayList<DumpPackageHeader> list = new ArrayList<DumpPackageHeader>();
		if (stream.skipBytes(position) != position) {
			throw new IOException();
		}
		this.setCurrentPosition(position);
		for (int i = 0; i < numbers; i++) {
			DumpPackageHeader header = readDumpPackageHeader(stream);
			if (header == null)
				return list;
			list.add(header);
			this.increaseCurrentPosition(16);
			int length = header.getActualLength();
			if (stream.skipBytes(length) != length) {
				return list;
			}
			this.increaseCurrentPosition(length);
		}
		stream.close();
		return list;
	}

	public void printDumpPackageHeaderInfo(DumpPackageHeader header) {
		//System.out.println("\nDumpPackageHeader info:");
		//System.out.println("\tposition : " + header.getPosition());
		//System.out.println("\tgmtTime : " + header.getGmtTime());
		//System.out.println("\tmicroTime : " + header.getMicroTime());
		//System.out.println("\tactualLength : " + header.getActualLength());
	}

	private DumpPackageHeader readDumpPackageHeader(DataInputStream stream) {
		byte[] headerBytes = new byte[16];
		try {
			if (stream.read(headerBytes, 0, 16) != 16)
				return null;
			// printBytes(headerBytes);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		DumpPackageHeader header;
		// process the bytes
		int position = getCurrentPosition();
		int gmtTime = bytesToInt(headerBytes, 0);
		int microTime = bytesToInt(headerBytes, 4);
		int dumpLength = bytesToInt(headerBytes, 8);
		int actualLength = bytesToInt(headerBytes, 12);
		if (dumpLength != actualLength)
			return null;
		header = new DumpPackageHeader(position, gmtTime, microTime,
				dumpLength, actualLength);
		//printDumpPackageHeaderInfo(header);
		return header;
	}

	private int getCurrentPosition() {
		return currentPosition;
	}

	private void setCurrentPosition(int currentPosition) {
		this.currentPosition = currentPosition;
	}

	private void increaseCurrentPosition(int increment) {
		this.currentPosition += increment;
	}

	private int bytesToInt(byte[] bytes, int start) {
		// reference http://jackle-liu.iteye.com/blog/51487
		int result = bytes[start] & 0xFF;
		result |= ((bytes[start + 1] << 8) & 0xFF00);
		result |= ((bytes[start + 2] << 16) & 0xFF0000);
		result |= ((bytes[start + 3] << 24) & 0xFF000000);
		return result;
	}

}
