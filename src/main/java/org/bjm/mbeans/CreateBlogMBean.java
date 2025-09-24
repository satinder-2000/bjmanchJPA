package org.bjm.mbeans;

import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
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
import java.util.logging.Logger;
import org.bjm.entities.Access;
import org.bjm.entities.Activity;
import org.bjm.entities.ActivityType;
import org.bjm.entities.Blog;
import org.bjm.dtos.BlogDto;
import org.bjm.ejbs.BlogServiceEjbLocal;
import org.bjm.ejbs.EmailServiceEjbLocal;
import org.bjm.ejbs.UserServiceEjbLocal;

/**
 *
 * @author singh
 */
@Named(value = "createBlogMBean")
@FlowScoped(value = "CreateBlog")
public class CreateBlogMBean implements Serializable {
    
    private static final Logger LOGGER=Logger.getLogger(CreateBlogMBean.class.getName());
    
    @Inject
    private ActivityMBean activityMBean;
    
    private BlogDto blogDto;
    
    private Blog blog;
    
    @Inject
    private UserServiceEjbLocal userServiceEjbLocal;
    
    @Inject
    private EmailServiceEjbLocal emailServiceEjbLocal;
    
    @PostConstruct
    public void init(){
        blogDto=new BlogDto();
        blog=new Blog();
        LOGGER.info("new BloDto initialised");
    }
    
    public String prepareBlog(){
        return "CreateBlogConfirm?faces-redirect=true";
    }
    
    public String amendBlog(){
        return "CreateBlog?faces-redirect=true";
    }
    
    //Will be an asynchronous call to this method.
    public String saveBlog(){
        ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
        if(blog.getId()==0){//First Save. we have to create Blog in collection
            blog.setTitle(blogDto.getTitle());
            blog.setText(blogDto.getText());
            HttpServletRequest httpServletRequest = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
            HttpSession session = httpServletRequest.getSession();
            Access access = (Access) session.getAttribute("access");
            blog.setPublishedByEmail(access.getEmail());
            blog.setPublishedByAccessId(access.getId().toString());
            blog.setPublishedOn(Timestamp.valueOf(LocalDateTime.now(ZoneId.of(servletContext.getInitParameter("ZoneId")))));
            blog.setSummary(blogDto.getSummary());
            blog=userServiceEjbLocal.createUserBlog(blog);
            LOGGER.info(String.format("Blog created with ID: %d", blog.getId()));
            FacesContext.getCurrentInstance().addMessage("desc", new FacesMessage(FacesMessage.SEVERITY_INFO,
                        "Blog initiated successfully!!", "Blog initiated successfully!!"));
        }else{//Just an update if required. This part might be called multiple time
            //We alredy have Blog with and Id i.e. the filter
            blog.setPublishedOn(Timestamp.valueOf(LocalDateTime.now(ZoneId.of(servletContext.getInitParameter("ZoneId")))));
            blog=userServiceEjbLocal.updateUserBlog(blog);
            LOGGER.info(String.format("Blog updated with ID: %d", blog.getId()));
            FacesContext.getCurrentInstance().addMessage("desc", new FacesMessage(FacesMessage.SEVERITY_INFO,
                        "Blog saved successfully!!", "Blog saved successfully!!"));
        }
        return null;
    }
            
            
       /* }
        blog.setTitle(blogDto.getTitle());
        blog.setText(blogDto.getText());
        HttpServletRequest httpServletRequest=(HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        HttpSession session=httpServletRequest.getSession();
        Access access= (Access)session.getAttribute("access");
        blog.setPublishedByEmail(access.getEmail());
        blog.setPublishedByAccessId(access.getId().toString());
        blog.setPublishedOn(LocalDateTime.now());
        blog.setSummary(blogDto.getSummary());
        
        ServletContext servletContext=(ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
        MongoClient mongoClient= (MongoClient) servletContext.getAttribute("mongoClient");
        CodecProvider pojoCodecProvider=PojoCodecProvider.builder().automatic(true).build();
        CodecRegistry pojoCodecRegistry=fromRegistries(MongoClientSettings.getDefaultCodecRegistry(), fromProviders(pojoCodecProvider));
        MongoDatabase mongoDatabase=mongoClient.getDatabase(servletContext.getInitParameter("MONGODB_DB")).withCodecRegistry(pojoCodecRegistry);
        MongoCollection<Blog> blogColl=mongoDatabase.getCollection("Blog", Blog.class);
        InsertOneResult blogResult=blogColl.insertOne(blog);
        blog.setId(new ObjectId(blogResult.getInsertedId().toString()));
        LOGGER.info(String.format("Blog created with ID: %s",blogResult.getInsertedId()));
    
    
    }*/
    
    private void submitBlog(){
        Blog blog=new Blog();
        blog.setTitle(blogDto.getTitle());
        blog.setText(blogDto.getText());
        HttpServletRequest httpServletRequest=(HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        HttpSession session=httpServletRequest.getSession();
        Access access= (Access)session.getAttribute("access");
        blog.setPublishedByEmail(access.getEmail());
        blog.setPublishedByAccessId(access.getId().toString());
        ServletContext servletContext=(ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
        blog.setPublishedOn(Timestamp.valueOf(LocalDateTime.now(ZoneId.of(servletContext.getInitParameter("ZoneId")))));
        blog.setSummary(blogDto.getSummary());
        blog = userServiceEjbLocal.createUserBlog(blog);
        LOGGER.info(String.format("Blog created with ID: %d",blog.getId()));
        //Activity now
        Activity activity=new Activity();
        activity.setActivityType(ActivityType.BLOG_CREATED.toString());
        activity.setActivityId(blog.getId());
        activity.setDescription("Blog Created "+blog.getTitle());
        activity.setOwnerEmail(access.getEmail());
        activity.setCreatedOn(Timestamp.valueOf(LocalDateTime.now(ZoneId.of(servletContext.getInitParameter("ZoneId")))));
        activityMBean.addActivity(activity);
        //Last Step - send Email
        emailServiceEjbLocal.sendBlogCreatedEmail(access, blog);
    }
    
    public String getReturnValue(){
        submitBlog();
        return "/flowreturns/CreateBlog-return?faces-redirect=true";
    }

    public BlogDto getBlogDto() {
        return blogDto;
    }

    public void setBlogDto(BlogDto blogDto) {
        this.blogDto = blogDto;
    }
    
    
    
    
}
