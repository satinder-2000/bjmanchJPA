package org.bjm.ejbs;

import jakarta.ejb.Stateful;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.logging.Logger;
import org.bjm.dtos.UserDto;
import org.bjm.entities.Access;
import org.bjm.entities.Blog;
import org.bjm.entities.Forum;
import org.bjm.entities.Survey;
import org.bjm.entities.SurveyFromForum;
import org.bjm.entities.User;

/**
 *
 * @author singh
 */
@Stateful
public class UserServiceEjb implements UserServiceEjbLocal {
    
    private static final Logger LOGGER=Logger.getLogger(UserServiceEjb.class.getName());
    
    @PersistenceContext(name = "bjmPU")
    private EntityManager em;
    
    @Inject
    private EmailServiceEjbLocal emailServiceEjbLocal;
    
    //Used to populate from the DB on request and then update with the password
    private Access access;
    
    @Override
    public Access registerUser(UserDto userDto) {
        User user= new User();
        user.setEmail(userDto.getEmail());
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setGender(userDto.getGender());
        user.setDob(Date.valueOf(userDto.getDob()));
        user.setMobile(userDto.getMobile());
        user.setStateName(userDto.getStateName());
        user.setLokSabha(userDto.getLokSabha());
        
        Access access= new Access();
        access.setEmail(userDto.getEmail());
        access.setCreatedOn(Timestamp.valueOf(LocalDateTime.now(ZoneId.of("Asia/Kolkata"))));
        access.setUpdatedOn(Timestamp.valueOf(LocalDateTime.now(ZoneId.of("Asia/Kolkata"))));
        
        em.persist(user);
        em.persist(access);
        em.flush();
        LOGGER.info("User regisered with ID "+user.getId()+" along with Access with ID "+access.getId());
        emailServiceEjbLocal.sendUserRegisteredEmail(access);
        
        
        return access;
    }

    @Override
    public boolean isUserRegistered(String email) {
        Query query=em.createNamedQuery("User.findByEmail", User.class);
        query.setParameter(1, email);
        if(query.getFirstResult()==0){
            return false;
        }else{
            return true;
        }
    }

    @Override
    public Access getAccessByEmail(String email) {
        Query query=em.createNamedQuery("Access.findByEmail", Access.class);
        query.setParameter(1, email);
        access= (Access)query.getResultList().get(0);
        return access;
    }

    @Override
    public Access getAccessById(int id) {
        Query query=em.createNamedQuery("Access.findById", Access.class);
        query.setParameter(1, id);
        access= (Access)query.getResultList().get(0);
        return access;
    }
    
    
    
    @Override
    public void createUserAccess(Access access) {
        Access toReturn=em.merge(access);
        em.flush();
        emailServiceEjbLocal.sendAccessCreatedEmail(toReturn);
    }

    @Override
    public Access changeUserPassword(Access access) {
        Access toReturn=em.merge(access);
        em.flush();
        emailServiceEjbLocal.sendPasswordChangedEmail(access);
        return toReturn;
    }

    @Override
    public Access changeUserProfileImage(Access access) {
        access =em.merge(access);
        em.flush();
        //emailServiceEjbLocal.sendPasswordChangedEmail(access);
        return access;
    }
    
    
    
    

    @Override
    public List<Forum> getUserForums(String email) {
        Query query=em.createNamedQuery("Forum.findByEmail", Forum.class);
        query.setParameter(1, email);
        return query.getResultList();
    
    }

    @Override
    public List<Survey> getUserSurveys(String email) {
        Query query=em.createNamedQuery("Survey.findByEmail", Survey.class);
        query.setParameter(1, email);
        return query.getResultList();
    }

    @Override
    public List<SurveyFromForum> getUserSurveyFromForums(String email) {
        Query query=em.createNamedQuery("SurveyFromForum.findByEmail", SurveyFromForum.class);
        query.setParameter(1, email);
        return query.getResultList();
    }

    @Override
    public List<Blog> getUserBlogs(String email) {
        Query query=em.createNamedQuery("Blog.findByEmail", Survey.class);
        query.setParameter(1, email);
        return query.getResultList();
    }

    @Override
    public Forum createUserForum(Forum forum) {
        em.persist(forum);
        em.flush();
        LOGGER.info(String.format("User Forum created with ID: ", forum.getId()));
        return forum;
    }

    @Override
    public Survey createUserSurvey(Survey survey) {
        em.persist(survey);
        em.flush();
        LOGGER.info(String.format("User Survey created with ID: ", survey.getId()));
        return survey;
    }

    @Override
    public Blog createUserBlog(Blog blog) {
        em.persist(blog);
        em.flush();
        LOGGER.info(String.format("User Blog created with ID: ", blog.getId()));
        return blog;
    }

    @Override
    public SurveyFromForum createUserSurveyFromForum(SurveyFromForum surveyFromForum) {
        em.persist(surveyFromForum);
        em.flush();
        LOGGER.info(String.format("User SurveyFromForum created with ID: ", surveyFromForum.getId()));
        return surveyFromForum;
    }

    @Override
    public Forum getUserForum(int forumId) {
        Query query =em.createNamedQuery("Forum.findById", Forum.class);
        query.setParameter(1, forumId);
        return (Forum)query.getResultList().get(0);
    }

    @Override
    public User getUserByEmail(String email) {
     Query query =em.createNamedQuery("User.findByEmail", User.class);
     query.setParameter(1, email);
     return (User)query.getResultList().get(0);}

    @Override
    public void changeUserState(User user) {
        User toReturn=em.merge(user);
        em.flush();
        emailServiceEjbLocal.sendUserStateChangedEmail(user);
        LOGGER.info(String.format("State details changed successfully for User with ID: ", user.getId()));
    }

    @Override
    public void changeUserPersonalDetails(User user) {
        em.merge(user);
        em.flush();
        emailServiceEjbLocal.sendUserPersonalDetailsChangedEmail(user);
        LOGGER.info(String.format("User Personal details changed successfully for User with ID: ", user.getId()));
    }

}
