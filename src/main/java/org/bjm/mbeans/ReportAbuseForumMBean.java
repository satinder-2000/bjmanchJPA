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
import org.bjm.entities.ForumAbuse;
import org.bjm.entities.ForumComment;
/**
 *
 * @author singh
 */
@Named(value = "reportAbuseForumMBean")
@ViewScoped
public class ReportAbuseForumMBean implements Serializable{
    
    private static final Logger LOGGER = Logger.getLogger(ReportAbuseForumMBean.class.getName());
    
    private ForumComment forumComment;
    private ForumAbuse forumAbuse;
    
    @Inject
    private UserServiceEjbLocal userServiceEjbLocal;
    
    @PostConstruct
    public void init(){
        forumAbuse=new ForumAbuse();
        HttpServletRequest request=(HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String commentId=request.getParameter("commentId");
        forumComment = userServiceEjbLocal.findByForumCommentById(Integer.getInteger(commentId));
        forumAbuse.setForumCommentId(forumComment.getId());
        HttpSession session=request.getSession();
        Access access=(Access) session.getAttribute("access");
        forumAbuse.setReportedByAccessId(access.getId());
        forumAbuse.setReportedByEmail(access.getEmail());
        LOGGER.info("ForumAbuse initialised.");
    }
    
    public String reportAbuse(){
        ServletContext servletContext=(ServletContext)FacesContext.getCurrentInstance().getExternalContext().getContext();
        forumAbuse.setReportedOn(Timestamp.valueOf(LocalDateTime.now(ZoneId.of(servletContext.getInitParameter("ZoneId")))));
        forumAbuse=userServiceEjbLocal.createForumAbuse(forumAbuse);
        LOGGER.info(String.format("ForumAbuse created with ID: %d", forumAbuse.getId()));
        FacesContext.getCurrentInstance().addMessage("",new FacesMessage(FacesMessage.SEVERITY_INFO, "Abuse reported", "Abuse reported"));
        return null;
    }

    public ForumComment getForumComment() {
        return forumComment;
    }

    public void setForumComment(ForumComment forumComment) {
        this.forumComment = forumComment;
    }

    
    public ForumAbuse getForumAbuse() {
        return forumAbuse;
    }

    public void setForumAbuse(ForumAbuse forumAbuse) {
        this.forumAbuse = forumAbuse;
    }
    
    
    
}
