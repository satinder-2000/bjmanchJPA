package org.bjm.mbeans;

import jakarta.annotation.PostConstruct;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.ServletContext;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;
import org.bjm.ejbs.BlogServiceEjbLocal;
import org.bjm.entities.Blog;

/**
 *
 * @author singh
 */
@Named(value = "blogSummaryMBean")
@ViewScoped
public class BlogSummaryMBean implements Serializable{
    
    private static final Logger LOGGER=Logger.getLogger(BlogSummaryMBean.class.getName());
    
    private List<Blog> allBlogs;
    
    @Inject
    private BlogServiceEjbLocal blogServiceEjbLocal;
    
    @PostConstruct
    public void init(){
        ServletContext servletContext=(ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
        int recordCount=Integer.getInteger(servletContext.getInitParameter("recordCount"));
        allBlogs=blogServiceEjbLocal.findRecentNBlogs(recordCount);
        LOGGER.info(String.format("Count of Blogs loaded : %d", allBlogs.size()));
    }

    public List<Blog> getAllBlogs() {
        return allBlogs;
    }

    public void setAllBlogs(List<Blog> allBlogs) {
        this.allBlogs = allBlogs;
    }

}
