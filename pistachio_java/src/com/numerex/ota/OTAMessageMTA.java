package com.numerex.ota;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;

public class OTAMessageMTA extends OTAMessageIntf {

	public OTAMessageMTA() throws Exception  {
		super(iOTAMessage.MOBILE_TERMINATED_ACK);
	}
	
	public OTAMessageMTA(int sequenceID) throws Exception {
		super(iOTAMessage.MOBILE_TERMINATED_ACK);
		this.setSeqId(sequenceID);
		
		// TODO Auto-generated constructor stub
	}

	public OTAMessageMTA(OTAMessageHeader header) {
		super(header);
	}

	
	public String tag() {
		// TODO Auto-generated method stub
		return "MOBILE_TERMINIATED_ACK";
	}

	 
	public boolean isEmpty() {
		
		return false;
	}
 
	protected void readObjs(DataInputStream input) throws Exception {
		// no object to read, only header and CRC. 
	}

	 
	protected byte updateCRC() throws Exception {
		CRC = ByteHelper.getCRC8(header.getBytes());		
		return CRC; 
	}
 
	public void sendAsStream(DataOutputStream output) throws Exception {
		// writting header;
		output.write(header.getBytes());
		CRC = updateCRC();
		output.write(CRC);
		output.flush();
		return;
	}

	

	 
	public int size() {
		// TODO Auto-generated method stub
		return (header_size + 1); // header + 1 byte CRC.
	}

	 
	public byte[] getBytes() throws Exception {

		ByteBuffer databuf = ByteBuffer.allocate(size());
		databuf.putBytes(0, header.getBytes(), 0, header_size);
		databuf.put(updateCRC());

		return databuf.array();
	}

	public boolean isRespRequired() throws Exception
	{	
		return false;
		
	}
	
	
	
	public String toString()
	{
		
		StringBuffer st = new StringBuffer("<" + tag() + ">\n");
		st.append(header);
		
		
		
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
	public static void main (String args[])
	{
		
		System.out.println("----- Create a OTAMessageMTA -----");
		OTAMessageIntf a = null;
		try {
			a = new OTAMessageMTA();
			System.out.println(a);
		} catch (Exception e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
	
		
		
		//byte [] data = {(byte) 0xdd, 0x10, 0x00, 0x00, 0x00, 0x00, 0x01, 0x31, (byte) 0xd3, 0x59, (byte) 0xff, 0x11, (byte) 0x9a};
	    byte[] data = null;
		try {
			data = a.getBytes();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
		
		
	DataInputStream inputStream = new DataInputStream(new ByteArrayInputStream(data, 0, data.length));
		
	    System.out.println("\n----- Read the MTA from stream ----");
	    OTAMessageIntf b = null;
	    try {
			 b = OTAMessageIntf.recv(inputStream);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    System.out.println(b);
	    
	}
}
