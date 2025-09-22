package org.bjm.mbeans;

import jakarta.annotation.PostConstruct;
import jakarta.faces.context.FacesContext;
import jakarta.faces.flow.FlowScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.logging.Logger;
import org.bjm.ejbs.EmailServiceEjbLocal;
import org.bjm.ejbs.UserServiceEjbLocal;
import org.bjm.entities.Access;
import org.bjm.entities.Activity;
import org.bjm.entities.ActivityType;
import org.bjm.entities.Forum;
import org.bjm.entities.SurveyFromForum;

/**
 *
 * @author singh
 */
@Named(value = "createSurveyFromForumMBean")
@FlowScoped(value = "CreateSurveyFromForum")
public class CreateSurveyFromForumMBean implements Serializable {
    
    private static final Logger LOGGER= Logger.getLogger(CreateSurveyFromForumMBean.class.getName());
    private Forum forum;
    private SurveyFromForum surveyFromForum;
    
    @Inject
    private ActivityMBean activityMBean;
    @Inject
    private UserServiceEjbLocal userServiceEjbLocal;
    @Inject
    private EmailServiceEjbLocal emailServiceEjbLocal;
    
    @PostConstruct
    public void init(){
        HttpServletRequest request=(HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String forumId=request.getParameter("forumId");
        forum = userServiceEjbLocal.getUserForum(Integer.getInteger(forumId));
        surveyFromForum=new SurveyFromForum();
        surveyFromForum.setCategoryType(forum.getCategoryType());
        surveyFromForum.setCategorySubType(forum.getCategorySubType());
        surveyFromForum.setForumId(forum.getId());
        surveyFromForum.setTitle(forum.getTitle());
        surveyFromForum.setDescription(forum.getDescription());
        LOGGER.info(String.format("Forum loaded with ID: %s", forum.getId()));
    }
    
    public String prepareSurvey(){
        return "CreateSurveyFromForumConfirm?faces-redirect=true";
    }
    
    public String amendSurvey(){
        return "CreateSurveyFromForum?faces-redirect=true";
    }
    
    private void submitSurvey(){
        HttpServletRequest request=(HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        HttpSession session= request.getSession();
        Access access=(Access) session.getAttribute("access");
        ServletContext servletContext=(ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
        surveyFromForum.setSurveyCreatorEmail(access.getEmail());
        surveyFromForum.setCreatedOn(Timestamp.valueOf(LocalDateTime.now(ZoneId.of(servletContext.getInitParameter("zoneId")))));
        surveyFromForum.setUpdatedOn(Timestamp.valueOf(LocalDateTime.now(ZoneId.of(servletContext.getInitParameter("zoneId")))));
        surveyFromForum=userServiceEjbLocal.createUserSurveyFromForum(surveyFromForum);
        LOGGER.info(String.format("New SurveyFromForum created with ID: %d", surveyFromForum.getId()));
        
        //Activity now
        Activity activity=new Activity();
        activity.setActivityType(ActivityType.SURVEY_FROM_FORUM_CREATED.toString());
        activity.setActivityId(surveyFromForum.getId());
        activity.setDescription("Survey From Forum Created "+surveyFromForum.getTitle());
        activity.setOwnerEmail(access.getEmail());
        activity.setCreatedOn(Timestamp.valueOf(LocalDateTime.now(ZoneId.of(servletContext.getInitParameter("zoneId")))));
        activityMBean.addActivity(activity);
        //Last Step - send Email
        emailServiceEjbLocal.sendSurveyCreatedFromForumEmail(access, surveyFromForum);
        surveyFromForum=new SurveyFromForum();//To erase the previous data as we are working in a session.
    }
    
    public String getReturnValue(){
        submitSurvey();
        return "/flowreturns/CreateSurveyFromForum-return?faces-redirect=true";
    }

    public Forum getForum() {
        return forum;
    }

    public void setForum(Forum forum) {
        this.forum = forum;
    }

    public SurveyFromForum getSurveyFromForum() {
        return surveyFromForum;
    }

    public void setSurveyFromForum(SurveyFromForum surveyFromForum) {
        this.surveyFromForum = surveyFromForum;
    }
    
    
    
    
    
}
