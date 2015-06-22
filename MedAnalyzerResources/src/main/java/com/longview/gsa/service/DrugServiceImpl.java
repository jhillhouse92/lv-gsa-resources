package com.longview.gsa.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.longview.gsa.domain.DrugLabel;
import com.longview.gsa.domain.GraphResult;
import com.longview.gsa.repository.DrugRepository;

@Service
public class DrugServiceImpl implements DrugService{
	
	@Autowired
	private DrugRepository drugRepository;
	
	@Autowired
	private AdminService adminSerivce;
	
	private static final String[] fieldNames = {"openfda.brand_name","openfda.generic_name","openfda.substance_name"}; 

	@Override
	public List<DrugLabel> fetchMedList(String criteriaValue){	
		return drugRepository.fetchMedsList(Arrays.asList(fieldNames), criteriaValue);		
	}

	@Override
	public List<GraphResult> fetchGraph(List<String> ids) {
		//create list of GraphResult
		List<GraphResult> results = new ArrayList<GraphResult>();
		
		//get list of the drugs
		List<DrugLabel> drugLabels = (List<DrugLabel>) drugRepository.findAllById(ids);
		
		//for each drug, look which warnings match
		for(DrugLabel drug : drugLabels){
			
			//for each warning check to see if this warning matches
			String warning = String.join(" ", drug.getWarnings());
			List<String> warningCategories = 
					adminSerivce.getWarningCategories().stream()
					.filter(w -> warning.contains(w.get_id().split(" ")[0]))
					.map(w -> w.get_id())
					.collect(Collectors.toList());
			GraphResult graphItem = new GraphResult();
			graphItem.setId(drug.getId());
			graphItem.setBrandName(String.join(" ", drug.getOpenfda().getBrand_name()));
			graphItem.setWarnings(warningCategories);
			results.add(graphItem);
		}
		
		return results;
	}
}
