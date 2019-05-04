/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.friendsbook.user;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author V H S Kishore
 */
@ManagedBean
@SessionScoped
public class User {
    private String name;
    private String id;
    private String password;
    private String gender;
    private String birthday;
    private String school;
    private String user_search;
    private UploadedFile file;
    
    private byte[] userImage;

    public byte[] getUserImage() {
        return userImage;
    }

    public void setUserImage(byte[] userImage) {
        this.userImage = userImage;
    }

    public UploadedFile getFile() {
        return file;
    }

    public void setFile(UploadedFile file) {
        this.file = file;
    }

    public String getUser_search() {
        return user_search;
    }

    public void setUser_search(String user_search) {
        this.user_search = user_search;
    }
    
    public String getName() {
            return name;
    }
    public void setName(String name) {
            this.name = name;
    }

    public String getId() {
            return id;
    }
    public void setId(String id) {
            this.id = id;
    }
    public String getPassword() {
            return password;
    }
    public void setPassword(String password) {
            this.password = password;
    }
    public String getGender() {
            return gender;
    }
    public void setGender(String gender) {
            this.gender = gender;
    }
    public String getBirthday() {
            return birthday;
    }
    public void setBirthday(String birthday) {
            this.birthday = birthday;
    }
    public String getSchool() {
            return school;
    }
    public void setSchool(String school) {
            this.school = school;
    }
}
