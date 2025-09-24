package org.bjm.mbeans;

import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
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
import org.bjm.ejbs.UserServiceEjbLocal;
import org.bjm.entities.Access;
import org.bjm.entities.SurveyFromForumAbuse;
import org.bjm.entities.SurveyFromForumVote;

/**
 *
 * @author singh
 */
@Named(value = "reportAbuseSurveyFromForumMBean")
@ViewScoped
public class ReportAbuseSurveyFromForumMBean implements Serializable {
    
    private static final Logger LOGGER = Logger.getLogger(ReportAbuseSurveyFromForumMBean.class.getName());
    
    private SurveyFromForumVote  surveyFromForumVote;
    private SurveyFromForumAbuse surveyFromForumAbuse;
    
    @Inject
    private UserServiceEjbLocal userServiceEjbLocal;
    
    @PostConstruct
    public void init(){
        surveyFromForumAbuse=new SurveyFromForumAbuse();
        HttpServletRequest request=(HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String voteId=request.getParameter("voteId");
        surveyFromForumVote=userServiceEjbLocal.findBySurveyFromForumVoteById(Integer.getInteger(voteId));
        surveyFromForumAbuse.setSurveyFromForumVoteId(surveyFromForumVote.getId());
        HttpSession session=request.getSession();
        Access access=(Access) session.getAttribute("access");
        surveyFromForumAbuse.setReportedByAccessId(access.getId());
        surveyFromForumAbuse.setReportedByEmail(access.getEmail());
        LOGGER.info("SurveyAbuse initialised.");
    }
    
    public String reportAbuse(){
        ServletContext servletContext=(ServletContext)FacesContext.getCurrentInstance().getExternalContext().getContext();
        surveyFromForumAbuse.setReportedOn(Timestamp.valueOf(LocalDateTime.now(ZoneId.of(servletContext.getInitParameter("ZoneId")))));
        surveyFromForumAbuse=userServiceEjbLocal.createSurveyFromForumAbuse(surveyFromForumAbuse);
        LOGGER.info(String.format("surveyFromForumAbuse created with ID: %d", surveyFromForumAbuse.getId()));
        FacesContext.getCurrentInstance().addMessage("",new FacesMessage(FacesMessage.SEVERITY_INFO, "Abuse reported", "Abuse reported"));
        return null;
    }

    public SurveyFromForumVote getSurveyFromForumVote() {
        return surveyFromForumVote;
    }

    public void setSurveyFromForumVote(SurveyFromForumVote surveyFromForumVote) {
        this.surveyFromForumVote = surveyFromForumVote;
    }

    public SurveyFromForumAbuse getSurveyFromForumAbuse() {
        return surveyFromForumAbuse;
    }

    public void setSurveyFromForumAbuse(SurveyFromForumAbuse surveyFromForumAbuse) {
        this.surveyFromForumAbuse = surveyFromForumAbuse;
    }
    
    
    
    
}
