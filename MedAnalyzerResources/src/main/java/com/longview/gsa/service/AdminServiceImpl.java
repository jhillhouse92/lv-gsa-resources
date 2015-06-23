package com.longview.gsa.service;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.longview.gsa.domain.FDAResult;
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
	public void importFromFDA(){
		
		FDAResult fdaResult = new FDAResult();
		try {
			fdaResult = new Gson().fromJson(IOUtils.toString(
					new URL(
							"https://api.fda.govv/drug/label.json?limit=100&skip=0"),
					Charset.forName("UTF-8")), FDAResult.class);
		} catch (JsonSyntaxException e) {
			throw new MedCheckerException("error error errror");
		} catch (MalformedURLException e) {
			throw new MedCheckerException("error error errror");
		} catch (IOException e) {
			throw new MedCheckerException("error error errror");
		}
		drugRepository.insert(fdaResult.getResults());
	}
	
	@Override
	public void ImportWarningCategories() {
		// TODO Auto-generated method stub
		List<WarningCategory> warningCategories = new ArrayList<WarningCategory>();
		try {
			Reader reader = new InputStreamReader(AdminServiceImpl.class.getClassLoader().getResourceAsStream("mongo/warningCategories.json"));
			warningCategories = new Gson().fromJson(
					reader, 
					new TypeToken<List<WarningCategory>>() {}.getType());

		} catch (JsonSyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch(JsonIOException e) {
			
			e.printStackTrace();
		}
		
		warningRepo.insert(warningCategories);
	}

	@Override
	public List<WarningCategory> getWarningCategories() {
		// TODO Auto-generated method stub
		return warningRepo.fetchValidWarnings();
	}
}
