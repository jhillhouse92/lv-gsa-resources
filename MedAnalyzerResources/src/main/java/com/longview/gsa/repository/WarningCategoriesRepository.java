package com.longview.gsa.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.longview.gsa.domain.WarningCategory;

public interface WarningCategoriesRepository extends MongoRepository<WarningCategory, String> {

}
