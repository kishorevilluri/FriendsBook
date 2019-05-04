/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.friendsbook.images;

import com.friendsbook.user.User;
import com.friendsbook.user.UserDaoImpl;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseId;
import javax.inject.Named;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

/**
 *
 * @author V H S Kishore
 */
@ManagedBean
@SessionScoped
public class UserImages {
    
    //@ManagedProperty("#{userDaoImpl}")
    private UserDaoImpl userDao;
    //private StreamedContent image;
    
    public StreamedContent getImage() throws IOException {
        FacesContext context = FacesContext.getCurrentInstance();
        
        String userId = context.getExternalContext().getRequestParameterMap().get("userPhotoId");
        if(userId != null && !userId.equals("")){
            User user = userDao.getUserImage(userId);
            return new DefaultStreamedContent(new ByteArrayInputStream(user.getUserImage()));
        }else{
            return new DefaultStreamedContent();
        }
    }
    
}
