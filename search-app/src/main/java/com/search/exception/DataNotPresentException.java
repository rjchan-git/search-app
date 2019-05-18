package com.search.exception;

public class DataNotPresentException extends Exception {

	private static final long serialVersionUID = 1L;
	
	public DataNotPresentException(String message){
		super(message);
	}

}
