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



	public static void main(String[] args) throws Exception {


		JAXRSServerFactoryBean sf = new JAXRSServerFactoryBean();
		sf.setResourceClasses(DeliveryService.class);
		sf.setAddress("http://localhost:9000/");
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
