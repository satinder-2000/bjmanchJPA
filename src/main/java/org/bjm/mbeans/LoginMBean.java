package org.bjm.mbeans;

import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.io.Serializable;
import java.util.logging.Logger;
import org.bjm.entities.Access;
import org.bjm.dtos.AccessDto;
import org.bjm.ejbs.UserServiceEjbLocal;
import org.bjm.utils.PasswordUtil;
/**
 *
 * @author singh
 */
@Named(value = "loginMBean")
@ViewScoped
public class LoginMBean implements Serializable {
    
    private static final Logger LOGGER=Logger.getLogger(LoginMBean.class.getName());
    
    @Inject
    private UserServiceEjbLocal userServiceEjbLocal;
    
    private AccessDto accessDto;
    
    @PostConstruct
    public void init(){
        accessDto=new AccessDto();
    }
    
    public String performLogin(){
        String email=accessDto.getEmail();
        Access access = userServiceEjbLocal.getAccessByEmail(email);
        String encodedPW = PasswordUtil.generateSecurePassword(accessDto.getPassword(), accessDto.getEmail());
        if (!access.getPassword().equals(encodedPW)) {
            FacesContext.getCurrentInstance().addMessage("password",
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Incorrect Login details", "Incorrect Login details"));
            return null;
        } else {
            HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
            HttpSession session = request.getSession();
            session.setAttribute("access", access);
            return "/home/userHome?faces-redirect=true";
        }
    }

    public AccessDto getAccessDto() {
        return accessDto;
    }

    public void setAccessDto(AccessDto accessDto) {
        this.accessDto = accessDto;
    }
    
    
}
