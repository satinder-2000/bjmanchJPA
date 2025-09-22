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
import org.bjm.ejbs.ForumServiceEjbLocal;
import org.bjm.ejbs.UserServiceEjbLocal;
import org.bjm.entities.Access;
import org.bjm.entities.Forum;
import org.bjm.entities.ForumComment;
import org.bjm.utils.BjmConstants;
import org.bjm.utils.ImageVO;

/**
 *
 * @author singh
 */
@Named(value = "forumDetailsMBean")
@ViewScoped
public class ForumDetailsMBean implements Serializable{
    
    private static final Logger LOGGER=Logger.getLogger(ForumDetailsMBean.class.getName());
    private int commentChars;
    private Forum forum;
    private String userComment;
    private List<ForumComment> otherForumComments;
    
    @Inject
    private ForumServiceEjbLocal forumServiceEjbLocal;
    @Inject
    private UserServiceEjbLocal userServiceEjbLocal;
    
    @PostConstruct
    public void init(){
        HttpServletRequest request=(HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String forumIdStr=request.getParameter("forumId");
        int forumId=Integer.getInteger(forumIdStr);
        forum = forumServiceEjbLocal.findById(forumId);
        LOGGER.info(String.format("Forum with ID: %d Loaded successfully.", forum.getId()));
        
        Access forumCreator=userServiceEjbLocal.getAccessByEmail(forum.getForumCreatorEmail());
        HttpSession session=request.getSession();
        session.setAttribute(BjmConstants.FORUM_CREATOR_ACCESS, forumCreator);
        LOGGER.info(String.format("Forum with ID: %s Loaded successfully.", forum.getId()));
    }
    
    public String postForumComment(){
        if(userComment==null || userComment.trim().isEmpty()){
           FacesContext.getCurrentInstance().addMessage("usercomment", new FacesMessage(FacesMessage.SEVERITY_ERROR, "No comment entered", "No comment entered"));
        }else{
            ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
            ForumComment forumComment = new ForumComment();
            forumComment.setComment(userComment);
            forumComment.setForumId(forum.getId());
            HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
            HttpSession session = request.getSession();
            Access access = (Access) session.getAttribute("access");
            forumComment.setForumCommenterAccessId(access.getId());
            forumComment.setForumCommenterEmail(access.getEmail());
            forumComment.setDated(Timestamp.valueOf(LocalDateTime.now(ZoneId.of(servletContext.getInitParameter("ZoneId")))));
            forumComment=forumServiceEjbLocal.postForumComment(forumComment);
            LOGGER.info(String.format("New ForumComment added with ID: %d", forumComment.getId()));
            FacesContext.getCurrentInstance().addMessage("usercomment", new FacesMessage(FacesMessage.SEVERITY_INFO, "Comment added successfully!!", "Comment added successfully!!"));
        }
        return null;
    }
    
    private void loadOtherForumComments(){
        
        List<ForumComment> otherForumComments=forumServiceEjbLocal.getAllCommentsOnForum(forum.getId());
        Map<Integer, ImageVO> forumCommenterImageMap=new HashMap<>();
        for(ForumComment fc: otherForumComments){
            Access forumCommenterAccess= userServiceEjbLocal.getAccessById(fc.getForumCommenterAccessId());
            String imageType=forumCommenterAccess.getProfileFile().substring(forumCommenterAccess.getProfileFile().indexOf('.')+1);
            ImageVO forumCommenterImageVO=new ImageVO(imageType, forumCommenterAccess.getImage());
            forumCommenterImageMap.put(forumCommenterAccess.getId(), forumCommenterImageVO);
        }
        HttpServletRequest request=(HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        HttpSession session=request.getSession();
        session.setAttribute(BjmConstants.FORUM_COMMENTER_IMAGE_MAP, forumCommenterImageMap);
        LOGGER.info(String.format("Count of other Forum Comments for Forum ID: %d is : %d", forum.getId(), otherForumComments.size()));
    }

    public Forum getForum() {
        return forum;
    }

    public void setForum(Forum forum) {
        this.forum = forum;
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
    
    public List<ForumComment> getOtherForumComments(){
        loadOtherForumComments();
        return otherForumComments;
    }
    
    public void setOtherForumComments(List<ForumComment> otherForumComments){
        this.otherForumComments=this.otherForumComments;
    }
    
    
    
    
}
