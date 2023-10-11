package com.db.dataplatform.techtest.server.api;

import com.db.dataplatform.techtest.server.exception.ChecksumNotMatchingException;
import com.db.dataplatform.techtest.server.exception.DataNotFoundException;
import com.db.dataplatform.techtest.server.exception.ErrorResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class RestResponseEntityExceptionHandler
        extends ResponseEntityExceptionHandler {

    private String NOT_FOUND = "NOT_FOUND";
    private String BAD_REQUEST = "BAD_REQUEST";

    @ExceptionHandler(value = {ConstraintViolationException.class})
    protected ResponseEntity<Object> handleConflict(Exception ex, WebRequest request) {

        String bodyOfResponse = "Provided input parameters have not met validation requirements";

        return handleExceptionInternal(ex, bodyOfResponse +" : "+ ex.getMessage(),
                new HttpHeaders(), HttpStatus.CONFLICT, request);
    }

    @ExceptionHandler(value = {DataNotFoundException.class})
    public final ResponseEntity<Object> processDataNotFoundException(DataNotFoundException dataNotFoundException)
    {
        List<String> details = new ArrayList<>();
        details.add(dataNotFoundException.getLocalizedMessage());

        ErrorResponse error = new ErrorResponse(NOT_FOUND, details);
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = {ChecksumNotMatchingException.class})
    public final ResponseEntity<Object> processChecksumNotMatchingException(ChecksumNotMatchingException checksumNotMatchingException)
    {
        List<String> details = new ArrayList<>();
        details.add(checksumNotMatchingException.getLocalizedMessage());

        ErrorResponse error = new ErrorResponse(BAD_REQUEST, details);
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
}
