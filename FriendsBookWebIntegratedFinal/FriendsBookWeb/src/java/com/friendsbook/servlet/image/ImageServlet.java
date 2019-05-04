/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.friendsbook.servlet.image;

import com.friendsbook.dbconnection.DataBaseConnection;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author V H S Kishore
 */
public class ImageServlet extends HttpServlet {
     private static final long serialVersionUID = -6449908958106497977L;
     private final Connection CONN = DataBaseConnection.getDBConnection();
     private final String SQL_GET_IMAGE = "select image from fbuseraccount where acctid = ?";
    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        
        try(PreparedStatement ps = CONN.prepareStatement(SQL_GET_IMAGE)){
            byte[] imageBytes = null;
            String id = request.getParameter("id");
            //FriendsBook bean = (FriendsBook) request.getSession().getAttribute("friendsBook");
            ps.setString(1, id);
            try(ResultSet rs = ps.executeQuery()){
                while(rs.next()){
                    imageBytes = rs.getBytes(1);
                }
            }
            response.getOutputStream().write(imageBytes);
            response.getOutputStream().close();
        } catch (SQLException ex) {
             Logger.getLogger(ImageServlet.class.getName()).log(Level.SEVERE, null, ex);
         }
        
    }

    

}
