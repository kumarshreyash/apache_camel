package com.shreyash.poc.errorhandling.dlc.procesor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.StringTokenizer;
import java.util.stream.Collectors;

import org.apache.camel.Exchange;
import org.springframework.stereotype.Component;

import com.shreyash.poc.errorhandling.dlc.exception.DataValidationFailed;
import com.shreyash.poc.errorhandling.dlc.model.producer.EmployeeDataEntity;

@Component
public class DeadLetterChannelRequestProcessor {

	public void validateRequest(Exchange exchange) throws DataValidationFailed {
		String requestBody = exchange.getIn().getBody(String.class);
		List<EmployeeDataEntity> employeeList = new ArrayList<>();
		List<String> totalLines = Collections.list(new StringTokenizer(requestBody, "\r\n")).stream()
				.map(objToken -> (String) objToken).collect(Collectors.toList());
		populateEmployeeData(employeeList, totalLines);
		
		exchange.getIn().setBody(employeeList);

	}

	private void populateEmployeeData(List<EmployeeDataEntity> employeeList, List<String> totalLines)
			throws DataValidationFailed {

		for (String rawData : totalLines) {
			String[] tokenisedEmployeeData = rawData.split(",");
			validateEmployeeData(tokenisedEmployeeData);
			employeeList.add(EmployeeDataEntity.builder().role((String) tokenisedEmployeeData[0])
					.name((String) tokenisedEmployeeData[1]).designation((String) tokenisedEmployeeData[2])
					.contactNumber((String) tokenisedEmployeeData[3]).build());
		}

	}

	private void validateEmployeeData(String[] tokenisedEmployeeData) throws DataValidationFailed {
		if (tokenisedEmployeeData.length < 3) {
			throw new DataValidationFailed("All fields are not present");
		} else if (tokenisedEmployeeData[3].length() < 9) {
			throw new DataValidationFailed("Invalid contact number");
		}
	}

}
