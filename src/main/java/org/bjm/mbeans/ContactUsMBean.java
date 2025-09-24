package org.bjm.mbeans;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.ServletContext;
import java.io.Serializable;
import java.util.logging.Logger;
import org.bjm.ejbs.EmailServiceEjbLocal;

/**
 *
 * @author singh
 */
@Named(value = "contactUsMBean")
@ViewScoped
public class ContactUsMBean implements Serializable {
    
    private static final long serialVersionUID = 1L;

    @Inject
    private EmailServiceEjbLocal emailServiceEjbLocal;
    
    private String userEmail;
    private String subject;
    private String message;
    
    public String sendRequest(){
        ServletContext servletContext=(ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
        String contactEmail=servletContext.getInitParameter("contactmail");
        emailServiceEjbLocal.sendContactUsEmail(contactEmail, userEmail, subject, message);
        FacesContext.getCurrentInstance().addMessage("", new FacesMessage(FacesMessage.SEVERITY_INFO,
                                                            "Message sent successfully","Message sent successfully"));
        return null;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    
    
    
    
}
