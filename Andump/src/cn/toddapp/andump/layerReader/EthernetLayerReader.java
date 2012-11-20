package cn.toddapp.andump.layerReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.HashMap;


public class EthernetLayerReader implements ILayerReader {

	private HashMap<String, String> properties;
	private ILayerReader innerLayerReader;

	public static final String PROPERTY_DST_MAC = "Destination MAC";
	public static final String PROPERTY_SRC_MAC = "Source MAC";
	public static final String PROPERTY_TYPE = "Type";

	public void readBytes(DataInputStream stream, int offset)
			throws IOException {
		this.properties = new HashMap<String, String>();
		stream.skipBytes(offset);

		StringBuilder builder1 = new StringBuilder();
		StringBuilder builder2 = new StringBuilder();
		for (int i = 0; i < 6; i++) {
			builder1.append(Integer.toHexString(stream.readUnsignedByte()));
			if (i != 5)
				builder1.append(":");
		}
		for (int i = 0; i < 6; i++) {
			builder2.append(Integer.toHexString(stream.readUnsignedByte()));
			if (i != 5)
				builder2.append(":");
		}
		this.properties.put(PROPERTY_DST_MAC, builder1.toString());
		this.properties.put(PROPERTY_SRC_MAC, builder2.toString());
		this.properties.put(PROPERTY_TYPE, Integer.toHexString(stream.readUnsignedShort()));
		
		
		
	}

	public void printInfo() {
		System.out.println("Ethernet Layer:");
		System.out.println("\t" + PROPERTY_DST_MAC + " : "
				+ this.properties.get(PROPERTY_DST_MAC));
		System.out.println("\t" + PROPERTY_SRC_MAC + " : "
				+ this.properties.get(PROPERTY_SRC_MAC));
		System.out.println("\t" + PROPERTY_TYPE + " : "
				+ this.properties.get(PROPERTY_TYPE));
	}

	public void readBytes(byte[] bytes) {
		// null
	}

	public String getLayerType() {
		return "Ethernet";
	}

	public HashMap<String, String> getLayerProperties() {
		return this.properties;
	}


	public String getLayerProperty(String key) {
		return this.properties.get(key);
	}


	public ILayerReader getInnerLayerReader() {
		return innerLayerReader;
	}

}
