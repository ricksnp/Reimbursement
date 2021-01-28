package com.reimb.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.reimb.model.User;
import com.reimb.repo.UserDao;

public class NewEmployeeController {
	
	public HttpServletRequest request;
	public HttpServletResponse response;
	Logger logger = Logger.getLogger("NewEmployeeController");
	public static String result;
	
	public NewEmployeeController(HttpServletRequest request, HttpServletResponse response) {
		this.request = request;
		this.response = response;
	}
	
	/**
	 * Grab the request parameters that were passed from the make employee form, make the object, and add it to the DB
	 * @throws IOException
	 */
	public void NewEmployee() throws IOException  {
		String newEmpUserName  = request.getParameter("newEmpUserName"),
		newEmpPassword         = request.getParameter("newEmpPassword"),
		newEmpFirstName        = request.getParameter("newEmpFirstName"), //These are all strings, so just write String once
		newEmpLastName         = request.getParameter("newEmpLastName"),
		newEmpEmail            = request.getParameter("newEmpEmail");
		
		addEmployee(new User( 0, newEmpUserName, newEmpPassword, newEmpFirstName, newEmpLastName, newEmpEmail, 2, "")); //2 is Employee, in Roles
	}
	
	public boolean addEmployee(User u) throws IOException {
		final boolean addedEmployee = new UserDao().create(u);
		if (addedEmployee) {
			say("Successfully added employee " + u.getUsername());
		}
		else {
			say("Was not able to add employee " + u.getUsername());
		}
		return addedEmployee;
	}
	
	/**
	 * Instead of calling reponse.getWriter().print(theString/Html), make a say method and pass it the String, which is HTML.
	 * @param s The String to be written to the browser
	 * @throws IOException
	 */
	void say(String s) throws IOException {
		if(response != null)response.getWriter().print(s); //response is null when we're testing
		logger.info(s);
		result = s;

	}

}
