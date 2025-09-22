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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import org.bjm.ejbs.SurveyServiceEjbLocal;
import org.bjm.ejbs.UserServiceEjbLocal;
import org.bjm.entities.Access;
import org.bjm.entities.Survey;
import org.bjm.entities.SurveyVote;
import org.bjm.utils.BjmConstants;
import org.bjm.utils.ImageVO;

/**
 *
 * @author singh
 */
@Named(value = "surveyDetailsMBean")
@ViewScoped
public class SurveyDetailsMBean implements Serializable{
    
    private static final Logger LOGGER=Logger.getLogger(SurveyDetailsMBean.class.getName());
    private int commentChars;
    private Survey survey;
    private String userComment;
    private List<SurveyVote> otherSurveyVotes;
    
    @Inject
    private SurveyServiceEjbLocal surveyServiceEjbLocal;
    @Inject
    private UserServiceEjbLocal userServiceEjbLocal;
    
    @PostConstruct
    public void init(){
        HttpServletRequest request=(HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String surveyIdStr=request.getParameter("surveyId");
        int surveyId=Integer.getInteger(surveyIdStr);
        survey = surveyServiceEjbLocal.findById(surveyId);
        LOGGER.info(String.format("Survey with ID: %d Loaded successfully.", survey.getId()));
        
        Access surveyCreator=userServiceEjbLocal.getAccessByEmail(survey.getSurveyCreatorEmail());
        HttpSession session=request.getSession();
        session.setAttribute(BjmConstants.FORUM_CREATOR_ACCESS, surveyCreator);
        LOGGER.info(String.format("Survey with ID: %s Loaded successfully.", survey.getId()));
    }
    
    public String postSurveyVote(){
        if(userComment==null || userComment.trim().isEmpty()){
           FacesContext.getCurrentInstance().addMessage("usercomment", new FacesMessage(FacesMessage.SEVERITY_ERROR, "No comment entered", "No comment entered"));
        }else{
            ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
            SurveyVote surveyVote = new SurveyVote();
            surveyVote.setComment(userComment);
            surveyVote.setSurveyId(survey.getId());
            HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
            HttpSession session = request.getSession();
            Access access = (Access) session.getAttribute("access");
            surveyVote.setSurveyVoterAccessId(access.getId());
            surveyVote.setSurveyVoterEmail(access.getEmail());
            surveyVote.setDated(Timestamp.valueOf(LocalDateTime.now(ZoneId.of(servletContext.getInitParameter("ZoneId")))));
            surveyVote=surveyServiceEjbLocal.postSurveyVote(surveyVote);
            LOGGER.info(String.format("New SurveyVote added with ID: %d", surveyVote.getId()));
            FacesContext.getCurrentInstance().addMessage("usercomment", new FacesMessage(FacesMessage.SEVERITY_INFO, "Comment added successfully!!", "Comment added successfully!!"));
        }
        return null;
    }
    
    private void loadOtherSurveyVotes(){
        
        List<SurveyVote> otherSurveyVotes=surveyServiceEjbLocal.getAllVotesOnSurvey(survey.getId());
        Map<Integer, ImageVO> surveyVoteerImageMap=new HashMap<>();
        for(SurveyVote sv: otherSurveyVotes){
            Access surveyVoterAccess= userServiceEjbLocal.getAccessById(sv.getSurveyVoterAccessId());
            String imageType=surveyVoterAccess.getProfileFile().substring(surveyVoterAccess.getProfileFile().indexOf('.')+1);
            ImageVO surveyVoterImageVO=new ImageVO(imageType, surveyVoterAccess.getImage());
            surveyVoteerImageMap.put(surveyVoterAccess.getId(), surveyVoterImageVO);
        }
        HttpServletRequest request=(HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        HttpSession session=request.getSession();
        session.setAttribute(BjmConstants.SURVEY_VOTER_IMAGE_MAP, surveyVoteerImageMap);
        LOGGER.info(String.format("Count of other Survey Comments for Survey ID: %d is : %d", survey.getId(), otherSurveyVotes.size()));
    }

    public Survey getSurvey() {
        return survey;
    }

    public void setSurvey(Survey survey) {
        this.survey = survey;
    }

    public String getUserComment() {
        return userComment;
    }

    public void setUserComment(String userComment) {
        this.userComment = userComment;
    }

    public int getCommentChars() {
        return commentChars;
    }

    public void setCommentChars(int commentChars) {
        this.commentChars = commentChars;
    }
    
    public List<SurveyVote> getOtherSurveyVotes(){
        loadOtherSurveyVotes();
        return otherSurveyVotes;
    }
    
    public void setOtherSurveyVotes(List<SurveyVote> otherSurveyVotes){
        this.otherSurveyVotes=otherSurveyVotes;
    }
    
    
    
    
}
