package com.stock.userprofile.dto;

import java.util.Date;

import org.springframework.http.HttpStatus;

public class ErrorResponseDTO {

	private Date timeStamp;
	private String message;
	private HttpStatus status;
	private String path;

	public Date getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(Date timeStamp) {
		this.timeStamp = timeStamp;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public HttpStatus getStatus() {
		return status;
	}

	public void setStatus(HttpStatus status) {
		this.status = status;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public ErrorResponseDTO(Date timeStamp, String message, HttpStatus status, String path) {
		super();
		this.timeStamp = timeStamp;
		this.message = message;
		this.status = status;
		this.path = path;
	}

	public ErrorResponseDTO() {
		super();
	}

}