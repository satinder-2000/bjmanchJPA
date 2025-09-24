package org.bjm.mbeans;

import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.event.AjaxBehaviorEvent;
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
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import org.bjm.ejbs.BlogServiceEjbLocal;
import org.bjm.ejbs.UserServiceEjbLocal;
import org.bjm.entities.Access;
import org.bjm.entities.Blog;
import org.bjm.entities.BlogComment;
import org.bjm.utils.BjmConstants;
import org.bjm.utils.ImageVO;

/**
 *
 * @author singh
 */
@Named(value = "blogDetailsMBean")
@ViewScoped
public class BlogDetailsMBean implements Serializable {
    
    private static final Logger LOGGER=Logger.getLogger(BlogDetailsMBean.class.getName());
    
    private Blog blog;
    private String blogPublishedOn;
    private BlogComment blogComment;
    private List<BlogComment> otherBlogComments;
    
    @Inject
    private BlogServiceEjbLocal blogServiceEjbLocal;
    
    @Inject
    private UserServiceEjbLocal userServiceEjbLocal;
    
    
    
    @PostConstruct
    public void init(){
        HttpServletRequest request=(HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String blogIdStr=request.getParameter("blogId");
        int blogId=Integer.getInteger(blogIdStr);
        blog = blogServiceEjbLocal.findByBlogId(blogId);
        blogComment=new BlogComment();
    }
    
    public String postComment(){
        HttpServletRequest request=(HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        HttpSession session=request.getSession();
        Access access=(Access) session.getAttribute("access");
        blogComment.setBlogCommenterAccessId(access.getId());
        blogComment.setBlogCommenterEmail(access.getEmail());
        ServletContext servletContext=(ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
        blogComment.setDated(Timestamp.valueOf(LocalDateTime.now(ZoneId.of(servletContext.getInitParameter("ZoneId")))));
        blogComment.setBlogId(blog.getId());
        blogComment = blogServiceEjbLocal.postBlogComment(blogComment);
        LOGGER.info(String.format("BlogComment created with ID: %d", blogComment.getId()));
        FacesContext.getCurrentInstance().addMessage("", new FacesMessage(FacesMessage.SEVERITY_INFO, "Comment posted successfully","Comment posted successfully"));
        return null;
    }
    
    private void loadOtherBlogComments(){
        otherBlogComments=blogServiceEjbLocal.findAllCommentsOnBlog(blog.getId());
        LOGGER.info(String.format("Count of other Blog Comments for Blog ID: %s is : %d", blog.getId(), otherBlogComments.size()));
        Map<Integer, ImageVO> blogCommenterImageMap=new HashMap<>();
        for (BlogComment bc: otherBlogComments){
            Access blogCommenterAccess = userServiceEjbLocal.getAccessById(bc.getBlogCommenterAccessId());
            String imgType=blogCommenterAccess.getProfileFile().substring(blogCommenterAccess.getProfileFile().indexOf('.')+1);
            ImageVO blogCommenterImageVO=new ImageVO(imgType, blogCommenterAccess.getImage());
            blogCommenterImageMap.put(blogCommenterAccess.getId(), blogCommenterImageVO);
        }
        HttpServletRequest request= (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();
        HttpSession session=request.getSession();
        session.setAttribute(BjmConstants.BLOG_COMMENTER_IMAGE_MAP, blogCommenterImageMap);
    }

    public Blog getBlog() {
        return blog;
    }

    public void setBlog(Blog blog) {
        this.blog = blog;
    }
    
    

    public String getBlogPublishedOn() {
        return blogPublishedOn;
    }

    public void setBlogPublishedOn(String blogPublishedOn) {
        this.blogPublishedOn = blogPublishedOn;
    }

    
    
    public BlogComment getBlogComment() {
        return blogComment;
    }

    public void setBlogComment(BlogComment blogComment) {
        this.blogComment = blogComment;
    }

    public List<BlogComment> getOtherBlogComments() {
        loadOtherBlogComments();
        return otherBlogComments;
    }

    public void setOtherBlogComments(List<BlogComment> otherBlogComments) {
        this.otherBlogComments = otherBlogComments;
    }
    
    
    
}
