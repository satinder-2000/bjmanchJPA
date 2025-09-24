package org.bjm.mbeans;

import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.flow.FlowScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.logging.Logger;
import org.apache.commons.text.similarity.FuzzyScore;
import org.bjm.ejbs.EmailServiceEjbLocal;
import org.bjm.ejbs.ReferenceDataServiceEjbLocal;
import org.bjm.ejbs.UserServiceEjbLocal;
import org.bjm.entities.Access;
import org.bjm.entities.State;
import org.bjm.entities.User;
import org.bjm.entities.VidhanSabha;
import org.bjm.entities.VidhanSabhaNominate;

/**
 *
 * @author singh
 */
@Named(value = "vidhanSabhaNominationMBean")
@FlowScoped(value = "VidhanSabhaNominate")
public class VidhanSabhaNominationMBean implements Serializable{
    
    private static final Logger LOGGER = Logger.getLogger(VidhanSabhaNominationMBean.class.getName());
    
    private User user;
    private VidhanSabhaNominate vidhanSabhaNominate;
    private State state;
    private List<String> constituencies;
    private List<String> fuzzyCandidates;
    private List<String> nominatedCandidates;
    
    private String candidateSelected;
    private String candidateNew;
    private boolean reloaded;
    private boolean newNomination;
    private boolean forceNomination;
    
    @Inject
    private EmailServiceEjbLocal emailServiceEjbLocal;
    
    @Inject
    private UserServiceEjbLocal userServiceEjbLocal;
    
    @Inject
    private ReferenceDataServiceEjbLocal referenceDataServiceEjbLocal;
    
    /**
    * The following steps need executing here
    * Fetch Access from Session
    * Using the email in Access fetch User
    * Using the StateName find StateCode
    * Use the StateCode to fetch Constituency
    * Using StateCode and Constituency load VSNominations
    * 
    */
    
    @PostConstruct
    public void init(){
        HttpServletRequest request=(HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        HttpSession session=request.getSession();
        Access access=(Access) session.getAttribute("access");
        
        //Fetch User from the email in Access
        user = userServiceEjbLocal.getUserByEmail(access.getEmail());
        state=referenceDataServiceEjbLocal.findStateByName(user.getStateName());
        List<VidhanSabha> vidhanSabhasInState=referenceDataServiceEjbLocal.getVidhanSabhasForState(state.getCode());
        List<VidhanSabhaNominate> vidhanSabhaNominations=referenceDataServiceEjbLocal.getVidhanSabhaNominationsForConstituency(state.getCode(), user.getVidhanSabhaConstituency());
        nominatedCandidates=new ArrayList();
        if(vidhanSabhaNominations.isEmpty()){
            nominatedCandidates.add("No nomination found");
        }else{
            nominatedCandidates.add("--Select One--");
            for (VidhanSabhaNominate lsn : vidhanSabhaNominations) {
                nominatedCandidates.add(lsn.getCandidateName());
            }
            LOGGER.info(String.format("Number of Nominations for Constituency %s in StateCode %s is %d", user.getVidhanSabhaConstituency(),state.getCode(),nominatedCandidates.size()));
        }
        fuzzyCandidates=new ArrayList<>();
    }
    
    /**
     * The following logic has been coded in this method. 
     * Step 1: No candidate has been nominated so far and the newNomination is the first one.
     *
     * Step 2a: User picks the existing Candidate and nominates it - the
     * nominationCount field is incremented by 1.
     *
     * Step 2b: User's newNomination clears the fuzzy match and the
     * newNomination is added as a Candidate.
     *
     * Step 2c: newNomination closely matches the existing name(s) and are
     * displayed on the front end for User to take further action (flag reloaded
     * is also set here)
     *
     * Step 3a: From the Step 2c, User accepts the existing Candidate and
     * nominates it. the nominationCount field is incremented by 1.
     *
     * Step 3b: User forces the newNomination and it is added as a new Candidate.
     *
     * @return
     */
    public String processNomination(){
        if((candidateSelected.equals("--Select One--")||candidateSelected.equals("--None Found--")) && candidateNew.isEmpty()){
            FacesContext.getCurrentInstance().addMessage("", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Either select a Candidate or Nominate another.", "Either select a Candidate or Nominate another."));
            return null;
        }
        if(!reloaded){ //These scenarios will take place in the main path of nomination i.e before the page reloading
            //Scenario 1: No nomination made so far, hence, this is the first one.
            if (nominatedCandidates.size()==1){//the first and only value is "--None Found--"
                if (!candidateNew.isEmpty()){
                    newNomination=true;
                    candidateSelected = "";
                    preSubmitNomination();
                    return "VidhanSabhaNominateConfirm?faces-redirect=true";
                }
            }
            
            //Scenario 2a: User picks from the list - candidate record incremented.
           if(!candidateSelected.equals("--Select One--") && candidateNew.isEmpty()){
               LOGGER.info(String.format("User %s has nominated Candidate for Constituency %s is %s", user.getEmail(), candidateSelected, user.getVidhanSabhaConstituency()));
               preSubmitSelection();
               return "VidhanSabhaNominateConfirm?faces-redirect=true";
           } 
           //Scenario 2b: User's newNomination fails the fizzy test (score>10)  All the fizzyCandidates (the matches ones)
           //are displayed on the front end for the User to make desicion.
           fuzzyCandidates.clear();
           FuzzyScore fuzzyScore=new FuzzyScore(Locale.ENGLISH);
           for(String ec:nominatedCandidates){
               int score=fuzzyScore.fuzzyScore(ec, candidateNew);
               if(score>10){
                   fuzzyCandidates.add(ec);
               }
           }
           
           if (!fuzzyCandidates.isEmpty()) {
                reloaded = true;
                return null;
            } else {
                candidateSelected = " ";
                newNomination = true;
                preSubmitNomination();//just preparing the data here, without actual submit in the DB.
                return "VidhanSabhaNominateConfirm?faces-redirect=true";
            }
        
        }else if(reloaded){
            //Scenario 3a: User accepts the item  from the fuzzy match - candidate record incremented.
            if(!candidateSelected.equals("--Select One--") && !forceNomination){
                LOGGER.info(String.format("User %1$s has nominated Candidate for Constituency %2$s is %3$s", user.getEmail(), candidateSelected, user.getVidhanSabhaConstituency()));
                candidateNew="";
                preSubmitSelection();
                return "VidhanSabhaNominateConfirm?faces-redirect=true";
            }//Scenario 3b: User forces the Nimination if the Candidate and it takes priority.
            else if (forceNomination && !candidateNew.isEmpty()) {
                candidateSelected = " ";
                LOGGER.info(String.format("Forced Nominated Candidate for Constituency %s is %s by User %s", user.getVidhanSabhaConstituency(), newNomination, user.getEmail()));
                newNomination = true;
                preSubmitNomination();//just prepating the data here, without actual submit in the DB.
                return "VidhanSabhaNominateConfirm?faces-redirect=true";
            }
        
        }
        return null;
    }
    
    
    private void preSubmitNomination(){
        vidhanSabhaNominate = new VidhanSabhaNominate();
        vidhanSabhaNominate.setStateCode(user.getStateName());
        vidhanSabhaNominate.setConstituency(user.getVidhanSabhaConstituency());
        vidhanSabhaNominate.setCandidateName(candidateNew);
        HttpServletRequest request=(HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        HttpSession session=request.getSession();
        Access access=(Access) session.getAttribute("access");
        vidhanSabhaNominate.setNominatedByAccessId(access.getId());
        vidhanSabhaNominate.setNominatedByEmail(access.getEmail());
        vidhanSabhaNominate.setNominationCount(1);
        ServletContext servletContext=(ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
        vidhanSabhaNominate.setNominatedOn(Timestamp.valueOf(LocalDateTime.now(ZoneId.of(servletContext.getInitParameter("ZoneId")))));
        newNomination=true; //Will need this flag during submit.
    }
    
    private void preSubmitSelection(){
        //User has picked from the List of pre-nominated Candidates. We just need increse the count of nominations on the Candidate
        for(String nc: nominatedCandidates){
            if (nc.equals(candidateSelected)){
                //perform the nomination increment here
                VidhanSabhaNominate vsn = referenceDataServiceEjbLocal.findByVSNominatedCandidate(state.getCode(), candidateSelected);
                vsn.setNominationCount(vsn.getNominationCount()+1);
            }
        }
    }
    
    private void submitNomination(){
        if(newNomination){//flag set in preSubmitNomination
            vidhanSabhaNominate=userServiceEjbLocal.nominateNewVidhanSabhaCandidate(vidhanSabhaNominate);
            LOGGER.info(String.format("StateCode %s - VidhanSabha %s - new Nominated created with ID: %d", user.getStateName(),user.getVidhanSabhaConstituency(),
                    vidhanSabhaNominate.getId()));
            emailServiceEjbLocal.sendNewVidhanSabhaNominationEmail(user, vidhanSabhaNominate);
        }else{
            vidhanSabhaNominate=userServiceEjbLocal.nominateReVidhanSabhaCandidate(vidhanSabhaNominate);
            LOGGER.info(String.format("StateCode %s - VidhanSabha %s - new Nominated created with ID: %d", user.getStateName(),user.getLokSabhaConstituency(),
                    vidhanSabhaNominate.getId()));
            emailServiceEjbLocal.sendVidhanSabhaReNominationEmail(user, vidhanSabhaNominate);
        }
    }
    
    public String getReturnValue(){
        submitNomination();
        return "/flowreturns/VidhanSabhaNominate-return?faces-redirect=true";
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public VidhanSabhaNominate getVidhanSabhaNominate() {
        return vidhanSabhaNominate;
    }

    public void setVidhanSabhaNominate(VidhanSabhaNominate vidhanSabhaNominate) {
        this.vidhanSabhaNominate = vidhanSabhaNominate;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public List<String> getConstituencies() {
        return constituencies;
    }

    public void setConstituencies(List<String> constituencies) {
        this.constituencies = constituencies;
    }

    public List<String> getFuzzyCandidates() {
        return fuzzyCandidates;
    }

    public void setFuzzyCandidates(List<String> fuzzyCandidates) {
        this.fuzzyCandidates = fuzzyCandidates;
    }

    

    public List<String> getNominatedCandidates() {
        return nominatedCandidates;
    }

    public void setNominatedCandidates(List<String> nominatedCandidates) {
        this.nominatedCandidates = nominatedCandidates;
    }

    public String getCandidateSelected() {
        return candidateSelected;
    }

    public void setCandidateSelected(String candidateSelected) {
        this.candidateSelected = candidateSelected;
    }

    public String getCandidateNew() {
        return candidateNew;
    }

    public void setCandidateNew(String candidateNew) {
        this.candidateNew = candidateNew;
    }

    public boolean isReloaded() {
        return reloaded;
    }

    public void setReloaded(boolean reloaded) {
        this.reloaded = reloaded;
    }

    public boolean isNewNomination() {
        return newNomination;
    }

    public void setNewNomination(boolean newNomination) {
        this.newNomination = newNomination;
    }

    public boolean isForceNomination() {
        return forceNomination;
    }

    public void setForceNomination(boolean forceNomination) {
        this.forceNomination = forceNomination;
    }
    
    
    
    
    
}
