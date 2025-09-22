package org.bjm.mbeans;

import jakarta.annotation.PostConstruct;
import jakarta.faces.context.FacesContext;
import jakarta.faces.event.AjaxBehaviorEvent;
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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;
import org.bjm.entities.Access;
import org.bjm.entities.Activity;
import org.bjm.entities.ActivityType;
import org.bjm.entities.Survey;
import org.bjm.entities.SurveyCategory;
import org.bjm.dtos.SurveyDto;
import org.bjm.ejbs.EmailServiceEjbLocal;
import org.bjm.ejbs.ReferenceDataServiceEjbLocal;
import org.bjm.ejbs.UserServiceEjbLocal;

/**
 *
 * @author singh
 */
@Named(value = "createSurveyMBean")
@FlowScoped(value = "CreateSurvey")
public class CreateSurveyMBean implements Serializable {
    
    private static final Logger LOGGER = Logger.getLogger(CreateSurveyMBean.class.getName());
    
    private SurveyDto surveyDto;
    
    @Inject
    private ActivityMBean activityMBean;
    
    @Inject        
    private UserServiceEjbLocal userServiceEjbLocal;
    @Inject
    private ReferenceDataServiceEjbLocal referenceDataServiceEjbLocal;
    @Inject
    private EmailServiceEjbLocal emailServiceEjbLocal;
    
    @PostConstruct
    public void init(){
        surveyDto = new SurveyDto();
        surveyDto.setSurveyCategoryMap(new HashMap<>());
        surveyDto.getSurveyCategoryMap().put("--Select One--", null);
        List<SurveyCategory> surveyCategoryList =referenceDataServiceEjbLocal.getSurveyCategories();
        for(SurveyCategory sc: surveyCategoryList){
            Set<String> mapKey=surveyDto.getSurveyCategoryMap().keySet();
            if(!mapKey.contains(sc.getType())){
                Set<String> valueSet=new HashSet<>();
                valueSet.add("--Select One--");
                valueSet.add(sc.getSubType());
                surveyDto.getSurveyCategoryMap().put(sc.getType(), valueSet);
            }else{
                Set<String> valueSet=surveyDto.getSurveyCategoryMap().get(sc.getType());
                valueSet.add(sc.getSubType());
            }
        }
        surveyDto.setCategoryTypes(surveyDto.getSurveyCategoryMap().keySet());
        LOGGER.info(String.format("SurveyDto surveyCatgoryMap populated with %d records", surveyDto.getSurveyCategoryMap().size()));
    }
    
    public void ajaxTypeListener(AjaxBehaviorEvent abe){
        surveyDto.setCategorySubTypes(surveyDto.getSurveyCategoryMap().get(surveyDto.getCategoryType()));
    }
    
    public String prepareSurvey(){
        return "CreateSurveyConfirm?faces-redirect=true";
    }

    public String amendSurvey(){
        return "CreateSurvey?faces-redirect=true";
    }
    
    private void submitSurvey(){
        Survey survey=new Survey();
        survey.setCategoryType(surveyDto.getCategoryType());
        survey.setCategorySubType(surveyDto.getCategorySubType());
        survey.setTitle(surveyDto.getTitle());
        survey.setDescription(surveyDto.getDescription());
        HttpServletRequest request=(HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        HttpSession session= request.getSession();
        Access access=(Access) session.getAttribute("access");
        survey.setSurveyCreatorEmail(access.getEmail());
        ServletContext servletContext=(ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
        survey.setCreatedOn(Timestamp.valueOf(LocalDateTime.now(ZoneId.of(servletContext.getInitParameter("zoneId")))));
        survey.setUpdatedOn(Timestamp.valueOf(LocalDateTime.now(ZoneId.of(servletContext.getInitParameter("zoneId")))));
        
        survey=userServiceEjbLocal.createUserSurvey(survey);
        LOGGER.info(String.format("Survey created with ID: %s",survey.getId()));
        
        //Activity now
        Activity activity=new Activity();
        activity.setActivityType(ActivityType.SURVEY_CREATED.toString());
        activity.setActivityId(survey.getId());
        activity.setDescription("Survey Created "+survey.getTitle());
        activity.setOwnerEmail(access.getEmail());
        activity.setCreatedOn(Timestamp.valueOf(LocalDateTime.now(ZoneId.of(servletContext.getInitParameter("zoneId")))));
        activityMBean.addActivity(activity);
        
        //Last Step - send Email
        emailServiceEjbLocal.sendSurveyCreatedEmail(access, survey);
        
    }
    
    public String getReturnValue(){
        submitSurvey();
        return "/flowreturns/CreateSurvey-return?faces-redirect=true";
    }
    
    public SurveyDto getSurveyDto() {
        return surveyDto;
    }

    public void setSurveyDto(SurveyDto surveyDto) {
        this.surveyDto = surveyDto;
    }
    
    
    
    
    
}
