package cn.toddapp.andump.layerReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;


public class TcpLayerReader implements ILayerReader {

	private HashMap<String, String> properties;
	private ILayerReader innerLayerReader;

	public static final String PROPERTY_SOURCE_PORT = "Source port";
	public static final String PROPERTY_DESTINATION_PORT = "Destination port";
	public static final String PROPERTY_SEQUENCE_NUMBER = "Sequence number";
	public static final String PROPERTY_ACKNOWLEDGEMENT_NUMBER = "Acknowledgement number";
	public static final String PROPERTY_HEADER_LENGTH = "Header length";
	public static final String PROPERTY_FLAGS = "Flags";
	public static final String PROPERTY_WINDOW_SIZE_VALUE = "Window size vlaue";
	public static final String PROPERTY_CHECKSUM = "Checksum";
	public static final String PROPERTY_OPTIONS = "Options";


	public void readBytes(DataInputStream stream, int offset)
			throws IOException {
		this.properties = new HashMap<String, String>();
		stream.skipBytes(offset);

		properties.put(PROPERTY_SOURCE_PORT, String.valueOf(stream.readUnsignedShort()));
		properties.put(PROPERTY_DESTINATION_PORT,
				String.valueOf(stream.readShort()));
		properties.put(PROPERTY_SEQUENCE_NUMBER,
				String.valueOf(stream.readInt()));
		properties.put(PROPERTY_ACKNOWLEDGEMENT_NUMBER,
				String.valueOf(stream.readInt()));
		int length = stream.read();
		properties.put(PROPERTY_HEADER_LENGTH, String.valueOf(length / 16 * 4));
		properties.put(PROPERTY_FLAGS, String.valueOf(stream.read()));
		properties.put(PROPERTY_WINDOW_SIZE_VALUE,
				String.valueOf(stream.readShort()));
		properties.put(PROPERTY_CHECKSUM, String.valueOf(stream.readShort()));

		int optionLength = length / 16 * 4 - 20;
		if (optionLength > 0)
			stream.skipBytes(optionLength);

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
		return "TCP";
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
