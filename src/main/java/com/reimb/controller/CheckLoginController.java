package com.reimb.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.reimb.model.User;
import com.reimb.repo.UserDao;

public class CheckLoginController {
	public HttpServletRequest request;
	public HttpServletResponse response;

	public CheckLoginController(HttpServletRequest request, HttpServletResponse response) {
		this.request = request;
		this.response = response;
	}

	public void CheckLogin() throws IOException  {
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		String md5Pass = LoginController.md5(password + "pepper");

		User u = new UserDao().findByUsernameAndPassword(username, md5Pass);

		if (u == null) {
			say("<h1 class = bad> No such user: " + username + " or invalid password</h1>");
		}
		else {
			say("<h1> Welcome: " + username + " </h1>");
		}
	}
	
	/**
	 * Instead of calling reponse.getWriter().print(theString/Html), make a say method and pass it the String, which is HTML.
	 * @param s The String to be written to the browser
	 * @throws IOException
	 */
	void say(String s) throws IOException {
		response.getWriter().print(s);
	}
		
}
