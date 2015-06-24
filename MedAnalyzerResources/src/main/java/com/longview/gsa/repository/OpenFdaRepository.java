package com.longview.gsa.repository;

import java.util.List;
import java.util.Map;

import com.longview.gsa.domain.DrugLabel;

public interface OpenFdaRepository {

	List<DrugLabel> searchFromFDA(List<String> fieldNames, String criteriaValue);

	DrugLabel searchFromFDAById(String id);

	List<DrugLabel> searchFromFDAById(List<String> ids);

	List<DrugLabel> searchFromFDA(Map<String, String> fieldNamesWithCriteria);
	
}
