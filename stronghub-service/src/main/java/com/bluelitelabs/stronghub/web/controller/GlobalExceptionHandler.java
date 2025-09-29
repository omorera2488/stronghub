package com.bluelitelabs.stronghub.web.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import jakarta.validation.ConstraintViolationException;

@ControllerAdvice
public class GlobalExceptionHandler {
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<?> handleInvalid(MethodArgumentNotValidException ex) {
		Map<String, Object> body = new HashMap<>();
		body.put("message", "Validation failed");
		Map<String, String> errors = new HashMap<>();
		ex.getBindingResult().getAllErrors().forEach(err -> {
			String field = err instanceof FieldError ? ((FieldError) err).getField() : err.getObjectName();
			errors.put(field, err.getDefaultMessage());
		});
		body.put("errors", errors);
		return ResponseEntity.badRequest().body(body);
	}

	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<?> handleConstraint(ConstraintViolationException ex) {
		Map<String, Object> body = new HashMap<>();
		body.put("message", "Constraint violation");
		Map<String, String> errors = new HashMap<>();
		ex.getConstraintViolations().forEach(v -> errors.put(v.getPropertyPath().toString(), v.getMessage()));
		body.put("errors", errors);
		return ResponseEntity.badRequest().body(body);
	}

	@ExceptionHandler(DataIntegrityViolationException.class)
	public ResponseEntity<?> handleConstraint(DataIntegrityViolationException ex) {
		return ResponseEntity.status(409).body(Map.of("message", "Conflict: data integrity violation", "detail",
				ex.getMostSpecificCause() != null ? ex.getMostSpecificCause().getMessage() : ex.getMessage()));
	}
}
