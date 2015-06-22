package com.longview.gsa.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.longview.gsa.domain.DrugLabel;
import com.mongodb.DBObject;

public interface DrugRepository extends MongoRepository<DrugLabel, String> {

	public void insertDBObject(DBObject dbObj);
	public List<DrugLabel> fetchMedsList(List<String> fieldNames, String criteriaValue);
}
