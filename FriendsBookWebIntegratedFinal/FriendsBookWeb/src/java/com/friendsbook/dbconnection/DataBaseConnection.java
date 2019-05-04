/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.friendsbook.dbconnection;

/**
 *
 * @author V H S Kishore
 */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DataBaseConnection {
    public static Connection getDBConnection(){
        final String DATABASE_URL = "jdbc:mysql://mis-sql.uhcl.edu/vermaa0974";
        final String DATABASE_USER_ID = "vermaa0974";
        final String DATABASE_PWD = "1653836";

        Connection con = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection(DATABASE_URL, DATABASE_USER_ID, DATABASE_PWD);
        } catch (SQLException e) {
            Logger.getLogger(DataBaseConnection.class.getName()).log(Level.SEVERE, null, e);
        } catch (ClassNotFoundException e) {
            Logger.getLogger(DataBaseConnection.class.getName()).log(Level.SEVERE, null, e);
        }
        return con;
    }
}
