package com.reimb.controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.reimb.config.EnvironmentConnectionUtil;
import com.reimb.model.Reimbursement;
import com.reimb.repo.ReimbursementDao;

public class ReimbursementController {
	public HttpServletRequest request;
	public HttpServletResponse response;
	Logger logger = Logger.getLogger("ReimbursementController");
	
	public ReimbursementController(HttpServletRequest request, HttpServletResponse response) {
		this.request  = request;
		this.response = response;
	}

	public void reimbursementRequest() throws IOException {

		double amount = Double.parseDouble(request.getParameter("amount"));
		String description = request.getParameter("desc");
		int author = Integer.parseInt(request.getParameter("author"));
		int type = Integer.parseInt(request.getParameter("type"));

		Reimbursement r = new Reimbursement(amount, description, author, 1, 1, type);
		ReimbursementDao rd = new ReimbursementDao();
		// Insert reimbursement into DB
		rd.create(r);
		logger.info("A new reimbursement was made by the user with user ID " + author);
		say("<table>");
		retrieveReimbursementsByUserId(author);
		say("</table>");
	}
	
	// Needs to go in ReimbursementDao
	public int retrieveReimbursementsByUserId(int author) throws IOException {
		int count = 0;
		try {
			EnvironmentConnectionUtil ev = new EnvironmentConnectionUtil();
			Connection conn = ev.getConnection();
			String query = "select * from reimbursement.view1 where reimb_author = ? ";
			PreparedStatement pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, author);
			ResultSet rs = pstmt.executeQuery();
			
			say("<tr><th>Amount<th>Description<th>Resolver First Name<th>Resolver Last name<th>Email<th>Status<th>Reason</tr>");
			
			while (rs.next()) {
				int amount = rs.getInt("reimb_amount");
				String desc = rs.getString("reimb_description");
				String firstname = rs.getString("user_first_name");
				String lastname = rs.getString("user_last_name");
				String statusname = rs.getString("reimb_status");
				String email = rs.getString("user_email");
				String reason = rs.getString("reimb_type");
				count++;
				say(	"<tr><td class = amount>"+amount+"</td>"
						+ "<td class = desc>"+desc+"</td>"
						+ "<td class = firstname>"+firstname+"</td>"
						+ "<td class = lastname>"+lastname+"</td>"
						+ "<td class = email>"+email+"</td>"
						+ "<td class = statusname>"+statusname+"</td>"
						+ "<td class = reason>"+reason+"</td>");
			}
			rs.close();
			conn.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return count;
	}
	
	// Needs to go in ReimbursementDao
	public List<Integer> retrieveAllReimbursements() throws IOException {
		List<Integer> remIds = new ArrayList<>();
		try {
			EnvironmentConnectionUtil ev = new EnvironmentConnectionUtil();
			Connection conn = ev.getConnection();
			String query = "select * from reimbursement.view1";
			PreparedStatement pstmt = conn.prepareStatement(query);
			ResultSet rs = pstmt.executeQuery();
				
			say("<tr><th>First Name<th>Last Name<th>Amount<th>Description<th>Resolver First Name<th>Resolver Last name<th>Email<th>Reason<th>Approve/Reject</tr>");
			
			while (rs.next()) {
				int amount = rs.getInt("reimb_amount");
				String desc = rs.getString("reimb_description");
				String firstname = rs.getString("user_first_name");
				String lastname = rs.getString("user_last_name");
				String statusname = rs.getString("reimb_status");
				String email = rs.getString("user_email");
				String reason = rs.getString("reimb_type");
				String p = statusname.equals("Pending")? "selected":"";
				String r = statusname.equals("Rejected")? "selected":"";
				String a = statusname.equals("Approved")? "selected":"";
				String authorFirstName = rs.getString("authorfirstname");
				String authorLastName = rs.getString("authorlastname");
				
				int reimbId = rs.getInt("reimb_id");
				remIds.add(reimbId);
				
				say(	"<tr id =  \"filterRow"+reimbId+"\" >"
						+ "<td class>"+authorFirstName+"</td>"
						+ "<td class>"+authorLastName+"</td>"
						+ "<td class = amount>"+amount+"</td>"
						+ "<td class = desc>"+desc+"</td>"
						+ "<td class = firstname>"+firstname+"</td>"
						+ "<td class = lastname>"+lastname+"</td>"
						+ "<td class = email>"+email+"</td>"
						+ "<td class = reason>"+reason+"</td>"
						+ "<td class = status>"+
				        "<select id=\"type"+reimbId+"\" class = reimbType name=\"type" + reimbId + "\">"+
				          "<option value=\"1\" "+p+">Pending</option>"+
				          "<option value=\"2\" "+r+">Rejected</option>"+
				          "<option value=\"3\" "+a+">Approved</option>"+
				        "</select></td>");
			}
			rs.close();
			conn.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return remIds;
	}
	
	/**
	 * Instead of calling reponse.getWriter().print(theString/Html), make a say method and pass it the String, which is HTML.
	 * @param s The String to be written to the browser
	 * @throws IOException
	 */
	void say(String s) throws IOException {
		if (response != null) response.getWriter().print(s);
	}
	
}
