package com.numerex.gwaas.example.webpushreciever;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;
import org.codehaus.jackson.jaxrs.JacksonJsonProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.numerex.gwaas.example.JacksonUtil;
import com.numerex.gwaas.example.MessagePushService;

public class App {
	private static final Logger log = LoggerFactory.getLogger(App.class);

/* to send a message to this simply:
 * 
 * curl -D- -X POST "http://localhost:9000/webpush/message" -H "Content-Type:application/json" -d '{"id":"289742f3-1423-44b1-990b-7b78273ac5bc","type":null,"timestamp":"2013-05-14T10:55:17.334-0500","data":{},"headers":{"msg_version":"1"}}'
 * 
 * point browser to http://localhost:9000/webpush/system_status  to check that service
 * 
 */

	public static void main(String[] args) throws Exception {


		JAXRSServerFactoryBean sf = new JAXRSServerFactoryBean();
		sf.setServiceBean(new WebpushReciever());
		sf.setAddress("http://localhost:9000/webpush");
		sf.setProvider(new JacksonJsonProvider(JacksonUtil.getMapper()));
		sf.create();


		System.out.println("press Q THEN ENTER to terminate");
		BufferedReader in=new BufferedReader(new InputStreamReader(System.in)); 
		while(true){  
			String msg="";
			try{  
				msg=in.readLine();  
			}catch(IOException e){  
				e.printStackTrace();  
			}  

			if(msg.equals("Q")) break;

		}  		




		System.exit(0);
	}






}
