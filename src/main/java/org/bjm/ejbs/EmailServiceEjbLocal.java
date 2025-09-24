package org.bjm.ejbs;

import jakarta.ejb.Local;
import org.bjm.entities.Access;
import org.bjm.entities.Blog;
import org.bjm.entities.Forum;
import org.bjm.entities.LokSabhaNominate;
import org.bjm.entities.Survey;
import org.bjm.entities.SurveyFromForum;
import org.bjm.entities.User;
import org.bjm.entities.VidhanSabhaNominate;

/**
 *
 * @author singh
 */
@Local
public interface EmailServiceEjbLocal {
    public void sendUserRegisteredEmail(Access access);
    public void sendAccessCreatedEmail(Access access);
    public void sendForumCreatedEmail(Access access, Forum forum);
    public void sendSurveyCreatedEmail(Access access, Survey survey);
    public void sendSurveyCreatedFromForumEmail(Access access, SurveyFromForum surveyFromForum);
    public void sendPasswordChangedEmail(Access access);
    public void sendUserStateChangedEmail(User user);
    public void sendUserPersonalDetailsChangedEmail(User user);
    public void sendBlogCreatedEmail(Access access, Blog blog);
    public void sendContactUsEmail(String adminEmail, String userEmail, String subject, String message);
    public void sendNewLokSabhaNominationEmail(User user, LokSabhaNominate lokSabhaNominate);
    public void sendLokSabhaReNominationEmail(User user, LokSabhaNominate lokSabhaNominate);
    public void sendNewVidhanSabhaNominationEmail(User user, VidhanSabhaNominate vidhanSabhaNominate);
    public void sendVidhanSabhaReNominationEmail(User user, VidhanSabhaNominate vidhanSabhaNominate);
    
}
