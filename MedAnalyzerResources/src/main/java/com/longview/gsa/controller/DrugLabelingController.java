package com.longview.gsa.controller;

import java.util.List;

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

/**
 * This RestController is to retrieve and process data regarding
 * Drug Labels from the OpenFDA API data source.
 * <p>
 * All Requests start with {@link http://ec2-54-243-195-170.compute-1.amazonaws.com:8080/drugs}.
 *
 * @author 	LongView GSA Back-End Team
 * @see		RestController
 * @see		RequestMapping
 * @see     <a href="https://open.fda.gov/drug/label/">Open FDA Drug Labeling</a>
 */
@RestController
@RequestMapping(value = "/drugs")
public class DrugLabelingController {
	
	@Autowired
	private DrugService drugService;
	
	@Autowired
	private AdminService adminService;
	
	/**
	 * GET: {@link http://ec2-54-243-195-170.compute-1.amazonaws.com:8080/drugs/setup}.
	 * This rest endpoint is to import the warning categories intially. These
	 * warning categories have been determined based on a Map Reduce algorithm
	 * ran against a subset of Open FDA Drug Labeling data in a MongoDB context.
	 * 
	 * @return 	A message based on the success
	 * @see 	RequestMapping 
	 * @see     <a href="https://open.fda.gov/drug/label/">Open FDA Drug Labeling</a>
	 * @see 	<a href="http://docs.mongodb.org/manual/core/map-reduce/">MongoDB Map Reduce</a>
	 */
	@RequestMapping(value = "/setup")
	public String setup() {
		adminService.ImportWarningCategories();
		return new String("Successfully imported warning categories.");
	}
	
	/**
	 * GET: {@link http://ec2-54-243-195-170.compute-1.amazonaws.com:8080/drugs/search/{keyWord}}.
	 * This rest endpoint is to search the OpenFDA API data source.
	 * 
	 * @return 	A list of Drug Search Results
	 * @see 	DrugService
	 * @see 	RequestMapping 
	 * @see     <a href="https://open.fda.gov/drug/label/">Open FDA Drug Labeling</a>
	 */
	@RequestMapping(value = "/search/{keyWord}")
	public List<DrugSearchResult> fetchMedList(@PathVariable String keyWord) {
		return drugService.fetchMedList(keyWord);
	}
	
	/**
	 * POST: {@link http://ec2-54-243-195-170.compute-1.amazonaws.com:8080/drugs/graph}.
	 * This rest endpoint is to import the warning categories intially. These
	 * warning categories have been determined based on a Map Reduce algorithm
	 * ran against a subset of data in a MongoDB context.
	 * 
	 * @return A message based on the success
	 * @see <a href="http://docs.mongodb.org/manual/core/map-reduce/">MongoDB Map Reduce</a>
	 * @see RequestMapping 
	 */
	@RequestMapping(value = "graph", method = RequestMethod.POST)
	public List<GraphResult> showGraph(@RequestBody List<String> ids){
		return drugService.fetchGraph(ids);
	}
	
	/**
	 * GET: {@link http://ec2-54-243-195-170.compute-1.amazonaws.com:8080/drugs/label/{id}}.
	 * This rest endpoint is to import the warning categories intially. These
	 * warning categories have been determined based on a Map Reduce algorithm
	 * ran against a subset of data in a MongoDB context.
	 * 
	 * @return A message based on the success
	 * @see <a href="http://docs.mongodb.org/manual/core/map-reduce/">MongoDB Map Reduce</a>
	 * @see RequestMapping 
	 */
	@RequestMapping(value = "label/{id}", method = RequestMethod.GET)
	public DrugLabel showLabel(@PathVariable String id){
		return drugService.fetchLabel(id);
	}
}
