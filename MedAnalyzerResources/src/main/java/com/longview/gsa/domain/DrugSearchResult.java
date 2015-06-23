package com.longview.gsa.domain;

import java.util.List;

public class DrugSearchResult {

	private List<DrugLabel> brandName;
	private List<DrugLabel> genericName;
	private List<DrugLabel> substanceName;
	
	public List<DrugLabel> getBrandName() {
		return brandName;
	}
	public void setBrandName(List<DrugLabel> brandName) {
		this.brandName = brandName;
	}
	public List<DrugLabel> getGenericName() {
		return genericName;
	}
	public void setGenericName(List<DrugLabel> genericName) {
		this.genericName = genericName;
	}
	public List<DrugLabel> getSubstanceName() {
		return substanceName;
	}
	public void setSubstanceName(List<DrugLabel> substanceName) {
		this.substanceName = substanceName;
	}
}
