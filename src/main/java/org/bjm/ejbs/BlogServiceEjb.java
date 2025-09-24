package org.bjm.ejbs;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import java.util.List;
import java.util.logging.Logger;
import org.bjm.entities.Blog;
import org.bjm.entities.BlogComment;

/**
 *
 * @author singh
 */
@Stateless
public class BlogServiceEjb implements BlogServiceEjbLocal {
    
    private static final Logger LOGGER = Logger.getLogger(BlogServiceEjb.class.getName());
    
    @PersistenceContext(name = "bjmPU")
    private EntityManager em;

    @Override
    public List<Blog> findByUserId(int userId) {
        Query query=em.createNamedQuery("Blog.findByUserId", Blog.class);
        query.setParameter(1, userId);
        List<Blog> toReturn=query.getResultList();
        LOGGER.info(String.format("Count of Blogs for UserId %d is %d", userId, toReturn.size()));
        return toReturn;
    }

    @Override
    public Blog findByBlogId(int blogId) {
        Query query=em.createNamedQuery("Blog.findByBlogId", Blog.class);
        query.setParameter(1, blogId);
        Blog toReturn=(Blog)query.getSingleResult();
        LOGGER.info(String.format("Blog %d extracted.", blogId));
        return toReturn;
    }

    @Override
    public List<Blog> findRecentNBlogs(int count) {
        Query query=em.createNamedQuery("Blog.findNBlogs", Blog.class);
        query.setParameter(1, count);
        List<Blog> toReturn=query.getResultList();
        LOGGER.info(String.format("Count of Blogs for extracted is %d", toReturn.size()));
        return toReturn;
    }

    @Override
    public BlogComment postBlogComment(BlogComment blogComment) {
        em.persist(blogComment);
        em.flush();
        LOGGER.info(String.format("BlogComment saved with ID %d", blogComment.getId()));
        return blogComment;
    }

    @Override
    public List<BlogComment> findAllCommentsOnBlog(int blogId) {
        Query query = em.createNamedQuery("BlogComment.findAllForBlog", BlogComment.class);
        query.setParameter(1, blogId);
        List<BlogComment> toReturn =query.getResultList();
        LOGGER.info(String.format("Count of Blogs for extracted for Blog %d is %d",blogId, toReturn.size()));
        return toReturn;
    }
    
    

}
