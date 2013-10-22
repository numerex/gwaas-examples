package com.numerex.ota;

public interface iOTAMessage {
	
	final static public byte MOBILE_ORIGINATED_EVENT = (byte) 0xAA;
	final static public byte MOBILE_ORIGINATED_ACK = (byte) 0xBB;
	final static public byte MOBILE_TERMINATED_EVENT = (byte)0xCC;
	final static public byte MOBILE_TERMINATED_ACK = (byte)0xDD;
	
	final static public int OBJTYPE_BYTE = 			0;
	final static public int OBJTYPE_INT = 			1;
	final static public int OBJTYPE_STRING = 		2;
	final static public int OBJTYPE_FLOAT = 		3;
	final static public int OBJTYPE_TIMESTAMP = 	4;
	final static public int OBJTYPE_ARRAY_BYTE = 	5;
	final static public int OBJTYPE_ARRAY_INT = 	6;
	final static public int OBJTYPE_ARRAY_FLOAT = 	7;
	
	final static public int SIZE_OF_FLOAT = 		4;
	final static public int SIZE_OF_TIMESTAMP = 	8;
	
	final static public int MAJOR_VERSION = 		1;
	final static public int MINOR_VERSION = 		0;
	
	final static public int header_size = 			13;
	final static public int MESSAGE_TYPE_POS = 		0;
	final static public int PROTOCOL_VERSION_POS = 	1;
	final static public int EVENT_CODE_POS = 		2;
	final static public int SEQ_ID_POS = 			3;
	final static public int TIMESTAMP_POS = 		5;

	final static public int MAX_PACKET_SIZE =      1024;
	
	public byte[] getBytes() throws Exception;
	
	
	
	public int size();
	
	public String toString();
	
	
	
	public String tag();
	
	public boolean isEmpty();
	

}
