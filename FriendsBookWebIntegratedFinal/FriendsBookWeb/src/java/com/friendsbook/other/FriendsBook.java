package com.friendsbook.other;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import com.friendsbook.user.User;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.YearMonth;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

/**
 *
 * @author ankcs
 */
@ManagedBean
@SessionScoped
public class FriendsBook {

    
    private String user;
    private String receiver;
    private FBUserAccount account;
    private FBNotification noti;
    private User userDetails;
    private ArrayList<FBUpdateAndPost> updateAndPost;
    private ArrayList<String> friends;
    private ArrayList<FBNotification> notification;
    private ArrayList<FBNotification> message; 
    private ArrayList<FBNotification> sender;
    private ArrayList<FBUserAccount> users;
    private int newNotificationCount;
    private int newMessageCount;
    private int fsdf;

    public User getUserDetails() {
        return userDetails;
    }

    public void setUserDetails(User userDetails) {
        this.userDetails = userDetails;
    }
        
     public int getNewMessageCount() {
        return newMessageCount;
    }
    public ArrayList<FBUserAccount> getUsers() {
        return users;
    }
     public FriendsBook(String receiver) {
        this.receiver = receiver;
    }
    public String getReceiver() {
        return receiver;
    }
     public String getUser() {
        return user;
    }
    public void setUser(String user) {
        this.user = user;
    }

    public ArrayList<FBNotification> getMessage() {
        return message;
    }
    public ArrayList<FBNotification> getSender() {
        return sender;
    }
    
    public int getNewNotificationCount() {
        return newNotificationCount;
    }
    public FBUserAccount getAccount(){
        return account;
    }
    public FBNotification getNoti(){
        return noti;
    }
    public ArrayList<FBUpdateAndPost> getPosts(){
        return updateAndPost;
    } 
    public ArrayList<String> getFriends(){
        return friends;
    }
    public ArrayList<FBNotification> getNotifications(){
        return notification;
    }
    
    public FriendsBook() {

        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Intitialize Globals
        account = new FBUserAccount();
        noti=new FBNotification();
        friends = new ArrayList<String>();
        updateAndPost = new ArrayList<FBUpdateAndPost>();
        notification=new ArrayList<FBNotification>();
        message=new ArrayList<FBNotification>();
        sender=new ArrayList<FBNotification>();
        users=new ArrayList<FBUserAccount>();
        newNotificationCount=0;
        newMessageCount=0;
        userDetails = new User();
    }
    
    
    public String login() {

        final String DB_URL = "jdbc:mysql://mis-sql.uhcl.edu/vermaa0974";
        Connection conn = null;
        Statement st = null;
        ResultSet rs = null;
        int i = 0;
        String output = null;
        try { 
           Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(DB_URL, "vermaa0974", "1653836");
            SimpleDateFormat dateFormatter = new SimpleDateFormat("MM/dd/YYYY");
            
            st = conn.createStatement();
            // do a query to make sure that id and ssn is not used

            rs = st.executeQuery("select * from FBUserAccount where acctID = '" + account.getAcctID() + "'");
            if (rs.next()) {
                if (account.getAcctID().equals(rs.getString("acctID")) && account.getPassword().equals(rs.getString("password"))) {
                    viewNotification();
//                    message();
//                    selectUpdateAndPost();
                        account.setAcctID(rs.getString("acctID"));
                        account.setPassword(rs.getString("password"));
                        account.setName(rs.getString("name"));
                        account.setSchool(rs.getString("school"));
                        account.setGender(rs.getInt("gender"));
                        String d = rs.getString("dob");
                        String da = YearMonth.from(
                                ZonedDateTime.parse(
                                        rs.getString("dob"),
                                        DateTimeFormatter.ofPattern( "E MMM d HH:mm:ss z uuuu" )
                                )
                        ).toString();
                        
                        account.setDob(da);
                        output= "home_page";
               }
                else
                {  
                    FacesMessage loginError = new FacesMessage(FacesMessage.SEVERITY_ERROR, "", "User Name of Password incorrect!");
                    FacesContext.getCurrentInstance().addMessage("loginForm:loginRef", loginError);
                    output= "index.xhtml";
                }
               
            }else{
                FacesMessage loginError = new FacesMessage(FacesMessage.SEVERITY_ERROR, "", "User ID not found!");
                FacesContext.getCurrentInstance().addMessage("loginForm:loginRef", loginError);
                output= "index.xhtml";
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
            
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(FBUserAccount.class.getName()).log(Level.SEVERE, null, ex);
            output = "error";
        } finally {
            try {
                conn.close();
                st.close();
                rs.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return output;
    }
    
    public String register() {
        
        final String DB_URL = "jdbc:mysql://mis-sql.uhcl.edu/vermaa0974";
        Connection conn = null;
        Statement st = null;
        ResultSet rs = null;
        int i = 0;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(DB_URL, "vermaa0974", "1653836");
            st = conn.createStatement();
             FacesContext context = FacesContext.getCurrentInstance(); 
            
            int valid = -1;
            do{
                if(valid == 1) {
                     
                 context.addMessage(null, new FacesMessage("Account ID: Value Required, Range: 3-10, must Contain any one of them: # * ? ! ")); 
                    return "index.xhtml";
                }
                if (valid == 2) {
                     context.addMessage(null, new FacesMessage("Account ID Alread Exists.")); 

                    return "index.xhtml";
                }
               
                valid = isValid(account.getAcctID());
            }while (valid != 0);

            
            
            if(account.getAcctID().equals(account.getPassword()))
            {
                context.addMessage(null, new FacesMessage("Password Should not be same as AccountID")); 
               return "index.xhtml";
            }
            if(account.getPassword().length()>16 || account.getPassword().length()<8)
            {
               context.addMessage(null, new FacesMessage("Password Range: 8-16")); 
               return "index.xhtml";
            }
            else{
            i = st.executeUpdate("Insert into FBUserAccount (acctID, password,name, school, gender, dob) values('" + account.getAcctID() + "', '" + account.getPassword() + "','" + account.getName() + "','" + account.getSchool() + "'," + account.getGender() + ",'" + account.getDob() + "')");
            }
            FacesContext.getCurrentInstance();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(FBUserAccount.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                conn.close();
                st.close();
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        
            if(i > 0){
                return "home_page";
            }else{
                return "";
            }
    }
 }
  
    public String UpdateProfile()
{
    
        final String DB_URL="jdbc:mysql://mis-sql.uhcl.edu/vermaa0974";
        Connection conn = null;
        Statement st = null;
        ResultSet rs = null;
         try
        {
          Class.forName("com.mysql.jdbc.Driver");
        }
        catch(Exception e)
        {
          return ("SQLError");
        }
      try
        {
           conn =  DriverManager.getConnection(DB_URL,"vermaa0974","1653836");
           st = conn.createStatement();
           rs=st.executeQuery("Select * from fbuseraccount where acctid='"+account.acctID+"'" );
            if(rs.next()) 
            {      
              int u=st.executeUpdate("update fbuseraccount set name ='"+account.getName()+"' , Password ='"+account.getPassword()+"' "
                       + ", Gender = '"+account.getGender()+"' ,school='"+account.getSchool()+"',dob = '"+account.getDob()+"'where acctid='"+account.getAcctID()+"'");
            
            } 
          return("home_page");  
            
        }
            catch(SQLException  e)
       {
          e.printStackTrace();
          return("ServerError");
       }
       finally
        {
            try
            {
                conn.close();
                st.close();
                rs.close();
            }
            catch(Exception e)
            {
                e.printStackTrace();
               return("error");
            }
        }
     }
     
    public String FriendList()
 {
 
  final String DB_URL = "jdbc:mysql://mis-sql.uhcl.edu/vermaa0974";
        Connection conn = null;
        Statement st = null;
        ResultSet rs = null;

        try {
            friends.clear();
            conn = DriverManager.getConnection(DB_URL, "vermaa0974", "1653836");
            st = conn.createStatement();

            rs = st.executeQuery("select * from FBFriend");
            while (rs.next()) {
                if ((rs.getString("FrndID1").equals(account.getAcctID()))) {
                    friends.add(rs.getString("FrndID2"));
                } else if ((rs.getString("FrndID2").equals(account.getAcctID()))) {
                    friends.add(rs.getString("FrndID1"));
                }
            }   
        }catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                conn.close();
                st.close();
                //rs.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
 return "FriendListTable";
 }
   public String FriendsProfile(String acctID)
   {
      final String DB_URL = "jdbc:mysql://mis-sql.uhcl.edu/vermaa0974";
        Connection conn = null;
        Statement st = null;
        ResultSet rs = null;

        try {
            
            conn = DriverManager.getConnection(DB_URL, "vermaa0974", "1653836");
            st = conn.createStatement();
            
            rs=st.executeQuery("select * from FBUserAccount where acctID='"+acctID+"'");
            while(rs.next())
            {
                userDetails.setId(rs.getString("acctID"));
                userDetails.setName(rs.getString("name"));
                userDetails.setSchool(rs.getString("school"));
                userDetails.setGender(rs.getInt("gender") == 1 ? "Female" : "Male");
                userDetails.setBirthday(rs.getString("dob"));
                return "FriendsProfile";
            }
           
            
        }catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                conn.close();
                st.close();
                //rs.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
 return null; 
   }
    
    public void selectUpdateAndPost() {

        final String DB_URL = "jdbc:mysql://mis-sql.uhcl.edu/vermaa0974";
        Connection conn = null;
        Statement st = null;
        ResultSet rs = null;

        try {
            conn = DriverManager.getConnection(DB_URL, "vermaa0974", "1653836");
            st = conn.createStatement();

            rs = st.executeQuery("select * from FBFriend");
            System.out.println("Your Friend's Updates and posts: ");
            while (rs.next()) {
                if ((rs.getString("FrndID1").equals(account.getAcctID()))) {
                    friends.add(rs.getString("FrndID2"));
                } else if ((rs.getString("FrndID2").equals(account.getAcctID()))) {
                    friends.add(rs.getString("FrndID1"));
                }
            }
            for (String friend : friends) {
                rs = st.executeQuery("select * from FBUpdateAndPost where acctID='" + friend + "' order by date desc limit 3");
                while (rs.next()) {
                    updateAndPost.add(new FBUpdateAndPost(rs.getString("postID"), rs.getString("acctID"), rs.getInt("type"), rs.getString("content"), rs.getTimestamp("date")));
                }
            }

            updateAndPost = FBDateAndTime.SortPostsByTime(updateAndPost);

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                conn.close();
                st.close();
                //rs.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
     public  void viewNotification() {
         this.newNotificationCount = 0;
         this.notification.clear();
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(FriendsBook.class.getName()).log(Level.SEVERE, null, ex);
        }
        final String DB_URL = "jdbc:mysql://mis-sql.uhcl.edu/vermaa0974";
        Connection conn = null;
        Statement st = null;
        ResultSet rs = null;

        try {
            conn = DriverManager.getConnection(DB_URL, "vermaa0974", "1653836");
            st = conn.createStatement();
         
            rs = st.executeQuery("select * from fbnotification where receiver ='" + account.getAcctID() + "' order by date desc");
            while (rs.next()) {
                FBNotification n = new FBNotification(rs.getString("notiID"), rs.getString("sender"), rs.getString("receiver"), rs.getInt("type"), rs.getString("content"), rs.getInt("status"),"friendreq");
                if (rs.getInt("status") == 0 && rs.getInt("type")==0) {
                    newNotificationCount++;
                  
                }
                 if (rs.getInt("status") == 0 && rs.getInt("type")==1) {
                    newMessageCount++;
                  
                }
                notification.add(n);
            }
            //st.executeUpdate("update fbnotification set status =1");
            
         
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                conn.close();
                st.close();
                rs.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
     
     public String viewMessage(String acctID)
     {
         message.clear();
         final String DB_URL = "jdbc:mysql://mis-sql.uhcl.edu/vermaa0974";
        Connection conn = null;
        Statement st = null;
        ResultSet rs = null;

        try {
             Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(DB_URL, "vermaa0974", "1653836");
            st = conn.createStatement();
           
            rs = st.executeQuery("select * from fbnotification where type=1 and sender='"+acctID+"' and receiver='"+account.getAcctID()+"' or sender='"+account.getAcctID()+"' and receiver='"+acctID+"'");
            while (rs.next()) {
                message.add(new FBNotification(rs.getString("notiID"), rs.getString("sender"), rs.getString("receiver"), rs.getInt("type"), rs.getString("content"), rs.getInt("status"),null));
            }
            receiver=acctID;
            return "message";
            
            
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(FriendsBook.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                conn.close();
                st.close();
                //rs.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        } 
     }
     
     public String acceptFriendRequest(String acctID,String notiID)
     {
        final String DB_URL = "jdbc:mysql://mis-sql.uhcl.edu/vermaa0974";
        Connection conn = null;
        Statement st = null;
        ResultSet rs = null;
        String webPage;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(DB_URL, "vermaa0974", "1653836");
            st = conn.createStatement();
            
            st.executeUpdate("insert into fbfriend values('"+acctID+"' ,'"+account.getAcctID()+"')");
            st.executeUpdate("delete from fbnotification where notiid='"+notiID+"'");
            viewNotification();
            webPage= "viewNotification";
            
        } catch (SQLException e) {
            e.printStackTrace();
            webPage="error";
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(FriendsBook.class.getName()).log(Level.SEVERE, null, ex);
            webPage="error";
        } finally {
            try {
                conn.close();
                st.close();
                //rs.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } 
        return webPage;
     }
      
     public String ignoreFriendRequest(String notiID)
     {
        final String DB_URL = "jdbc:mysql://mis-sql.uhcl.edu/vermaa0974";
        Connection conn = null;
        Statement st = null;
        ResultSet rs = null;

        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(DB_URL, "vermaa0974", "1653836");
            st = conn.createStatement();
            
             st.executeUpdate("delete from fbnotification where notiid='"+notiID+"'");
             viewNotification();
             return "viewNotification";
            
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(FriendsBook.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                conn.close();
                st.close();
                //rs.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        } 
     }
     public String searchFriend(String acctID)
       {
        final String DB_URL = "jdbc:mysql://mis-sql.uhcl.edu/vermaa0974";
        Connection conn = null;
        Statement st = null;
        ResultSet rs = null;
        boolean flag=false;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(DB_URL, "vermaa0974", "1653836");
            st = conn.createStatement();
            
            rs=st.executeQuery("select * from fbuseraccount");
            while(rs.next())
            {
                users.add(new FBUserAccount(rs.getString("acctID"),rs.getString("password"),rs.getString("name"),rs.getString("school"),rs.getInt("gender"),rs.getString("dob")));
            }
            rs=st.executeQuery("select * from fbuseraccount where acctID='"+acctID+"'");
            if(rs.next()){
              flag=true;
           }
          
            
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(FriendsBook.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                conn.close();
                st.close();
                //rs.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            if(flag)
            {
                 return "searchFriend";
            }
            else{
           return "ErrorPage";
            }
       }
       }
     public static int isValid(String acctID) {
        //access the database and then login
        final String DB_URL = "jdbc:mysql://mis-sql.uhcl.edu/vermaa0974";
        Connection conn = null;
        Statement st = null;
        ResultSet rs = null;

        try {
             Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(DB_URL, "vermaa0974", "1653836");
            st = conn.createStatement();

            rs = st.executeQuery("select * from FBUserAccount where acctID = '" + acctID + "'");
            if (rs.next()) {
                return 2;
            }
            if (acctID.length() < 3 || acctID.length() > 10) {
                return 1;
            }
            boolean containsDigits = false;
            for (int i = 0; i < acctID.length(); i++) {
                if (acctID.charAt(i) >= '0' && acctID.charAt(i) <= '9') {
                    containsDigits = true;
                }
            }
            if (containsDigits == false) {
                return 1;
            }
            boolean containsSpecChar = false;
            {
                for (int i = 0; i < acctID.length(); i++) {
                    if (acctID.charAt(i) == '#' || acctID.charAt(i) == '?' || acctID.charAt(i) == '!' || acctID.charAt(i) == '*') {
                        containsSpecChar = true;
                    }
                }
                if (containsSpecChar == false) {
                    return 1;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(FriendsBook.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                conn.close();
                st.close();
                rs.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return 0;
    }
     
    public String redirectToMessages(){
        this.newMessageCount = 0;
        return "messages";
    }

    public String logout(){
        this.account = null;
        this.notification = null;
        this.friends= null;
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        return "index";
        
    }
}
