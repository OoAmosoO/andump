package cn.toddapp.andump.layerReader;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;


public class ArpLayerReader implements ILayerReader {

	private HashMap<String, String> properties;
	private ILayerReader innerLayerReader;

	public static final String PROPERTY_HARDWARE_TYPE = "Hardware type";
	public static final String PROPERTY_PROTOCOL_TYPE = "Protocol type";
	public static final String PROPERTY_HARDWARE_SIZE = "Hardware size";
	public static final String PROPERTY_PROTOCOL_SIZE = "Protocol size";
	public static final String PROPERTY_OPCODE = "Opcode";
	public static final String PROPERTY_SENDER_MAC = "Sender mac";
	public static final String PROPERTY_SENDER_IP = "Sender ip";
	public static final String PROPERTY_TARGET_MAC = "Target mac";
	public static final String PROPERTY_TARGET_IP = "Target ip";

	public void readBytes(DataInputStream stream, int offset)
			throws IOException {
		this.properties = new HashMap<String, String>();
		stream.skipBytes(offset);

		properties.put(PROPERTY_HARDWARE_TYPE,
				String.valueOf(stream.readUnsignedShort()));
		properties.put(PROPERTY_PROTOCOL_TYPE,
				String.valueOf(stream.readUnsignedShort()));
		properties.put(PROPERTY_HARDWARE_SIZE,
				String.valueOf(stream.readUnsignedByte()));
		properties.put(PROPERTY_PROTOCOL_SIZE,
				String.valueOf(stream.readUnsignedByte()));
		properties.put(PROPERTY_OPCODE,
				String.valueOf(stream.readUnsignedShort()));

		StringBuilder builder = new StringBuilder();

		for (int i = 0; i < 6; i++) {
			builder.append(stream.readUnsignedByte());
			if (i < 5)
				builder.append(":");
		}
		properties.put(PROPERTY_SENDER_MAC, String.valueOf(builder.toString()));

		builder = new StringBuilder();
		for (int i = 0; i < 4; i++) {
			builder.append(stream.readUnsignedByte());
			if (i < 3)
				builder.append(".");
		}
		properties.put(PROPERTY_SENDER_IP, String.valueOf(builder.toString()));

		builder = new StringBuilder();
		for (int i = 0; i < 6; i++) {
			builder.append(stream.readUnsignedByte());
			if (i < 5)
				builder.append(":");
		}
		properties.put(PROPERTY_TARGET_MAC, String.valueOf(builder.toString()));

		builder = new StringBuilder();
		for (int i = 0; i < 4; i++) {
			builder.append(stream.readUnsignedByte());
			if (i < 3)
				builder.append(".");
		}
		properties.put(PROPERTY_TARGET_IP, String.valueOf(builder.toString()));

	}

	public void printInfo() {
		// http://blog.csdn.net/jkafei/article/details/3439200
		System.out.println("TCP Layer");
		Iterator iterator = this.properties.entrySet().iterator();
		while (iterator.hasNext()) {
			Entry entry = (Entry) iterator.next();
			System.out
					.println("\t" + entry.getKey() + " : " + entry.getValue());
		}
	}

	public void readBytes(byte[] bytes) {
		// null
	}

	public String getLayerType() {
		return "ARP";
	}

	public HashMap<String, String> getLayerProperties() {
		return this.properties;
	}

	public String getLayerProperty(String key) {
		return this.properties.get(key);
	}

	public ILayerReader getInnerLayerReader() {
		return this.innerLayerReader;
	}

}
