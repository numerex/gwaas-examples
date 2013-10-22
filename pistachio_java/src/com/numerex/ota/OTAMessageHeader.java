package com.numerex.ota;

import java.util.Date;

public class OTAMessageHeader implements iOTAMessage {
	
	// construct a empty header
	public OTAMessageHeader() throws Exception{
		
		this(0, 0, 0, System.currentTimeMillis());
		// put current timestamp
	
	}
	
	public OTAMessageHeader(int MessageType) throws Exception {
		
		this(MessageType, 0, 0, System.currentTimeMillis());
		
	}
	
	public int getMajorVersion() {
		return major_version;
	}

	public int getMinorVersion() {
		return minor_version;
	}

	public OTAMessageHeader(int MessageType, int EventCode) throws Exception {
		
		this(MessageType, EventCode, 0, System.currentTimeMillis());
		
	}
	
	public OTAMessageHeader(int MessageType, int EventCode, int SeqId) throws Exception {
		
		this(MessageType, EventCode, SeqId, System.currentTimeMillis());
		
	}
	
	public OTAMessageHeader(byte[] incoming) throws Exception {
		
		header = ByteBuffer.wrap(incoming);
		this.message_type = header.getByte();
		
		// skip protocol version
		header.getByte();
		
		this.event_code = header.getByte();
		this.seq_id = header.getShort();
		this.timestamp = header.getLong();
	
	}

	public OTAMessageHeader(int messageType, int eventCode, int seqId, long time) throws Exception {

		header = ByteBuffer.allocate(header_size);
				 
		// message type
		header.put((byte)messageType);
		this.message_type = messageType;

		// protocol version
		byte ver = (major_version << 4)  | (minor_version) ;
		header.put(ver);
				
		// event code
		header.put((byte) eventCode);
		this.setEvent_code(eventCode);
		
		// seq id
		header.putShort((short) seqId);
		this.setSeq_id(seqId);
	
		// put current timestamp
		
		header.putLong(time);
		this.setTimestamp(time);
		
	}
	
	
	
	
	public int getEvent_code() {
		return event_code;
	}

	public void setEvent_code(int event_code) {
		try {
			header.putByte(EVENT_CODE_POS, (byte)event_code);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.event_code = event_code;
	}


	public int getSeq_id() {
		return seq_id;
	}

	public void setSeq_id(int seq_id) {
		try {
			header.putShort(SEQ_ID_POS, (short) seq_id);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.seq_id = seq_id;
	}




	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		try {
			header.putLong(TIMESTAMP_POS, timestamp);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.timestamp = timestamp;
	}

	public int getMessage_type() {
		return message_type;
	}

	public void setMessage_type(int  message_type) {
		try {
			header.putLong(MESSAGE_TYPE_POS, message_type);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.message_type = message_type;
	}

	
	public ByteBuffer getHeader() {
		return header;
		
	}
	
	public byte[] getBytes() {
		
		return header.array();
	}
	
	public static String strMessageType(int m_type){

		String mtype = "UNKNOWN";
		switch (m_type) {
		case MOBILE_ORIGINATED_EVENT:
			mtype = "MOBILE ORIGINATED EVENT";
			break;
		case MOBILE_ORIGINATED_ACK:
			mtype = "MOBILE ORIGINATED ACK";
			break;
		case MOBILE_TERMINATED_EVENT:
			mtype = "MOBILE TERMINATED EVENT";
			break;
		case MOBILE_TERMINATED_ACK:
			mtype = "MOBILE TERMINATED ACK";
			break;
		}
		
		return mtype;
	}
	
	public String toString() {
		
		StringBuffer out = new StringBuffer();
			
		header.position(0);
    	int m_type;
		try {
			m_type = (byte) header.getByte();
		
    	out.append("<Header>\n");
    	out.append("   <Type = \"" + strMessageType(m_type) + "\"/>\n");
    	
    	
    	byte ver = (byte) header.getByte(PROTOCOL_VERSION_POS);
    	int major = (ver & 0xF0) >> 4;
    	int minor = ver & 0x0F;
    	out.append("   <Major Version = \"" + major + "\"/>\n");
    	out.append("   <Minor Version = \"" + minor + "\"/>\n");
    	
    	byte ecode = (byte) header.getByte(EVENT_CODE_POS);
    	out.append("   <Event Code = \"" + ecode + "\"/>\n");
    	
    	short seqid = (short) header.getShort(SEQ_ID_POS);
    	out.append("   <Seq ID = \"" + seqid + "\"/>\n");
    	
    	//Timestamp t = new Timestamp(header.getLong());
			Date t = new Date(header.getLong(TIMESTAMP_POS));
    	out.append("   <Timestamp = \"" + t + "\"/>\n");
		out.append("</Header>\n");
	} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		return out.toString();
		
	}
	

	public int size() {
		
		return header_size;
	}


	public String tag() {
		
		return "OTA_MESSAGE_HEADER";
	}
	

	public boolean isEmpty() {

		return (header.capacity() == 0);
	}

	
	private ByteBuffer header; 
	private int message_type;
	final public static int major_version = iOTAMessage.MAJOR_VERSION;
	final public static int minor_version = iOTAMessage.MINOR_VERSION;
	private int event_code;
	private int seq_id;
	private long timestamp;
	
	
	
}
