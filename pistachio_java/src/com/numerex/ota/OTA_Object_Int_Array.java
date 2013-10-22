package com.numerex.ota;

public class OTA_Object_Int_Array extends OTAObject implements iOTAMessage {
	
	public OTA_Object_Int_Array (int objectid) throws Exception {
		
		super(objectid, iOTAMessage.OBJTYPE_ARRAY_INT);
		obj_data = new long[0];
		resize();
		
	}
	
	public OTA_Object_Int_Array(int objectid, long[] data) throws Exception {
		
		super(objectid, iOTAMessage.OBJTYPE_ARRAY_INT);
		setValue(data);
	}
	
	public OTA_Object_Int_Array(int objectid, int[] data) throws Exception {
		
		super(objectid, iOTAMessage.OBJTYPE_ARRAY_INT);
		setValue(data);
	}
	
	public OTA_Object_Int_Array(int objectid, short[] data) throws Exception {
		
		super(objectid, iOTAMessage.OBJTYPE_ARRAY_INT);
	    setValue(data);
	}
	
	public OTA_Object_Int_Array(int objectid, byte[] data) throws Exception {
		
		super(objectid, iOTAMessage.OBJTYPE_ARRAY_INT);
		setValue(data);
	}
	
	
	public void setValue(byte[] data) throws Exception
	{
		obj_data = new long[data.length];
		for(int i=0; i<data.length; i++)
			obj_data[i] = data[i];
		element_size = 1;
		obj_size = (short) (data.length * element_size);
		resize();
		setData(data,element_size);
		
	}
	
	public void setValue(short[] data) throws Exception
	{
		obj_data = new long[data.length];
		for(int i=0; i<data.length; i++)
			obj_data[i] = data[i];
		element_size = 2;
		obj_size = (short) (data.length * element_size);
		resize();
		setData(data,element_size);
		
	}
	
	public void setValue(int[] data) throws Exception
	{
		obj_data = new long[data.length];
		for(int i=0; i<data.length; i++)
			obj_data[i] = data[i];
		element_size = 4;
		obj_size = (short) (data.length * element_size);
		resize();
		setData(data,element_size);
		
	}
	
	public void setValue(long[] data) throws Exception
	{
		obj_data = new long[data.length];
		for(int i=0; i<data.length; i++)
			obj_data[i] = data[i];
		element_size = 8;
		obj_size = (short) (data.length * element_size);
		resize();
		setData(data,element_size);
		
	}
	
	
	
	
	public OTA_Object_Int_Array(byte[] incoming) throws Exception{
		
		super(incoming);
		
		try {
			obj_size = (short)byteBuffer.getShort();
		
		element_size = (byte)byteBuffer.getByte();
		int elements = obj_size /element_size;
		obj_data = new long[elements];
		//resize(byteBuffer.position());
		switch(element_size) {
		
		case 1:
			for(int i=0; i<elements; i++)
				obj_data[i] = byteBuffer.getByte();
			break;
			
		case 2:
			for(int i=0; i<elements; i++)
				obj_data[i] = byteBuffer.getShort();
			break;
	
		case 4:
			for(int i=0; i<elements; i++)
				obj_data[i] = byteBuffer.getInt();
			break;
			
		case 8:
			for(int i=0; i<elements; i++)
				obj_data[i] = byteBuffer.getLong();
			break;
		
		default:
			break;
				
		}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public String toString() {
		
		StringBuffer msg = new StringBuffer("<Object>");
		msg.append("   <ID = \"" + objectID + "\">\n");
		msg.append("   <Type = \"" + strObjectType() + "\"/>\n");
		msg.append("   <Payload Size = \"" + obj_size + "\"/>\n");
		msg.append("   <Element Size = \"" + element_size + "\"/>\n");
						
		msg.append("   <Payload = {");
		for(int j=0; j<obj_data.length; j++) {
			msg.append(obj_data[j]);
			if(j != obj_data.length-1)
				msg.append(", ");
		}
		msg.append("}/>\n");
		msg.append("   <Object Size = \"" + size() + "\"/>\n");
		msg.append("   " + OTAObject.bytesToHexString(byteBuffer.array()));
	    msg.append("</Object>\n");
		return msg.toString();
		
	}
	

	
 
	public int size() {
	
		// objid + objtype + 2 bytes obj_size + element_size + payload.
		return (5 + obj_size);
	} 
	
 
	public String tag() {
		
		return "OTA_OBJECT_INT_ARRAY";
	}
	 
	public boolean isEmpty() {
		return (obj_data.length == 0)?true:false;
	}

	 
	public Object getValue() {
		StringBuffer sb = new StringBuffer();
		for(int i=0; i<obj_data.length; i++)
		{
			sb.append(obj_data[i]);
			if(i != obj_data.length-1)
				sb.append(", ");
		}
		return sb.toString();
	}
	
	
	private short obj_size;
	private byte element_size;
	private long[] obj_data;
	
		
	static public void main (String[] args){
		
		System.out.println("----- Create an empty object -----");
		int oid = 50;
		OTA_Object_Int_Array obj = null;
		try {
			obj = new OTA_Object_Int_Array (oid++);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		System.out.println(obj);
		
		
		System.out.println("---- Add Byte array to the object -----");
		byte [] data1 = {(byte)0x01, (byte)0x02, (byte)0x03, (byte)0x04};
		try {
			obj.setValue(data1);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		System.out.println(obj);
		
		System.out.println("---- Add Short array to the object -----");
		short[] data2 = {1,2,3,4};
		try {
			obj.setValue(data2);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		System.out.println(obj);
		
		System.out.println("----- Add Int array to the object -----");
		int [] data3 = {1,2,3,4,5};
		try {
			obj.setValue(data3);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		System.out.println(obj);
		
		System.out.println("----- Create a new object with Int array -----");
		try {
			obj = new OTA_Object_Int_Array(oid++, data3);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		System.out.println(obj);
		
		OTA_Object_Int_Array obj4 = obj;
		
		
		System.out.println("----- Construct an object from byte stream -----");
		byte[] data4 = {(byte)0x32, (byte)0x06, (byte)0x00, (byte)0x04, (byte)0x01, (byte)0x01, (byte)0x02, (byte)0x03, (byte)0x04 };
		try {
			obj = new OTA_Object_Int_Array(data4);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println(obj);
		
		
		System.out.println("----- Construct an object from existing obj ----");
		try {
			obj = new OTA_Object_Int_Array(obj4.getBytes());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println(obj);
		
		
	}


	
	

}
