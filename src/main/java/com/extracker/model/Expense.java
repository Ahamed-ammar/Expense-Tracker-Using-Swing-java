package com.extracker.model;

public class Expense {
	private int id;
	private double amount;
	private java.sql.Date expenseDate;
	private int categoryId;
	private String description;

	public Expense() {
		this.id = 0;
		this.amount = 0.0;
		this.expenseDate = new java.sql.Date(System.currentTimeMillis());
		this.categoryId = 0;
		this.description = "";
	}

	public Expense(double amount, java.sql.Date expenseDate, int categoryId, String description) {
		this.amount = amount;
		this.expenseDate = expenseDate;
		this.categoryId = categoryId;
		this.description = description;
	}

	public Expense(int id, double amount, java.sql.Date expenseDate, int categoryId, String description) {
		this.id = id;
		this.amount = amount;
		this.expenseDate = expenseDate;
		this.categoryId = categoryId;
		this.description = description;
	}

	public int getId() {
		return id;
	}

	public double getAmount() {
		return amount;
	}

	public java.sql.Date getExpenseDate() {
		return expenseDate;
	}

	public int getCategoryId() {
		return categoryId;
	}

	public String getDescription() {
		return description;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public void setExpenseDate(java.sql.Date expenseDate) {
		this.expenseDate = expenseDate;
	}

	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
