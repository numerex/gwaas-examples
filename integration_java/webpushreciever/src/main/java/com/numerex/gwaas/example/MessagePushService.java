package com.numerex.gwaas.example;


import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

@WebService
public interface MessagePushService {

	
		@WebMethod
		@POST
		@Consumes(APPLICATION_JSON)
		@Path("/message")
		public void deliverMessage(Message message);
	
		@WebMethod
		@POST
		@Consumes(APPLICATION_JSON)
		@Path("/messages")
		public void deliverMessages(List<Message> messages);
		
		@WebMethod
		@GET
		@Consumes(APPLICATION_JSON)
		@Path("/system_status")
		public boolean getStatusOK();

}
