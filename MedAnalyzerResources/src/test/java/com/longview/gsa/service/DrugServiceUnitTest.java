package com.longview.gsa.service;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
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
import com.longview.gsa.domain.OpenFDA;
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
	private AdminService adminSerivce;
	
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
	@Ignore
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
	@Ignore
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
	@Ignore
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
	@Ignore
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
	@Ignore
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
	@Ignore
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
	@Ignore
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
		Assert.assertEquals(null, drugSearchResult.get(2).getBrandName());
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
		
		DrugLabel drugLabel = new DrugLabel();
		OpenFDA openFda = new OpenFDA();
		openFda.setBrand_name(new String[]{"CRESTOR"});
		openFda.setManufacturer_name(new String[]{"Bryant Ranch Prepack"});
		drugLabel.setOpenfda(openFda);
		drugLabel.setId("6d611956-c43b-4178-9f6c-ef44de100fe6");
		drugLabels.add(drugLabel);
		
		DrugLabel drugLabel1 = new DrugLabel();
		OpenFDA openFda1 = new OpenFDA();
		openFda1.setBrand_name(new String[]{"CRESTOR"});
		drugLabel1.setOpenfda(openFda1);
		drugLabel1.setId("6d611956-c43b-4178-9f6c-ef44de100fe6");
		drugLabels.add(drugLabel1);
		
		DrugLabel drugLabel2 = new DrugLabel();
		OpenFDA openFda2 = new OpenFDA();
		drugLabel2.setOpenfda(openFda2);
		drugLabel2.setId("6d611956-c43b-4178-9f6c-ef44de100fe6");
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
	
}