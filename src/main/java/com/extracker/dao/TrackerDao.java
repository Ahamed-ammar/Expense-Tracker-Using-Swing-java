package com.extracker.dao;

import java.sql.*;
import java.util.*;
import com.extracker.model.Expense;
import com.extracker.util.dbConnection;
public class TrackerDao {
	private static final String SELECT_ALL_EXPENSES = "select * from expenses";
	private static final String INSERT_EXPENSE = "insert into expenses (amount, date, category_id, notes) values (?, ?, ?, ?)";
	private static final String UPDATE_EXPENSE = "update expenses set amount = ?, date = ?, category_id = ?, notes = ? where expense_id = ?";
	private static final String DELETE_EXPENSE = "delete from expenses where expense_id = ?";
	private static final String SELECT_BY_ID = "select * from expenses where expense_id = ?";

	private Expense mapExpense(ResultSet rs) throws SQLException {
		int id = rs.getInt("expense_id");
		double amount = rs.getDouble("amount");
		java.sql.Date expenseDate = rs.getDate("date");
		int categoryId = rs.getInt("category_id");
		String description = rs.getString("notes");
		return new Expense(id, amount, expenseDate, categoryId, description);
	}

	public List<Expense> getAllExpenses() throws SQLException {
		List<Expense> expenses = new ArrayList<>();
		try (
			Connection cn = dbConnection.getDBConnection();
			PreparedStatement stmt = cn.prepareStatement(SELECT_ALL_EXPENSES, Statement.RETURN_GENERATED_KEYS);
			ResultSet rs = stmt.executeQuery();
		) {
			while (rs.next()) {
				expenses.add(mapExpense(rs));
			}
		}
		return expenses;
	}

	public boolean addExpense(Expense expense) throws SQLException {
		try (
			Connection cn = dbConnection.getDBConnection();
			PreparedStatement stmt = cn.prepareStatement(INSERT_EXPENSE, Statement.RETURN_GENERATED_KEYS);
		) {
			stmt.setDouble(1, expense.getAmount());
			stmt.setDate(2, expense.getExpenseDate());
			stmt.setInt(3, expense.getCategoryId());
			stmt.setString(4, expense.getDescription());
			int rowsAffected = stmt.executeUpdate();
			if (rowsAffected > 0) {
				ResultSet generatedKeys = stmt.getGeneratedKeys();
				if (generatedKeys.next()) {
					expense.setId(generatedKeys.getInt(1));
				}
				return true;
			}
			return false;
		}
	}

	public boolean updateExpense(Expense expense) throws SQLException {
		try (
			Connection cn = dbConnection.getDBConnection();
			PreparedStatement stmt = cn.prepareStatement(UPDATE_EXPENSE);
		) {
			stmt.setDouble(1, expense.getAmount());
			stmt.setDate(2, expense.getExpenseDate());
			stmt.setInt(3, expense.getCategoryId());
			stmt.setString(4, expense.getDescription());
			stmt.setInt(5, expense.getId());
			int rowsAffected = stmt.executeUpdate();
			return rowsAffected > 0;
		}
	}

	public boolean deleteExpense(int expenseId) throws SQLException {
		try (
			Connection cn = dbConnection.getDBConnection();
			PreparedStatement stmt = cn.prepareStatement(DELETE_EXPENSE);
		) {
			stmt.setInt(1, expenseId);
			int rowsAffected = stmt.executeUpdate();
			return rowsAffected > 0;
		}
	}

	public Expense getExpenseById(int expenseId) throws SQLException {
		try (
			Connection cn = dbConnection.getDBConnection();
			PreparedStatement stmt = cn.prepareStatement(SELECT_BY_ID);
		) {
			stmt.setInt(1, expenseId);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				return mapExpense(rs);
			}
			return null;
		}
	}
}
