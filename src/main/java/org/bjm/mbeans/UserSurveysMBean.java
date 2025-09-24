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
import org.bjm.entities.Forum;
import org.bjm.entities.Survey;

/**
 *
 * @author singh
 */
@Named(value = "userSurveysMBean")
@ViewScoped
public class UserSurveysMBean implements Serializable {
    
    private static final Logger LOGGER = Logger.getLogger(UserSurveysMBean.class.getName());
    
    @Inject
    private UserServiceEjbLocal userServiceEjbLocal;
    
    private List<Survey> userSurveys;
    
    @PostConstruct
    public void init(){
        HttpServletRequest request=(HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        HttpSession session=request.getSession();
        Access access=(Access)session.getAttribute("access");
        userSurveys= userServiceEjbLocal.getUserSurveys(access.getEmail());
        LOGGER.info(String.format("User %s has %d Surveys", access.getEmail(), userSurveys.size()));
    }

    public List<Survey> getUserSurveys() {
        return userSurveys;
    }

    public void setUserSurveys(List<Survey> userSurveys) {
        this.userSurveys = userSurveys;
    }
    
    
    
    
}
