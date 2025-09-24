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
    @NamedQuery(name = "Blog.findByBlogId", query = "SELECT b FROM Blog b WHERE b.id = :id"),
    @NamedQuery(name = "Blog.findByUserId", query = "SELECT b FROM Blog b WHERE b.publishedByAccessId = :accessId"),
    @NamedQuery(name = "Blog.findByEmail", query = "SELECT b FROM Blog b WHERE b.publishedByEmail = :email"),
    @NamedQuery(name = "Blog.findNBlogs", query = "SELECT b FROM Blog b ORDER BY id DESC LIMIT :count")
})
public class Blog {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private Timestamp publishedOn;
    private String title;
    private String summary;
    private String text;
    private String publishedByAccessId;
    private String publishedByEmail;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Timestamp getPublishedOn() {
        return publishedOn;
    }

    public void setPublishedOn(Timestamp publishedOn) {
        this.publishedOn = publishedOn;
    }

    

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getPublishedByAccessId() {
        return publishedByAccessId;
    }

    public void setPublishedByAccessId(String publishedByAccessId) {
        this.publishedByAccessId = publishedByAccessId;
    }

    public String getPublishedByEmail() {
        return publishedByEmail;
    }

    public void setPublishedByEmail(String publishedByEmail) {
        this.publishedByEmail = publishedByEmail;
    }
    
    
}
