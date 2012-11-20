package cn.toddapp.andump.layerReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;


public class UdpLayerReader implements ILayerReader {

	private HashMap<String, String> properties;
	private ILayerReader innerLayerReader;

	public static final String PROPERTY_SOURCE_PORT = "Source port";
	public static final String PROPERTY_DESTINATION_PORT = "Destination port";
	public static final String PROPERTY_HEADER_LENGTH = "Header length";
	public static final String PROPERTY_CHECKSUM = "Checksum";


	public void readBytes(DataInputStream stream, int offset)
			throws IOException {
		this.properties = new HashMap<String, String>();
		stream.skipBytes(offset);

		properties.put(PROPERTY_SOURCE_PORT,
				String.valueOf(stream.readUnsignedShort()));
		properties.put(PROPERTY_DESTINATION_PORT,
				String.valueOf(stream.readUnsignedShort()));
		properties.put(PROPERTY_HEADER_LENGTH,
				String.valueOf(stream.readUnsignedShort()));
		properties.put(PROPERTY_CHECKSUM,
				String.valueOf(stream.readUnsignedShort()));

	}

	public void printInfo() {
		// http://blog.csdn.net/jkafei/article/details/3439200
		System.out.println("UDP Layer");
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
		return "UDP";
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
