package com.longview.gsa.controller;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.List;

import net.sf.extjwnl.JWNLException;
import net.sf.extjwnl.data.IndexWord;
import net.sf.extjwnl.data.POS;
import net.sf.extjwnl.data.Synset;
import net.sf.extjwnl.dictionary.Dictionary;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.longview.gsa.domain.FDAResult;
import com.longview.gsa.domain.Greeting;
import com.longview.gsa.repository.DrugRepository;


@RestController
@RequestMapping(value = "/drugs")
public class DrugLabelingController {
	
	@Autowired
	private DrugRepository drugRepository;

	@RequestMapping(value = "/setup")
	public Greeting setup() {
		FDAResult fdaResult = new FDAResult();
		try {
			fdaResult = new Gson().fromJson(IOUtils.toString(
					new URL(
							"https://api.fda.gov/drug/label.json?limit=100&skip=0"),
					Charset.forName("UTF-8")), FDAResult.class);
		} catch (JsonSyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		drugRepository.insert(fdaResult.getResults());

		return new Greeting(1, "Setup ran!");
	}
	
	@RequestMapping(value = "/clean-warnings")
	public Greeting cleanWarnings() {
		try {
			Dictionary d = 	Dictionary.getDefaultResourceInstance();
			IndexWord word = d.lookupIndexWord(POS.VERB, "accomplish");
			List<Synset> wordSynset = word.getSenses();
			
			return new Greeting(1, wordSynset.get(0).getGloss());
		} catch (JWNLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
