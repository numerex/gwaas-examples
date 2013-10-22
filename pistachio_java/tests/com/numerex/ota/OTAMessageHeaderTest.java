/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.numerex.ota;

import java.lang.System;

import org.junit.*;
import static org.junit.Assert.*;

/**
 *
 * @author josephbell
 */
public class OTAMessageHeaderTest {
    
    public OTAMessageHeaderTest() {
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

    /**
     * Test of getMajorVersion method, of class OTAMessageHeader.
     */
    @Test
    public void testGetVersion() throws Exception {
        System.out.println("getMajorVersion");
        
        OTAMessageHeader instance = new OTAMessageHeader();

        int result = instance.getMajorVersion();
        
        assertEquals(iOTAMessage.MAJOR_VERSION, instance.getMajorVersion());
        assertEquals(iOTAMessage.MINOR_VERSION, instance.getMinorVersion());
    }

    /**
     * Test of getEvent_code method, of class OTAMessageHeader.
     */
    @Test
    public void testSetGetEvent_code() throws Exception {
        System.out.println("getEvent_code");
        OTAMessageHeader instance = new OTAMessageHeader();
        
        int eventCode = 0x66;
        
        instance.setEvent_code(eventCode);

        int result = instance.getEvent_code();
        assertEquals(eventCode, result);
    }
    
    /**
     * Test of setSeq_id method, of class OTAMessageHeader.
     */
    @Test
    public void testSetSeq_id() throws Exception {
        System.out.println("setSeq_id");
        
        int seq_id = 0x1234;
        
        OTAMessageHeader instance = new OTAMessageHeader();
        
        instance.setSeq_id(seq_id);
        
        int result = instance.getSeq_id();
        assertEquals(seq_id, result);

    }

    /**
     * Test of setTimestamp method, of class OTAMessageHeader.
     */
    @Test
    public void testSetTimestamp() throws Exception {
        System.out.println("setTimestamp");
        
        long timestamp = System.currentTimeMillis();
        OTAMessageHeader instance = new OTAMessageHeader();
        
        instance.setTimestamp(timestamp);
        
        long result = instance.getTimestamp();
        
        assertEquals(timestamp, result);
    }

    /**
     * Test of getMessage_type method, of class OTAMessageHeader.
     */
    @Test
    public void testMessage_type() throws Exception {
        System.out.println("getMessage_type");
        OTAMessageHeader instance = new OTAMessageHeader();
        
        int messageType = iOTAMessage.MOBILE_ORIGINATED_EVENT;
        instance.setMessage_type(messageType);
        
        assertEquals(messageType, instance.getMessage_type());

    }

    /**
     * Test of getHeader method, of class OTAMessageHeader.
     */
    @Test
    public void testGetHeader1() throws Exception {
        System.out.println("getHeader");
        
        long now = System.currentTimeMillis();
        
        // Sleep for a brief amount to ensure the OTAMessageHeader gets
        // a different timestamp than 'now'
        Thread.sleep(100);
        
        OTAMessageHeader instance = new OTAMessageHeader();
        
        ByteBuffer result = instance.getHeader();
        result.parseOffset = 0;
        
        int messageType     = (byte)result.getByte(); // Will be zero
        int protocolVersion = result.getByte(); // parse
        byte major_version  = (byte) ((0xf0 & protocolVersion) >> 4);
        byte minor_version  = (byte) ((0x0f & protocolVersion));
        int eventCode       = result.getByte();
        short sequenceId    = (short)result.getShort();
        
        long timestamp      = result.getLong();

        
        assertEquals(messageType,   0);
        assertEquals(major_version, iOTAMessage.MAJOR_VERSION);
        assertEquals(minor_version, iOTAMessage.MINOR_VERSION);
        assertEquals(eventCode,     0);
        assertEquals(sequenceId,    0);
        assertTrue(timestamp > now);

    }
    
        /**
     * Test of getHeader method, of class OTAMessageHeader.
     */
    @Test
    public void testGetHeader2() throws Exception {
        System.out.println("getHeader");
        
        long now = System.currentTimeMillis();
        
        // Sleep for a brief amount to ensure the OTAMessageHeader gets
        // a different timestamp than 'now'
        Thread.sleep(100);
        
        OTAMessageHeader instance = 
                new OTAMessageHeader(iOTAMessage.MOBILE_ORIGINATED_ACK,
                0x66,
                1,
                now);
        
        ByteBuffer result = instance.getHeader();
        result.parseOffset = 0;
        
        int messageType     = (byte)result.getByte(); 
        int protocolVersion = result.getByte(); 
        byte major_version  = (byte) ((0xf0 & protocolVersion) >> 4);
        byte minor_version  = (byte) ((0x0f & protocolVersion));
        int eventCode       = result.getByte();
        short sequenceId    = (short)result.getShort();
        
        long timestamp      = result.getLong();

        
        assertEquals(messageType,   iOTAMessage.MOBILE_ORIGINATED_ACK);
        assertEquals(major_version, iOTAMessage.MAJOR_VERSION);
        assertEquals(minor_version, iOTAMessage.MINOR_VERSION);
        assertEquals(eventCode,     0x66);
        assertEquals(sequenceId,    1);
        assertEquals(timestamp,     now);

    }

    /**
     * Test of getBytes method, of class OTAMessageHeader.
     */
    @Test
    public void testGetBytes() throws Exception {
        System.out.println("getBytes");
        OTAMessageHeader instance = new OTAMessageHeader();
        byte[] expResult = null;
        byte[] result = instance.getBytes();
        
        // First  byte should be zero
        // Second byte should be protocol version
        
        byte messageType = result[0];
        byte protocolVersion = result[1];
        
        byte major_version  = (byte) ((0xf0 & protocolVersion) >> 4);
        byte minor_version  = (byte) ((0x0f & protocolVersion));

        assertEquals(messageType,   0);
        assertEquals(major_version, iOTAMessage.MAJOR_VERSION);
        assertEquals(minor_version, iOTAMessage.MINOR_VERSION);
        
    }


    /**
     * Test of size method, of class OTAMessageHeader.
     */
    @Test
    public void testSize() throws Exception {
        System.out.println("size");
        OTAMessageHeader instance = new OTAMessageHeader();
        int expResult = 13;
        int result = instance.size();
        assertEquals(expResult, result);
    }

    /**
     * Test of tag method, of class OTAMessageHeader.
     */
    @Test
    public void testTag() throws Exception {
        System.out.println("tag");
        OTAMessageHeader instance = new OTAMessageHeader();
        String expResult = "OTA_MESSAGE_HEADER";
        String result = instance.tag();
        assertEquals(expResult, result);
    }

}
