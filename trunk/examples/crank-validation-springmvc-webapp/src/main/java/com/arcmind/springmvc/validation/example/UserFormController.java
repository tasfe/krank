package com.arcmind.springmvc.validation.example;


import org.springframework.web.servlet.mvc.SimpleFormController;


public class UserFormController extends SimpleFormController {

	
	protected void doSubmitAction(Object pojoForm) throws Exception {
		
		User  user = (User) pojoForm;
		//System.out.println("GOT USER " + user.getFirstName());
		
	}
		
}
