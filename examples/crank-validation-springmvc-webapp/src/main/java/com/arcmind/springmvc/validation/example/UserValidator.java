package com.arcmind.springmvc.validation.example;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;



public class UserValidator implements Validator{
	public boolean supports(Class clazz) {
		return User.class.isAssignableFrom(clazz);
	}

	public void validate(Object formPOJO, Errors errors) {
		User user = (User) formPOJO;

		ValidationUtils.rejectIfEmpty(errors, "firstName", "required", "required");
		//ValidationUtils.rejectIfEmpty(errors, "lastName", "required", "required");
		
		user.getAge();
		
	}
	
}
