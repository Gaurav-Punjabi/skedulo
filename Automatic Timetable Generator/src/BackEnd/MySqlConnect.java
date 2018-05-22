package BackEnd;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author gauravpunjabi
 */
public class MySqlConnect {
    static Connection connection;
    static PreparedStatement preparedStatement;
    static ResultSet resultSet;
    static String connectionString = "jdbc:mysql://localhost:3306/automatic_timetable";
    public static Connection connectDb()
    {
        try 
        {
            connection = DriverManager.getConnection(connectionString,"Gaurav","password");
            return connection;
        } 
        catch (SQLException e) {
            JOptionPane.showMessageDialog(null,"Sql error : " + e.getMessage(),"MySqlConnect",JOptionPane.OK_OPTION);
            return null;
        }
        catch(Exception e) {
            JOptionPane.showMessageDialog(null,"error : " + e.getMessage(),"MySqlConnect",JOptionPane.OK_OPTION);
            return null;
        }
    }
}
