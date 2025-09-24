package org.bjm.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import java.sql.Timestamp;


/**
 *
 * @author singh
 */
@Entity
@NamedQueries({
    @NamedQuery(name="BlogComment.findAllForBlog", query = "SELECT BlogComment bc FROM BlogComment bc WHERE bc.blogId=:blogId"),
    @NamedQuery(name="BlogComment.findById", query = "SELECT BlogComment bc FROM BlogComment bc WHERE bc.id=:Id")
})
public class BlogComment {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String comment;
    private Timestamp dated;
    private int blogId;
    private String blogCommenterEmail;
    private int blogCommenterAccessId;

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

    public int getBlogId() {
        return blogId;
    }

    public void setBlogId(int blogId) {
        this.blogId = blogId;
    }

    public String getBlogCommenterEmail() {
        return blogCommenterEmail;
    }

    public void setBlogCommenterEmail(String blogCommenterEmail) {
        this.blogCommenterEmail = blogCommenterEmail;
    }

    public int getBlogCommenterAccessId() {
        return blogCommenterAccessId;
    }

    public void setBlogCommenterAccessId(int blogCommenterAccessId) {
        this.blogCommenterAccessId = blogCommenterAccessId;
    }

    
    
}
