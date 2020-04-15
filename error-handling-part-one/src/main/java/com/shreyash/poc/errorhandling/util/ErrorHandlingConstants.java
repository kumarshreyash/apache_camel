package com.shreyash.poc.errorhandling.util;

public final class ErrorHandlingConstants {
	
	private ErrorHandlingConstants() {};
	
	public static final String REDELIVERY_POLICY_ROUTE_ID_FILE_POLLING_ROUTE="file-poller-route";
	
	public static final String REDELIVERY_POLICY_ROUTE_ID_GLOBAL_ERROR_HANDLER_ROUTE="global-error-route";
	
	public static final String REDELIVERY_POLICY_ROUTE_ID_DLC_ROUTE="dead-letter-channel-route";
	
	public static final String REDELIVERY_POLICY_ROUTE_ID_DLC_POLLER_ROUTE="dead-letter-channel-poller-route";
	
	public static final String REDELIVERY_POLICY_ROUTE_ID_WEBS_ROUTE="call-web-service-route";
	
	
}
