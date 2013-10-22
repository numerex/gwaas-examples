package com.numerex.ota;

public class OTA_Object_Byte_Array extends OTAObject implements iOTAMessage {

	public OTA_Object_Byte_Array(int objectid) throws Exception {
		super(objectid, iOTAMessage.OBJTYPE_ARRAY_BYTE);
		obj_data = new byte[0];
		resize();
		
	}
	
	
	public OTA_Object_Byte_Array(int objectid, byte[] data) throws Exception {
		super(objectid, iOTAMessage.OBJTYPE_ARRAY_BYTE);
		setValue(data);
	}
	
	
	public OTA_Object_Byte_Array(byte[] incoming) throws Exception {
		super(incoming);
		try {
			obj_size = (short) byteBuffer.getShort();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		obj_data = new byte[obj_size];
		byteBuffer.get(obj_data);
		//resize(byteBuffer.position());
		
	}
	
	public void setValue(byte[] data) throws Exception
	{
		obj_data = data;
		obj_size = (short)data.length;
		resize();
		setData(obj_data);
		
	}
	
	public String toString() {
		
		StringBuffer msg = new StringBuffer("<Object>\n");
		
		msg.append("   <ID = \"" + objectID + "\">\n");
		msg.append("   <Type = \"" + strObjectType() + "\">\n");
		msg.append("   <Payload Size = \"" + obj_size + "\">\n");
	
		if(obj_data != null)
			msg.append("   " + OTAObject.bytesToHexString(obj_data));			

		msg.append("   <Obj Size = \"" + size() + "\">\n"); 
		msg.append("   " + OTAObject.bytesToHexString(byteBuffer.array()));

		msg.append("</Object>\n");
		return msg.toString();
		
	}
	
	
	

	
	public int size() {
		
		// objid, objtype + 2 bytes of size
		return (2 + 2 + obj_data.length); 
		
	}
	
	
   
	public String tag() {
	
		return "OBJECT_BYTE_ARRAY";
	}
    
    
    private short obj_size; 
	private byte[] obj_data; 
	

	public static void main( String args[])
	{
		int oid = 55;
		
		System.out.println("---- Creating an empty object -------");
		OTA_Object_Byte_Array obj = null;
		try {
			obj = new OTA_Object_Byte_Array(oid++);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		System.out.println(obj);
		
		
			byte[] b =  {(byte)0x12, (byte)0x13, (byte)0x14, (byte)0x55};
		try {
			obj.setValue(b);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	    
		System.out.println(obj);	
		
		System.out.println("---- Set value to the object --------");
		byte[] a = {(byte)0x12, (byte)0x13, (byte)0x14};
		try {
			obj.setValue(a);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}	
		
		System.out.println(obj);
		

		
		
	
		byte[] data = {
			(byte)0x37, (byte)0x05, (byte)0x00, (byte)0x03, (byte)0x12, (byte)0x13, (byte)0x14
		};
		
		System.out.println("----- Construct object from byte stream ------");
		try {
			obj = new OTA_Object_Byte_Array(data);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(obj);
		
		
	}



	public boolean isEmpty() {
		// TODO Auto-generated method stub
		return (obj_data.length == 0)?true:false;
	}



	public Object getValue() {
		
			String value = "";
			try {
				value = ByteHelper.chompBytesAsHexString(obj_data, 0, obj_data.length);
			} catch (Exception e) {
			
				e.printStackTrace();
			}
			return value;
		
	}

}
