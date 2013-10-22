package com.numerex.ota;

public class OTA_Object_Float_Array extends OTAObject implements iOTAMessage {
	
	
	public OTA_Object_Float_Array(int objectid) throws Exception {
		
		super(objectid, iOTAMessage.OBJTYPE_ARRAY_FLOAT);
		obj_data = new double[0];
		resize();
		
	}
	
	
	public OTA_Object_Float_Array(int objectid, float[] data) throws Exception {
		
		super(objectid, iOTAMessage.OBJTYPE_ARRAY_FLOAT);
		setValue(data);
	}
	
	
	
	
	public OTA_Object_Float_Array(int objectid, float[] data, boolean half_prec) throws Exception {
		
		super(objectid, iOTAMessage.OBJTYPE_ARRAY_FLOAT);
		setValue(data, half_prec);
		
	}
	
	public OTA_Object_Float_Array(int objectid, double[] data) throws Exception {
		
		super(objectid, iOTAMessage.OBJTYPE_ARRAY_FLOAT);
		setValue(data);
	}
	
	
	public void setValue(float[] data, boolean half_prec) throws Exception
	{
		
		obj_data = new double[data.length];
		for(int i=0; i<data.length; i++)
			obj_data[i]  = data[i];
		
		/*
		if(half_prec)
		{
			element_size = 2;
			obj_size = (short) (obj_data.length * element_size);
			short[] half_floats = new short[data.length];
			for(int i=0; i<data.length; i++)
				half_floats[i] = (short) fromFloat(data[i]);
			resize();
			setData(data, element_size);
			chopBuffer();
		}else {
			setValue(data);
		}	
		*/
		setValue(data);
	}
	
	
	public void setValue(float[] data ) throws Exception {
		obj_data = new double[data.length];
		for(int i=0; i<data.length; i++)
			obj_data[i]  = data[i];
		element_size = 4;
		obj_size = (short) (obj_data.length * element_size);
		resize();
		setData(data, element_size);
	
	}
	
	public void setValue(double [] data ) throws Exception {
		obj_data = data;
		element_size = 8;
		obj_size = (short) (obj_data.length * element_size);
		resize();
		setData(data, element_size);
		
	}
	
	public OTA_Object_Float_Array(byte[] incoming) throws Exception {
		
		super(incoming);
		obj_size = (short) byteBuffer.getShort();
		element_size = (byte) byteBuffer.getByte();
		
		int elements = obj_size/element_size;
		obj_data = new double[elements];
		//resize(byteBuffer.position());
	
		int value = 0;
		
		switch(element_size) {
	
		case 2:
			for(int i=0; i<elements; i++)
			{
				value = byteBuffer.getShort();
				System.out.println("value = " + value);
				obj_data[i] = toFloat(value);
				System.out.println("Float = " + (float)obj_data[i]);
			}
			break;
		
			
		case 4:
			for(int i=0; i<elements; i++)
				obj_data[i] =  byteBuffer.getFloat();
			
			break;
		
		case 8:
			for(int i=0; i<elements; i++)
				obj_data[i] = byteBuffer.getDouble();
			break;
			
		default:
			break;
				
		}
		
		
		
	}
	
 
	public String toString() {
		StringBuffer msg = new StringBuffer("<Object>\n");
		msg.append("   <ID = \"" + objectID + "\"/>\n");
		msg.append("   <Type = \"" + strObjectType() + "\"/>\n");
		msg.append("   <Payload Size = \"" + obj_size + "\"/>\n");
		msg.append("   <Element Size = \"" + element_size + "\"/>\n");
		
		if(obj_data != null)
		{
		msg.append("   <Payload = {");
		for(int j=0; j<obj_data.length; j++) {
			msg.append((float) obj_data[j]);
			if(j != obj_data.length-1)
				msg.append(", ");
		}
		msg.append("}/>\n");
		msg.append("   <Object Size = \"" + size() + "\"/>\n");
		msg.append("   " + OTAObject.bytesToHexString(byteBuffer.array()));
		}
		else 
			msg.append("Empty Payload!");
		msg.append("</Object>\n");
		return msg.toString();
		
		
	}

	
	
	 
	public int size() {
		
		//objid, objtype, 2 bytes size + 1 byte  
		return (5 + obj_size);
	}
	
	// ignores the higher 16 bits
	public static float toFloat( int hbits )
	{
	    int mant = hbits & 0x03ff;            // 10 bits mantissa
	    int exp =  hbits & 0x7c00;            // 5 bits exponent
	    if( exp == 0x7c00 )                   // NaN/Inf
	        exp = 0x3fc00;                    // -> NaN/Inf
	    else if( exp != 0 )                   // normalized value
	    {
	        exp += 0x1c000;                   // exp - 15 + 127
	        if( mant == 0 && exp > 0x1c400 )  // smooth transition
	            return Float.intBitsToFloat( ( hbits & 0x8000 ) << 16
	                                            | exp << 13 | 0x3ff );
	    }
	    else if( mant != 0 )                  // && exp==0 -> subnormal
	    {
	        exp = 0x1c400;                    // make it normal
	        do {
	            mant <<= 1;                   // mantissa * 2
	            exp -= 0x400;                 // decrease exp by 1
	        } while( ( mant & 0x400 ) == 0 ); // while not normal
	        mant &= 0x3ff;                    // discard subnormal bit
	    }                                     // else +/-0 -> +/-0
	    return Float.intBitsToFloat(          // combine all parts
	        ( hbits & 0x8000 ) << 16          // sign  << ( 31 - 15 )
	        | ( exp | mant ) << 13 );         // value << ( 23 - 10 )
	}
	
	// returns all higher 16 bits as 0 for all results
	public static int fromFloat( float fval )
	{
	    int fbits = Float.floatToIntBits( fval );
	    int sign = fbits >>> 16 & 0x8000;          // sign only
	    int val = ( fbits & 0x7fffffff ) + 0x1000; // rounded value

	    if( val >= 0x47800000 )               // might be or become NaN/Inf
	    {                                     // avoid Inf due to rounding
	        if( ( fbits & 0x7fffffff ) >= 0x47800000 )
	        {                                 // is or must become NaN/Inf
	            if( val < 0x7f800000 )        // was value but too large
	                return sign | 0x7c00;     // make it +/-Inf
	            return sign | 0x7c00 |        // remains +/-Inf or NaN
	                ( fbits & 0x007fffff ) >>> 13; // keep NaN (and Inf) bits
	        }
	        return sign | 0x7bff;             // unrounded not quite Inf
	    }
	    if( val >= 0x38800000 )               // remains normalized value
	        return sign | val - 0x38000000 >>> 13; // exp - 127 + 15
	    if( val < 0x33000000 )                // too small for subnormal
	        return sign;                      // becomes +/-0
	    val = ( fbits & 0x7fffffff ) >>> 23;  // tmp exp for subnormal calc
	    return sign | ( ( fbits & 0x7fffff | 0x800000 ) // add subnormal bit
	         + ( 0x800000 >>> val - 102 )     // round depending on cut off
	      >>> 126 - val );   // div by 2^(1-(exp-127+15)) and >> 13 | exp=0
	}
	
	 
	public String tag() {
		// TODO Auto-generated method stub
		return "OTA_OBJECT_FLOAT_ARRAY";
	}
	
	 
	public boolean isEmpty() {
		// TODO Auto-generated method stub
		return (obj_data.length==0)?true:false;
	}


	 
	public Object getValue() {
		// TODO Auto-generated method stub
		StringBuffer sb = new StringBuffer();
		for(int i=0; i<obj_data.length; i++)
		{
			sb.append(obj_data[i]);
			if(i != obj_data.length-1)
				sb.append(", ");
		}
		return sb.toString();
	}

	private short obj_size;
	private byte element_size;
	private double[] obj_data;
	

    public static void main(String args[]){
    	
    	System.out.println("----- Construct an empty object -----");
    	int oid = 100;
    	OTA_Object_Float_Array obj = null;
		try {
			obj = new OTA_Object_Float_Array(oid++);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
    	System.out.println(obj);
    	
    	
    	System.out.println("----- Add float arrary to the object -----");
    	float[] data = {(float) 2.1, (float)2.2, (float) 2.3, (float) 4.5};
    	try {
			obj.setValue(data);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
    	System.out.println(obj);
    	
    	System.out.println("----- Half precision float is not supported, use single precision for now -----");
    	data[0] = 4.6f;
    	data[1] = 4.7f;
    	data[2] = 4.8f;
    	data[3] = 4.9f;
    	try {
			obj.setValue(data, true);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
    	System.out.println(obj);
    	
    	
    	
    	System.out.println("----- Construct object from bytes -----");
        //byte []data2 = {(byte)0x64, (byte)0x07, (byte)0x00, (byte)0x08,(byte) 0x02,(byte) 0x40,(byte) 0x06, (byte)0x66,(byte) 0x66, (byte)0x40, (byte)0x0c, (byte)0xcc, (byte)0xcd};
        byte [] data2 = {0x64, 0x07, 0x00, 0x10, 0x04, 0x40, 0x06, 0x66, 0x66, 0x40, 0x0c, (byte) 0xcc, (byte) 0xcd, 0x40, 0x13, 0x33, 0x33, 0x40, (byte) 0x90, 0x00, 0x00};
    	try {
			obj = new OTA_Object_Float_Array(data2);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	System.out.println(obj);
    	
    
    	System.out.println("----- Adding double precision floats -----");
        double [] data3 = {3.1, 3.2,3,3,3,4};
        try {
			obj.setValue(data3);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        System.out.println(obj);
        
        byte[] data4 = {0x64, 0x07, 0x00, 0x30, 0x08, 0x40, 0x08, (byte) 0xcc, (byte) 0xcc, (byte) 0xcc, (byte) 0xcc, (byte) 0xcc, (byte) 0xcd, 0x40, 0x09, (byte) 0x99, 
        		(byte) 0x99, (byte) 0x99, (byte) 0x99, (byte) 0x99, (byte) 0x9a, 0x40, 0x08, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x40, 0x08, 
        		0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x40, 0x08, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x40, 
        		0x10, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};
  
        System.out.println("----- Construct object from double precision floats in bytes ------");
        try {
			obj = new OTA_Object_Float_Array(data4);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        System.out.println(obj);
        
        
    }



	



}
