package com.longview.gsa.service;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import com.longview.gsa.config.ApplicationConfig;
import com.longview.gsa.domain.DrugLabel;
import com.longview.gsa.domain.DrugSearchResult;
import com.longview.gsa.domain.GraphResult;
import com.longview.gsa.domain.OpenFDA;
import com.longview.gsa.domain.WarningCategory;
import com.longview.gsa.exception.MedCheckerException;
import com.longview.gsa.exception.OpenFdaExceptionHandler;
import com.longview.gsa.repository.DrugRepository;
import com.longview.gsa.repository.OpenFdaRepositoryImpl;

@ContextConfiguration(classes=ApplicationConfig.class)
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
public class DrugServiceUnitTest {

	@InjectMocks
	private DrugServiceImpl drugService;
	
	@Mock
	private DrugRepository drugRepository;
	
	@Mock
	private AdminServiceImpl adminSerivce;
	
	@Mock
	private OpenFdaRepositoryImpl openFdaRepository;
	
	@Before
	public void init() {
		MockitoAnnotations.initMocks(this);
		ReflectionTestUtils.setField(openFdaRepository, "endPoint", "https://api.fda.gov/drug/label.json");
		RestTemplate restTemplate = new RestTemplate();
		restTemplate.setErrorHandler(new OpenFdaExceptionHandler());
		ReflectionTestUtils.setField(openFdaRepository, "restTemplate", restTemplate);		
	}
	
	/**
	 * Check for No Match exception
	 */
	@Test
	public void noMatchException() {
		try{
			drugService.fetchLabel("sdfsdfsfdsdvsd");
		}
		catch(Exception e){
			Assert.assertEquals("No matches found!", e.getMessage());
			Assert.assertTrue(e instanceof MedCheckerException);
		}
	}
	
	/**
	 * Check for null parameter
	 */
	@Test
	public void nullParameterException() {
		try{
			drugService.fetchLabel(null);
		}
		catch(Exception e){
			Assert.assertEquals("Invalid parameter", e.getMessage());
			Assert.assertTrue(e instanceof MedCheckerException);
		}
	}
	
	/**
	 * Check for findOne returns null
	 */
	@Test
	public void checkfindOneForFetchLabel() {
		DrugLabel drugLabel = null;
		try{
			Mockito.when(drugRepository.findOne("")).thenReturn(null);
			drugLabel = drugService.fetchLabel("iuyiuhhki");
		}
		catch(Exception e){
			Assert.assertEquals("Invalid paramater", e.getMessage());
			Assert.assertTrue(e instanceof MedCheckerException);
		}
		Assert.assertNull(drugLabel);
	}
	
	/**
	 * Check for fetch label
	 */
	@Test
	public void checkFetchLabel() {
		DrugLabel drugLabel = mockDrugLabel();
		Mockito.when(drugRepository.findOne("6d611956-c43b-4178-9f6c-ef44de100fe6")).thenReturn(drugLabel);
		drugLabel = drugService.fetchLabel("6d611956-c43b-4178-9f6c-ef44de100fe6");
		Assert.assertNotNull(drugLabel);
		Assert.assertEquals("CRESTOR", drugLabel.getOpenfda().getBrand_name()[0]);
	}
	
	/**
	 * Check invalid parameter for search results
	 */
	@Test
	public void checkInvalidSearchResults() {
		try{
			drugService.fetchMedList(null);
		}
		catch(Exception e){
			Assert.assertEquals("Invalid parameter", e.getMessage());
			Assert.assertTrue(e instanceof MedCheckerException);
		}
	}
	
	/**
	 * Check no match found for search results
	 */
	@Test
	public void checkNoMatchSearchResults() {
		try{
			drugService.fetchMedList("sdfsdfsdfsdfsfs");
		}
		catch(Exception e){
			Assert.assertEquals("No matches found!", e.getMessage());
			Assert.assertTrue(e instanceof MedCheckerException);
		}
	}
	
	/**
	 * Check search results
	 */
	@Test
	public void checkSearchResults() {
		Mockito.when(openFdaRepository.searchFromFDA(getFieldNames(), "CRESTOR")).thenCallRealMethod();
		List<DrugSearchResult> drugSearchResult = drugService.fetchMedList("CRESTOR");
		Assert.assertNotNull(drugSearchResult);
		Assert.assertEquals("CRESTOR", drugSearchResult.get(0).getBrandName());
	}
	
	/**
	 * Check search results for some null fields
	 */
	@Test
	public void checkForSomeNullFieldsSearchResults() {	
		Mockito.when(openFdaRepository.searchFromFDA(getFieldNames(), "CRESTOR")).thenReturn(mockListOfDrugLabel());
		List<DrugSearchResult> drugSearchResult = drugService.fetchMedList("CRESTOR");
		Assert.assertNotNull(drugSearchResult);
		Assert.assertEquals("CRESTOR", drugSearchResult.get(0).getBrandName());
		Assert.assertNotNull(drugSearchResult);
		Assert.assertEquals("CRESTOR", drugSearchResult.get(1).getBrandName());
		Assert.assertNotNull(drugSearchResult);
		Assert.assertEquals("", drugSearchResult.get(2).getBrandName());
	}
	
	/**
	 * Check invalid parameter for graph
	 */
	@Test
	public void checkInvalidParameterForGraph() {
		try{
			drugService.fetchGraph(null);
		}
		catch(Exception e){
			Assert.assertEquals("Invalid parameters", e.getMessage());
			Assert.assertTrue(e instanceof MedCheckerException);
		}
	}
	
	/**
	 * Check graph
	 */
	@Test
	public void checkForGraph() {
		
		Mockito.when(drugRepository.findAll(listOfIds())).thenReturn(mockListOfDrugLabel());
		Mockito.when(adminSerivce.getWarningCategories()).thenReturn(listOfWarningCategory());
		List<GraphResult> graphResult = drugService.fetchGraph(listOfIds());
		Assert.assertTrue(graphResult.size()>0);
		Assert.assertEquals("bleeding warning",graphResult.get(0).getWarnings().get(0));
	}
	
	private DrugLabel mockDrugLabel(){
		DrugLabel drugLabel = new DrugLabel();
		OpenFDA openFda = new OpenFDA();
		openFda.setBrand_name(new String[]{"CRESTOR"});
		openFda.setManufacturer_name(new String[]{"Bryant Ranch Prepack"});
		drugLabel.setOpenfda(openFda);
		drugLabel.setId("6d611956-c43b-4178-9f6c-ef44de100fe6");
		return drugLabel;
	}
	
	private List<DrugLabel> mockListOfDrugLabel(){
		List<DrugLabel> drugLabels =  new ArrayList<DrugLabel>();
		
		String[] warnings = new String[1];
		warnings[0]="bleeding warning";
		
		DrugLabel drugLabel = new DrugLabel();
		OpenFDA openFda = new OpenFDA();
		openFda.setBrand_name(new String[]{"CRESTOR"});
		openFda.setManufacturer_name(new String[]{"Bryant Ranch Prepack"});
		drugLabel.setOpenfda(openFda);
		drugLabel.setId("6d611956-c43b-4178-9f6c-ef44de100fe6");
		drugLabel.setWarnings(warnings);
		drugLabels.add(drugLabel);
		
		String[] warningsandcautions = new String[1];
		warningsandcautions[0]="warnings and precautions skeletal";
		DrugLabel drugLabel1 = new DrugLabel();
		OpenFDA openFda1 = new OpenFDA();
		openFda1.setBrand_name(new String[]{"CRESTOR"});
		drugLabel1.setOpenfda(openFda1);
		drugLabel1.setId("77a9257b-0826-4283-84d9-f88ad9125474");
		drugLabel1.setWarnings_and_cautions(warningsandcautions);
		drugLabels.add(drugLabel1);
		
		DrugLabel drugLabel2 = new DrugLabel();
		OpenFDA openFda2 = new OpenFDA();
		drugLabel2.setOpenfda(openFda2);
		drugLabel2.setId("52559552-5697-4d25-9620-9bb259f05aab");
		drugLabels.add(drugLabel2);
		
		return drugLabels;
	}
	
	private ArrayList<String> getFieldNames(){
		ArrayList<String> fieldNames = new ArrayList<String>();
		fieldNames.add("openfda.brand_name");
		fieldNames.add("openfda.generic_name");
		fieldNames.add("openfda.substance_name");
		return fieldNames;
	}
	
	private List<String> listOfIds(){
		List<String> ids = new ArrayList<String>();
		ids.add("6d611956-c43b-4178-9f6c-ef44de100fe6");
		ids.add("77a9257b-0826-4283-84d9-f88ad9125474");
		ids.add("52559552-5697-4d25-9620-9bb259f05aab");
		return ids;
	}
	
	private List<WarningCategory> listOfWarningCategory(){
		List<WarningCategory> listWarningCategory = new ArrayList<WarningCategory>();
		WarningCategory warningCategory1 = new WarningCategory();
		warningCategory1.set_id("bleeding warning");
		warningCategory1.setValid(true);
		listWarningCategory.add(warningCategory1);
		
		WarningCategory warningCategory2 = new WarningCategory();
		warningCategory2.set_id("skeletal warnings and precautions");
		warningCategory2.setValid(true);
		listWarningCategory.add(warningCategory2);
		
		return listWarningCategory;
	}
	
}