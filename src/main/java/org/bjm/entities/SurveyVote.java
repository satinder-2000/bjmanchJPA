package org.bjm.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.validation.constraints.Size;
import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 *
 * @author singh
 */
@Entity
@NamedQueries({
    @NamedQuery(name = "SurverVote.findAllForSurvey", query = "SELECT sv FROM SurverVote sv WHERE sv.surveyId = :surveyId ORDER BY sv.id")
})
public class SurveyVote {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Size(min = 2, max = 2500)
    private String comment;
    private Timestamp dated;
    private int surveyId;
    private String surveyVoterEmail;
    private int surveyVoterAccessId;

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

    public int getSurveyId() {
        return surveyId;
    }

    public void setSurveyId(int surveyId) {
        this.surveyId = surveyId;
    }

    public String getSurveyVoterEmail() {
        return surveyVoterEmail;
    }

    public void setSurveyVoterEmail(String surveyVoterEmail) {
        this.surveyVoterEmail = surveyVoterEmail;
    }

    public int getSurveyVoterAccessId() {
        return surveyVoterAccessId;
    }

    public void setSurveyVoterAccessId(int surveyVoterAccessId) {
        this.surveyVoterAccessId = surveyVoterAccessId;
    }

    

}
