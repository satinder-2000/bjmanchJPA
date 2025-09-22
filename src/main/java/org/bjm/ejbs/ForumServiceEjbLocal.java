package org.bjm.ejbs;

import jakarta.ejb.Local;
import java.util.List;
import org.bjm.entities.Forum;
import org.bjm.entities.ForumComment;

/**
 *
 * @author singh
 */
@Local
public interface ForumServiceEjbLocal {
    
    public Forum findById(int forumId);
    public ForumComment postForumComment(ForumComment forumComment);
    public List<ForumComment> getAllCommentsOnForum(int forumId);
    
}
