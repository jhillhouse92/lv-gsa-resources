package com.longview.gsa.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.longview.gsa.domain.WarningCategory;

public interface WarningCategoriesRepository extends MongoRepository<WarningCategory, String> {

	List<WarningCategory> fetchValidWarnings();
}
