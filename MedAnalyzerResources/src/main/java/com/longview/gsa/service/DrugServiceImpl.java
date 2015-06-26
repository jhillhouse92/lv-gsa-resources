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
import com.longview.gsa.utility.GsaParameterChecking;
import com.longview.gsa.utility.NullCheck;

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
		GsaParameterChecking.check(criteriaValue);
		
		List<DrugSearchResult> dsrList = new ArrayList<DrugSearchResult>();
		
		List<DrugLabel> apiSearchResults = openFdaRepository.searchFromFDA(fieldNames, criteriaValue);
		
		for(DrugLabel dl : apiSearchResults){
			String brandName = "", genericName = "", substanceName = "", manufacturerName = "";	
			if(!NullCheck.isNullish(dl.getOpenfda().getBrand_name()))
				brandName = String.join(" ", dl.getOpenfda().getBrand_name());
			if(!NullCheck.isNullish(dl.getOpenfda().getGeneric_name()))
				genericName = String.join(" ", dl.getOpenfda().getGeneric_name());
			if(!NullCheck.isNullish(dl.getOpenfda().getSubstance_name()))
				substanceName = String.join(" ", dl.getOpenfda().getSubstance_name());
			if(!NullCheck.isNullish(dl.getOpenfda().getManufacturer_name()))
				manufacturerName = String.join(" ", dl.getOpenfda().getManufacturer_name());
			List<String> match = new ArrayList<String>(2); //initialize match as 2 is maximum
			
			DrugSearchResult dsr = new DrugSearchResult();
			
			dsr.setId(dl.getId());
			dsr.setBrandName(brandName);
			dsr.setManufacturerName(manufacturerName);
			
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
		GsaParameterChecking.check(ids);
		
		//create list of GraphResult
		List<GraphResult> results = new ArrayList<GraphResult>();
		
		//get list of the drugs
		List<DrugLabel> drugLabels = (List<DrugLabel>) drugRepository.findAll(ids);
		
		if(ids.size()!=drugLabels.size()){
			for(DrugLabel drug : drugLabels){
				ids.remove(drug.getId());
			}
			drugLabels.addAll(addDrugLabelsToDB(ids));
		}
		
		//for each drug, look which warnings match
		for(DrugLabel drug : drugLabels){
			
			//for each warning check to see if this warning matches
			
			StringBuffer buffer = new StringBuffer();
			if(!NullCheck.isNullish(drug.getWarnings())){
				buffer.append(String.join(" ", drug.getWarnings()));
			}
			if(!NullCheck.isNullish(drug.getWarnings_and_cautions())){
				buffer.append(String.join(" ", drug.getWarnings_and_cautions()));
			}
			
			String warning = buffer.toString().replaceAll("[^a-zA-Z\\s]", "");
			
				//find based on mapReduce results using same algorithm of warning before or after
				List<String> warningCategories = 
						adminSerivce.getWarningCategories().stream()
						.filter(w -> warning.toLowerCase().contains(w.get_id().split(" ")[0].toLowerCase().trim() + " warning")
									|| warning.toLowerCase().contains("warning " + w.get_id().split(" ")[0].toLowerCase().trim())
									|| warning.toLowerCase().contains("warnings " + w.get_id().split(" ")[0].toLowerCase().trim())
									|| warning.toLowerCase().contains("warnings and precautions " + w.get_id().split(" ")[0].toLowerCase().trim())
								)
						.map(w -> w.get_id())
						.collect(Collectors.toList());
				GraphResult graphItem = new GraphResult();
				graphItem.setId(drug.getId());
				if(!NullCheck.isNullish(drug.getOpenfda().getBrand_name()))
					graphItem.setBrandName(String.join(" ", drug.getOpenfda().getBrand_name()));
				graphItem.setWarnings(warningCategories);
				if(NullCheck.isNullish(graphItem.getWarnings()) && NullCheck.isNotNullish(warning)){
					warningCategories.add("Other Warnings");
					graphItem.setWarnings(warningCategories);
				}
				
				results.add(graphItem);
			}
		return results;
	}

	@Override
	public DrugLabel fetchLabel(String id) {
		GsaParameterChecking.check(id);
		
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
