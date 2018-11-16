package com.mcams.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class ValidationService implements IValidationService {
	
	@Override
	public boolean validateUserId(String userId) {
		if(userId.matches("[0-9]{6}"))	return true;
		else return false;
	}
	
	@Override
	public int validateChoice(String input) {
		if(input.matches("[0-9]{1}"))
			return Integer.parseInt(input);
		else return 0;
	}
	
	@Override
	public boolean validateName(String name) {
		if(name.matches("[a-zA-Z\\s\\.]{3,50}")) return true;
		else return false;
	}
	
	@Override
	public LocalDate validateDate(String date) {
		if(date.matches("^\\d{2}/\\d{2}/\\d{4}")){
			LocalDate localDate = LocalDate.parse(date, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
			
			if(localDate.isAfter(LocalDate.now())) return null;
			
			return localDate;
		}
		else
			return null;
	}
	
	@Override
	public boolean validateCaeIpi(String caeIpi) {
		if(caeIpi.matches("[a-zA-Z0-9\\s]{3,10}")) return true;
		else return false;
	}

	public boolean validateMSocietyId(char[] mSocietyId) {
		if(mSocietyId.length==3) {
			// TODO check in database if found return true otherwise false
			return true;
		}
		else return false;
	}

	public LocalTime validateDuration(String nextLine) {
		LocalDate ld;
		if(nextLine.matches("[0-5]{1}[0-9]{1}:[0-5]{1}[0-9]{1}")) {
			System.out.println("correct");
		}
		else {
			System.out.println("Incorrect");
		}
		return null;
	}
	
}
