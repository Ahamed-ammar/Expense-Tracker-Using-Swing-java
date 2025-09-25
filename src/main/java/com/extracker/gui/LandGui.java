package com.extracker.gui;
import javax.swing.*;
import java.util.*;
import java.util.List;

import javax.swing.table.DefaultTableModel;
import com.extracker.dao.CategoriesDao;
import com.extracker.model.*;
import java.awt.*;
import java.sql.SQLException;

public class LandGui extends JFrame{
    private JButton CategoryBtn, ExpenseBtn;
    public LandGui() {
        setTitle("Expence Tracker Application");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        //setupEventListeners();

        ExpenseBtn = new JButton("Expense");
        CategoryBtn = new JButton("Categories");
        CategoryBtn.setPreferredSize(new Dimension(150, 40));
        ExpenseBtn.setPreferredSize(new Dimension(150, 40));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 200));
        buttonPanel.add(CategoryBtn);
        buttonPanel.add(ExpenseBtn);
        CategoryBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        ExpenseBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        getContentPane().add(buttonPanel, BorderLayout.NORTH);
        setupEventListeners();
    }
    public void setupEventListeners() {
        CategoryBtn.addActionListener(
            (e) -> {
                try {
                    CategoriesGui categoryTable = new CategoriesGui();
                    System.out.print("Enter successful..");
                    categoryTable.setVisible(true);
                } catch(Exception err) {
                    System.err.println("Failed to launch the application: " + err.getMessage());
                }
            }
        );
    }

}

class CategoriesGui extends JFrame {
    private CategoriesDao categoryDao;
    private JTable categoryTable;
    private DefaultTableModel tableModel;

    private JTextField titleField;
    private JTextArea descriptionArea; 

    private JButton addButton;
    private JButton updateButton;
    private JButton deleteButton;
    private JButton refreshButton;

    public CategoriesGui() {
        categoryDao = new CategoriesDao();
        initializeComponents();
        setupLayout();
        setupEventListeners();
        loadCategories();
    }
    
    public void initializeComponents() {
        setTitle("Categories Management - Expense Tracker");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
       
        titleField = new JTextField(20);
        descriptionArea = new JTextArea(3, 20);
        addButton = new JButton("Add");
        updateButton = new JButton("Update");
        deleteButton = new JButton("Delete");
        refreshButton = new JButton("Refresh");

        categoryTable = new JTable();
        JScrollPane scrollPane = new JScrollPane(categoryTable);
        add(scrollPane, BorderLayout.CENTER);
        
        // Make table selection work properly
        categoryTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    private void setupLayout() {
        setLayout(new BorderLayout());

        // Input panel for title and description
        JPanel inputPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridx = 0;
        gbc.gridy = 0;
        inputPanel.add(new JLabel("Category Name:"), gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        inputPanel.add(titleField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        inputPanel.add(new JLabel("Description:"), gbc);
        gbc.gridx = 1;
        inputPanel.add(new JScrollPane(descriptionArea), gbc);

        // Button panel for Add, Update, Delete, Refresh
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(refreshButton);

        // North panel to combine input and button panels
        JPanel northPanel = new JPanel(new BorderLayout());
        northPanel.add(inputPanel, BorderLayout.CENTER);
        northPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(northPanel, BorderLayout.NORTH);
        add(new JScrollPane(categoryTable), BorderLayout.CENTER);
    }

    private void setupEventListeners() {
        addButton.addActionListener(e -> addCategory());
        updateButton.addActionListener(e -> updateCategory());
        deleteButton.addActionListener(e -> deleteCategory());
        refreshButton.addActionListener(e -> loadCategories());
        
        // Add table selection listener to populate fields when row is selected
        categoryTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                populateFieldsFromSelectedRow();
            }
        });
    }

    private void addCategory() {
        String title = titleField.getText().trim();
        String description = descriptionArea.getText().trim();
        
        if (title.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a category name.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try {
            Categories category = new Categories(title, description);
            boolean success = categoryDao.addCategory(category);
            
            if (success) {
                JOptionPane.showMessageDialog(this, "Category added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                clearFields();
                loadCategories();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to add category.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error adding category: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void updateCategory() {
        int selectedRow = categoryTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a category to update.", "Selection Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String title = titleField.getText().trim();
        String description = descriptionArea.getText().trim();
        
        if (title.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a category name.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try {
            // Get the category ID from the table model
            int categoryId = (Integer) tableModel.getValueAt(selectedRow, 0);
            Categories category = new Categories(categoryId, title, description);
            
            boolean success = categoryDao.updateCategory(category);
            
            if (success) {
                JOptionPane.showMessageDialog(this, "Category updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                clearFields();
                loadCategories();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to update category.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error updating category: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void deleteCategory() {
        int selectedRow = categoryTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a category to delete.", "Selection Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to delete this category?", 
            "Confirm Delete", 
            JOptionPane.YES_NO_OPTION);
            
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                int categoryId = (Integer) tableModel.getValueAt(selectedRow, 0);
                boolean success = categoryDao.deleteCategory(categoryId);
                
                if (success) {
                    JOptionPane.showMessageDialog(this, "Category deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    clearFields();
                    loadCategories();
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to delete category.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error deleting category: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }
    }

    private void populateFieldsFromSelectedRow() {
        int selectedRow = categoryTable.getSelectedRow();
        if (selectedRow != -1) {
            String title = (String) tableModel.getValueAt(selectedRow, 1);
            String description = (String) tableModel.getValueAt(selectedRow, 2);
            
            titleField.setText(title);
            descriptionArea.setText(description);
        }
    }

    private void clearFields() {
        titleField.setText("");
        descriptionArea.setText("");
        categoryTable.clearSelection();
    }

    private void updateTable(List<Categories> categories) {
        String[] columnNames = {"ID", "Category Name", "Description"};
        Object[][] data = new Object[categories.size()][3];
        for(int i = 0; i < categories.size(); i++) {
            Categories category = categories.get(i);
            data[i][0] = category.getId();
            data[i][1] = category.getTitle();
            data[i][2] = category.getDescription();
        }
        tableModel = new DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table read-only
            }
        };
        categoryTable.setModel(tableModel);
    }

    private void loadCategories(){
        List<Categories> cat;
        try {
            cat = categoryDao.getAllCategories();
            updateTable(cat);
        }
        catch(SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading categories: " + e.getMessage());
            e.printStackTrace();
        }
    }
}