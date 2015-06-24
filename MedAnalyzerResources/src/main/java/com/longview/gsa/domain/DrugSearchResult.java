package com.longview.gsa.domain;

import java.util.List;


public class DrugSearchResult {

	String id;
	String brandName;
	String manufacturerName;
	List<String> match;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getBrandName() {
		return brandName;
	}
	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}
	public List<String> getMatch() {
		return match;
	}
	public void setMatch(List<String> match) {
		this.match = match;
	}
	public String getManufacturerName() {
		return manufacturerName;
	}
	public void setManufacturerName(String manufacturerName) {
		this.manufacturerName = manufacturerName;
	}
}
