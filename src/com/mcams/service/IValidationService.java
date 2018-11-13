package com.mcams.service;

import java.time.LocalDate;

public interface IValidationService {

	boolean validateUserId(String userId);
	int validateChoice(String input);
	boolean validateName(String name);
	boolean validateCaeIpi(String caeIpi);
	LocalDate validateDate(String nextLine);
	
}
