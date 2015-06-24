package com.longview.gsa.service;

import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.longview.gsa.domain.WarningCategory;
import com.longview.gsa.exception.MedCheckerException;
import com.longview.gsa.repository.DrugRepository;
import com.longview.gsa.repository.WarningCategoriesRepository;

@Service
public class AdminServiceImpl implements AdminService {

	private static org.apache.log4j.Logger log = Logger.getLogger(AdminServiceImpl.class);
	   
	@Autowired
	private DrugRepository drugRepository;
	
	@Autowired
	public WarningCategoriesRepository warningRepo;
	
	@Override
	public void ImportWarningCategories() {
		List<WarningCategory> warningCategories = new ArrayList<WarningCategory>();
		try {
			Reader reader = new InputStreamReader(AdminServiceImpl.class.getClassLoader().getResourceAsStream("mongo/warningCategories.json"));
			warningCategories = new Gson().fromJson(
					reader, 
					new TypeToken<List<WarningCategory>>() {}.getType());

		} catch (JsonSyntaxException e) {
			log.error(e.getMessage(), e);
			throw new MedCheckerException(e.getMessage());
		} catch(JsonIOException e) {
			log.error(e.getMessage(), e);
			throw new MedCheckerException(e.getMessage());
		}
		
		warningRepo.insert(warningCategories);
	}

	@Override
	public List<WarningCategory> getWarningCategories() {
		return warningRepo.fetchValidWarnings();
	}
}
