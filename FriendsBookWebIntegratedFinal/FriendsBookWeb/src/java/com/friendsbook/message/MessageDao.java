/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.friendsbook.message;

import com.friendsbook.dbconnection.DataBaseConnection;
import com.friendsbook.user.UserDaoImpl;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author V H S Kishore
 */
@ManagedBean
@SessionScoped
public class MessageDao {
    private List<Message> msgList = new ArrayList<Message>();

    public List<Message> getMsgList() {
        return msgList;
    }

    public void setMsgList(List<Message> msgList) {
        this.msgList = msgList;
    }
    private final Connection CONN = DataBaseConnection.getDBConnection();
    private final String SQL_SEND_MESSAGE = "insert into fbnotification (sender,receiver,content,type,status) values (?,?,?,?,?)";
    private final String SQL_MESSAGE_SENDERS = "select sender from fbnotification where receiver = ? and type = 1";
    private final String SQL_RETRIEVE_MESSAGES = "select sender, receiver, content from fbnotification where (sender = ? and receiver = ?) or (sender = ? and receiver = ?) order by date asc";
    
    public void sendMessage(String message, String userId, String reciever) {
        try(PreparedStatement ps = CONN.prepareStatement(SQL_SEND_MESSAGE)){
            ps.setString(1, userId);
            if(reciever.isEmpty())
                ps.setString(2, this.msgList.get(0).getReceiver().equals(userId) ? this.msgList.get(0).getSender() : this.msgList.get(0).getReceiver());
            else
                ps.setString(2, reciever);
            ps.setString(3, message);
            ps.setInt(4, 1);
            ps.setInt(5, 0);
            ps.executeUpdate();
             
            System.out.println("----->Message sent successfully!<-----");
        }catch(SQLException e){
            Logger.getLogger(UserDaoImpl.class.getName()).log(Level.SEVERE, null, e);
        }
            if(reciever == null || reciever.isEmpty()){
                retriveMessages(userId, this.msgList.get(0).getReceiver().equals(userId) ? this.msgList.get(0).getSender() : this.msgList.get(0).getReceiver());
            }
    }
    
    public List<String> retrieveSenders(String userId){
        List<String> friendsList = new ArrayList<String>();
        try(PreparedStatement ps = CONN.prepareStatement(SQL_MESSAGE_SENDERS)){
            ps.setString(1, userId);
            try(ResultSet rs = ps.executeQuery()){
                while(rs.next()){
                    if(!friendsList.contains(rs.getString(1))){
                        friendsList.add(rs.getString(1));
                    }
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(MessageDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return friendsList;
    }
    
    public void retriveMessages(String userId, String senderId){
        this.msgList.clear();
        try(PreparedStatement ps = CONN.prepareStatement(SQL_RETRIEVE_MESSAGES)){
            ps.setString(1, userId);
            ps.setString(2, senderId);
            ps.setString(3, senderId);
            ps.setString(4, userId);
            try(ResultSet rs = ps.executeQuery()){
                while(rs.next()){
                    Message message = new Message();
                    message.setSender(rs.getString(1));
                    message.setReceiver(rs.getString(2));
                    message.setContent(rs.getString(3));
                    msgList.add(message);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(MessageDao.class.getName()).log(Level.SEVERE, null, ex);
        }
    } 
}
