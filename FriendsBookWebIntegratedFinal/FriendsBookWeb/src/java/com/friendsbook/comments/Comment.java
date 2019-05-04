/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.friendsbook.comments;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

/**
 *
 * @author V H S Kishore
 */
@ManagedBean
@RequestScoped
public class Comment {
    private String comment;
    private String userId;
    private int postId;

    public String getComment() {
            return comment;
    }
    public void setComment(String comment) {
            this.comment = comment;
    }
    public String getUserId() {
            return userId;
    }
    public void setUserId(String userId) {
            this.userId = userId;
    }
    public int getPostId() {
            return postId;
    }
    public void setPostId(int postId) {
            this.postId = postId;
    }
    
    public void emptyComment(){
        this.comment = null;
    }
}
