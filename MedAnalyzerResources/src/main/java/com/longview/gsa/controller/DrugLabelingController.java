package com.longview.gsa.controller;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.longview.gsa.domain.DrugLabel;
import com.longview.gsa.domain.Greeting;
import com.longview.gsa.repository.DrugRepository;

@RestController
@RequestMapping(value = "/drugs")
public class DrugLabelingController {
	
	@Autowired
	private DrugRepository drugRepository;

	@RequestMapping(value = "/setup")
	public Greeting setup() {
		JSONObject json = new JSONObject();

		try {
			json = new JSONObject(
					IOUtils.toString(
							new URL(
									"https://api.fda.gov/drug/label.json?search=openfda.substance_name:%22acetaminophen%22&limit=1"),
							Charset.forName("UTF-8")));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		JSONArray results = json.getJSONArray("results");
		for(int i = 0; i<results.length(); i++){
			DrugLabel dl = new DrugLabel();
		}
		
		drugRepository.insertDBObject(o);

		return new Greeting(1, "Setup ran!");
	}
}
