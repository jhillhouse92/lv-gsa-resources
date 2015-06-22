package com.longview.gsa.service;

import java.util.List;

import com.longview.gsa.domain.DrugLabel;

public interface MedCheckerService {

	List<DrugLabel> fetchMedList(String criteriaValue);
}
