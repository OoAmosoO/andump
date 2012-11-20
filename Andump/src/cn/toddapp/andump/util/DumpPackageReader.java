package cn.toddapp.andump.util;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import cn.toddapp.andump.layerReader.ArpLayerReader;
import cn.toddapp.andump.layerReader.EthernetLayerReader;
import cn.toddapp.andump.layerReader.ILayerReader;
import cn.toddapp.andump.layerReader.IpLayerReader;
import cn.toddapp.andump.layerReader.TcpLayerReader;
import cn.toddapp.andump.layerReader.UdpLayerReader;

public class DumpPackageReader {

	private String dumpFilePath;
	private DataInputStream stream;

	private DumpPackageHeader dumpPackageHeader;
	private ILayerReader linkLayerReader;
	private ILayerReader networkLayerReader;
	private ILayerReader transportLayerReader;

	public DumpPackageReader(String dumpFilePath, DumpPackageHeader header)
			throws IOException {

		this.dumpPackageHeader = header;
		this.dumpFilePath = dumpFilePath;
		this.stream = new DataInputStream(new BufferedInputStream(
				new FileInputStream(dumpFilePath)));
	}

	public boolean read() throws IOException {
		stream.skipBytes(dumpPackageHeader.getPosition());
		stream.skipBytes(16); // skip the package header data segment

		linkLayerReader = new EthernetLayerReader();
		linkLayerReader.readBytes(stream, 0);
		// linkLayerReader.printInfo();

		String networkLayerType = linkLayerReader
				.getLayerProperty(EthernetLayerReader.PROPERTY_TYPE);

		if (networkLayerType.equals("800")) {
			networkLayerReader = new IpLayerReader();
			networkLayerReader.readBytes(stream, 0);
			// networkLayerReader.printInfo();

			String transportLayerType = networkLayerReader
					.getLayerProperty(IpLayerReader.PROPERTY_PROTOCOL);
			if (transportLayerType.equals("6")) {
				transportLayerReader = new TcpLayerReader();
				transportLayerReader.readBytes(stream, 0);
				// transportLayerReader.printInfo();
			} else if (transportLayerType.equals("17")) {
				transportLayerReader = new UdpLayerReader();
				transportLayerReader.readBytes(stream, 0);
				// transportLayerReader.printInfo();
			} else {
				return false;
			}
		} else if (networkLayerType.equals("806")) {
			networkLayerReader = new ArpLayerReader();
			networkLayerReader.readBytes(stream, 0);
			// networkLayerReader.printInfo();
		} else {
			return false;
		}
		return true;
	}

	public ILayerReader getLinkLayerReader() {
		return linkLayerReader;
	}

	public ILayerReader getNetworkLayerReader() {
		return networkLayerReader;
	}

	public ILayerReader getTransportLayerReader() {
		return transportLayerReader;
	}

}
