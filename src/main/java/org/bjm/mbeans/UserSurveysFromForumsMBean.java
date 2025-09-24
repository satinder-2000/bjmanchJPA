package org.bjm.mbeans;

import jakarta.annotation.PostConstruct;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;
import org.bjm.ejbs.UserServiceEjbLocal;
import org.bjm.entities.Access;
import org.bjm.entities.Survey;
import org.bjm.entities.SurveyFromForum;

/**
 *
 * @author singh
 */
@Named(value = "userSurveysFromForumsMBean")
@ViewScoped
public class UserSurveysFromForumsMBean implements Serializable {
    
    private static final Logger LOGGER = Logger.getLogger(UserSurveysFromForumsMBean.class.getName());
    
    @Inject
    private UserServiceEjbLocal userServiceEjbLocal;
    
    private List<SurveyFromForum> userSurveysFromForums;
    
    @PostConstruct
    public void init(){
        HttpServletRequest request=(HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        HttpSession session= request.getSession();
        Access access=(Access) session.getAttribute("access");
        userSurveysFromForums=userServiceEjbLocal.getUserSurveyFromForums(access.getEmail());
        LOGGER.info(String.format("User %s has %d SurveysFromForums", access.getEmail(), userSurveysFromForums.size()));
    }

    public List<SurveyFromForum> getUserSurveysFromForums() {
        return userSurveysFromForums;
    }

    public void setUserSurveysFromForums(List<SurveyFromForum> userSurveysFromForums) {
        this.userSurveysFromForums = userSurveysFromForums;
    }
    
    
    
    
}
