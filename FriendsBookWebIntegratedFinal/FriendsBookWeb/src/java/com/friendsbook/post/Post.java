/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.friendsbook.post;

import com.friendsbook.comments.Comment;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author V H S Kishore
 */
@ManagedBean
@SessionScoped
public class Post {
    private int post_id;
    private String user_id;
    private String content;
    private String title;
    private Date postDate;
    private List<Comment> comment = new ArrayList<Comment>();
    
    public Date getPostDate() {
        return postDate;
    }

    public void setPostDate(Date postDate) {
        this.postDate = postDate;
    }
    
    
    public int getPost_id() {
            return post_id;
    }
    public void setPost_id(int post_id) {
            this.post_id = post_id;
    }
    public String getUser_id() {
            return user_id;
    }
    public void setUser_id(String user_id) {
            this.user_id = user_id;
    }
    public String getContent() {
            return content;
    }
    public void setContent(String content) {
            this.content = content;
    }
    public String getTitle() {
            return title;
    }
    public void setTitle(String title) {
            this.title = title;
    }
    public List<Comment> getComment() {
            return comment;
    }
    public void setComment(List<Comment> comment) {
            this.comment = comment;
    }
    public void setEmpty(){
        this.comment = null;
        this.content = null;
        this.post_id = 0;
        this.user_id = null;
    }
}
