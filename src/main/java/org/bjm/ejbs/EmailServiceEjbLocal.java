package org.bjm.ejbs;

import jakarta.ejb.Local;
import org.bjm.entities.Access;

/**
 *
 * @author singh
 */
@Local
public interface EmailServiceEjbLocal {
    public void sendUserRegisteredEmail(Access access);
    public void sendAccessCreatedEmail(Access access);
    /*public void sendForumCreatedEmail(Access access, Forum forum);
    public void sendSurveyCreatedEmail(Access access, Survey survey);
    public void sendSurveyCreatedFromForumEmail(Access access, SurveyFromForum surveyFromForum);
    public void sendNewLokSabhaNominationEmail(User user, LokSabhaNominate lokSabhaNominate);
    public void sendLokSabhaReNominationEmail(User user, LokSabhaNominate lokSabhaNominate);
    public void sendNewVidhanSabhaNominationEmail(User user, VidhanSabhaNominate vidhanSabhaNominate);
    public void sendVidhanSabhaReNominationEmail(User user, VidhanSabhaNominate vidhanSabhaNominate);
    public void sendContactUsEmail(String adminEmail, String userEmail, String subject, String message);
    public void sendBlogCreatedEmail(Access access, Blog blog);
    public void sendPasswordChangedEmail(Access access);*/
    
}
