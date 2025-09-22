package org.bjm.mbeans;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.event.AjaxBehaviorEvent;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import org.bjm.entities.Access;
import org.bjm.entities.LokSabha;
import org.bjm.entities.State;
import org.bjm.entities.User;
import org.bjm.entities.VidhanSabha;
import org.bjm.dtos.AccessDto;
import org.bjm.dtos.UserDto;
import org.bjm.ejbs.EmailServiceEjbLocal;
import org.bjm.ejbs.ReferenceDataServiceEjbLocal;
import org.bjm.ejbs.UserServiceEjbLocal;
import org.bjm.utils.PasswordUtil;

/**
 *
 * @author singh
 */
@Named(value = "manageAccountMBean")
@SessionScoped
public class ManageAccountMBean implements Serializable {
    
    private static final Logger LOGGER =Logger.getLogger(ManageAccountMBean.class.getName());
    
    private AccessDto accessDto;
    private UserDto userDto;
    private User user;
    
    private Part profileImage;
    
    @Inject
    private ReferenceDataServiceEjbLocal referenceDataServiceEjbLocal;
    
    @Inject
    private UserServiceEjbLocal userServiceEjbLocal;
    
    @Inject
    private EmailServiceEjbLocal emailServiceEjbLocal;
    
    @PostConstruct 
    public void init(){
        HttpServletRequest request=(HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        HttpSession session=request.getSession();
        Access access=(Access)session.getAttribute("access");
        accessDto=new AccessDto();
        accessDto.setEmail(access.getEmail());
        user=userServiceEjbLocal.getUserByEmail(access.getEmail());
        userDto=new UserDto();
        userDto.setFirstName(user.getFirstName());
        userDto.setLastName(user.getLastName());
        userDto.setMobile(user.getMobile());
        userDto.setPhone(user.getPhone());
        userDto.setStateName(user.getStateName());
        userDto.setLokSabha(user.getLokSabha());
        userDto.setVidhanSabha(user.getVidhanSabha());
    }
    
    public String changePasswordReq(){
        return "/home/changePassword?faces-redirect=true";
    }
    
    public String changePersonalDetailsReq(){
        return "/home/changePersonalDetails?faces-redirect=true";
    }
    
    public String changeProfileImageReq(){
        return "/home/changeProfileImage?faces-redirect=true";
    }
    
    public String submitNewPassword(){
        if (!accessDto.getPassword().equals(accessDto.getConfirmPassword())){
            FacesContext.getCurrentInstance().addMessage("password", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Passwords do not match","Passwords do not match"));
            return null;
        }else{
            HttpServletRequest request=(HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
            HttpSession session=request.getSession();
            Access access=(Access)session.getAttribute("access");
            access.setPassword(PasswordUtil.generateSecurePassword(accessDto.getPassword(), access.getEmail()));
            ServletContext servletContext=(ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
            access.setUpdatedOn(Timestamp.valueOf(LocalDateTime.now(ZoneId.of(servletContext.getInitParameter("zoneId")))));
            access=userServiceEjbLocal.changeUserPassword(access);
            emailServiceEjbLocal.sendPasswordChangedEmail(access);
            LOGGER.info(String.format("Password changed For Access ID %d ",access.getId()));
            FacesContext.getCurrentInstance().addMessage("password", new FacesMessage(FacesMessage.SEVERITY_INFO, "Password changed successfully","Password changed successfully"));
            emailServiceEjbLocal.sendPasswordChangedEmail(access);
            return null;
        }
    }
    
    public String changeStateReq(){
        List<State> statedDb=referenceDataServiceEjbLocal.getAllStates();
        userDto.setAllStates(new ArrayList<>());
        State dummy=new State();
        dummy.setCode("--");
        dummy.setName("--Select One--");
        userDto.getAllStates().add(dummy);
        userDto.getAllStates().addAll(statedDb);
        return "/home/changeState?faces-redirect=true";        
    }
    
    public void constituencyListener(AjaxBehaviorEvent event){
        //While we are here let's set the stateName in UserDto
        List<State> allStates=referenceDataServiceEjbLocal.getAllStates();
        for(State state:allStates){
            if(userDto.getStateCode().equals(state.getCode())){
                userDto.setStateName(state.getName());
                break;
            }
        }
        //Change LokSabhas for the newly selected State now
        List<LokSabha> lokSabhasForState = referenceDataServiceEjbLocal.getLokSabhasForState(userDto.getStateCode());
        LokSabha dummy = new LokSabha();
        dummy.setStateCode("--");
        dummy.setConstituency("--Select One--");
        userDto.setLokSabhas(new ArrayList());
        userDto.getLokSabhas().add(dummy);
        userDto.getLokSabhas().addAll(lokSabhasForState);
        LOGGER.info(String.format("UserDto initialided with LokSabha count %d for StateCode : %s", userDto.getLokSabhas().size(),userDto.getStateCode()));
        
        //Change VidhanSabhas for the newly selected State now
        List<VidhanSabha> vidhanSabhasForState = referenceDataServiceEjbLocal.getVidhanSabhasForState(userDto.getStateCode());
        userDto.setVidhanSabhas(new ArrayList());
        if(vidhanSabhasForState.isEmpty()){//Case for UTs such as Chandigarh
            VidhanSabha dummyVS = new VidhanSabha();
            dummyVS.setStateCode("NA");
            dummyVS.setConstituency("No Vidhan Sabha");
            userDto.getVidhanSabhas().add(dummyVS);
            LOGGER.info(String.format("UserDto initialided without any Vidhan Sabha for StateCode : %s", userDto.getStateCode()));
        }else{
            VidhanSabha dummyVS = new VidhanSabha();
            dummyVS.setStateCode("--");
            dummyVS.setConstituency("--Select One--");
            userDto.getVidhanSabhas().add(dummyVS);
            userDto.getVidhanSabhas().addAll(vidhanSabhasForState);
            LOGGER.info(String.format("UserDto initialided with VidhanSabha count %d for StateCode : %s", userDto.getVidhanSabhas().size(),userDto.getStateCode()));
        
        }
    }
    
    public String submitNewState(){
        HttpServletRequest request=(HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        HttpSession session=request.getSession();
        Access access=(Access)session.getAttribute("access");
        User user = userServiceEjbLocal.getUserByEmail(access.getEmail());
        List<State> allStates = referenceDataServiceEjbLocal.getAllStates();
        for(State s : allStates){
            if(s.getCode().equals(userDto.getStateCode())){
                user.setStateName(s.getName());
                break;
            }
        }
        user.setLokSabha(userDto.getLokSabha());
        user.setVidhanSabha(userDto.getVidhanSabha());
        userServiceEjbLocal.changeUserState(user);
        FacesContext.getCurrentInstance().addMessage("state", new FacesMessage(FacesMessage.SEVERITY_INFO, "State details changed successfully","State details changed successfully"));
        return null;
    }
    
    public String changePersonalDetails(){
        HttpServletRequest request=(HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        HttpSession session=request.getSession();
        Access access=(Access)session.getAttribute("access");
        User user=userServiceEjbLocal.getUserByEmail(access.getEmail());
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setMobile(userDto.getMobile());
        user.setPhone(userDto.getPhone());
        ServletContext servletContext=(ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
        user.setUpdatedOn(Timestamp.valueOf(LocalDateTime.now(ZoneId.of(servletContext.getInitParameter("ZoneId")))));
        LOGGER.info(String.format("Personal details updated for User ID %d",user.getId()));
        FacesContext.getCurrentInstance().addMessage("", new FacesMessage(FacesMessage.SEVERITY_INFO, "Personal details updated successfully","Personal details updated successfully"));
        return null;
        
    }
    
    public String changeProfileImage() {
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        HttpSession session = request.getSession();
        Access access = (Access) session.getAttribute("access");
        
        try {
            InputStream inputStream = profileImage.getInputStream();
            int imageSize = (int) profileImage.getSize();
            if (imageSize > (1024 * 1000)) {
                FacesContext.getCurrentInstance().addMessage("profileImage",
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Profile Image size exceeds 1MB.", "Profile Image size exceeds 1MB."));
            } else {
                String fullFileName = profileImage.getSubmittedFileName();
                String fileType = fullFileName.substring(fullFileName.indexOf('.'));
                byte[] imageData = new byte[inputStream.available()];
                inputStream.read(imageData);
                //userDto.setProfileFile(fullFileName);
                //userDto.setImage(imageData);
                access.setProfileFile(fullFileName);
                access.setImage(imageData);
                access.setProfileFile(access.getProfileFile());
                access=userServiceEjbLocal.changeUserProfileImage(access);
                LOGGER.info(String.format("Profile imaged updated For Access ID %d", access.getId()));
                FacesContext.getCurrentInstance().addMessage("profileImage", new FacesMessage(FacesMessage.SEVERITY_INFO, "Profile Image updated successfully", "Profile Image updated successfully"));

            }
        } catch (IOException ex) {
            LOGGER.severe(ex.getMessage());
            throw new RuntimeException(ex.getMessage());
        }

        return null;

    }
        
        

    public AccessDto getAccessDto() {
        return accessDto;
    }

    public void setAccessDto(AccessDto accessDto) {
        this.accessDto = accessDto;
    }

    public UserDto getUserDto() {
        return userDto;
    }

    public void setUserDto(UserDto userDto) {
        this.userDto = userDto;
    }

    public Part getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(Part profileImage) {
        this.profileImage = profileImage;
    }

    

    
    
    
    
}
