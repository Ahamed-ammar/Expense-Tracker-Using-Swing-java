package com.extracker;
import java.sql.Connection;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import com.extracker.util.dbConnection; 
import com.extracker.gui.LandGui;
public class Main {
    public static void main(String args[]) {
        try(Connection conn = dbConnection.getDBConnection()){
            System.out.println("Database connected successfully");
        } catch(Exception e){
            System.out.println("Database connection failed: " + e.getMessage());
        }

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        catch(ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e){
            System.out.println("Failed to set Look and Feel" + e.getMessage());
        }
        SwingUtilities.invokeLater(
            () -> {
                LandGui landGui = new LandGui();
                landGui.setVisible(true);
            }
        );
    }
}
