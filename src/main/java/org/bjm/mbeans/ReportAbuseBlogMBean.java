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
import org.bjm.entities.BlogAbuse;
import org.bjm.entities.BlogComment;

/**
 *
 * @author singh
 */
@Named(value = "reportAbuseBlogMBean")
@ViewScoped
public class ReportAbuseBlogMBean implements Serializable {
    
    private static final Logger LOGGER = Logger.getLogger(ReportAbuseBlogMBean.class.getName());
    
    private BlogComment blogComment;
    private BlogAbuse blogAbuse;
    
    @Inject
    private UserServiceEjbLocal userServiceEjbLocal;
    
    @PostConstruct
    public void init(){
        blogAbuse=new BlogAbuse();
        HttpServletRequest request=(HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String commentId=request.getParameter("commentId");
        blogComment=userServiceEjbLocal.findByBlogCommentById(Integer.getInteger(commentId));
        blogAbuse.setBlogCommentId(blogComment.getId());
        HttpSession session=request.getSession();
        Access access=(Access) session.getAttribute("access");
        blogAbuse.setReportedByAccessId(access.getId());
        blogAbuse.setReportedByEmail(access.getEmail());
        LOGGER.info("BlogAbuse initialised.");
    
    }
    
    public String reportAbuse(){
        ServletContext servletContext=(ServletContext)FacesContext.getCurrentInstance().getExternalContext().getContext();
        blogAbuse.setReportedOn(Timestamp.valueOf(LocalDateTime.now(ZoneId.of(servletContext.getInitParameter("ZoneId")))));
        blogAbuse=userServiceEjbLocal.createBlogAbuse(blogAbuse);
        LOGGER.info(String.format("BlogAbuse created with ID: %d", blogAbuse.getId()));
        FacesContext.getCurrentInstance().addMessage("",new FacesMessage(FacesMessage.SEVERITY_INFO, "Abuse reported", "Abuse reported"));
        return null;
    }
    

    public BlogComment getBlogComment() {
        return blogComment;
    }

    public void setBlogComment(BlogComment blogComment) {
        this.blogComment = blogComment;
    }

    public BlogAbuse getBlogAbuse() {
        return blogAbuse;
    }

    public void setBlogAbuse(BlogAbuse blogAbuse) {
        this.blogAbuse = blogAbuse;
    }
    
    
    
}
