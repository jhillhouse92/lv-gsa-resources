package com.longview.gsa.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.longview.gsa.domain.DrugLabel;
import com.longview.gsa.domain.DrugSearchResult;
import com.longview.gsa.domain.GraphResult;
import com.longview.gsa.repository.DrugRepository;
import com.longview.gsa.repository.OpenFdaRepository;

@Service
public class DrugServiceImpl implements DrugService{
	
	@Autowired
	private DrugRepository drugRepository;
	
	@Autowired
	private AdminService adminSerivce;
	
	@Autowired 
	private OpenFdaRepository openFdaRepository;
	
	@SuppressWarnings("serial")
	private static final ArrayList<String> fieldNames = new ArrayList<String>() {{
		add("openfda.brand_name");
		add("openfda.generic_name");
		add("openfda.substance_name");
	}};
	
	@Override
	public List<DrugSearchResult> fetchMedList(String criteriaValue){	
		List<DrugSearchResult> dsrList = new ArrayList<DrugSearchResult>();
		
		List<DrugLabel> apiSearchResults = openFdaRepository.searchFromFDA(fieldNames, criteriaValue);
		String brandName = "", genericName = "", substanceName = "";
		for(DrugLabel dl : apiSearchResults){
			if(dl.getOpenfda().getBrand_name()!=null)
				brandName = String.join(" ", dl.getOpenfda().getBrand_name());
			if(dl.getOpenfda().getGeneric_name()!=null)
				genericName = String.join(" ", dl.getOpenfda().getGeneric_name());
			if(dl.getOpenfda().getSubstance_name()!=null)
				substanceName = String.join(" ", dl.getOpenfda().getSubstance_name());
			List<String> match = new ArrayList<String>(2); //initialize match as 2 is maximum
			
			DrugSearchResult dsr = new DrugSearchResult();
			
			dsr.setId(dl.getId());
			dsr.setBrandName(brandName);
			
			//when: match on brand name
			//then: create empty array (we don't want it redudanant)
			if(genericName.toLowerCase().trim().contains(criteriaValue.toLowerCase().trim())){
				match.add(genericName);
			}
			else if(substanceName.toLowerCase().trim().contains(criteriaValue.toLowerCase().trim())){
				match.add(substanceName);
			}
			
			dsr.setMatch(match);
			dsrList.add(dsr);
		}
		
		return dsrList;
	}

	@Override
	public List<GraphResult> fetchGraph(List<String> ids) {
		//create list of GraphResult
		List<GraphResult> results = new ArrayList<GraphResult>();
		
		//get list of the drugs
		List<DrugLabel> drugLabels = (List<DrugLabel>) drugRepository.findAll(ids);
		
		for(DrugLabel drug : drugLabels){
			ids.remove(drug.getId());
		}
		
		drugLabels.addAll(addDrugLabelsToDB(ids));
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

	@Override
	public DrugLabel fetchLabel(String id) {
		DrugLabel drugLabel = drugRepository.findOne(id);
		if(null == drugLabel){
			drugLabel = addDrugLabelToDB(id);
		}
		return drugLabel;
	}
	
	private DrugLabel addDrugLabelToDB(String id){
		DrugLabel drugLabel = openFdaRepository.searchFromFDAById(id);
		drugRepository.insert(drugLabel);
		return drugLabel;
	}
	
	private List<DrugLabel> addDrugLabelsToDB(List<String> ids){
		List<DrugLabel> drugLabels = openFdaRepository.searchFromFDAById(ids);
		drugRepository.insert(drugLabels);
		return drugLabels;
	}
}
