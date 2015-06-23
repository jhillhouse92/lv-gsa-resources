package com.longview.gsa.repository;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import com.longview.gsa.domain.DrugLabel;
import com.longview.gsa.domain.FDAResult;
import com.longview.gsa.exception.MedCheckerException;
import com.longview.gsa.exception.OpenFdaExceptionHandler;

@Repository
public class OpenFdaRepositoryImpl implements OpenFdaRepository {
	
	private static org.apache.log4j.Logger log = Logger.getLogger(OpenFdaRepositoryImpl.class);
	
	@Autowired
	RestTemplate restTemplate;
	
	@Override
	public List<DrugLabel> searchFromFDA(List<String> fieldNames, String criteriaValue){
		FDAResult fdaResult = new FDAResult();
		StringBuffer buffer = new StringBuffer();
		restTemplate.setErrorHandler(new OpenFdaExceptionHandler());
		for (String fieldName: fieldNames) {
			buffer.append(fieldName);
			buffer.append(":");
			buffer.append(criteriaValue);
			buffer.append("+");
		}
		buffer.append("&limit=100");
		UriComponents uriComponents = UriComponentsBuilder.fromUriString("https://api.fda.gov/drug/label.json?search="+buffer.toString()).build();
		try{
			fdaResult = restTemplate.getForObject(uriComponents.toUri(),FDAResult.class);
		}catch(Exception e){
			throw new MedCheckerException(e.getMessage());
		}		
		return (fdaResult==null?null:fdaResult.getResults());
	}
}