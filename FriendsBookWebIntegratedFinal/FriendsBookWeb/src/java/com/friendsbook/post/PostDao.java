/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.friendsbook.post;

import com.friendsbook.comments.CommentDao;
import com.friendsbook.dbconnection.DataBaseConnection;
import com.friendsbook.user.UserDaoImpl;
import com.friendsbook.hashtag.HashTag;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

/**
 *
 * @author V H S Kishore
 */
@ManagedBean
@SessionScoped
public class PostDao {
    private final String SQL_FRIENDS_POSTS = "select postid, acctid, content, date from fbupdateandpost ORDER BY date DESC";
    private final String SQL_USER_POSTS = "select postid, acctid, content from fbupdateandpost where acctid = ? ORDER BY date DESC";
    private final String SQL_CREATE_POST = "insert into fbupdateandpost(acctID,type,content) values(?,?,?)";
    private final String SQL_SAVE_HASHTAGS = "insert into hashtags(hashtag_name,post_id) values (?,?)";
    private final String SQL_GET_HASHTAGS = "SELECT hashtag_name,count(hashtag_id) num FROM hashtags group by hashtag_name order by num desc LIMIT 3";
    private final String SQL_GET_TOP_HASHTAG_POSTS = "SELECT postID, acctID, content FROM fbupdateandpost p join hashtags h on p.postID = h.post_id and hashtag_name = ? order by date desc LIMIT 3";
    private final Connection CONN = DataBaseConnection.getDBConnection();
    private List<Post> topPosts = new ArrayList<Post>();
    
    public List<Post> getTopPosts() {
        return topPosts;
    }

    public void setTopPosts(List<Post> topPosts) {
        this.topPosts = topPosts;
    }
    
    public List<Post> retrievePosts(String userId){
        UserDaoImpl userDao = new UserDaoImpl();
        List<String> friends = userDao.retrieveFriendList(userId);
        List<Post> posts = new ArrayList<Post>();
        int count = 1;
            try(PreparedStatement ps = CONN.prepareStatement(SQL_FRIENDS_POSTS)){
                try(ResultSet rs = ps.executeQuery()){
                    while(rs.next() && count < 4){
                        if(friends.contains(rs.getString(2))){
                            Post post = new Post();
                            post.setPost_id(rs.getInt(1));
                            post.setUser_id(rs.getString(2));
                            post.setContent(rs.getString(3));
                            post.setPostDate(rs.getDate(4));
                            posts.add(post);
                            count++;
                        }
                    }
                }
            }catch(Exception e){
                Logger.getLogger(PostDao.class.getName()).log(Level.SEVERE, null, e);
            }
        CommentDao commentDaoImpl = new CommentDao();
        posts = commentDaoImpl.retrieveComments(posts);
        return posts;
    }
    
    public List<Post> retrieveUserPosts(String userId){
        UserDaoImpl userDao = new UserDaoImpl();
        List<Post> posts = new ArrayList<Post>();
        int count = 1;
            try(PreparedStatement ps = CONN.prepareStatement(SQL_USER_POSTS)){
                ps.setString(1, userId);
                try(ResultSet rs = ps.executeQuery()){
                    while(rs.next() && count < 5){
                            Post post = new Post();
                            post.setPost_id(rs.getInt(1));
                            post.setUser_id(rs.getString(2));
                            post.setContent(rs.getString(3));
                            posts.add(post);
                            count++;
                    }
                }
            }catch(Exception e){
                Logger.getLogger(PostDao.class.getName()).log(Level.SEVERE, null, e);
            }
        CommentDao commentDaoImpl = new CommentDao();
        posts = commentDaoImpl.retrieveComments(posts);
        return posts;
    }
    
    //Create post.
    public String createPost(Post post, String userId) {
        List<HashTag> hashTags = hashTag(post.getContent(), post.getPost_id());
        int postId = 0;
        try(PreparedStatement ps = CONN.prepareStatement(SQL_CREATE_POST,Statement.RETURN_GENERATED_KEYS)){
            ps.setString(1, userId);
            ps.setInt(2, 1);
            ps.setString(3, post.getContent());
            ps.executeUpdate();
            try(ResultSet rs = ps.getGeneratedKeys()){
                while(rs.next()){
                    postId = rs.getInt(1);
                }
                saveHashTags(postId, hashTags);
            }
        }catch(Exception e){
                Logger.getLogger(PostDao.class.getName()).log(Level.SEVERE, null, e);
        }
        
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "", "Successfully Posted!");
        FacesContext.getCurrentInstance().addMessage("postForm:ref", message);
        return "home_page";
    }
    
    //Get HashTags and save to DB.
    private List<HashTag> hashTag(String content, int postId){
        String[] titleArray = content.split("#");
        
        List<HashTag> hashtagList = new ArrayList<>();
        boolean hashAtZero = content.startsWith("#");
        if(hashAtZero){
            for (String string : titleArray) {
                HashTag hashtag = new HashTag();
                int i = string.indexOf(" ");
                if(i > 0){
                    hashtag.setHashtagName(string.substring(0, i));
                    hashtag.setPost_id(postId);
                    hashtagList.add(hashtag);
                }	
                else{
                    hashtag.setHashtagName(string);
                    hashtag.setPost_id(postId);
                    hashtagList.add(hashtag);
                }
            }
        }else{
                for(int i = 1 ; i < titleArray.length; i++){
                    HashTag hashtag = new HashTag();
                    int j = titleArray[i].indexOf(" ");
                    if(j > 0){
                        hashtag.setHashtagName(titleArray[i].substring(0, j));
                        hashtag.setPost_id(postId);
                        hashtagList.add(hashtag);
                    }else{
                        hashtag.setHashtagName(titleArray[i]);
                        hashtag.setPost_id(postId);
                        hashtagList.add(hashtag);
                    }
                }
        }
        return hashtagList;
    }
    
    public void saveHashTags(int postId, List<HashTag> hashTags){
        try(PreparedStatement ps = CONN.prepareStatement(SQL_SAVE_HASHTAGS)){
            for (HashTag hashTag : hashTags) {
                ps.setString(1, hashTag.getHashtagName());
                ps.setInt(2, postId);
                ps.executeUpdate();
            }
        }catch(SQLException e){
            Logger.getLogger(PostDao.class.getName()).log(Level.SEVERE, null, e);
            System.out.println("Something went wrong with inserting hashtags");
        }
    }
    
    public List<String> retrieveHashTags() {
        List<String> topHashTags = new ArrayList<String>(3);
        try(PreparedStatement ps = CONN.prepareStatement(SQL_GET_HASHTAGS)){
            try(ResultSet rs = ps.executeQuery()){
                while(rs.next()){
                    topHashTags.add("#"+rs.getString(1));
                }
            }
        }catch(SQLException e){
            Logger.getLogger(PostDao.class.getName()).log(Level.SEVERE, null, e);
        }
        return topHashTags;
    }
    
	public String retrieveTopHashTagPosts(String hashTagName) {
            List<Post> posts = new ArrayList<Post>(3);
            try(PreparedStatement ps = CONN.prepareStatement(SQL_GET_TOP_HASHTAG_POSTS)){
                ps.setString(1, hashTagName.substring(1).toString());
                try(ResultSet rs = ps.executeQuery()){
                    while(rs.next()){
                        Post post = new Post();
                        post.setPost_id(1);
                        post.setContent(rs.getString(3));
                        post.setUser_id(rs.getString(2));
                        posts.add(post);
                    }
                }
            CommentDao commentDaoImpl = new CommentDao();
            topPosts = commentDaoImpl.retrieveComments(posts); 
            }catch(SQLException e){
                    Logger.getLogger(PostDao.class.getName()).log(Level.SEVERE, null, e);
            }
            return "top_posts";
	}
}
