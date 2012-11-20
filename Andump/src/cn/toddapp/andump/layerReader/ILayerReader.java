package cn.toddapp.andump.layerReader;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.HashMap;

public interface ILayerReader {
	
	public void readBytes(DataInputStream stream,int offset) throws IOException;
	
	public void readBytes(byte[] bytes);
	
	public String getLayerType();

	public HashMap<String, String> getLayerProperties();
	
	public String getLayerProperty(String key);

	public ILayerReader getInnerLayerReader();
	
	public void printInfo();
	
	//public void setInnerLayerReader(ILayerReader iLayerReader);

}
