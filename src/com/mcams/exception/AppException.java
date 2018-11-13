package com.mcams.exception;

@SuppressWarnings("serial")
public class AppException extends Exception {
	String message;
	
	public AppException(String msg) {
		message = msg;
	}
	
	@Override
	public String getMessage(){
		return this.message;
	}

}