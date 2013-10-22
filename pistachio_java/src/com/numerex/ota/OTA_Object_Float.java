package com.numerex.ota;

public class OTA_Object_Float extends OTAObject implements iOTAMessage {

	public OTA_Object_Float(int objectid) throws Exception {
		super(objectid, iOTAMessage.OBJTYPE_FLOAT);
	
	}
	
	
	public OTA_Object_Float(int objectid, float data) throws Exception {
		super(objectid, iOTAMessage.OBJTYPE_FLOAT);
		setValue(data);
		
	}


	public void setValue(float data) throws Exception {
		byteBuffer.position(2);
		obj_data = data;
		setData(data, false);
		
	}


	public OTA_Object_Float(int objectid, int data) throws Exception {
		super(objectid, iOTAMessage.OBJTYPE_FLOAT);
		setValue(data);
	}
	
	
	public OTA_Object_Float (byte[] incoming) throws Exception{
		super(incoming);
		obj_data =  byteBuffer.getFloat();
		//resize();
	}
	
	
	
	
	
	
	
	public String toString() {
		
		StringBuffer msg = new StringBuffer("<Object>\n");
		msg.append("   <ID = \"" + objectID + "\"/>\n");
		msg.append("   <Type = \"" + strObjectType() + "\"/>\n");
		msg.append("   <Payload = \"" + obj_data + "\"/>\n");
		msg.append("   <Obj Size = \"" + size() + "\"/>\n");
		msg.append("   " + OTAObject.bytesToHexString(byteBuffer.array()));
		msg.append("</Object>\n");
		return msg.toString();
		
	}
	

	
	public int size() {
		// TODO Auto-generated method stub
		return (2 + 4);
	}
	
	public String tag() {
		// TODO Auto-generated method stub
		return "OTA_OBJECT_FLOAT";
	}


	public boolean isEmpty() {
		// TODO Auto-generated method stub
		return false;
	}

	
	public Object getValue() {
		// TODO Auto-generated method stub
		return new Float(obj_data);
	}
	
	//private float obj_data;
	private float obj_data;
	
	
	
	
	static public void main (String args[]) {
		
		int oid = 10;
		System.out.println("----- Creating a empty object with ID -----");
		OTA_Object_Float obj = null;
		try {
			obj = new OTA_Object_Float (oid++);
		} catch (Exception e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		System.out.println(obj);
		
		System.out.println("----- Set value for this object -----");
		try {
			 obj.setValue((float)11.2F);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		System.out.println(obj);
		
		
		System.out.println("----- Creating an object with ID and Data ------");
		float data = (float) 3.123;
		try {
			OTA_Object_Float obj1 = new OTA_Object_Float(1, data);
			
			System.out.println("OBJECT 1: ");
			System.out.println(obj1);
			obj = new OTA_Object_Float(1, Float.floatToIntBits(data));
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		System.out.println(obj);
		
	
		//System.out.println("Float value = " + floatValue);
		
		System.out.println("----- Construct an object from byte stream -----");
		byte [] bdata = { (byte)0x01, (byte)0x03, (byte)0x40, (byte)0x47, (byte)0xdf, (byte)0x3b };
		try {
			obj = new OTA_Object_Float(bdata);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(obj);
		
		
		
		
	}





	
	
}
