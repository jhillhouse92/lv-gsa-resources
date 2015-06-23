package com.longview.gsa.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FdaApiError {
	private ErrorResource error;

	public ErrorResource getError() {
		return error;
	}

	public void setError(ErrorResource error) {
		this.error = error;
	}	
}