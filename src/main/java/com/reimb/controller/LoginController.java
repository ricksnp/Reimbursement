package com.reimb.controller;

import java.util.List;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import com.reimb.model.User;
import com.reimb.repo.ReimbursementDao;
import com.reimb.repo.UserDao;

public class LoginController {

	public HttpServletRequest request;
	public HttpServletResponse response;
	Logger logger = Logger.getLogger("LoginController");

	public LoginController(HttpServletRequest request, HttpServletResponse response) {
		this.request = request;
		this.response = response;
	}

	public boolean LoginRequest() throws IOException {
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		return loginUserNameAndPassword(username, password);
	}
	
	public boolean loginUserNameAndPassword(String username, String password) throws IOException {

		String md5Pass = md5(username + password + "pepper");
		User u = new UserDao().findByUsernameAndPassword(username, md5Pass);

		if (u == null) {
			say("<h1> No such user: " + username + " or invalid password</h1>");
			return false;
		}
		
		// Employee View
		else if (u.getRoleName().equals("Employee")) {
			logger.info(u.getUsername() + " logged in.");
			say("<!DOCTYPE html>" + "<html>"
					+ "<head><link rel=\"stylesheet\" href=\"Reimbursement/css/employee.css\"> </head>" + "<body>"
					+ "<h1> Welcome back, " + u.getUsername() + "</h1>"
					+ "<table class = employeeTable id=employeeTable >");
			new ReimbursementController(request, response).retrieveReimbursementsByUserId(u.getUserId());
			say("</table>" + "<div id = \"newreimb\"><form id = newTicket action = \"NewReimbursement\" method = \"post\">"
					+ "<input type=\"hidden\" name=\"username\" id=\"username\" value=" + username + ">"
					+ "<input type=\"hidden\" name=\"author\" id=\"author\"   value=" + u.getUserId() + ">"
					+ "<input type=\"hidden\" name=\"password\" id=\"password\" value=" + password + ">"
					+ "<table>"
					+ "<tr><td>Amount        <td> <input type=\"text\"   placeholder=\"amount\" id=\"amount\" name=\"amount\">"
					+ "<tr><td>Description   <td> <input type=\"text\"   placeholder=\"reason\" id=\"reason\" name=\"desc\">"
					+ "<tr><td>Choose a type <td> <select id=\"type\" name=\"type\">"
					+ "<option value=\"1\">Travel</option>" + "<option value=\"2\">Business</option>"
					+ "<option value=\"3\">Healthcare</option>" + "<option value=\"4\">Food</option>"
					+ "<option value=\"5\">Other</option>" + "</select>"
					+ "<tr><td><input type=\"submit\" id = submitNewTicket value=\"Submit new ticket\">" + "</table></form></div>"

					+ "<script src = \"Reimbursement/javascript/reimb.js\"></script>" + "</body></html>");
			return true;
			
		} else if (u.getRoleName().equals("Manager")) {
			logger.info(u.getUsername() + " logged in.");
			displayManager(u);
			return true;
		}

		else {
			say("<h1> NO such role" + u.getRoleName() + "</h1>");
			return false;
		}
	}
	
	/**
	 * What a manager sees when he logs in.  Notice, instead of writing the HTML directly using the response,
	 * it is more ideal to populate for e.g. the manager table.  Our response is all of the HTML, realistically, 
	 * one will receive this information in JSON format, and then parse through the JSON and create the table that
	 * way. 
	 * @param u
	 * @throws IOException
	 */
	public void displayManager(User u) throws IOException {
		say("<!DOCTYPE html>" + "<html>"
				+ "<head> <link rel=\"stylesheet\" href=\"Reimbursement/css/manager.css\"> </head>"
				+ "<body> <h1> All Reimbursements </h1> ");
		Float f = new ReimbursementDao().viewTotalReimbursements();
		if (f != null) say("<p>Total outstanding reimbursements: "+f+"</p>");

		say("<form action = \"UpdateStatus\" method = \"post\"><table id = allReimbursements>");
		List<Integer> remIds = new ReimbursementController(request, response).retrieveAllReimbursements();
		String result = "";
		for (Integer rem : remIds) {
			result += rem + " ";
		}
		say("</table>"
		 +"<div id = \"filter\"><input type=\"checkbox\" id=\"showPending\" name=\"showPending\">" 
		 +"<label for=\"showPending\"> Show Pending</label><br>" 
		 +"<input type=\"checkbox\" id=\"showRejected\" name=\"showRejected\">" 
		 +"<label for=\"showRejected\"> Show Rejected</label><br>" 
		 +"<input type=\"checkbox\" id=\"showAccepted\" name=\"showAccepted\">" 
		 +"<label for=\"showAccepted\"> Show Accepted</label><br>" 
		 +"<input type=\"button\" id = filterButton value=\"Filter\">" 
		 +"<input type=\"submit\" id = \"updateButton\" value=\"Update\">" 
		 +"<input type=\"hidden\" value=\"" + result + "\" name = \"remIdList\" id = remIdList>" 
		 +"<input type=\"hidden\" id=reimbResolverId value=\"" + u.getUserId() + "\" name = \"resolverId\"></div></form>"
		 
		 +"<p><button id = buttonMakeEmployee>Make new employee</button></p>"
		 
		 + "<div id = divMakeEmployee class = hidden><form id = makeEmployee>"
		 + "<table>"
		 + "<tr><td>New Employee Username"
		 +" <td><input type = text id = newEmpUserName placeholder = \"Employee Username\"  name = newEmpUserName>"
		 + "<tr><td>New Employee Password"
		 +"<td><input type = text id = newEmpPassword placeholder = \"Employee Password\"  name = newEmpPassword>"
		 + "<tr><td>New Employee First Name"
		 +"<td><input type = text id = newEmpFirstName placeholder = \"Employee First Name\"  name = newEmpFirstName>"
		 + "<tr><td>New Employee Last Name"
		 +"<td><input type = text id = newEmpLastName placeholder = \"Employee Last Name\"  name = newEmpEmail>"
		 + "<tr><td>New Employee Email"
		 +"<td><input type = text id = newEmpEmail placeholder = \"Employee Email\"  name = newEmpEmail>"
		 +"<tr><td><input type=\"submit\" id = \"submitNewEmp\" value=\"Create Employee\">" 
		 + "</table>"
		 + "</form></div>"
		 +"</body></html>"); 
	}
	
	/**
	 * Instead of calling reponse.getWriter().print(theString/Html), make a say method and pass it the String, which is HTML.
	 * @param s The String to be written to the browser
	 * @throws IOException
	 */
	void say(String s) throws IOException {
		if (response != null) response.getWriter().print(s);
	}
	
	/**
	 * Encrypts any string using the MD5 encryption method
	 * @param input the string to be encrypted
	 * @return the encrypted string
	 */
	public static String md5(String input) {
		try {
			// Static getInstance method is called with hashing MD5
			MessageDigest md = MessageDigest.getInstance("MD5");

			// digest() method is called to calculate message digest
			// of an input digest() return array of byte
			byte[] messageDigest = md.digest(input.getBytes());
			// Convert byte array into signum representation
			BigInteger no = new BigInteger(1, messageDigest);
			// Convert message digest into hex value
			String hashtext = no.toString(16);
			while (hashtext.length() < 32) {
				hashtext = "0" + hashtext;
			}
			return hashtext;
		}

		catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
	}

}
