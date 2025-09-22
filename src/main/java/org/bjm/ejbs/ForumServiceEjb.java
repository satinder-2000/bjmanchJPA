package org.bjm.ejbs;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import java.util.List;
import java.util.logging.Logger;
import org.bjm.entities.Forum;
import org.bjm.entities.ForumComment;

/**
 *
 * @author singh
 */
@Stateless
public class ForumServiceEjb implements ForumServiceEjbLocal {
    
    private Logger LOGGER = Logger.getLogger(ForumServiceEjb.class.getName());
    
     @PersistenceContext(name = "bjmPU")
    private EntityManager em;

    @Override
    public Forum findById(int forumId) {
        Query query =em.createNamedQuery("Forum.findById", Forum.class);
        query.setParameter(1, forumId);
        Forum toReturn = (Forum) query.getResultList().get(0);
        return toReturn;
    }

    @Override
    public ForumComment postForumComment(ForumComment forumComment) {
        em.persist(forumComment);
        em.flush();
        LOGGER.info(String.format("ForumComment posted with ID %d", forumComment.getId()));
        return forumComment;
    }

    @Override
    public List<ForumComment> getAllCommentsOnForum(int forumId) {
        Query query = em.createNamedQuery("ForumComment.findAllForForum", ForumComment.class);
        query.setParameter(1, forumId);
        List<ForumComment> toReturn =query.getResultList();
        LOGGER.info(String.format("Count of ForumComment on Forum Id %d is %d", forumId, toReturn.size()));
        return toReturn;
    }

    
}
