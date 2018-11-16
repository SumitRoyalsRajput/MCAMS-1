package com.mcams.exception;

@SuppressWarnings("serial")
public class AppException extends Exception {
	String message;
	
	public AppException(String msg) {
		message = msg;
		System.out.println("Wow! Got an exception!");
	}
	
	@Override
	public String getMessage(){
		return this.message;
	}

}