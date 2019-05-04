/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.friendsbook.comments;

import com.friendsbook.dbconnection.DataBaseConnection;
import com.friendsbook.post.Post;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
public class CommentDao {
    private final Connection CONN = DataBaseConnection.getDBConnection();
    private final String SQL_POST_COMMENT = "insert into fbcomment (acctid, postid, comment) values (?,?,?)";
    private final String SQL_GET_COMMENT = "select acctid, comment from fbcomment where postid = ?";
    
    public List<Post> retrieveComments(List<Post> posts) {
        try(PreparedStatement ps = CONN.prepareStatement(SQL_GET_COMMENT)){
            int listCount = 0;
            for(Post post : posts){
                List<Comment> comments = new ArrayList<Comment>();
                ps.setInt(1, post.getPost_id());
                try(ResultSet rs = ps.executeQuery()){
                    while(rs.next()){
                        Comment comment = new Comment();
                        comment.setUserId(rs.getString(1));
                        comment.setComment(rs.getString(2));
                        comments.add(comment);
                    }
                    post.setComment(comments);
                    posts.set(listCount, post);
                    listCount++;
                }
            }
        }catch(SQLException e){
                Logger.getLogger(CommentDao.class.getName()).log(Level.SEVERE, null, e);
                
        }
        return posts;
    }
    
    public String saveComment(Comment comment, String userId, int postId){
        int flag = 0;
        try(PreparedStatement ps = CONN.prepareStatement(SQL_POST_COMMENT)){
            ps.setString(1, userId);
            ps.setInt(2, postId);
            ps.setString(3, comment.getComment());
            flag = ps.executeUpdate();
        }catch(SQLException e){
            Logger.getLogger(CommentDao.class.getName()).log(Level.SEVERE, null, e);
        }
        return "home_page";
    }
}
