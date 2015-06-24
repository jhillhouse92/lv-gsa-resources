package com.longview.gsa.service;

import java.util.List;

import com.longview.gsa.domain.DrugLabel;
import com.longview.gsa.domain.DrugSearchResult;
import com.longview.gsa.domain.GraphResult;

public interface DrugService {

	List<DrugSearchResult> fetchMedList(String criteriaValue);
	List<GraphResult> fetchGraph(List<String> ids);
	DrugLabel fetchLabel(String id);
}
