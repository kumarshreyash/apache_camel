package com.shreyash.poc.errorhandling.dlc.route;

import static com.shreyash.poc.errorhandling.util.ErrorHandlingConstants.REDELIVERY_POLICY_ROUTE_ID_DLC_POLLER_ROUTE;
import static com.shreyash.poc.errorhandling.util.ErrorHandlingConstants.REDELIVERY_POLICY_ROUTE_ID_DLC_ROUTE;
import static com.shreyash.poc.errorhandling.util.ErrorHandlingConstants.REDELIVERY_POLICY_ROUTE_ID_FILE_POLLING_ROUTE;
import static com.shreyash.poc.errorhandling.util.ErrorHandlingConstants.REDELIVERY_POLICY_ROUTE_ID_GLOBAL_ERROR_HANDLER_ROUTE;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.shreyash.poc.errorhandling.dlc.procesor.DeadLetterChannelRequestProcessor;



@Component
public class ErrorHandlingDeadLetterChannelRoute extends RouteBuilder {
	
	@Autowired
	DeadLetterChannelRequestProcessor requestProcessor;

	@Override
	public void configure() throws Exception {
		
	 getContext().setStreamCaching(true);
	 
		 
	/* This is a global onException Block to  handle exceptions from all the route  */ 
	 
	 onException(Throwable.class)
	 	.routeId(REDELIVERY_POLICY_ROUTE_ID_GLOBAL_ERROR_HANDLER_ROUTE)	 	
	 	.handled(true)	
	 	.to("direct:dead-letter-queue")
	 	.end();
	 
	 
		from("file://{{file.polling.directory}}?noop=true")			
			.routeId(REDELIVERY_POLICY_ROUTE_ID_FILE_POLLING_ROUTE)
			.bean(requestProcessor,"validateRequest")
			.log("File Successfully Processed:: ${body}");	
		
		
		from("direct:dead-letter-queue")		
		/* This is a local onException block  to  handle exceptions for the below route  */		
			.onException(Throwable.class)
				.handled(true)				
				.log("Error occurred while invoking Dead Letter Queue")
				.end()
		
			.routeId(REDELIVERY_POLICY_ROUTE_ID_DLC_ROUTE)
			.log("Error occurred while routing, sending message to DLC Queue:: ${body}")
			.to("activemq:queue:dead-letter-queue-1");
			
		
		
		from("activemq:queue:dead-letter-queue-1")
			.routeId(REDELIVERY_POLICY_ROUTE_ID_DLC_POLLER_ROUTE)
			.log("Polling message from DLC Queue :: ${body}");
		
	}
	
	

}
