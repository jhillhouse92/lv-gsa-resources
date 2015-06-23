package com.longview.gsa.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.longview.gsa.domain.ErrorResource;

@ControllerAdvice
public class MedCheckerExceptionHandler extends ResponseEntityExceptionHandler{

    @ExceptionHandler({ MedCheckerException.class })
    protected ResponseEntity<Object> handleInvalidRequest(RuntimeException e, WebRequest request) {
    	MedCheckerException ire = (MedCheckerException) e;
        
        ErrorResource error = new ErrorResource(HttpStatus.BAD_REQUEST.name(), ire.getMessage());
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        return handleExceptionInternal(e, error, headers, HttpStatus.UNPROCESSABLE_ENTITY, request);
    }
}
