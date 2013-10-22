package com.numerex.ota;

import java.util.Date;

public class OTA_Object_Timestamp extends OTAObject implements iOTAMessage {

	public OTA_Object_Timestamp(int objectid) throws Exception {
		super(objectid, iOTAMessage.OBJTYPE_TIMESTAMP);
		//setCurrentTime();
	}
	
	public OTA_Object_Timestamp(int objectid, long time) throws Exception {
		super(objectid, iOTAMessage.OBJTYPE_TIMESTAMP);
		setTime(time);
	}
	
	public void setTime(long time) throws Exception{
		byteBuffer.position(2);
		setData(time, false);
		obj_data = time;
	
	}
	
	
	public OTA_Object_Timestamp(byte[] incoming) throws Exception{
		super(incoming);
   		try {
			obj_data = (long) byteBuffer.getLong();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
   		//resize();
   		
	}
	
	public String toString() {
		
		StringBuffer msg = new StringBuffer("<Object>\n");
		msg.append("   <ID = \"" + objectID + "\">\n");
		msg.append("   <Type = \"" + strObjectType() + "\">\n"); 
		//Timestamp t = new Timestamp(obj_data);
		Date t = new Date(obj_data);
		msg.append("   <Payload = \"" + t + "," + obj_data + "\">\n");
		msg.append("   <Obj Size = \"" + size() + "\">\n"); 
		msg.append("   " + OTAObject.bytesToHexString(byteBuffer.array()));
		msg.append("</Object>\n");
		return msg.toString();
		
	}
	
	private long obj_data;

	
	public int size() {
		return (2 + 8); 
	}
	
	
	public String tag() {
		// TODO Auto-generated method stub
		return "OTA_OBJECT_TIMESTAMP";
	}
	
	public boolean isEmpty() {
		// TODO Auto-generated method stub
		return (obj_data == 0);
	}


	public Object getValue() {
		// TODO Auto-generated method stub
		try {
			return  new TimeService().getDBDateTimeFromMillis(obj_data);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	
	
	static public void main (String args[]){
		
		System.out.println("---- Create an empty object -----");
		int  oid = 10;
		OTA_Object_Timestamp obj;
		try {
			obj = new OTA_Object_Timestamp(oid);
			System.out.println(obj);

			System.out.println("----- Set time for this object ----");

			obj.setTime((long) 1313078357496L);
			System.out.println(obj);

			System.out.println("----- Set current time for this object ----");
			//obj.setCurrentTime();
			//System.out.println(obj);

			System.out.println("----- Construct object from byte stream -----");
			byte[] testdata = { (byte) 0x0b, (byte) 0x04, (byte) 0x00,
					(byte) 0x00, (byte) 0x01, (byte) 0x31, (byte) 0xb9,
					(byte) 0x81, (byte) 0xe3, (byte) 0xab };

			obj = new OTA_Object_Timestamp(testdata);
			System.out.println(obj);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}


	
	
	
}
