package com.numerex.ota;

public class OTA_Object_String extends OTAObject implements iOTAMessage {

	public OTA_Object_String(int objectid) throws Exception {
		super(objectid, iOTAMessage.OBJTYPE_STRING);
		obj_data = "";
		resize();
	}
	
	public OTA_Object_String(int objectid, String data) throws Exception {
		super(objectid, iOTAMessage.OBJTYPE_STRING);
		setValue(data);
	}
	
	
	public void setValue(String data) throws Exception {
		obj_data = data;
		len = (short) data.length();
		resize();
		super.setData(data);
		
	}
	
	public OTA_Object_String(byte[] incoming) throws Exception{
		super(incoming);
        try {
			len = (short) byteBuffer.getShort();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
       
        byte[] payload = new byte[len];
        byteBuffer.get(payload);
        obj_data = new String(payload).trim();   
        resize(byteBuffer.position());
	}
	
	public String getObjData() {
		return obj_data;
	}
	
	public String toString() {
		
		StringBuffer msg = new StringBuffer("<Object>\n");
		msg.append("   <ID = \"" + objectID + "\"/>\n");
		msg.append("   <Type = \"" + strObjectType() + "\"/>\n");
		msg.append("   <Payload size = \"" + len + "\"/>\n");
		msg.append("   <Payload = \"" + obj_data + "\"/>\n");
		msg.append("   <Obj Size = \"" + size() + "\"/>\n"); 
		msg.append("   " + OTAObject.bytesToHexString(byteBuffer.array()));
		msg.append("</Object>\n");
		return msg.toString();
		
	}
	
	
	
	
	 
	public int size() {
		// TODO Auto-generated method stub
		return (2 + 2 + obj_data.length());
	}	
	
	 
	public String tag() {
		return "OTA_OBJECT_STRING";
	}
	 
	public boolean isEmpty() {
		// TODO Auto-generated method stub
		if(obj_data == null)
			return true;
		return (obj_data.length()== 0); 
			
	}
	
	 
	public Object getValue() {
		// TODO Auto-generated method stub
		return obj_data;
	}
		
	private short len; 
	private String obj_data;


	
	static public void main (String args[]) throws Exception{
	
		int oid = 50; 
		System.out.println("----- Create an empty object -----");
		OTA_Object_String obj = new OTA_Object_String(oid++);
		System.out.println(obj);
		
		System.out.println("----- Set value for the object -----");
		obj.setValue("TESTING NUMBER 1");
		System.out.println(obj);
		
		System.out.println("----- Reset value for the object -----");
		obj.setValue("TESTING NUMBER 111111");
		System.out.println(obj);
		
		System.out.println("----- Create object with data -----");
		obj = new OTA_Object_String (oid, "THIS IS TEST");
		System.out.println(obj);
		
		System.out.println("----- Construct object from byte stream -----");
		byte[] testdata = {(byte)0x34, (byte)0x02, (byte)0x00, (byte)0x0c, (byte)0x54, (byte)0x48, (byte)0x49, (byte)0x53, (byte)0x20, (byte)0x49, (byte)0x53, (byte)0x20, (byte)0x54, (byte)0x45, (byte)0x53, (byte)0x54}; 
		obj = new OTA_Object_String(testdata);
		System.out.println(obj);
		
	}


	
	
	
	
}
