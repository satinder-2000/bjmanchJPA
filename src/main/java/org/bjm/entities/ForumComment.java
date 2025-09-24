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
    @NamedQuery(name = "ForumComment.findAllForForum", query = "SELECT fc FROM ForumComment fc WHERE fc.forumId = :forumId ORDER BY fc.id"),
    @NamedQuery(name="ForumComment.findById", query = "SELECT ForumComment fc FROM ForumComment fc WHERE fc.id = :id")
})
public class ForumComment {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Size(min = 2, max = 2500)
    private String comment;
    private Timestamp dated;
    private int forumId;
    private String forumCommenterEmail;
    private int forumCommenterAccessId;

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

    public int getForumId() {
        return forumId;
    }

    public void setForumId(int forumId) {
        this.forumId = forumId;
    }

    public String getForumCommenterEmail() {
        return forumCommenterEmail;
    }

    public void setForumCommenterEmail(String forumCommenterEmail) {
        this.forumCommenterEmail = forumCommenterEmail;
    }

    public int getForumCommenterAccessId() {
        return forumCommenterAccessId;
    }

    public void setForumCommenterAccessId(int forumCommenterAccessId) {
        this.forumCommenterAccessId = forumCommenterAccessId;
    }

}
