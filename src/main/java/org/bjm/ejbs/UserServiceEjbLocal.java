package org.bjm.ejbs;

import jakarta.ejb.Local;
import java.util.List;
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
@Local
public interface UserServiceEjbLocal {
    
    public Access registerUser(UserDto userDto);
    public boolean isUserRegistered(String email);
    public Access getAccessByEmail(String email);
    public Access getAccessById(int id);
    public User getUserByEmail(String email);
    public void createUserAccess(Access access);
    public Access changeUserPassword(Access access);
    public Forum getUserForum(int forumId);
    public List<Forum> getUserForums(String email);
    public List<Survey> getUserSurveys(String email);
    public List<Blog> getUserBlogs(String email);
    public List<SurveyFromForum> getUserSurveyFromForums(String email);
    public Forum createUserForum(Forum forum);
    public Survey createUserSurvey(Survey survey);
    public Blog createUserBlog(Blog blog);
    public SurveyFromForum createUserSurveyFromForum(SurveyFromForum surveyFromForum);
    public void changeUserState(User user);
    public void changeUserPersonalDetails(User user);
    public Access changeUserProfileImage(Access access);
    
}
