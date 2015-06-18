package com.longview.gsa.repository;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

import com.longview.gsa.domain.DrugLabel;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;

@Repository
public class DrugRepositoryImpl implements DrugRepository {

	@Autowired
	private MongoTemplate mongoTemplate;
	
	@Override
	public <S extends DrugLabel> List<S> save(Iterable<S> entites) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<DrugLabel> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<DrugLabel> findAll(Sort sort) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <S extends DrugLabel> S insert(S entity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <S extends DrugLabel> List<S> insert(Iterable<S> entities) {
		// TODO Auto-generated method stub
		mongoTemplate.insertAll((Collection<? extends Object>) entities);
		return null;
	}

	@Override
	public Page<DrugLabel> findAll(Pageable pageable) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <S extends DrugLabel> S save(S entity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DrugLabel findOne(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean exists(String id) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Iterable<DrugLabel> findAll(Iterable<String> ids) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long count() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void delete(String id) {
		// TODO Auto-generated method stub

	}

	@Override
	public void delete(DrugLabel entity) {
		// TODO Auto-generated method stub

	}

	@Override
	public void delete(Iterable<? extends DrugLabel> entities) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteAll() {
		// TODO Auto-generated method stub

	}

	@Override
	public void insertDBObject(DBObject dbObj) {
		// TODO Auto-generated method stub
		DBCollection collection = mongoTemplate.getCollection("drugs");
		collection.insert(dbObj);
	}

}
