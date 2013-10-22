/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.numerex.ota;

import org.junit.*;
import static org.junit.Assert.*;

/**
 *
 * @author josephbell
 */
public class OTAMessageMOETest {
    
  public OTAMessageMOETest() {
  }

  @BeforeClass
  public static void setUpClass() throws Exception {
  }

  @AfterClass
  public static void tearDownClass() throws Exception {
  }
    
  @Before
  public void setUp() {
  }
    
  @After
  public void tearDown() {
  }
    
    
  @Test
  public void testEventCode() throws Exception {
    System.out.println("----- Create an empty Mobile_Originated_Event message with event code and seq id ----");
	  int eventcode = 20;
	  int seqid = 50;
	   
    
    OTAMessageMO msg = new OTAMessageMOE(eventcode, seqid);
	  System.out.println(msg);		
    
    int messageType = msg.getMessageType();
    int eventCode   = msg.getEventCode();
    int sequenceId  = msg.getSeqId();
	
	  assertEquals(messageType, iOTAMessage.MOBILE_ORIGINATED_EVENT);
    assertEquals(eventCode,   eventcode);
    assertEquals(sequenceId,  seqid);
  }
  
  @Test
  public void testFloatObject() throws Exception {
    
    int eventcode = 20;
	  int seqid = 50;
    
    System.out.println("\n---- Add a float to the message -----");
    int   oid = 1;
    float val = 79.5F;
    
    OTAMessageMO msg = new OTAMessageMOE(eventcode, seqid);
	  OTAObject    obj = new OTA_Object_Float(oid, val);
	  msg.addObject(obj);
    System.out.println(msg);
    
  }
  
  @Test
    public void testStringObject() throws Exception {
    
    int eventcode = 20;
	  int seqid = 50;
    
    OTAMessageMO msg = new OTAMessageMOE(eventcode, seqid);
    
    System.out.println("\n---- Add a float to the message -----");
    int   oid = 1;
    float val = 79.5F;
    

	  OTAObject    fObj = new OTA_Object_Float(oid++, val);
	  msg.addObject(fObj);
    
    System.out.println("\n---- Add a string to the message -----");
    OTAObject    strObj = new OTA_Object_String(oid++, "Temperature");
    msg.addObject(strObj);
   
    System.out.println(msg);
    
  }
	   
    
}
