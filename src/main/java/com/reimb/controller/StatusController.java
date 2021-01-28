package com.reimb.controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.reimb.config.EnvironmentConnectionUtil;

public class StatusController {

	public HttpServletRequest request;
	public HttpServletResponse response;

	public StatusController(HttpServletRequest request, HttpServletResponse response) {
		this.request = request;
		this.response = response;
	}

	// Needs to go in ReimbursementDao
	public void StatusRequest() throws IOException {
		String listOfRemIds = request.getParameter("remIdList");
		int userId = Integer.parseInt(request.getParameter("resolverId"));
		String remIds[] = listOfRemIds.split(" ");

		try {
			EnvironmentConnectionUtil ev = new EnvironmentConnectionUtil();
			Connection conn = ev.getConnection();

			String sql = "update reimbursement.ers_reimbursement set reimb_status_id = ?, reimb_resolver = ?, reimb_resolved = CURRENT_TIMESTAMP where reimb_id = ? and reimb_status_id != ? ";
			PreparedStatement pstmt = conn.prepareStatement(sql);

			for (String s : remIds) {
				String status = request.getParameter("type" + s);

				pstmt.setInt(1, Integer.parseInt(status));
				pstmt.setInt(2, userId);
				pstmt.setInt(3, Integer.parseInt(s));
				pstmt.setInt(4, Integer.parseInt(status));
				pstmt.executeUpdate();
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}


	/**
	 * Instead of calling reponse.getWriter().print(theString/Html), make a say
	 * method and pass it the String, which is HTML.
	 * 
	 * @param s The String to be written to the browser
	 * @throws IOException
	 */
	void say(String s) throws IOException {
		response.getWriter().print(s);
	}

}
