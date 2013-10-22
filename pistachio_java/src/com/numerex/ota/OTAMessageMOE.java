package com.numerex.ota;

public class OTAMessageMOE extends OTAMessageMO implements iOTAMessage {

	public OTAMessageMOE( int event_code, int seq_id) throws Exception {
		super(MOBILE_ORIGINATED_EVENT, event_code, seq_id);
		
	}

	public OTAMessageMOE(int event_code) throws Exception {
		super(MOBILE_ORIGINATED_EVENT, event_code);
		
	}

	public OTAMessageMOE () throws Exception {
		super(MOBILE_ORIGINATED_EVENT);
	}
	
	public OTAMessageMOE(OTAMessageHeader header) {
		super(header);
	}

	 
	public String tag() {
		
		return "MOBILE_ORIGINATED_EVENT";
	}
	
	
	
}
