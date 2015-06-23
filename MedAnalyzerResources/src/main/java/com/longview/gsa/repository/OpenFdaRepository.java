package com.longview.gsa.repository;

import java.util.List;

import com.longview.gsa.domain.DrugLabel;

public interface OpenFdaRepository {

	List<DrugLabel> searchFromFDA(List<String> fieldNames, String criteriaValue);
	
}
