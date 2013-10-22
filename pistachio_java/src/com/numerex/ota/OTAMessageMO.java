package com.numerex.ota;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Vector;

public abstract class OTAMessageMO extends OTAMessageIntf implements iOTAMessage {

	public OTAMessageMO(int message_type, int event_code) throws Exception{
		
		super(message_type, event_code);
	    init();
	}
	
	public OTAMessageMO(int message_type, int event_code, int seqid) throws Exception {

		super(message_type, event_code, seqid);
	    init();
	}
	
	public OTAMessageMO(OTAMessageHeader header) {

		super( header);
		init();
		
	}
	
	public OTAMessageMO(int message_type) throws Exception {
		this(message_type,0,0);
	}
	
	public void init () {
		
	    objs = new Vector();
	}
	
	public int addObject(OTAObject obj) throws Exception
	{
		// increase objcnt
		if(obj != null && !obj.isEmpty())
		{
			
			objs.addElement(obj);
			objcnt = objs.size();
		}
		return objcnt; 
	}
	
	
	protected byte updateCRC() throws Exception {
	
		byte[] temp = new byte[size() - 1];
		System.arraycopy(header.getHeader().array(), 0, temp, 0, header_size);
		int offset = header_size;
		temp[offset++] = (byte) objs.size();
		
		
		 Enumeration e = objs.elements ();
		  while (e.hasMoreElements ()) {
			OTAObject ota = (OTAObject) e.nextElement();
			System.arraycopy(ota.getBytes(), 0, temp, offset, ota.size());
			offset += ota.size();
		  }
		
		CRC = ByteHelper.getCRC8(temp);
		
		return CRC; 
		
	}
	
    public String toString() {
    	StringBuffer out = new StringBuffer("<" + tag() + ">\n");
    	out.append(header);
  
    	
		out.append("<Object Count = \"" + objs.size() + "\"/>\n");
		Enumeration e = objs.elements ();
		while (e.hasMoreElements ()) {
			OTAObject ota = (OTAObject) e.nextElement();
			out.append(ota);
		}
    	try {
 
			out.append("<CRC = \"" + updateCRC() + "\"/>\n");
			out.append(OTAObject.bytesToHexString(getBytes()));
			out.append("<Total Obj Bytes = \"" + size()  + "\"/>\n");
		} catch (Exception ex) {
		
			ex.printStackTrace();
		}
    	out.append("</" + tag() + ">");
    	return out.toString();
    }

    
    // construct the message from input stream
	 public void readObjs(DataInputStream input) throws Exception{
	
	   try {
		  
		   int cnt = input.read();
		   if (cnt == -1)
			   throw new Exception ("Socket read failed - object cnt");
		   
		   for(int i=0; i<cnt; i++)
		   {
			   OTAObject otaobj = OTAObject.constructFromInputStream(input);
			   addObject(otaobj);
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
	   
	
	}

	public void sendAsStream(DataOutputStream output) throws Exception{
	
		// writting header;
		output.write(header.getBytes());
		output.write((byte)objcnt);
		
		 Enumeration e = objs.elements ();
		  while (e.hasMoreElements ()) {
			OTAObject ota = (OTAObject) e.nextElement();
			output.write(ota.getBytes());
		  }
		
		
		
		
		byte CRC = updateCRC();
		output.write(CRC);
		output.flush();
		return;
	}
	
	public void sendAsDatagram(String ip, int port) throws Exception {
	
	   throw new Exception("Not supported!");

	}
	
	
	
	public int size()
	{
		int ret = header_size + 1; // objs count.
		Enumeration e = objs.elements ();
		while (e.hasMoreElements ()) {
			OTAObject ota = (OTAObject) e.nextElement();
			ret += ota.size();
		  }
		ret++; // CRC
		return ret; 
	}
	
	public byte[] getBytes() throws Exception {
			
			ByteBuffer databuf = ByteBuffer.allocate(size());
			byte[] h = header.getBytes();
			databuf.putBytes(0 ,h, 0, header_size);
			databuf.put((byte)objs.size());
			
			Enumeration e = objs.elements ();
			while (e.hasMoreElements ()) {
				OTAObject ota = (OTAObject) e.nextElement();
				databuf.putBytes(databuf.position(), ota.getBytes(), 0, ota.size());
			}
			
			try {	  
				databuf.put(updateCRC());
			}catch (Exception ex) 	{
				ex.printStackTrace();
			}
		return databuf.array();
	}
	
	public boolean isEmpty() {
		
		return objs.isEmpty();
	}
	
	public Vector  getObjects() { return objs; }
	
	protected Vector  objs; 
	
	// this is back byte array holding complete message
	
	static public void main(String args[]) throws Exception
	{
		
		
	   System.out.println("----- Create an empty Mobile_Originated_Event message with event code and seq id ----");	
	   int eventcode = 20;
	   int seqid = 50;
	   OTAMessageMO msg = new OTAMessageMOE(eventcode++, seqid++);
	   System.out.println(msg);		
	
	   
	   
	   System.out.println("\n----- Add a float object to the message ----");
	   int oid = 1;
	   float a = 2.5F;
	   OTAObject obj = new OTA_Object_Float(oid++, a);
	   msg.addObject(obj);
	   
	   
	   System.out.println("\n---- Add a string object to the message -----");
	   OTAObject obj2 = new OTA_Object_String(oid++, "THIS IS TEST");
	   msg.addObject(obj2);
	   
	   System.out.println("\n---- Add a int object to the message -----");
	   OTAObject obj3 = new OTA_Object_Int(oid++, 33);
	   msg.addObject(obj3);
	   
	   
	   System.out.println("\n---- Add a 2 bytes int object to the message -----");
	   OTAObject obj4 = new OTA_Object_Int(oid++, (short)35);
	   msg.addObject(obj4);
	   System.out.println(msg);
	   
	   System.out.println("\n---- Add a float array object to the message -----");
	   float [] data5 = { (float) 2.1, (float) 3.4, (float) 1.8, (float) 5.5, (float) 6.2, (float) 9.4 };
	   OTAObject obj5 = new OTA_Object_Float_Array(oid++, data5);
	   msg.addObject(obj5);
	   
	   System.out.println(msg);
	   
	}

	
	
}
/*
class OidComparator implements Comparator 
{
	 
	public int compare(OTAObject o1, OTAObject o2) {
	
	return (o1.getObjectID() - o2.getObjectID());
   }

	public int compare(Object arg0, Object arg1) {
		OTAObject o1 = (OTAObject) arg0;
		OTAObject o2 = (OTAObject) arg1;
		
		return compare(o1, o2);
	}
}
*/
