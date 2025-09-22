package org.bjm.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;



/**
 *
 * @author singh
 */
@Entity
@NamedQueries({
    @NamedQuery(name = "SurveyFromForum.findById", query = "select sf FROM SurveyFromForum sf WHERE sf.id = :id"),
    @NamedQuery(name = "SurveyFromForum.findByEmail", query = "select sf FROM SurveyFromForum sf WHERE sf.surveyCreatorEmail = :email")
})
public class SurveyFromForum extends Survey {
    
    private int id;
    private int forumId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getForumId() {
        return forumId;
    }

    public void setForumId(int forumId) {
        this.forumId = forumId;
    }
    
    

    
    
}
