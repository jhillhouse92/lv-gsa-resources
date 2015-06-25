package com.longview.gsa.repository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.longview.gsa.domain.DrugLabel;
import com.longview.gsa.utility.NullCheck;
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
		List<String> idList = StreamSupport.stream(ids.spliterator(), false)
			.collect(Collectors.toList());
		return mongoTemplate.find(new Query().addCriteria(Criteria.where("id").in(idList)), DrugLabel.class);
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
		DBCollection collection = mongoTemplate.getCollection("drugs");
		collection.insert(dbObj);
	}
	
	@Override
	public List<DrugLabel> fetchMedsList(List<String> fieldNames, String criteriaValue) {
		if(NullCheck.isNotNullish(fieldNames) && NullCheck.isNotNullish(criteriaValue)){
			Criteria criteria = new Criteria();
			List<Criteria> criteriaList = new ArrayList<Criteria>();
			for (String fieldName: fieldNames) {
				criteriaList.add(Criteria.where(fieldName).regex(Pattern.compile(criteriaValue,Pattern.CASE_INSENSITIVE)));
			}	
			return mongoTemplate.find(new Query(criteria.orOperator(criteriaList.toArray(new Criteria[fieldNames.size()]))), DrugLabel.class);
		}
		return null;
	}
}