/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.friendsbook.hashtag;

import com.friendsbook.post.Post;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author V H S Kishore
 */
@ManagedBean
@SessionScoped
public class HashTag {
    private int hashtagId;
    private String hashtagName;
    private int hashtagCount;
    private int post_id;
    private List<Post> topPosts = new ArrayList<Post>();

    public List<Post> getTopPosts() {
        return topPosts;
    }

    public void setTopPosts(List<Post> topPosts) {
        this.topPosts = topPosts;
    }
    
    public int getHashtagId() {
            return hashtagId;
    }
    public void setHashtagId(int hashtagId) {
            this.hashtagId = hashtagId;
    }
    public String getHashtagName() {
            return hashtagName;
    }
    public void setHashtagName(String hashtagName) {
            this.hashtagName = hashtagName;
    }
    public int getHashtagCount() {
            return hashtagCount;
    }
    public void setHashtagCount(int hashtagCount) {
            this.hashtagCount = hashtagCount;
    }
    public int getPost_id() {
            return post_id;
    }
    public void setPost_id(int post_id) {
            this.post_id = post_id;
    }
}
