package com.numerex.ota;

public class OTA_Object_Byte extends OTAObject implements iOTAMessage {

	
	public OTA_Object_Byte(int objectid) throws Exception {
		super(objectid, iOTAMessage.OBJTYPE_BYTE);
	    
	}
	
	public OTA_Object_Byte(int objectid, byte data) throws Exception {
		super(objectid, iOTAMessage.OBJTYPE_BYTE);
		setValue(data);
		
	}
	
	public OTA_Object_Byte(byte[] incoming) throws Exception {
		super(incoming);
		try {
			obj_data = (byte) byteBuffer.getByte(2);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//resize();
	}
	
	public void setValue(byte data){
		try {
			byteBuffer.putByte(2, data);
		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		obj_data = data; 
		//setData(data, false);
	
	}
	
	public byte getObjData() {
		return obj_data;
	}
	
	public String toString() {
		
		StringBuffer msg = new StringBuffer("<Object>\n");
		msg.append("   <ID = \"" + objectID + "\">\n");
		msg.append("   <Type = \"" + strObjectType() + "\">\n");
		
		String payloadHexString = "0x" + Integer.toHexString((obj_data & 0xff));
		msg.append("   <Payload = \"" + payloadHexString + "\">\n");
		
		msg.append("   <Obj Size = \"" + size() + "\"/>\n");
		msg.append("   " + OTAObject.bytesToHexString(byteBuffer.array()));
		msg.append("Object/>\n");
		
		return msg.toString();
		
	}
	
	
	private byte obj_data;


	 
	public int size() {
		// objid, objtype, 
		return 3;
	}
	
	 
	public String tag() {
		// TODO Auto-generated method stub
		return "OTA_OBJECT_BYTE";
	}

 
	public Object getValue() {
		// TODO Auto-generated method stub
		return new Byte(obj_data);
	}	
	
	public static void main(String args[]) {
		

		 // test
		float a = (float)3.2;
		System.out.println(a);

		// construct a object byte with objid
		int objid = 10;
		
		System.out.println("----- Creating empty object with ID only -----");
		OTA_Object_Byte obj = null;
		try {
			obj = new OTA_Object_Byte(objid);
		} catch (Exception e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		System.out.println(obj);
		
		byte data = 55;
		System.out.println("----- Set value for the object -----");
		obj.setValue(data);
		System.out.println(obj);
		
		
		data = 58;
		System.out.println("----- Reset value for the object -----");
		obj.setValue(data);
		System.out.println(obj);
		
		// construct an object with objid and data
		System.out.println("----- Creating object with ID and data -----");
		data = (byte)0xAB;
		objid = 11;
		OTA_Object_Byte obj2 = null;
		try {
			obj2 = new OTA_Object_Byte(objid, data);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		System.out.println(obj2);
		
	
		
		System.out.println("------ Construct object from bytes -----");
		// construct an object from byte array.
		byte[] in = {12, 0, 55}; 
		OTA_Object_Byte obj3;
		try {
			obj3 = new OTA_Object_Byte(in);
			System.out.println(obj3);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
				
		
	}

	 
	public boolean isEmpty() {
		// TODO Auto-generated method stub
		return false;
	}


	

}
