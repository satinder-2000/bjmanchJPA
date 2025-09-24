package org.bjm.ejbs;

import jakarta.ejb.Local;
import java.util.List;
import org.bjm.entities.Blog;
import org.bjm.entities.BlogComment;

/**
 *
 * @author singh
 */
@Local
public interface BlogServiceEjbLocal {
    
    public List<Blog> findByUserId(int userId);
    public Blog findByBlogId(int blogId);
    public List<Blog> findRecentNBlogs(int count);
    public BlogComment postBlogComment(BlogComment blogComment); 
    public List<BlogComment> findAllCommentsOnBlog(int blogId);
    
    
}
