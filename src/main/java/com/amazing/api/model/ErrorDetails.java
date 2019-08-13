package com.amazing.api.model;

import java.util.Date;

/**
 * DTO object for representing the details necessar when an exception is thrown from the system.
 * 
 * @author kkailasa
 *
 */
public class ErrorDetails {
	
	private Date timestamp;
	
	private String message;
	
	private String details;

	public ErrorDetails(Date timestamp, String message, String details) {
		super();
		this.timestamp = timestamp;
		this.message = message;
		this.details = details;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public String getMessage() {
		return message;
	}

	public String getDetails() {
		return details;
	}
}
