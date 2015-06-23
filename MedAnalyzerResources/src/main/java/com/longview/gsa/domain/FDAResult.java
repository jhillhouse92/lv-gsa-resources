package com.longview.gsa.domain;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FDAResult {

	private Meta meta;
	private List<DrugLabel> results;
	
	public static class Meta {
        //data from FDA
    }

	public Meta getMeta() {
		return meta;
	}

	public void setMeta(Meta meta) {
		this.meta = meta;
	}

	public List<DrugLabel> getResults() {
		return results;
	}

	public void setResults(List<DrugLabel> results) {
		this.results = results;
	}
	
	
}
