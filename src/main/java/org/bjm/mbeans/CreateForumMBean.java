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
import org.bjm.entities.Forum;
import org.bjm.entities.ForumCategory;
import org.bjm.dtos.ForumDto;
import org.bjm.ejbs.EmailServiceEjbLocal;
import org.bjm.ejbs.ReferenceDataServiceEjbLocal;
import org.bjm.ejbs.UserServiceEjbLocal;

/**
 *
 * @author singh
 */
@Named(value = "createForumMBean")
@FlowScoped(value = "CreateForum")
public class CreateForumMBean implements Serializable {
    
    private static final Logger LOGGER = Logger.getLogger(CreateForumMBean.class.getName());
    
    private ForumDto forumDto;
    @Inject
    private ActivityMBean activityMBean;
    @Inject        
    private UserServiceEjbLocal userServiceEjbLocal;
    @Inject
    private ReferenceDataServiceEjbLocal referenceDataServiceEjbLocal;
    @Inject
    private EmailServiceEjbLocal emailServiceEjbLocal;
    
    private int forumDescriptionChars;
    
    @PostConstruct
    public void init(){
        forumDto = new ForumDto();
        forumDto.setForumCategoryMap(new HashMap<>());
        forumDto.getForumCategoryMap().put("--Select One--", null);
        List<ForumCategory> forumCategoryList =referenceDataServiceEjbLocal.getForumCategories();
        for(ForumCategory fc: forumCategoryList){
            Set<String> mapKey=forumDto.getForumCategoryMap().keySet();
            if(!mapKey.contains(fc.getType())){
                Set<String> valueSet=new HashSet<>();
                valueSet.add("--Select One--");
                valueSet.add(fc.getSubType());
                forumDto.getForumCategoryMap().put(fc.getType(), valueSet);
            }else{
                Set<String> valueSet=forumDto.getForumCategoryMap().get(fc.getType());
                valueSet.add(fc.getSubType());
            }
        }
        forumDto.setCategoryTypes(forumDto.getForumCategoryMap().keySet());
        LOGGER.info(String.format("ForumDto forumCatgoryMap populated with %d records", forumDto.getForumCategoryMap().size()));
    }
    
    public void ajaxTypeListener(AjaxBehaviorEvent abe){
        forumDto.setCategorySubTypes(forumDto.getForumCategoryMap().get(forumDto.getCategoryType()));
    }
    
    public String prepareForum(){
        return "CreateForumConfirm?faces-redirect=true";
    }
    
    public String amendForum(){
        return "CreateForum?faces-redirect=true";
    }
    
    private  void submitForum(){
        Forum forum=new Forum();
        forum.setCategoryType(forumDto.getCategoryType());
        forum.setCategorySubType(forumDto.getCategorySubType());
        forum.setTitle(forumDto.getTitle());
        forum.setDescription(forumDto.getDescription());
        HttpServletRequest request=(HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        HttpSession session=request.getSession();
        Access access=(Access) session.getAttribute("access");
        forum.setForumCreatorEmail(access.getEmail());
        ServletContext servletContext=(ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
        forum.setCreatedOn(Timestamp.valueOf(LocalDateTime.now(ZoneId.of(servletContext.getInitParameter("zoneId")))));
        forum.setUpdatedOn(Timestamp.valueOf(LocalDateTime.now(ZoneId.of(servletContext.getInitParameter("zoneId")))));
                
        forum = userServiceEjbLocal.createUserForum(forum);
        
        LOGGER.info(String.format("Forum created with ID: %s",forum.getId()));
        //Activity now
        Activity activity=new Activity();
        activity.setActivityType(ActivityType.FORUM_CREATED.toString());
        activity.setActivityId(forum.getId());
        activity.setDescription("Forum Created "+forum.getTitle());
        activity.setOwnerEmail(access.getEmail());
        activity.setCreatedOn(Timestamp.valueOf(LocalDateTime.now(ZoneId.of(servletContext.getInitParameter("zoneId")))));
        activityMBean.addActivity(activity);
        //Last Step - send Email
        emailServiceEjbLocal.sendForumCreatedEmail(access, forum);
        forumDto=new ForumDto();//To erase the previous data as we are working in a session.
    }
    
    public String getReturnValue(){
        submitForum();
        return "/flowreturns/CreateForum-return?faces-redirect=true";
    }
    
    public ForumDto getForumDto() {
        return forumDto;
    }

    public void setForumDto(ForumDto forumDto) {
        this.forumDto = forumDto;
    }

    public int getForumDescriptionChars() {
        return forumDescriptionChars;
    }

    public void setForumDescriptionChars(int forumDescriptionChars) {
        this.forumDescriptionChars = forumDescriptionChars;
    }
    
    
    
    
    
    
}
