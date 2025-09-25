package com.extracker.dao;
import com.extracker.model.Categories;
import java.sql.*;
import com.extracker.util.*;

import java.sql.Connection;
import java.util.*;
public class CategoriesDao {
    private static final String SELECT_ALL_TODOS = "select * from categories";
    private static final String INSERT_CATEGORY = "insert into categories (category_name, description) values (?, ?)";
    private static final String UPDATE_CATEGORY = "update categories set category_name = ?, description = ? where category_id = ?";
    private static final String DELETE_CATEGORY = "delete from categories where category_id = ?";
    private static final String SELECT_BY_ID = "select * from categories where category_id = ?";

    public Categories getCatRow(ResultSet rs) throws SQLException{
        int id = rs.getInt("category_id");
        String title = rs.getString("category_name");
        String description = rs.getString("description");

        return new Categories(id, title, description);
    }

    public List<Categories> getAllCategories() throws SQLException{
        List<Categories> categories = new ArrayList<>();
        try(
            Connection cn = dbConnection.getDBConnection();
            PreparedStatement stmt = cn.prepareStatement(SELECT_ALL_TODOS, Statement.RETURN_GENERATED_KEYS);
            ResultSet rs = stmt.executeQuery();
        ) {
            
            System.out.println("Query Executed Successfully..");

            while(rs.next()) {
                Categories cat = getCatRow(rs);
                categories.add(cat);
            }
        }
        catch(SQLException e) {
            e.printStackTrace();
        }
        return categories;
    }
    
    public boolean addCategory(Categories category) throws SQLException {
        try(
            Connection cn = dbConnection.getDBConnection();
            PreparedStatement stmt = cn.prepareStatement(INSERT_CATEGORY, Statement.RETURN_GENERATED_KEYS);
        ) {
            stmt.setString(1, category.getTitle());
            stmt.setString(2, category.getDescription());
            
            int rowsAffected = stmt.executeUpdate();
            
            if (rowsAffected > 0) {
                ResultSet generatedKeys = stmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    category.setId(generatedKeys.getInt(1));
                }
                return true;
            }
            return false;
        }
    }
    
    public boolean updateCategory(Categories category) throws SQLException {
        try(
            Connection cn = dbConnection.getDBConnection();
            PreparedStatement stmt = cn.prepareStatement(UPDATE_CATEGORY);
        ) {
            stmt.setString(1, category.getTitle());
            stmt.setString(2, category.getDescription());
            stmt.setInt(3, category.getId());
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        }
    }
    
    public boolean deleteCategory(int categoryId) throws SQLException {
        try(
            Connection cn = dbConnection.getDBConnection();
            PreparedStatement stmt = cn.prepareStatement(DELETE_CATEGORY);
        ) {
            stmt.setInt(1, categoryId);
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        }
    }
    
    public Categories getCategoryById(int categoryId) throws SQLException {
        try(
            Connection cn = dbConnection.getDBConnection();
            PreparedStatement stmt = cn.prepareStatement(SELECT_BY_ID);
        ) {
            stmt.setInt(1, categoryId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return getCatRow(rs);
            }
            return null;
        }
    }
}
