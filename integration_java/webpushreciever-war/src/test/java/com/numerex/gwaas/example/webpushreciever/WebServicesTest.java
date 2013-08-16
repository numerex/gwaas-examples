package com.numerex.gwaas.example.webpushreciever;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.numerex.gwaas.example.Message;
import com.numerex.gwaas.example.MessagePushService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/test-context.xml")
public class WebServicesTest {


	@Autowired
	@Qualifier("messagePushService_testClient")
	private MessagePushService messagePushService;


    @Test
    public void testWebServices() {
 
    	
    	Assert.assertTrue(messagePushService.getStatusOK());
    	
    	messagePushService.deliverMessage(new Message());

    }
    
    
}
