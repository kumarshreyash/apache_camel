package com.shreyash.poc.errorhandling.dlc.model.producer;

import lombok.Builder;
import lombok.Value;


@Value
@Builder
public class EmployeeDataEntity {
	
	private String role;
	private String name;	
	private String designation;	
	private String contactNumber;
	
}
