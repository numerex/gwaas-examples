package com.numerex.ota;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Vector;

public class OTAMessageMTE extends OTAMessageIntf implements iOTAMessage {
	public OTAMessageMTE( int event_code, int seq_id) throws Exception {
		super(MOBILE_TERMINATED_EVENT, event_code, seq_id);
		objids = new Vector();
	}

	public OTAMessageMTE(int event_code) throws Exception {
		super(MOBILE_TERMINATED_EVENT, event_code);
		objids = new Vector();
		
	}

	public OTAMessageMTE () throws Exception {
		super(MOBILE_TERMINATED_EVENT);
		objids = new Vector();
	}

	public OTAMessageMTE (OTAMessageHeader header) {
		super(header);
		objids = new Vector();
	}

	
	protected byte updateCRC() throws Exception {
		
		byte[] temp = new byte[size() - 1];
		System.arraycopy(header.getHeader().array(), 0, temp, 0, header_size);
		int offset = header_size;
		temp[offset++] = (byte) objids.size();
		
		Enumeration e = objids.elements ();
		while (e.hasMoreElements ()) {
			Byte ota = (Byte) e.nextElement();
			temp[offset++] = ota.byteValue();
		  }
		
		CRC = ByteHelper.getCRC8(temp);
		
		return CRC; 
	}

	
	public byte[] getBytes() throws Exception
	{
		ByteBuffer databuf = ByteBuffer.allocate(size());
		databuf.putBytes(0, header.getBytes(), 0, header_size);
		databuf.put((byte)objids.size());
		
		Enumeration e = objids.elements ();
		while (e.hasMoreElements ()) {
			Byte ota = (Byte) e.nextElement();
			databuf.put ( ota.byteValue());
		  }
		
		
		databuf.put(updateCRC());
	
		return databuf.array();
	}
	
	public byte[]  getObjs() {
		
		if(objids.isEmpty())
			return new byte[0];
		
		byte[] ids = new byte[objids.size()];
		int pos = 0;
		
		Enumeration e = objids.elements ();
		while (e.hasMoreElements ()) {
			Byte ota = (Byte) e.nextElement();
			ids[pos++] =  ota.byteValue();
		  }
		
		
		return ids;
	}
	
	public void addObjs(int i) throws Exception
	{
		Byte objid = new Byte((byte)i);
		if(!objids.contains(objid))
			objids.addElement(new Byte((byte) i)); 
		objcnt = objids.size();
		
	}
	public int size()
	{
		// size is header_size + objcnt + objs + CRC
		return (header_size + 1 + objids.size() + 1);
		
	}
	

	protected void readObjs(DataInputStream input) throws Exception {
		 try {
			  
			   int cnt = input.read();
			   if (cnt == -1)
				   throw new Exception ("Socket read failed - object cnt");
			   
			 //  else 
			 //	   System.out.println("Reading objcnt as " + cnt);
			   
			   for(int i=0; i<cnt; i++)
			   {
				   int oid = input.read();
				   if (oid == -1)
					   throw new Exception ("Socket read failed - object cnt");
				   addObjs((byte)oid);
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
	

	
	public void sendAsStream(DataOutputStream output) throws IOException, Exception {
		// writting header;
		output.write(header.getBytes());
		output.write((byte)objcnt);
		
		
		Enumeration e = objids.elements ();
		while (e.hasMoreElements ()) {
			Byte ota = (Byte) e.nextElement();
			output.write(ota.byteValue());
		  }
		
		
		CRC = updateCRC();
		
		output.write(CRC);
		output.flush();
		
		return;
		
	}
	

	
	public String toString()
	{
		
		StringBuffer st = new StringBuffer("<" + tag() + ">\n");
		st.append(header);
		
		
		st.append("<Obj Count = \"" + objcnt + "\">\n");
		
		if(!objids.isEmpty())
		{
		st.append("<Objs> \n");
		
		
		int i=0;
		byte[] temp = new byte[objids.size()];
		
		Enumeration e = objids.elements ();
		while (e.hasMoreElements ()) {
			Byte oid = (Byte) e.nextElement();
			temp[i++] = oid.byteValue();
			st.append("   <ID = \"" + oid + "\"/>\n");
		  }
		
		
			
		st.append("   " + OTAObject.bytesToHexString(temp));
		st.append("</Objs>\n");
		}
		try {
			CRC = updateCRC();
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		st.append("<CRC = \""+ CRC + "\">\n");
		
		try {
			st.append(OTAObject.bytesToHexString(getBytes()));
		} catch (Exception e) {
		
			e.printStackTrace();
		}
		st.append("<Total Object Bytes = \"" + size() + "\"/>\n");
		st.append("</" + tag() + ">");
		return st.toString();

	}
	
	 
	public String tag() {
		
		return "MOBILE_TERMINATED_EVENT";
	}
	
	 
	public boolean isEmpty() {
		// TODO Auto-generated method stub
		return (objids.isEmpty());
	}
	
	public boolean isRespRequired() throws Exception {
		if(isEmpty())
			throw new Exception("Message not initialized.");
	     return true;	
	}
	
	private Vector  objids;
	
	
	static public void main(String args[]) throws Exception {
		
		// construct a new MTE message
		int eventcode = 100;
		int seqid = 45;
		System.out.println("-------   Creating an empty message ----------");
		OTAMessageMTE msg = new OTAMessageMTE(eventcode,seqid);
		System.out.println(msg);
	
		
		System.out.println("\n\n----------  Adding object ids to the message ----------");
		msg.addObjs(1);
		msg.addObjs(2);
		msg.addObjs(2);
		msg.addObjs(100);
		
		System.out.println(msg);
		
	}


	
	
}
