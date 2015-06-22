package com.longview.gsa.controller;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sf.extjwnl.JWNLException;
import net.sf.extjwnl.data.IndexWordSet;
import net.sf.extjwnl.data.POS;
import net.sf.extjwnl.dictionary.Dictionary;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.longview.gsa.domain.DrugLabel;
import com.longview.gsa.domain.FDAResult;
import com.longview.gsa.domain.Greeting;
import com.longview.gsa.repository.DrugRepository;
import com.longview.gsa.service.MedCheckerService;


@RestController
@RequestMapping(value = "/drugs")
public class DrugLabelingController {
	
	@Autowired
	private DrugRepository drugRepository;

	@Autowired
	private MedCheckerService medCheckerService;
	
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
			IndexWordSet word = d.lookupAllIndexWords("keep");
			String nounCount = "There are " + word.getSenseCount(POS.NOUN) + " nouns";
			String verbCount = "There are " + word.getSenseCount(POS.VERB) + " verbs";
			String adjectiveCount = "There are " + word.getSenseCount(POS.ADJECTIVE) + " adjectives";
			String adverbCount = "There are " + word.getSenseCount(POS.ADVERB) + " adverbs";
			
			List<Speech> typeOfSpeech = new ArrayList<Speech>();
			typeOfSpeech.add(new Speech(POS.NOUN, word.getSenseCount(POS.NOUN)));
			typeOfSpeech.add(new Speech(POS.VERB, word.getSenseCount(POS.VERB)));
			typeOfSpeech.add(new Speech(POS.ADJECTIVE, word.getSenseCount(POS.NOUN)));
			typeOfSpeech.add(new Speech(POS.ADVERB, word.getSenseCount(POS.ADVERB)));
				
			Collections.sort(typeOfSpeech, (object1, object2) -> object2.count.compareTo(object1.count));
			
			if(typeOfSpeech.get(0).pos == POS.NOUN) 
				return new Greeting(1, "The type of speech is:" + typeOfSpeech.get(0).pos + ". The count is:" + typeOfSpeech.get(0).count);
		} catch (JWNLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public class Speech{
		
		public POS pos;
		public Integer count;
		
		public Speech(POS pos, Integer count){
			this.pos = pos;
			this.count = count;
		}
	}
	
	@RequestMapping(value = "/search/{keyWord}")
	public List<DrugLabel> fetchMedList(@PathVariable String keyWord) {
		return medCheckerService.fetchMedList(keyWord);
	}
}
