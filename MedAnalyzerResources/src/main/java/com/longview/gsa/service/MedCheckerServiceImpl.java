package com.longview.gsa.service;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.longview.gsa.domain.DrugLabel;
import com.longview.gsa.repository.DrugRepository;

@Service
public class MedCheckerServiceImpl implements MedCheckerService{
	
	@Autowired
	private DrugRepository drugRepository;
	
	private static final String[] fieldNames = {"openfda.brand_name","openfda.generic_name","openfda.substance_name"}; 

	@Override
	public List<DrugLabel> fetchMedList(String criteriaValue){	
		return drugRepository.fetchMedsList(Arrays.asList(fieldNames), criteriaValue);		
	}
}
