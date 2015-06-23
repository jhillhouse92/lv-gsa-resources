package com.longview.gsa.exception;

import org.apache.log4j.Logger;

public class MedCheckerException extends RuntimeException{
	
	private static org.apache.log4j.Logger log = Logger.getLogger(MedCheckerException.class);
	
	private static final long serialVersionUID = 5566238862313119778L;
	
    public MedCheckerException(String message) {
        super(message);
        log.error(message);
    }
    
    public MedCheckerException(String message, Throwable t) {
        super(message, t);
        log.error(t);
    }
}
