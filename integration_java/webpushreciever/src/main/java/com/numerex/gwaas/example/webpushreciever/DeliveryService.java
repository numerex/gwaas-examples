package com.numerex.gwaas.example.webpushreciever;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.numerex.gwaas.example.Message;
import com.numerex.gwaas.example.MessagePushService;

public class DeliveryService implements MessagePushService {
	private static final Logger log = LoggerFactory.getLogger(DeliveryService.class);


	@Override
	@POST
	@Consumes(APPLICATION_JSON)
	@Path("/message")
	public void deliverMessage(Message message) {
		log.debug("Message: {}",message);
	}

	@Override
	@POST
	@Consumes(APPLICATION_JSON)
	@Path("/messages")
	public void deliverMessages(List<Message> messages) {
		for (Message m : messages) {
			log.debug("Message: {}",m);
		}
		
	}

	@Override
	@GET
	@Consumes(APPLICATION_JSON)
	@Path("/system_status")
	public boolean getStatusOK() {
		return true;
	}
	
	
}
