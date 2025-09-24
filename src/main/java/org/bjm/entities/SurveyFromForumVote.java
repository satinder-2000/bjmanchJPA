package org.bjm.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 *
 * @author singh
 */
@Entity
@NamedQueries({
    @NamedQuery(name = "SurveyFromForumVote.findAllForSurvey", query = "SELECT sv FROM SurveyFromForumVote sv WHERE sv.surveyId = :surveyId ORDER BY sv.id"),
    @NamedQuery(name="SurveyFromForumVote.findById", query = "SELECT SurveyFromForumVote sfv FROM SurveyFromForumVote sfv WHERE sfv.id = :id")    
})
public class SurveyFromForumVote implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @Size(min = 2, max = 2500)
    private String comment;
    private Timestamp dated;
    private int surveyFromForumId;
    private String surveyFromForumVoterEmail;
    private int surveyFromForumVoterAccessId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Timestamp getDated() {
        return dated;
    }

    public void setDated(Timestamp dated) {
        this.dated = dated;
    }

    public int getSurveyFromForumId() {
        return surveyFromForumId;
    }

    public void setSurveyFromForumId(int surveyFromForumId) {
        this.surveyFromForumId = surveyFromForumId;
    }

    public String getSurveyFromForumVoterEmail() {
        return surveyFromForumVoterEmail;
    }

    public void setSurveyFromForumVoterEmail(String surveyFromForumVoterEmail) {
        this.surveyFromForumVoterEmail = surveyFromForumVoterEmail;
    }

    public int getSurveyFromForumVoterAccessId() {
        return surveyFromForumVoterAccessId;
    }

    public void setSurveyFromForumVoterAccessId(int surveyFromForumVoterAccessId) {
        this.surveyFromForumVoterAccessId = surveyFromForumVoterAccessId;
    }
    
    

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) id;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SurveyFromForumVote)) {
            return false;
        }
        SurveyFromForumVote other = (SurveyFromForumVote) object;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.bjm.entities.SurveyFromForumVote[ id=" + id + " ]";
    }
    
}
