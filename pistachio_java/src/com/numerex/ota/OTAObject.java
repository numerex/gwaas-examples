package com.numerex.ota;

import java.io.DataInputStream;
import java.io.IOException;



public abstract class OTAObject implements iOTAMessage {
	
	public OTAObject(int objectid, int objtype) throws Exception {
		objectID = objectid;
		objectType = objtype;
		
		switch(objtype) 
		{
		case OBJTYPE_BYTE:
			byteBuffer = ByteBuffer.allocate(3);
			break;
			
		case OBJTYPE_INT:
			byteBuffer = ByteBuffer.allocate(11);
			break;
			
		case OBJTYPE_FLOAT:
			byteBuffer = ByteBuffer.allocate(6);
			break;
			
		case OBJTYPE_TIMESTAMP:
			byteBuffer = ByteBuffer.allocate(10);
			break;
		
		default:
			byteBuffer = ByteBuffer.allocate(1024);
			break;
		
		}
		
		
		
		byteBuffer.put((byte)objectid);
		byteBuffer.put((byte)objtype);
		
	}
	
	public OTAObject(int objid) throws Exception {
		
		this(objid, OBJTYPE_BYTE);
	}
	
	public OTAObject (byte[] incoming) throws Exception {
		
		byteBuffer = ByteBuffer.wrap(incoming);
		
		objectID = byteBuffer.get();
		objectType = byteBuffer.get();
	}
	
	
	public int getObjectID(){
		return objectID;
	}
	
	
	public int getObjectType() {
		return objectType;
	}
	
	
	
	public ByteBuffer getDataBuffer() {
		return byteBuffer;
	}
	
	public byte[] getBytes() {
		return byteBuffer.array();
	}
	
	
	protected void setData(byte data, boolean withsize) throws Exception{
		byte size = 1;
		if(withsize)
			byteBuffer.put(size);
		byteBuffer.put(data);
		//byteBuffer.limit(3);
	}
	
	
	
	protected void setData(short data, boolean withsize) throws Exception{
		byte size = 2; 
		if(withsize)
			byteBuffer.put(size);
		byteBuffer.putShort(byteBuffer.position(), data);
		
	}
	
	protected void setData(int data, boolean withsize) throws Exception{
		byte size = 4;
		if(withsize)
			byteBuffer.put(size);
		byteBuffer.putInt(byteBuffer.position(),data);
			
	}
	
	protected void setData(long data, boolean withsize) throws Exception{
		byte size = 8;
		if(withsize)
			byteBuffer.put(size);
		byteBuffer.putLong(byteBuffer.position(), data);
		
	}
	
	
	protected void setData(float data, boolean withsize) throws Exception{
		byte size = 4;
		if(withsize)
			byteBuffer.put(size);
		byteBuffer.putFloat(data);
	
	}
	
	
	
	protected void setData(byte[] data) throws Exception
	{
		short size = (short)data.length;
		byteBuffer.putShort(size);
		byteBuffer.putBytes(byteBuffer.position(), data, 0, data.length);
		
		
	}
	
	
	protected void setData(float[] data, int floatsize) throws Exception {
		short size = (short) (data.length * floatsize);
		byteBuffer.putShort(size);
		byteBuffer.put((byte)floatsize);
		for(int i=0; i<data.length; i++)
			byteBuffer.putFloat(data[i]);
		
		
	}
		
	
	protected void setData(byte[] data, int intsize) throws Exception {
		short size = (short) (data.length * intsize);
		byteBuffer.putShort(size);
		byteBuffer.put((byte) intsize);
		for(int i=0; i<data.length; i++)
			byteBuffer.put(data[i]);
		
		
	}
	
	
	protected void setData(int[] data, int intsize) throws Exception {
		short size = (short) (data.length * 4);
		byteBuffer.putShort(size);
		byteBuffer.put((byte) intsize);
		for(int i=0; i<data.length; i++)
			byteBuffer.putInt(data[i]);
		
		
	}
	
	
	protected void setData(short[] data, int intsize) throws Exception {
		short size = (short) (data.length * 2);
		byteBuffer.putShort(size);
		byteBuffer.put((byte)intsize);
		for(int i=0; i<data.length; i++)
			byteBuffer.putShort(data[i]);
		
		
	}
	
	protected void setData(long[] data, int intsize) throws Exception {
		short size = (short) (data.length * 8);
		byteBuffer.putShort(size);
		byteBuffer.put((byte)intsize);
		for(int i=0; i<data.length; i++)
			byteBuffer.putLong(data[i]);
		
		
	}

	
	protected void setData(double[] data, int intsize) throws Exception {
		short size = (short) (data.length * 8);
		byteBuffer.putShort(size);
		byteBuffer.put((byte)intsize);
		for(int i=0; i<data.length; i++)
			byteBuffer.putDouble(data[i]);
	
	}
	
	protected void setData(String data) throws Exception{
		/*
		short size = (short)(data.length());
		byteBuffer.putShort(size);
		byte[] str = data.getBytes();
		byteBuffer.putBytes(position(), data.getBytes(), 0, data.getBytes().length);
		*/
		
		this.setData(data.getBytes());
			
	}
	
	
	protected void resize(int position) throws Exception 
	{
		reallocateByteBuffer();
		byteBuffer.position(position);
		
	}
	
	protected void resize() throws Exception
	{
		reallocateByteBuffer();
	}
	
	protected void reallocateByteBuffer() throws Exception
	{
		if(byteBuffer.capacity() < size())
		{
			byteBuffer = ByteBuffer.allocate(size());
			byteBuffer.put((byte) getObjectID());
			byteBuffer.put((byte) getObjectType());
		}
		else if (byteBuffer.capacity() > size())
			chopBuffer();
		byteBuffer.position(2); // object id and object type;
		
	}
	
	protected String strObjectType() {
		String strObjType = "UNKNOWN";
		switch(objectType)
		{
		case OBJTYPE_BYTE:
			strObjType = "BYTE";
			break;
		case OBJTYPE_INT:
			strObjType = "INT";
			break;
		case OBJTYPE_FLOAT:
			strObjType = "FLOAT";
			break;
		case OBJTYPE_STRING:
			strObjType = "STRING";
			break;
		case OBJTYPE_TIMESTAMP:
			strObjType = "TIMESTAMP";
			break;
		case OBJTYPE_ARRAY_BYTE:
			strObjType = "ARRAY OF BYTE";
			break;
		case OBJTYPE_ARRAY_INT:
			strObjType = "ARRAY OF INT";
			break;
		case OBJTYPE_ARRAY_FLOAT:
			strObjType = "ARRAY OF FLOAT";
			break;
		
		}
		return strObjType;
	}
	

	
		
	static public OTAObject constructFromInputStream (DataInputStream input) throws Exception{
		
		ByteBuffer buffer = ByteBuffer.allocate(MAX_PACKET_SIZE);
		OTAObject obj = null;
		
		try {
			int id = input.read();
			if(id == -1)
				throw new Exception ("Socket read failed - object id");
			
			int type = input.read();
			if( type == -1)
				throw new Exception ("Socket read failed - object type");

			buffer.put((byte)id);
			buffer.put((byte)type);

			byte[] payload = null;
			byte[] size_2 = new byte[2];
			
			switch (type) {

			case iOTAMessage.OBJTYPE_BYTE:
				int data =  input.read();
				if(data == -1)
					throw new Exception ("Socket read failed - object type byte");
				buffer.put((byte)data);
		
				obj = new OTA_Object_Byte(buffer.array());
				break;

			case iOTAMessage.OBJTYPE_INT:

				int intsize =  input.read();
				if(intsize == -1)
					throw new Exception ("Socket read failed - object type int");
				
				buffer.put((byte)intsize);
				payload = new byte[intsize];
				input.read(payload);
				buffer.addBytes(payload);
			
				obj = new OTA_Object_Int(buffer.array());
				break;

			case iOTAMessage.OBJTYPE_FLOAT:

				payload = new byte[iOTAMessage.SIZE_OF_FLOAT];
				int read = input.read(payload);
				if(read == -1 || read < payload.length)
					throw new Exception ("Socket read failed - object type float");
				// TODO
				buffer.addBytes(payload);
		
				obj = new OTA_Object_Float(buffer.array());
				break;

			case iOTAMessage.OBJTYPE_STRING:

				read = input.read(size_2);
				if(read == -1 || read < 2)
					throw new Exception ("Socket read failed - object type string");
				buffer.addBytes(size_2);

				int objectSize = (int) (ByteHelper.chompLong(size_2, 0, 2));

				payload = new byte[objectSize];
				read = input.read(payload);
				if(read == -1 || read < objectSize)
					throw new Exception ("Socket read failed - object type string");
				
				buffer.addBytes(payload);

				
				obj = new OTA_Object_String(buffer.array());
				break;

			case iOTAMessage.OBJTYPE_TIMESTAMP:
				byte[] time_data = new byte[SIZE_OF_TIMESTAMP];
				read = input.read(time_data);
				if(read == -1 || read < SIZE_OF_TIMESTAMP)
					throw new Exception ("Socket read failed - object type string");
				
				buffer.addBytes(time_data);
				
				
				obj = new OTA_Object_Timestamp(buffer.array());
				break;

			case iOTAMessage.OBJTYPE_ARRAY_BYTE:
				
				read = input.read(size_2);
				if(read == -1 || read < 2)
					throw new Exception ("Socket read failed - object type byte array");
				
				buffer.addBytes(size_2);
				
				objectSize = (int) (ByteHelper.chompLong(size_2, 0, 2));
				payload = new byte[objectSize];
				
				read = input.read(payload);
				if(read == -1 || read < payload.length)
					throw new Exception ("Socket read failed - object type byte array");
				
				buffer.addBytes(payload);
				
				obj = new OTA_Object_Byte_Array(buffer.array());
				break;

			case iOTAMessage.OBJTYPE_ARRAY_INT:
				read = input.read(size_2);
				if(read == -1 || read < 2)
					throw new Exception ("Socket read failed - object type int array");
				
				buffer.addBytes(size_2);
				objectSize = (int) (ByteHelper.chompLong(size_2, 0, 2));

				intsize =  input.read();
				if(read == -1 )
					throw new Exception ("Socket read failed - object type int array");
				
				buffer.put((byte)intsize);

				int elements = objectSize / intsize;
				payload = new byte[intsize];
				for (int j = 0; j < elements; j++) {
				
					read = input.read(payload);
					if(read == -1 || read < payload.length)
						throw new Exception ("Socket read failed - object type int array");
					
					buffer.addBytes(payload);
				}
			
				obj = new OTA_Object_Int_Array(buffer.array());
				break;

			case iOTAMessage.OBJTYPE_ARRAY_FLOAT:
				read = input.read(size_2);
				if(read == -1 || read < 2)
					throw new Exception ("Socket read failed - object type float array");
				
				buffer.addBytes(size_2);

				objectSize = (int) (ByteHelper.chompLong(size_2, 0, 2));
				intsize =  input.read();
				if(intsize == -1)
					throw new Exception ("Socket read failed - object type float array");
				
				buffer.put((byte)intsize);

				elements = objectSize / intsize;
				payload = new byte[intsize];
				for (int j = 0; j < elements; j++) {
					read = input.read(payload);
					if(read == -1 || read < payload.length)
						throw new Exception ("Socket read failed - object type float array");
					buffer.addBytes(payload);
				}
			
				obj = new OTA_Object_Float_Array(buffer.array());
				break;
			}
		}
		catch (IOException e) 
		{
			throw e;
		}
		catch (Exception e)
		{
			throw e;
		}
		
		if(obj == null)
			throw new Exception ("Failed to build Object ");
    	return obj;
    }
				

	public static String bytesToHexString(byte[] requestConfigBytes)
	{
		
		StringBuffer payloadHexString = new StringBuffer("<Hex> ");
		for (int j = 0; j < requestConfigBytes.length; j++) {
			String hexString =  Integer.toHexString(requestConfigBytes[j]);
			if (hexString.length() < 2) hexString = "0" + hexString;
			if (hexString.length() > 2) hexString = hexString.substring(hexString.length() - 2);
			payloadHexString.append("0x");
			payloadHexString.append(hexString);
			if (j < requestConfigBytes.length - 1) payloadHexString.append(", ");
			if (j > 0 && j != (requestConfigBytes.length - 1) && j % 15 == 0) payloadHexString.append("\r\n");
		}
		payloadHexString.append(" </Hex>\n");
		return payloadHexString.toString();
	}
	
	protected void chopBuffer() {
    	byte [] temp = new byte[size()];
    	System.arraycopy(byteBuffer.array(), 0, temp, 0, size());
    	byteBuffer = ByteBuffer.wrap(temp);
    	
    }
	
	
	
	abstract public Object getValue();
	protected final int objectID;
	protected final int objectType;
	protected ByteBuffer byteBuffer;
	
	

}
