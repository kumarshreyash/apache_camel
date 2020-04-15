package com.shreyash.poc.errorhandling.dlc.exception;

public class DataValidationFailed extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public DataValidationFailed(String errorMessage) {
		super(errorMessage);
	}

}
