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
import org.bjm.entities.Blog;
import org.bjm.entities.Forum;
import org.bjm.entities.Survey;
import org.bjm.entities.SurveyFromForum;

/**
 *
 * @author singh
 */
@Named(value = "homeMBean")
@ViewScoped
public class HomeMBean implements Serializable {
    
    private static final Logger LOGGER=Logger.getLogger(HomeMBean.class.getName());
    
    @Inject
    private UserServiceEjbLocal userServiceEjbLocal;
    
    private List<Forum> userForums;
    private List<Survey> userSurveys;
    private List<SurveyFromForum> userSurveysFromForums;
    private List<Blog> userBlogs;
    
    @PostConstruct
    public void init(){
        HttpServletRequest request= (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
        HttpSession session=request.getSession();
        Access access=(Access) session.getAttribute("access");
        
        userForums=userServiceEjbLocal.getUserForums(access.getEmail());
        userSurveys=userServiceEjbLocal.getUserSurveys(access.getEmail());
        userSurveysFromForums=userServiceEjbLocal.getUserSurveyFromForums(access.getEmail());
        userBlogs=userServiceEjbLocal.getUserBlogs(access.getEmail());
        LOGGER.info(String.format("Count of Forums by User %s is %d", access.getEmail(), userForums.size()));
        LOGGER.info(String.format("Count of Surveys by User %s is %d", access.getEmail(), userSurveys.size()));
        LOGGER.info(String.format("Count of SurveysFromForums by User %s is %d", access.getEmail(), userSurveys.size()));
        LOGGER.info(String.format("Count of Blogs by User %s is %d", access.getEmail(), userBlogs.size()));
        
    }
    
    
    
    public String goHome(){
        return "/home/userHome?faces-redirect=true";
    }

    public List<Forum> getUserForums() {
        return userForums;
    }

    public void setUserForums(List<Forum> userForums) {
        this.userForums = userForums;
    }

    public List<Survey> getUserSurveys() {
        return userSurveys;
    }

    public void setUserSurveys(List<Survey> userSurveys) {
        this.userSurveys = userSurveys;
    }

    public List<SurveyFromForum> getUserSurveysFromForums() {
        return userSurveysFromForums;
    }

    public void setUserSurveysFromForums(List<SurveyFromForum> userSurveysFromForums) {
        this.userSurveysFromForums = userSurveysFromForums;
    }

    public List<Blog> getUserBlogs() {
        return userBlogs;
    }

    public void setUserBlogs(List<Blog> userBlogs) {
        this.userBlogs = userBlogs;
    }
    
    
    
}
