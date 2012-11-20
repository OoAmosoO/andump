package cn.toddapp.andump.layerReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;


public class IpLayerReader implements ILayerReader {

	private HashMap<String, String> properties;
	private ILayerReader innerLayerReader;

	public static final String PROPERTY_VERSION = "Version";
	public static final String PROPERTY_HEADER_LENGTH = "Header length";
	public static final String PROPERTY_DSF = "Differentiated Services Filed";
	public static final String PROPERTY_TOTAL_LENGTH = "Total length";
	public static final String PROPERTY_INDENTIFICATION = "Identification";
	public static final String PROPERTY_FLAGS = "Flags";
	public static final String PROPERTY_FRAGMENT_OFFSET = "Fragment offset";
	public static final String PROPERTY_TIME_TO_LIVE = "Time to live";
	public static final String PROPERTY_PROTOCOL = "Protocol";
	public static final String PROPERTY_HEADER_CHECKSUM = "Header checksum";
	public static final String PROPERTY_SOURCE = "Source";
	public static final String PROPERTY_DESTINATION = "Destination";


	public void readBytes(DataInputStream stream, int offset)
			throws IOException {
		this.properties = new HashMap<String, String>();
		stream.skipBytes(offset);

		int version = stream.read();
		properties.put(PROPERTY_VERSION, String.valueOf((int) version / 16));
		properties.put(PROPERTY_HEADER_LENGTH,
				String.valueOf((int) version % 32 * 4));
		properties.put(PROPERTY_DSF, String.valueOf(stream.read()));
		byte[] bytes = new byte[2];
		stream.read(bytes, 0, 2);
		properties.put(PROPERTY_TOTAL_LENGTH,
				String.valueOf(bytes2ToInt(bytes, 0)));
		properties.put(PROPERTY_INDENTIFICATION,
				String.valueOf(stream.readUnsignedShort()));
		int flag = stream.read();
		properties.put(PROPERTY_FLAGS, String.valueOf(flag / 64));
		properties.put(PROPERTY_FRAGMENT_OFFSET, String.valueOf(stream.read()+flag % 64 * 512));
		properties.put(PROPERTY_TIME_TO_LIVE, String.valueOf(stream.read()));
		properties.put(PROPERTY_PROTOCOL, String.valueOf(stream.read()));
		properties.put(PROPERTY_HEADER_CHECKSUM,
				String.valueOf(stream.readShort()));

		StringBuilder src = new StringBuilder();
		StringBuilder dst = new StringBuilder();
		for (int i = 0; i < 4; i++) {
			src.append(stream.read());
			if (i != 3)
				src.append(".");
		}
		for (int i = 0; i < 4; i++) {
			dst.append(stream.read());
			if (i != 3)
				dst.append(".");
		}
		properties.put(PROPERTY_SOURCE, src.toString());
		properties.put(PROPERTY_DESTINATION, dst.toString());

		int optionBytesLength = (version % 32 * 4) - 20;
		if (optionBytesLength > 0)
			stream.skipBytes(optionBytesLength);
	}
	
	public void printInfo(){
		//http://blog.csdn.net/jkafei/article/details/3439200
		System.out.println("IP Layer");
		Iterator iterator = this.properties.entrySet().iterator();
		while(iterator.hasNext()){
			Entry entry = (Entry)iterator.next();
			System.out.println("\t"+entry.getKey()+" : "+entry.getValue());
		}
	}


	public void readBytes(byte[] bytes) {
		// null
	}

	public String getLayerType() {
		return "IP";
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

	public static int bytes2ToInt(byte[] bytes, int start) {
		// reference http://jackle-liu.iteye.com/blog/51487
		int result = bytes[start] & 0xFF;
		result |= ((bytes[start + 1] << 8) & 0xFF00);
		return result;
	}

}
