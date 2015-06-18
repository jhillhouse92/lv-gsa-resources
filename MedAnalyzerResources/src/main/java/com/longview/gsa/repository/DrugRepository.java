package com.longview.gsa.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.longview.gsa.domain.DrugLabel;
import com.mongodb.DBObject;

public interface DrugRepository extends MongoRepository<DrugLabel, String> {

	public void insertDBObject(DBObject dbObj);
}
