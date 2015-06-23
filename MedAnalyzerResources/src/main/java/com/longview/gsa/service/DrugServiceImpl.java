package com.longview.gsa.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.longview.gsa.domain.DrugLabel;
import com.longview.gsa.domain.DrugSearchResult;
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
	public DrugSearchResult fetchMedList(String criteriaValue){	
		DrugSearchResult dsr = new DrugSearchResult();
		dsr.setBrandName(drugRepository.fetchMedsList(Arrays.asList(fieldNames[0]), criteriaValue));
		dsr.setGenericName(drugRepository.fetchMedsList(Arrays.asList(fieldNames[1]), criteriaValue));
		dsr.setSubstanceName(drugRepository.fetchMedsList(Arrays.asList(fieldNames[2]), criteriaValue));
		
		return dsr;
	}

	@Override
	public List<GraphResult> fetchGraph(List<String> ids) {
		//create list of GraphResult
		List<GraphResult> results = new ArrayList<GraphResult>();
		
		//get list of the drugs
		List<DrugLabel> drugLabels = (List<DrugLabel>) drugRepository.findAll(ids);
		
		//for each drug, look which warnings match
		for(DrugLabel drug : drugLabels){
			
			//for each warning check to see if this warning matches
			if(null != drug.getWarnings()){
				String warning = String.join(" ", drug.getWarnings());
				
				//find based on mapReduce results using same algorithm of warning before or after
				List<String> warningCategories = 
						adminSerivce.getWarningCategories().stream()
						.filter(w -> warning.toLowerCase().contains(w.get_id().split(" ")[0].toLowerCase().trim() + " warning")
									|| warning.toLowerCase().contains("warning " + w.get_id().split(" ")[0].toLowerCase().trim())
									|| warning.toLowerCase().contains("warnings " + w.get_id().split(" ")[0].toLowerCase().trim())
								)
						.map(w -> w.get_id())
						.collect(Collectors.toList());
				GraphResult graphItem = new GraphResult();
				graphItem.setId(drug.getId());
				graphItem.setBrandName(String.join(" ", drug.getOpenfda().getBrand_name()));
				graphItem.setWarnings(warningCategories);
				results.add(graphItem);
			}
		}
		
		return results;
	}
}
