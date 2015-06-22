package com.longview.gsa.repository;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.longview.gsa.domain.WarningCategory;

@Repository
public class WarningCategoriesRepositoryImpl implements
		WarningCategoriesRepository {

	@Autowired
	MongoTemplate mongoTemplate;
	
	@Override
	public <S extends WarningCategory> List<S> save(Iterable<S> entites) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<WarningCategory> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<WarningCategory> findAll(Sort sort) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <S extends WarningCategory> S insert(S entity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <S extends WarningCategory> List<S> insert(Iterable<S> entities) {
		// TODO Auto-generated method stub
		mongoTemplate.dropCollection(WarningCategory.class);
		mongoTemplate.insertAll((Collection<? extends Object>) entities);
		return null;
	}

	@Override
	public Page<WarningCategory> findAll(Pageable pageable) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <S extends WarningCategory> S save(S entity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public WarningCategory findOne(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean exists(String id) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Iterable<WarningCategory> findAll(Iterable<String> ids) {
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
	public void delete(WarningCategory entity) {
		// TODO Auto-generated method stub

	}

	@Override
	public void delete(Iterable<? extends WarningCategory> entities) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteAll() {
		// TODO Auto-generated method stub

	}

	@Override
	public List<WarningCategory> fetchValidWarnings() {
		// TODO Auto-generated method stub
		return mongoTemplate.find(new Query().addCriteria(Criteria.where("valid").is(true)), WarningCategory.class);
	}

}
