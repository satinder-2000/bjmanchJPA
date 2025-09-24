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
import org.bjm.entities.SurveyAbuse;
import org.bjm.entities.SurveyVote;


/**
 *
 * @author singh
 */
@Named(value = "reportAbuseSurveyMBean")
@ViewScoped
public class ReportAbuseSurveyMBean implements Serializable {
    
    private static final Logger LOGGER = Logger.getLogger(ReportAbuseSurveyMBean.class.getName());
    
    private SurveyVote surveyVote;
    private SurveyAbuse surveyAbuse;
    
    @Inject
    private UserServiceEjbLocal userServiceEjbLocal;
    
    @PostConstruct
    public void init(){
        surveyAbuse=new SurveyAbuse();
        HttpServletRequest request=(HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String voteId=request.getParameter("voteId");
        surveyVote=userServiceEjbLocal.findBySurveyVoteById(Integer.getInteger(voteId));
        surveyAbuse.setSurveyVoteId(surveyVote.getId());
        HttpSession session=request.getSession();
        Access access=(Access) session.getAttribute("access");
        surveyAbuse.setReportedByAccessId(access.getId());
        surveyAbuse.setReportedByEmail(access.getEmail());
        LOGGER.info("SurveyAbuse initialised.");
    }
    
    public String reportAbuse(){
        ServletContext servletContext=(ServletContext)FacesContext.getCurrentInstance().getExternalContext().getContext();
        surveyAbuse.setReportedOn(Timestamp.valueOf(LocalDateTime.now(ZoneId.of(servletContext.getInitParameter("ZoneId")))));
        surveyAbuse=userServiceEjbLocal.createSurveyAbuse(surveyAbuse);
        LOGGER.info(String.format("SurveyAbuse created with ID: %d", surveyAbuse.getId()));
        FacesContext.getCurrentInstance().addMessage("",new FacesMessage(FacesMessage.SEVERITY_INFO, "Abuse reported", "Abuse reported"));
        return null;
    }

    public SurveyVote getSurveyVote() {
        return surveyVote;
    }

    public void setSurveyVote(SurveyVote surveyVote) {
        this.surveyVote = surveyVote;
    }

    public SurveyAbuse getSurveyAbuse() {
        return surveyAbuse;
    }

    public void setSurveyAbuse(SurveyAbuse surveyAbuse) {
        this.surveyAbuse = surveyAbuse;
    }
    
    
    
    
    
}
