package com.numerex.ota;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public abstract class OTAMessageIntf implements iOTAMessage {
	
	public OTAMessageIntf (int message_type, int event_code) throws Exception {	
	
		header = new OTAMessageHeader(message_type, event_code);
		init();
		
	}
	
	public OTAMessageIntf(int message_type, int event_code, int seqid) throws Exception {

		header = new OTAMessageHeader(message_type, event_code, seqid);
	    init();
	}
	
	public OTAMessageIntf(OTAMessageHeader header) {

		this.header = header;
		init();
		
	}
	
	public OTAMessageIntf(int message_type) throws Exception {
		
		this(message_type,0,0);
	}
	
	
	protected void init () {
		
	}
	
    public int getSeqId() {
    	
    	return header.getSeq_id();
    }
    
    public void setSeqId(int seqid)
    {
    	
    	header.setSeq_id(seqid);
    }
    
	static public OTAMessageIntf recv(DataInputStream input) throws Exception{	
		   
		   OTAMessageIntf msg = null;
		   int ret = 0;
		   try {
			   byte [] header_temp = new byte[header_size];
			   ret = input.read(header_temp);
			   if(ret == -1)
				   throw new Exception ("Socket read failed - header");

			  OTAMessageHeader header =  new OTAMessageHeader(header_temp);
			  switch((byte)header.getMessage_type())
			  {
				case MOBILE_ORIGINATED_EVENT:
					msg = new OTAMessageMOE(header);
					break;

				case MOBILE_ORIGINATED_ACK:
					msg = new OTAMessageMOA(header);
					break;

				case MOBILE_TERMINATED_EVENT:
					msg = new OTAMessageMTE(header);
					break;
			   
				case MOBILE_TERMINATED_ACK:
					msg = new OTAMessageMTA(header);
					break;
					
				 default:
					 throw new Exception("UNRECOGNIZED MESSAGE");
			  }
			  
			  
              
			  msg.readObjs(input);
			  
			  int in_crc = input.read();
			   if(in_crc == -1)
				   throw new Exception ("Socket read failed - crc");
			   
				//int my_crc = (int) msg.updateCRC();
			   byte recvCRC = (byte) (in_crc & 0x000000FF); 
			   byte myCRC = msg.updateCRC();
		   	   if (recvCRC != myCRC) 
			   		throw new Exception (" Mismatched crc. Recvd CRC = " + recvCRC + ", My CRC = " + myCRC);
		   } 	
		   catch (IOException e)
		   {
			   throw e;
		   }
		   catch (Exception e)
		   {
			   throw e;
		   }
		   return msg;
		
		}



	public boolean isRespRequired() throws Exception {
		if(isEmpty())
			throw new Exception("Message not initialized.");
	     return (this.getSeqId() > 0);	
	}
	
	public int getMajorProtocolVersion ()
	{
		return header.getMajorVersion();
		
	}
	
	public int getMinorProtocolVersion ()
	{
		return header.getMinorVersion();
		
	}
	
	public int getObjecCnt() { return objcnt;} 
	public String getTimestamp() {
		try {
			return  new TimeService().getDBDateTimeFromMillis(header.getTimestamp());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
		
	}
	public int getEventCode () {
		return header.getEvent_code();
	}
	
	
	public Byte  getCRC() throws Exception {
		return  new Byte(updateCRC());
	}
	
	
	public void sendAsDatagram(String ip, int port) throws Exception {
		throw new Exception("Not supported!");
		
	}
	
	public int getMessageType() {
		return header.getMessage_type();
		
	}
	abstract protected void readObjs(DataInputStream input) throws Exception;
	abstract protected byte updateCRC() throws Exception;
	abstract public void sendAsStream(DataOutputStream output) throws Exception;
	//abstract public void sendAsDatagram(String ip, int port) throws Exception;
	abstract public int size();
	abstract public byte[] getBytes() throws Exception;
	
	

	protected OTAMessageHeader header; 
	protected int objcnt;
	protected byte CRC;

	
	

}
