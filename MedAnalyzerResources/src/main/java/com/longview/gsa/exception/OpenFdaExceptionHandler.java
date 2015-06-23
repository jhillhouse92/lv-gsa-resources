package com.longview.gsa.exception;

import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.ResponseErrorHandler;

import com.google.gson.Gson;

public class OpenFdaExceptionHandler implements  ResponseErrorHandler{

	private ResponseErrorHandler errorHandler = new DefaultResponseErrorHandler();

    public boolean hasError(ClientHttpResponse response) throws IOException {
        return errorHandler.hasError(response);
    }

    public void handleError(ClientHttpResponse response) throws IOException {
    	String theString = IOUtils.toString(response.getBody());	
    	com.longview.gsa.domain.FdaApiError error = new Gson().fromJson(theString, com.longview.gsa.domain.FdaApiError.class);
        MedCheckerException exception = new MedCheckerException(error.getError().getMessage());
        throw exception;
    }
}