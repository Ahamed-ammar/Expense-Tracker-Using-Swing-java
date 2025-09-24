package com.extracker.gui;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import com.extracker.dao.LandDao;
import java.util.List;

public class LandGui extends JFrame{
    private JButton CategoryBtn, ExpenseBtn;
    public LandGui() {
        setTitle("Expence Tracker Application");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

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
        //setupLayout();
    }
    public void setupEvetListener() {
        
    }
}

