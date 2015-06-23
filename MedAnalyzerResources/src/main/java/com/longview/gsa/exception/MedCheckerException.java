package com.longview.gsa.exception;


public class MedCheckerException extends RuntimeException{

	private static final long serialVersionUID = 5566238862313119778L;
	
	private String error;

    public MedCheckerException(String message, String error) {
        super(message);
        this.error = error;
    }

    public String getErrors() { return error; }
}
