package com.longview.gsa.service;

import java.util.List;

import com.longview.gsa.domain.WarningCategory;

public interface AdminService {

	void ImportWarningCategories();
	List<WarningCategory> getWarningCategories();
}
