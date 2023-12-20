package com.stock.whishlist.exception;

import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.stock.whishlist.dto.ErrorResponseDTO;

import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ResponseStatus(code = HttpStatus.CONFLICT)
	@ExceptionHandler({ InvalidCredentialsException.class })
	public ErrorResponseDTO InvalidCredentialsException(Exception exception, HttpServletRequest request) {
		return new ErrorResponseDTO(new Date(), HttpStatus.CONFLICT.value(), HttpStatus.CONFLICT.getReasonPhrase(),
				exception.getMessage(), request.getRequestURI());
	}
	
	@ResponseStatus(code = HttpStatus.CONFLICT)
	@ExceptionHandler({ ResourceAlreadyExistsException.class })
	public ErrorResponseDTO ResouceAlreadyExistException(ResourceAlreadyExistsException exception, HttpServletRequest request) {
		return new ErrorResponseDTO(new Date(), HttpStatus.CONFLICT.value(), HttpStatus.CONFLICT.getReasonPhrase(),
				exception.getMessage(), request.getRequestURI());
	}
	
	@ResponseStatus(code = HttpStatus.CONFLICT)
	@ExceptionHandler({ ResourceNotFoundException.class })
	public ErrorResponseDTO ResourceNotFoundException(ResourceNotFoundException exception, HttpServletRequest request) {
		return new ErrorResponseDTO(new Date(), HttpStatus.CONFLICT.value(), HttpStatus.CONFLICT.getReasonPhrase(),
				exception.getMessage(), request.getRequestURI());
	}

}