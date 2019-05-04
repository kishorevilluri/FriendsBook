/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.friendsbook.user;

import com.friendsbook.dbconnection.DataBaseConnection;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

/**
 *
 * @author V H S Kishore
 */
@ManagedBean
@RequestScoped
public class UserDaoImpl {
    
    private final Connection CONN = DataBaseConnection.getDBConnection();
    private final String SQL_FRIENDS_LIST = "select * from fbfriend where frndid1 = ? or frndid2 = ?";
    private final String SQL_USER_FROM_NOTIFICATIONS = "select status from fbnotification where sender = ? and receiver = ? and type = 0";
    private final String SQL_FRIEND_REQUEST = "insert into fbnotification (sender, receiver, type, content, status) values (?,?,?,?,?)";
    private final String SQL_DELETE_REQUEST = "delete from fbnotification where sender = ? and receiver = ? and type = 0";
    //private final String SQL_LOGIN_USER = "select * from user where id= ?";
    private final String SQL_GET_USERS = "select name, acctid from fbuseraccount where name like ?";
    private final String SQL_SAVE_USER_IMAGE = "update fbuseraccount set image = ? where acctID = ?";
    private final String SQL_GET_USER_IMAGE = "select image from fbuseraccount where acctid = ?";
    
    
    public void sendFriendRequest(String currentUserId, String friendId) {
        try(PreparedStatement ps = CONN.prepareStatement(SQL_FRIEND_REQUEST)){
            ps.setString(1, currentUserId);
            ps.setString(2, friendId);
            ps.setInt(3, 0);
            ps.setString(4, friendId+" wants to be your friend!");
            ps.setString(5, "0");
            ps.executeUpdate();
            System.out.println("----->Friend Request sent successfully!<-----");
        }catch (SQLIntegrityConstraintViolationException e) {
            System.err.println("You are Already friend with "+friendId);
        }catch(SQLException e){
            Logger.getLogger(UserDaoImpl.class.getName()).log(Level.SEVERE, null, e);
        }
    }
    
    public void deleteRequest(String currentUserId, String friendId){
        try(PreparedStatement ps = CONN.prepareStatement(SQL_DELETE_REQUEST)){
            ps.setString(1, currentUserId);
            ps.setString(2,friendId);
            ps.executeUpdate();
        }catch(SQLException e){
            e.printStackTrace();
        }
    }
     
    public List<String> retrieveFriendList(String userId){
        List<String> friends = new ArrayList<String>();
        try(PreparedStatement ps = CONN.prepareStatement(SQL_FRIENDS_LIST)){
            ps.setString(1,userId);
            ps.setString(2, userId);
            try(ResultSet rs=ps.executeQuery()){
                while(rs.next()){
                    if(rs.getString(1).equals(userId)){
                            friends.add((rs.getString(2)));
                    }else if(rs.getString(2).equals(userId)){
                            friends.add(rs.getString(1));
                    }
                }
            }
        }catch(SQLException e){
                Logger.getLogger(UserDaoImpl.class.getName()).log(Level.SEVERE, null, e);
        }
        return friends;
    }
    
    public ArrayList<User> searchUser(String userName){
        ArrayList<User> users = new ArrayList<User>();
        try(PreparedStatement ps = CONN.prepareStatement(SQL_GET_USERS)){
            ps.setString(1, "%"+userName+"%");
            try(ResultSet rs = ps.executeQuery()){
                while(rs.next()){
                    User user = new User();
                    user.setName(rs.getString(1));
                    user.setId(rs.getString(2));
                    users.add(user);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(UserDaoImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return users;
    }
    
    public boolean isFriend(String user, String otherUser){
        return retrieveFriendList(user).contains(otherUser);
    }
    public boolean isNotFriend(String user, String otherUser){
        if(!isFriend(user, otherUser) && !pendingRequest(user, otherUser)){
            return true;
        }else{
            return false;
        }
    }
    
    public boolean pendingRequest(String user, String otherUser){
        int status = 1;
        try(PreparedStatement ps = CONN.prepareStatement(SQL_USER_FROM_NOTIFICATIONS)){
            ps.setString(1, user);
            ps.setString(2, otherUser);
            try(ResultSet rs = ps.executeQuery()){
                while(rs.next()){
                   status = rs.getInt(1);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(UserDaoImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        if(status == 0){
            return true;
        }else {
            return false;
        }
    }
    
    public void storeImage(User user, String userId){
        try(PreparedStatement ps = CONN.prepareStatement(SQL_SAVE_USER_IMAGE)){
            ps.setBinaryStream(1, user.getFile().getInputstream());
            ps.setString(2, userId);
            ps.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(UserDaoImpl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(UserDaoImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public User getUserImage(String userId){
        User user = new User();
        try(PreparedStatement ps = CONN.prepareStatement(SQL_GET_USER_IMAGE)){
            ps.setString(1, userId);
            try(ResultSet rs = ps.executeQuery()){
                while(rs.next()){
                    user.setUserImage(rs.getBytes(1));
                }
            }            
        } catch (SQLException ex) {
            Logger.getLogger(UserDaoImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return user;
    }
}
