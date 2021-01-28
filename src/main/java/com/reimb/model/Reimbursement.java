package com.reimb.model;

public class Reimbursement {
	
	// Blob, date resolved, and date requested, are not passed.  Only used in the Database
	private double amount;
	private String description;
	private int author;
	private int resolver;
	private int status;
	private int type;
	
	public Reimbursement() {
		super();
	}
	
	public Reimbursement(double amount, String description, int author, int resolver, int status, int type) {
		super();
		this.amount = amount;
		this.description = description;
		this.author = author;
		this.resolver = resolver;
		this.status = status;
		this.type = type;
	}
	
	


	public double getAmount() {
		return amount;
	}


	public void setAmount(double amount) {
		this.amount = amount;
	}


	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}


	public int getAuthor() {
		return author;
	}


	public void setAuthor(int author) {
		this.author = author;
	}


	public int getResolver() {
		return resolver;
	}


	public void setResolver(int resolver) {
		this.resolver = resolver;
	}


	public int getStatus() {
		return status;
	}


	public void setStatus(int status) {
		this.status = status;
	}


	public int getType() {
		return type;
	}


	public void setType(int type) {
		this.type = type;
	}
	
	
	
	
		
	}

