package com.friendsbook.other;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author ankcs
 */
@ManagedBean
@SessionScoped
public class FBNotification {

    /**
     * Creates a new instance of FBNotification
     */
    
    
    private String notiID;
    private String sender;
    private String receiver;
    private int type;
    public String content;
    private int status;
    private String linkMap;
    
public FBNotification() {
    }
    public FBNotification(String notiID, String sender, String receiver, int type, String content, int status, String linkMap) {
        this.notiID = notiID;
        this.sender = sender;
        this.receiver = receiver;
        this.type = type;
        this.content = content;
        this.status = status;
        this.linkMap=linkMap;

    }

    public String getLinkMap() {
        return linkMap;
    }

    public void setLinkMap(String linkMap) {
        this.linkMap = linkMap;
    }
    public FBNotification(String sender) {
        
        this.sender = sender;
        

    }

    public String getNotiID() {
        return notiID;
    }

    public void setNotiID(String notiID) {
        this.notiID = notiID;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return getContent();
    }

}
