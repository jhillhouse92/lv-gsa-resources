package com.longview.gsa.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sf.extjwnl.JWNLException;
import net.sf.extjwnl.data.IndexWordSet;
import net.sf.extjwnl.data.POS;
import net.sf.extjwnl.dictionary.Dictionary;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.longview.gsa.domain.DrugLabel;
import com.longview.gsa.domain.DrugSearchResult;
import com.longview.gsa.domain.GraphResult;
import com.longview.gsa.service.AdminService;
import com.longview.gsa.service.DrugService;


@RestController
@RequestMapping(value = "/drugs")
public class DrugLabelingController {
	
	@Autowired
	private DrugService drugService;
	
	@Autowired
	private AdminService adminService;
	
	@RequestMapping(value = "/setup")
	public String setup() {
		adminService.ImportWarningCategories();
		return new String("Successfully imported warning categories.");
	}
	
	@RequestMapping(value = "/clean-warnings")
	public String cleanWarnings() {
		try {
			Dictionary d = 	Dictionary.getDefaultResourceInstance();
			IndexWordSet word = d.lookupAllIndexWords("keep");
			
			List<Speech> typeOfSpeech = new ArrayList<Speech>();
			typeOfSpeech.add(new Speech(POS.NOUN, word.getSenseCount(POS.NOUN)));
			typeOfSpeech.add(new Speech(POS.VERB, word.getSenseCount(POS.VERB)));
			typeOfSpeech.add(new Speech(POS.ADJECTIVE, word.getSenseCount(POS.NOUN)));
			typeOfSpeech.add(new Speech(POS.ADVERB, word.getSenseCount(POS.ADVERB)));
				
			Collections.sort(typeOfSpeech, (object1, object2) -> object2.count.compareTo(object1.count));
			
			if(typeOfSpeech.get(0).pos == POS.NOUN) 
				return new String("The type of speech is:" + typeOfSpeech.get(0).pos + ". The count is:" + typeOfSpeech.get(0).count);
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
	public List<DrugSearchResult> fetchMedList(@PathVariable String keyWord) {
		return drugService.fetchMedList(keyWord);
	}
	
	@RequestMapping(value = "graph", method = RequestMethod.POST)
	public List<GraphResult> showGraph(@RequestBody List<String> ids){
		return drugService.fetchGraph(ids);
	}
	
	@RequestMapping(value = "label", method = RequestMethod.POST)
	public DrugLabel showLabel(@RequestBody String id){
		return drugService.fetchLabel(id);
	}
}
