package com.friendsbook.other;

import java.text.SimpleDateFormat;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author ankcs
 */
public class FBUserAccount {

    protected String acctID;
    protected String password;
    protected String name;
    protected String School;
    protected int gender;
    protected String dob;
     
public FBUserAccount()
{
    
}
    public FBUserAccount(String acctID, String password, String name, String School, int gender, String dob) {
        this.acctID = acctID;
        this.password = password;
        this.name = name;
        this.School = School;
        this.gender = gender;
        this.dob = dob;
    }
    
    public FBUserAccount(String acctID, String name, String School, int gender, String dob) {
        this.acctID = acctID;
       
        this.name = name;
        this.School = School;
        this.gender = gender;
        this.dob = dob;
    }

    public String getAcctID() {
        return acctID;
    }

    public void setAcctID(String acctID) {
        this.acctID = acctID;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSchool() {
        return School;
    }

    public void setSchool(String School) {
        this.School = School;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getDob() {
        return dob; 
    }

    public void setDob(String dob) {
        this.dob = dob;
    }
 
    
}
