package com.numerex.ota;

public class OTAMessageMOA extends OTAMessageMO implements iOTAMessage {
	public OTAMessageMOA( int event_code, int seq_id) throws Exception {
		super(MOBILE_ORIGINATED_ACK, event_code, seq_id);
	}

	public OTAMessageMOA(int event_code) throws Exception {
		super(MOBILE_ORIGINATED_ACK, event_code);
		
	}

	public OTAMessageMOA () throws Exception {
		super(MOBILE_ORIGINATED_ACK);
	}
	
	
	public OTAMessageMOA(OTAMessageHeader header) {
		super(header);	
	}
	
	
	 
	public String tag() {
		
		return "MOBILE_ORIGINATED_ACK";
	}

	

	

}
