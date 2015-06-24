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
