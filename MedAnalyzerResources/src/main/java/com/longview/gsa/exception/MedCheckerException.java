package com.longview.gsa.exception;

import org.apache.log4j.Logger;

public class MedCheckerException extends RuntimeException{
	
	private static org.apache.log4j.Logger logger = Logger.getLogger(MedCheckerException.class);
	
	private static final long serialVersionUID = 5566238862313119778L;
	
    public MedCheckerException(String message) {
        super(message);
        logger.error(message);
    }
}
