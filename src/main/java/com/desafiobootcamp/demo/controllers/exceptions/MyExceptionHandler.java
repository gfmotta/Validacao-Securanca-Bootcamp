package com.desafiobootcamp.demo.controllers.exceptions;

import java.time.Instant;

import javax.servlet.http.HttpServletRequest;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.desafiobootcamp.demo.services.exceptions.DatabaseException;
import com.desafiobootcamp.demo.services.exceptions.ResourceNotFoundException;

@ControllerAdvice
public class MyExceptionHandler {

	@ExceptionHandler(DatabaseException.class)
	public ResponseEntity<StandardError> database(DatabaseException exception, HttpServletRequest request) {
		HttpStatus status = HttpStatus.BAD_REQUEST;
		
		StandardError error = new StandardError();
		error.setTimespan(Instant.now());
		error.setStatus(status.value());
		error.setError("Database exception");
		error.setMessage(exception.getMessage());
		error.setPath(request.getRequestURI());
		
		return ResponseEntity.status(status).body(error);
	}
	
	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<StandardError> resourceNotFound(ResourceNotFoundException exception, HttpServletRequest request) {
		HttpStatus status = HttpStatus.NOT_FOUND;
		
		StandardError error = new StandardError();
		error.setTimespan(Instant.now());
		error.setStatus(status.value());
		error.setError("Resource not found");
		error.setMessage(exception.getMessage());
		error.setPath(request.getRequestURI());
		
		return ResponseEntity.status(status).body(error);
	}
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ValidationError> argumentNotValid(MethodArgumentNotValidException exception, HttpServletRequest request) {
		HttpStatus status = HttpStatus.UNPROCESSABLE_ENTITY;
		
		ValidationError error = new ValidationError();
		error.setTimespan(Instant.now());
		error.setStatus(status.value());
		error.setError("Validation exception");
		error.setMessage(exception.getMessage());
		error.setPath(request.getRequestURI());
		
		for (FieldError x : exception.getBindingResult().getFieldErrors()) {
			error.addError(x.getField(), x.getDefaultMessage());
		}
		
		return ResponseEntity.status(status).body(error);
	}
	
	@ExceptionHandler(DataIntegrityViolationException.class)
	public ResponseEntity<StandardError> resourceNotFound(DataIntegrityViolationException exception, HttpServletRequest request) {	
		HttpStatus status = HttpStatus.BAD_REQUEST;
		
		StandardError error = new StandardError();
		error.setTimespan(Instant.now());
		error.setStatus(status.value());
		error.setError("Database exception");
		error.setMessage("Voce n??o pode deletar esse recurso no momento");
		error.setPath(request.getRequestURI());
		
		if (request.getRequestURI().contains("/events")) {
			status = HttpStatus.NOT_FOUND;
			error.setError("Resource not found");
			error.setMessage("Cidade n??o encontrada");
		}

		return ResponseEntity.status(status).body(error);
	}
}
