package com.gdn.onestop.web.controller;

import com.gdn.onestop.response.Response;
import com.gdn.onestop.service.exception.InvalidRequestException;
import com.gdn.onestop.service.exception.NotFoundException;
import com.gdn.onestop.web.config.jwt.InvalidJwtAuthenticationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;


@ControllerAdvice
public class DefaultControllerAdvice {
    private HttpHeaders headers = new HttpHeaders();

    private Response buildExceptionResponse(int code, String message){
        return Response.builder().code(code).status(message).build();
    }


    @ExceptionHandler(value={Exception.class, RuntimeException.class})
    @RequestMapping(produces = "application/vnd.error+json")
    public ResponseEntity<Response> DefaultExceptionHandler(Exception exception){
        exception.printStackTrace();

        return new ResponseEntity<>(
                buildExceptionResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), exception.getMessage()),
                headers, HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    @ExceptionHandler(NotFoundException.class)
    @RequestMapping(produces = "application/vnd.error+json")
    public ResponseEntity<Response> DataNotFoundExceptionHandler(NotFoundException exception){
        exception.printStackTrace();

        return new ResponseEntity<>(
                buildExceptionResponse(HttpStatus.NOT_FOUND.value(), exception.getMessage()),
                headers, HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler(value = {InvalidRequestException.class})
    @RequestMapping(produces = "application/vnd.error+json")
    public ResponseEntity<Response> duplicateEntryExceptionHandler(InvalidRequestException exception){
        exception.printStackTrace();

        return new ResponseEntity<>(
                buildExceptionResponse(HttpStatus.BAD_REQUEST.value(), exception.getMessage()),
                headers, HttpStatus.BAD_REQUEST
        );

    }

    @ExceptionHandler(value = {InvalidJwtAuthenticationException.class})
    @RequestMapping(produces = "application/vnd.error+json")
    public ResponseEntity<Response> invalidJwtToken(Exception exception) {
        exception.printStackTrace();

        return new ResponseEntity<>(
                buildExceptionResponse(HttpStatus.UNAUTHORIZED.value(), exception.getMessage()),
                headers, HttpStatus.UNAUTHORIZED
        );

    }
}
