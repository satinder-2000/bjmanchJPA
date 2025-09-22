package org.bjm.mbeans;

import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.logging.Logger;
import org.bjm.entities.Access;
import org.bjm.dtos.AccessDto;
import org.bjm.ejbs.UserServiceEjbLocal;
import org.bjm.utils.PasswordUtil;

/**
 *
 * @author singh
 */
@Named(value = "createAccessMBean")
@ViewScoped
public class CreateAccessMBean implements Serializable {
    
    private static final Logger LOGGER=Logger.getLogger(CreateAccessMBean.class.getName());
    
    private AccessDto accessDto;
    private Access access;
    
    @Inject
    private UserServiceEjbLocal userServiceEjbLocal;
    
    @PostConstruct
    public void init(){
        HttpServletRequest request=(HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String email = request.getParameter("email");
        if(!userServiceEjbLocal.isUserRegistered(email)){
            FacesContext.getCurrentInstance().addMessage("email", 
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Email not found.","Email not found."));
        }else{
            accessDto=new AccessDto();
            accessDto.setEmail(access.getEmail());
            LOGGER.info(String.format("Access loaded for email %s", request.getParameter("email")));
        }
    }
    
    public String createAccess(){
        if(!accessDto.getPassword().equals(accessDto.getConfirmPassword())){
            FacesContext.getCurrentInstance().addMessage("password", new FacesMessage(FacesMessage.SEVERITY_ERROR,"Passwords not matching","Passwords not matching"));
            return null;
        }
        String encodedPw=PasswordUtil.generateSecurePassword(accessDto.getPassword(), accessDto.getEmail());
        access.setPassword(encodedPw);
        access.setUpdatedOn(Timestamp.valueOf(LocalDateTime.now(ZoneId.of("Asia/Kolkata"))));
        userServiceEjbLocal.createUserAccess(access);
        LOGGER.info(String.format("Password created for Id: %s!!", access.getId()));
        FacesContext.getCurrentInstance().addMessage("password",new FacesMessage(FacesMessage.SEVERITY_INFO, "Password set successfully.","Password set successfully."));
        return null;
    }

    public AccessDto getAccessDto() {
        return accessDto;
    }

    public void setAccessDto(AccessDto accessDto) {
        this.accessDto = accessDto;
    }
    
    
    
    
    
}
