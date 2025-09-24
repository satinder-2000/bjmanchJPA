package org.bjm.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 *
 * @author singh
 */
@Entity
@NamedQueries({
    @NamedQuery(name = "LokSabhaNominate.findForStateCodeAndConstituency", query = "SELECT LokSabhaNominate lsn from LokSabhaNominate lsn WHERE lsn.stateCode=:stateCode "
            + "and lsn.constituency=:constituency"),
    @NamedQuery(name = "LokSabhaNominate.findByCandidateName", query = "SELECT LokSabhaNominate lsn from LokSabhaNominate lsn WHERE lsn.stateCode=:stateCode "
            + "and lsn.candidateName=:candidateName")
})
public class LokSabhaNominate {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String candidateName;
    private String constituency;
    private String stateCode;
    private int nominatedByAccessId;
    private String nominatedByEmail;
    private Timestamp nominatedOn;
    private int nominationCount;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    

    public String getCandidateName() {
        return candidateName;
    }

    public void setCandidateName(String candidateName) {
        this.candidateName = candidateName;
    }

    public String getConstituency() {
        return constituency;
    }

    public void setConstituency(String constituency) {
        this.constituency = constituency;
    }

    public String getStateCode() {
        return stateCode;
    }

    public void setStateCode(String stateCode) {
        this.stateCode = stateCode;
    }

    public int getNominatedByAccessId() {
        return nominatedByAccessId;
    }

    public void setNominatedByAccessId(int nominatedByAccessId) {
        this.nominatedByAccessId = nominatedByAccessId;
    }

    

    public String getNominatedByEmail() {
        return nominatedByEmail;
    }

    public void setNominatedByEmail(String nominatedByEmail) {
        this.nominatedByEmail = nominatedByEmail;
    }

    public Timestamp getNominatedOn() {
        return nominatedOn;
    }

    public void setNominatedOn(Timestamp nominatedOn) {
        this.nominatedOn = nominatedOn;
    }

    

    public int getNominationCount() {
        return nominationCount;
    }

    public void setNominationCount(int nominationCount) {
        this.nominationCount = nominationCount;
    }
    
    
    
    
}
