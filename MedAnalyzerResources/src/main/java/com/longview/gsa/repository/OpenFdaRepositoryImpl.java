package com.longview.gsa.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import com.longview.gsa.domain.DrugLabel;
import com.longview.gsa.domain.FDAResult;
import com.longview.gsa.exception.MedCheckerException;

@Repository
public class OpenFdaRepositoryImpl implements OpenFdaRepository {
	
	@Autowired
	RestTemplate restTemplate;
	
	@Override
	public List<DrugLabel> searchFromFDA(List<String> fieldNames, String criteriaValue){
		StringBuffer searchString = new StringBuffer();
		for (String fieldName: fieldNames) {
			searchString.append(fieldName)
			.append(":")
			.append(criteriaValue)
			.append("+");
		}
		return fdaDataSet(searchString.toString());
	}

	@Override
	public DrugLabel searchFromFDAById(String id) {
		StringBuffer searchString = new StringBuffer("id")
		.append(":")
		.append(id);
		List<DrugLabel> drugLabel = fdaDataSet(searchString.toString());
		return (drugLabel==null?null:drugLabel.get(0));
	}

	@Override
	public List<DrugLabel> searchFromFDAById(List<String> ids) {
		StringBuffer searchString = new StringBuffer();
		for (String id: ids) {
			searchString.append("id")
			.append(":")
			.append(id)
			.append("+");
		}
		return fdaDataSet(searchString.toString());
	}
	
	private List<DrugLabel> fdaDataSet(String searchString){
		FDAResult fdaResult = null;
		
		StringBuffer buffer = new StringBuffer("https://api.fda.gov/drug/label.json?search=")
		.append(searchString)
		.append("&limit=100");
		
		UriComponents uriComponents = UriComponentsBuilder.fromUriString(buffer.toString()).build();
		
		try{
			fdaResult = restTemplate.getForObject(uriComponents.toUri(),FDAResult.class);
		}catch(Exception e){
			throw new MedCheckerException(e.getMessage());
		}		
		
		return (fdaResult==null?null:fdaResult.getResults());
	}
}