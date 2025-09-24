package org.bjm.ejbs;

import jakarta.ejb.Local;
import java.util.List;
import org.bjm.dtos.UserDto;
import org.bjm.entities.Access;
import org.bjm.entities.Blog;
import org.bjm.entities.BlogAbuse;
import org.bjm.entities.BlogComment;
import org.bjm.entities.Forum;
import org.bjm.entities.ForumAbuse;
import org.bjm.entities.ForumComment;
import org.bjm.entities.LokSabhaNominate;
import org.bjm.entities.Survey;
import org.bjm.entities.SurveyAbuse;
import org.bjm.entities.SurveyFromForum;
import org.bjm.entities.SurveyFromForumAbuse;
import org.bjm.entities.SurveyFromForumVote;
import org.bjm.entities.SurveyVote;
import org.bjm.entities.User;
import org.bjm.entities.VidhanSabhaNominate;

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
    public Blog updateUserBlog(Blog blog);
    public SurveyFromForum createUserSurveyFromForum(SurveyFromForum surveyFromForum);
    public void changeUserState(User user);
    public void changeUserPersonalDetails(User user);
    public Access changeUserProfileImage(Access access);
    public LokSabhaNominate nominateNewLokSabhaCandidate(LokSabhaNominate lokSabhaNominate);
    public LokSabhaNominate nominateLokSabhaCandidate(LokSabhaNominate lokSabhaNominate);
    public VidhanSabhaNominate nominateNewVidhanSabhaCandidate(VidhanSabhaNominate vidhanSabhaNominate);
    public VidhanSabhaNominate nominateReVidhanSabhaCandidate(VidhanSabhaNominate vidhanSabhaNominate);
    public BlogComment findByBlogCommentById(int id);
    public ForumComment findByForumCommentById(int id);
    public SurveyVote findBySurveyVoteById(int id);
    public SurveyFromForumVote findBySurveyFromForumVoteById(int id);
    public BlogAbuse createBlogAbuse(BlogAbuse blogAbuse);
    public ForumAbuse createForumAbuse(ForumAbuse forumAbuse);
    public SurveyAbuse createSurveyAbuse(SurveyAbuse surveyAbuse);
    public SurveyFromForumAbuse createSurveyFromForumAbuse(SurveyFromForumAbuse surveyFromForumAbuse);
}
