package com.longview.gsa.service;

import java.util.List;

import com.longview.gsa.domain.DrugLabel;
import com.longview.gsa.domain.GraphResult;

public interface DrugService {

	List<DrugLabel> fetchMedList(String criteriaValue);
	List<GraphResult> fetchGraph(List<String> ids);
}
