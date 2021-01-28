package com.reimb.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.reimb.controller.CheckLoginController;
import com.reimb.controller.LoginController;
import com.reimb.controller.NewEmployeeController;
import com.reimb.controller.ReimbursementController;
//import com.reimb.model.Reimbursement;
//import com.reimb.repo.ReimbursementDao;
import com.reimb.controller.StatusController;

public class RequestForwarder {

	public void routes(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		switch (request.getRequestURI()) {

		case "/Reimbursement/NewReimbursement": {
			new ReimbursementController(request, response).reimbursementRequest();
		}
			break;

		case "/Reimbursement/login": {
			new LoginController(request, response).LoginRequest();
		}
			break;

		case "/Reimbursement/UpdateStatus": {
			new StatusController(request, response).StatusRequest();
		}
		break;
		case "/Reimbursement/CheckLogin": {
			new CheckLoginController(request, response).CheckLogin();
		}
		break;
		case "/Reimbursement/NewEmployee": {
		     new NewEmployeeController(request, response).NewEmployee();
		}
			break;

		default: {
			System.out.println("HERE IS THE URI" + request.getRequestURI());
		}
		}

	}
}