package org.bjm.ejbs;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import jakarta.ejb.Stateless;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Multipart;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.URLName;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import java.util.logging.Logger;
import org.bjm.entities.Access;
import org.bjm.entities.Forum;
import org.bjm.entities.Survey;
import org.bjm.entities.SurveyFromForum;
import org.bjm.entities.User;

/**
 *
 * @author singh
 */
@Stateless
public class EmailServiceEjb implements EmailServiceEjbLocal {
    
    private static final Logger LOGGER = Logger.getLogger(EmailServiceEjb.class.getName());
    
    @Resource(mappedName = "java:comp/env/mail/bjm")//Tomee
    private Session session;
    
    @Resource(name = "webURI")
    private String webURI;
    
    @Resource(name = "createAccessURI")
    private String createAccessURI;
    
    @Resource(name = "forumCreatedURI")
    private String forumCreatedURI;
    
    @Resource(name = "surveyCreatedURI")
    private String surveyCreatedURI;
    
    @Resource(name = "surveyCreatedFromForumURI")
    private String surveyCreatedFromForumURI;
    
    @PostConstruct
    public void init(){
        final URLName url = new URLName(
                    session.getProperty("mail.transport.protocol"),
                    session.getProperty("mail.smtp.host"), -1, null,
                    session.getProperty("mail.smtp.user"), null);
        session.setPasswordAuthentication(url, new PasswordAuthentication(session.getProperty("mail.smtp.user"), session.getProperty("mail.smtp.password")));
        LOGGER.info("MailSession set successfully!!");
    }

    @Override
    public void sendUserRegisteredEmail(Access access) {
        try {
            Message mimeMessage = new MimeMessage(session);
            mimeMessage.setFrom(new InternetAddress(session.getProperty("mail.smtp.user")));
            mimeMessage.setRecipient(Message.RecipientType.TO,
                    new InternetAddress(access.getEmail()));
            mimeMessage.setSubject("User Registration");
            StringBuilder htmlMsg = new StringBuilder("<html><body>");
            htmlMsg.append("<h2>Dear, ").append(access.getEmail()).append("</h2>");
            htmlMsg.append("<p>Congratulations on registering yourself successfully with us !!").append(".</p>");
            htmlMsg.append("<p>As a final step, please create your account password by following the link below:</p>");
            String createAccess = String.format(createAccessURI, access.getEmail());
            htmlMsg.append("<a href=\"").append(webURI).append(createAccess).append("\">")
                    .append(webURI).append(createAccess)
                    .append("</a>");

            htmlMsg.append("<p>Best Wishes, <br/>www.bjmanch.org Admin</p>");
            htmlMsg.append("</body></html>");
            MimeBodyPart htmlPart = new MimeBodyPart();
            Multipart multipart = new MimeMultipart();
            htmlPart.setContent(htmlMsg.toString(), "text/html; charset=utf-8");
            multipart.addBodyPart(htmlPart);
            mimeMessage.setContent(multipart);
            Transport.send(mimeMessage);
            LOGGER.info("Email sent successfully....");

        } catch (MessagingException ex) {
            LOGGER.severe(ex.getMessage());
        }
        
    }

    @Override
    public void sendAccessCreatedEmail(Access access) {
        MimeMessage mimeMessage= new MimeMessage(session);
        Multipart multipart= new MimeMultipart();
        StringBuilder htmlMsg = new StringBuilder("<html><body>");
        htmlMsg.append("<h2>Dear, ").append(access.getEmail()).append("</h2>");
        htmlMsg.append("<p>Congratulations on completing your access details successfully!!").append(".</p>");
        htmlMsg.append("<p>You may now proceed to the website and login to your account.</p>");
        htmlMsg.append("<a href=\"").append(webURI).append("\">")
                .append(webURI)
                .append("</a>");
        htmlMsg.append("<p>Best Wishes, <br/>www.bjmanch.org Admin</p>");
        htmlMsg.append("</body></html>");
        MimeBodyPart htmlPart = new MimeBodyPart();
        try{
            htmlPart.setContent( htmlMsg.toString(), "text/html; charset=utf-8" );
            multipart.addBodyPart(htmlPart);
            mimeMessage.setFrom(new InternetAddress(session.getProperty("mail.smtp.user")));
            mimeMessage.setRecipient(Message.RecipientType.TO,new InternetAddress(access.getEmail()));
            mimeMessage.setContent(multipart);
            mimeMessage.setSubject("Welcome. Access Confirmed!!");
            Transport.send(mimeMessage);
            LOGGER.info("Email sent successfully....");
        }catch(MessagingException ex){
            LOGGER.severe(ex.getMessage());
        }
    }

    @Override
    public void sendForumCreatedEmail(Access access, Forum forum) {
        MimeMessage mimeMessage = new MimeMessage(session);
        Multipart multipart = new MimeMultipart();
        StringBuilder htmlMsg = new StringBuilder("<html><body>");
        htmlMsg.append("<h2>Dear, ").append(access.getEmail()).append("</h2>");
        htmlMsg.append("<p>Congratulations on creating a new Forum successfully!!").append(".</p>");
        htmlMsg.append("<p>You may wish to view your Forum at the link provided below:</p>");
        String forumCreated = String.format(forumCreatedURI, forum.getId(), access.getEmail());
        htmlMsg.append("<a href=\"").append(webURI).append(forumCreated).append("\">")
                .append(webURI).append(forumCreated)
                .append("</a>");
        //htmlMsg.append("<p>"+accessCreate+"</p>");
        htmlMsg.append("<p>Best Wishes, <br/>www.bjmanch.org Admin</p>");
        htmlMsg.append("</body></html>");
        MimeBodyPart htmlPart = new MimeBodyPart();
        try {
            htmlPart.setContent(htmlMsg.toString(), "text/html; charset=utf-8");
            multipart.addBodyPart(htmlPart);
            mimeMessage.setFrom(new InternetAddress(session.getProperty("mail.smtp.user")));
            mimeMessage.setRecipient(Message.RecipientType.TO, new InternetAddress(access.getEmail()));
            mimeMessage.setContent(multipart);
            mimeMessage.setSubject("Forum Created");
            Transport.send(mimeMessage);
            LOGGER.info("Sent message successfully....");
        } catch (MessagingException ex) {
            LOGGER.severe(ex.getMessage());
        }
    }

    @Override
    public void sendSurveyCreatedEmail(Access access, Survey survey) {
        MimeMessage mimeMessage = new MimeMessage(session);
        Multipart multipart = new MimeMultipart();
        StringBuilder htmlMsg = new StringBuilder("<html><body>");
        htmlMsg.append("<h2>Dear, ").append(access.getEmail()).append("</h2>");
        htmlMsg.append("<p>Congratulations on creating a new Survey successfully!!").append(".</p>");
        htmlMsg.append("<p>You may wish to view your Survey at the link provided below:</p>");
        String surveyCreated = String.format(surveyCreatedURI, survey.getId(), access.getEmail());
        htmlMsg.append("<a href=\"").append(webURI).append(surveyCreated).append("\">")
                .append(webURI).append(surveyCreated)
                .append("</a>");
        //htmlMsg.append("<p>"+accessCreate+"</p>");
        htmlMsg.append("<p>Best Wishes, <br/>www.bjmanch.in Admin</p>");
        htmlMsg.append("</body></html>");
        MimeBodyPart htmlPart = new MimeBodyPart();
        try {
            htmlPart.setContent(htmlMsg.toString(), "text/html; charset=utf-8");
            multipart.addBodyPart(htmlPart);
            mimeMessage.setFrom(new InternetAddress(session.getProperty("mail.smtp.user")));
            mimeMessage.setRecipient(Message.RecipientType.TO, new InternetAddress(access.getEmail()));
            mimeMessage.setContent(multipart);
            mimeMessage.setSubject("Survey Created");
            Transport.send(mimeMessage);
            LOGGER.info("Sent message successfully....");
        } catch (MessagingException ex) {
            LOGGER.severe(ex.getMessage());
        }
    }

    @Override
    public void sendSurveyCreatedFromForumEmail(Access access, SurveyFromForum surveyFromForum) {
        MimeMessage mimeMessage = new MimeMessage(session);
        Multipart multipart = new MimeMultipart();
        StringBuilder htmlMsg = new StringBuilder("<html><body>");
        htmlMsg.append("<h2>Dear, ").append(access.getEmail()).append("</h2>");
        htmlMsg.append("<p>Congratulations on creating a new Survey From Forum successfully!!").append(".</p>");
        htmlMsg.append("<p>You may wish to view your Survey From Forum at the link provided below:</p>");
        String surveyCreated = String.format(surveyCreatedFromForumURI, surveyFromForum.getId(), access.getEmail());
        htmlMsg.append("<a href=\"").append(webURI).append(surveyCreated).append("\">")
                .append(webURI).append(surveyCreated)
                .append("</a>");
        //htmlMsg.append("<p>"+accessCreate+"</p>");
        htmlMsg.append("<p>Best Wishes, <br/>www.bjmanch.in Admin</p>");
        htmlMsg.append("</body></html>");
        MimeBodyPart htmlPart = new MimeBodyPart();
        try {
            htmlPart.setContent(htmlMsg.toString(), "text/html; charset=utf-8");
            multipart.addBodyPart(htmlPart);
            mimeMessage.setFrom(new InternetAddress(session.getProperty("mail.smtp.user")));
            mimeMessage.setRecipient(Message.RecipientType.TO, new InternetAddress(access.getEmail()));
            mimeMessage.setContent(multipart);
            mimeMessage.setSubject("Survey From Forum Created");
            Transport.send(mimeMessage);
            LOGGER.info("Sent message successfully....");
        } catch (MessagingException ex) {
            LOGGER.severe(ex.getMessage());
        }
    }

    @Override
    public void sendPasswordChangedEmail(Access access) {
        MimeMessage mimeMessage = new MimeMessage(session);
        Multipart multipart = new MimeMultipart();
        StringBuilder htmlMsg = new StringBuilder("<html><body>");
        htmlMsg.append("<h2>Dear, ").append(access.getEmail()).append("</h2>");
        htmlMsg.append("<p>You have successfully changed your password.");
        htmlMsg.append("<p>Best Wishes, <br/>www.bjmanch.in Admin</p>");
        htmlMsg.append("</body></html>");
        MimeBodyPart htmlPart = new MimeBodyPart();
        try {
            htmlPart.setContent(htmlMsg.toString(), "text/html; charset=utf-8");
            multipart.addBodyPart(htmlPart);
            mimeMessage.setFrom(new InternetAddress(session.getProperty("mail.smtp.user")));
            mimeMessage.setRecipient(Message.RecipientType.TO, new InternetAddress(access.getEmail()));
            mimeMessage.setContent(multipart);
            mimeMessage.setSubject("Password changed");
            Transport.send(mimeMessage);
            LOGGER.info("Sent message successfully....");
        } catch (MessagingException ex) {
            LOGGER.severe(ex.getMessage());
        }
    }

    @Override
    public void sendUserStateChangedEmail(User user) {
        MimeMessage mimeMessage = new MimeMessage(session);
        Multipart multipart = new MimeMultipart();
        StringBuilder htmlMsg = new StringBuilder("<html><body>");
        htmlMsg.append("<h2>Dear, ").append(user.getEmail()).append("</h2>");
        htmlMsg.append("<p>You have successfully changed your State to ").append(user.getStateName()).append(".</p>");
        htmlMsg.append("<p>Best Wishes, <br/>www.bjmanch.org Admin</p>");
        htmlMsg.append("</body></html>");
        MimeBodyPart htmlPart = new MimeBodyPart();
        try {
            htmlPart.setContent(htmlMsg.toString(), "text/html; charset=utf-8");
            multipart.addBodyPart(htmlPart);
            mimeMessage.setFrom(new InternetAddress(session.getProperty("mail.smtp.user")));
            mimeMessage.setRecipient(Message.RecipientType.TO, new InternetAddress(user.getEmail()));
            mimeMessage.setContent(multipart);
            mimeMessage.setSubject("State of residence changed");
            Transport.send(mimeMessage);
            LOGGER.info("Sent message successfully....");
        } catch (MessagingException ex) {
            LOGGER.severe(ex.getMessage());
        }
    }

    @Override
    public void sendUserPersonalDetailsChangedEmail(User user) {
        MimeMessage mimeMessage = new MimeMessage(session);
        Multipart multipart = new MimeMultipart();
        StringBuilder htmlMsg = new StringBuilder("<html><body>");
        htmlMsg.append("<h2>Dear, ").append(user.getEmail()).append("</h2>");
        htmlMsg.append("<p>You have successfully changed your Peronal Details.").append(".</p>");
        htmlMsg.append("<p>Best Wishes, <br/>www.bjmanch.org Admin</p>");
        htmlMsg.append("</body></html>");
        MimeBodyPart htmlPart = new MimeBodyPart();
        try {
            htmlPart.setContent(htmlMsg.toString(), "text/html; charset=utf-8");
            multipart.addBodyPart(htmlPart);
            mimeMessage.setFrom(new InternetAddress(session.getProperty("mail.smtp.user")));
            mimeMessage.setRecipient(Message.RecipientType.TO, new InternetAddress(user.getEmail()));
            mimeMessage.setContent(multipart);
            mimeMessage.setSubject("Personal details changed");
            Transport.send(mimeMessage);
            LOGGER.info("Sent message successfully....");
        } catch (MessagingException ex) {
            LOGGER.severe(ex.getMessage());
        }
    }

    
}
