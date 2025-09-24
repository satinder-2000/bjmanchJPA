package org.bjm.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Size;
import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 *
 * @author singh
 */
@Entity
public class SurveyAbuse {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int surveyVoteId;
    private int reportedByAccessId;
    private String reportedByEmail;
    @Size(min = 5, max = 2500)
    private String reportText;
    private Timestamp reportedOn;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSurveyVoteId() {
        return surveyVoteId;
    }

    public void setSurveyVoteId(int surveyVoteId) {
        this.surveyVoteId = surveyVoteId;
    }

    public int getReportedByAccessId() {
        return reportedByAccessId;
    }

    public void setReportedByAccessId(int reportedByAccessId) {
        this.reportedByAccessId = reportedByAccessId;
    }

    public String getReportedByEmail() {
        return reportedByEmail;
    }

    public void setReportedByEmail(String reportedByEmail) {
        this.reportedByEmail = reportedByEmail;
    }

    public String getReportText() {
        return reportText;
    }

    public void setReportText(String reportText) {
        this.reportText = reportText;
    }

    public Timestamp getReportedOn() {
        return reportedOn;
    }

    public void setReportedOn(Timestamp reportedOn) {
        this.reportedOn = reportedOn;
    }

    
}
