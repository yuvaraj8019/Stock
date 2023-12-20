package com.stock.userprofile.exception;

import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.stock.userprofile.dto.ErrorResponseDTO;

import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {
	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<ErrorResponseDTO> handleInvalidUserException(ResourceNotFoundException ex,
			HttpServletRequest request) {
		ErrorResponseDTO messageResponse = new ErrorResponseDTO();
		messageResponse.setMessage(ex.getMessage());
		messageResponse.setStatus(HttpStatus.NOT_FOUND);
		messageResponse.setTimeStamp(new Date());
		messageResponse.setPath(request.getRequestURI());
		return new ResponseEntity<>(messageResponse, HttpStatus.NOT_FOUND);

	}

	@ExceptionHandler(ResourceAlreadyExistsException.class)
	public ResponseEntity<Object> handleResourceAlreadyExistsException(ResourceAlreadyExistsException ex,
			HttpServletRequest request) {
		ErrorResponseDTO messageResponse = new ErrorResponseDTO();
		messageResponse.setMessage(ex.getMessage());
		messageResponse.setStatus(HttpStatus.CONFLICT);
		messageResponse.setTimeStamp(new Date());
		messageResponse.setPath(request.getRequestURI());
		return new ResponseEntity<>(messageResponse, HttpStatus.CONFLICT);
	}

	@ExceptionHandler(NullPointerException.class)
	public ResponseEntity<Object> handleNullPointerException(NullPointerException ex, HttpServletRequest request) {
		ErrorResponseDTO messageResponse = new ErrorResponseDTO();
		messageResponse.setMessage(ex.getMessage());
		messageResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
		messageResponse.setTimeStamp(new Date());
		messageResponse.setPath(request.getRequestURI());
		return new ResponseEntity<>(messageResponse, HttpStatus.INTERNAL_SERVER_ERROR);
	}

}
