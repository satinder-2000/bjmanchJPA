/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.bjm.entities;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 *
 * @author singh
 */
@Entity
@Table(name = "SurveyCategory")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SurveyCategory.findAll", query = "SELECT s FROM SurveyCategory s"),
    @NamedQuery(name = "SurveyCategory.findById", query = "SELECT s FROM SurveyCategory s WHERE s.id = :id"),
    @NamedQuery(name = "SurveyCategory.findByType", query = "SELECT s FROM SurveyCategory s WHERE s.type = :type"),
    @NamedQuery(name = "SurveyCategory.findBySubType", query = "SELECT s FROM SurveyCategory s WHERE s.subType = :subType"),
    @NamedQuery(name = "SurveyCategory.findByDescription", query = "SELECT s FROM SurveyCategory s WHERE s.description = :description")})
public class SurveyCategory implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "type")
    private String type;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "subType")
    private String subType;
    @Size(max = 150)
    @Column(name = "description")
    private String description;

    public SurveyCategory() {
    }

    public SurveyCategory(Integer id) {
        this.id = id;
    }

    public SurveyCategory(Integer id, String type, String subType) {
        this.id = id;
        this.type = type;
        this.subType = subType;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSubType() {
        return subType;
    }

    public void setSubType(String subType) {
        this.subType = subType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SurveyCategory)) {
            return false;
        }
        SurveyCategory other = (SurveyCategory) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.bjm.entities.SurveyCategory[ id=" + id + " ]";
    }
    
}
