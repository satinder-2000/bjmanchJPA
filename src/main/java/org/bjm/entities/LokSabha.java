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
@Table(name = "LokSabha")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "LokSabha.findAll", query = "SELECT l FROM LokSabha l"),
    @NamedQuery(name = "LokSabha.findById", query = "SELECT l FROM LokSabha l WHERE l.id = :id"),
    @NamedQuery(name = "LokSabha.findByStateCode", query = "SELECT l FROM LokSabha l WHERE l.stateCode = :stateCode"),
    @NamedQuery(name = "LokSabha.findByConstituency", query = "SELECT l FROM LokSabha l WHERE l.constituency = :constituency")})
public class LokSabha implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2)
    @Column(name = "stateCode")
    private String stateCode;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "constituency")
    private String constituency;

    public LokSabha() {
    }

    public LokSabha(Integer id) {
        this.id = id;
    }

    public LokSabha(Integer id, String stateCode, String constituency) {
        this.id = id;
        this.stateCode = stateCode;
        this.constituency = constituency;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getStateCode() {
        return stateCode;
    }

    public void setStateCode(String stateCode) {
        this.stateCode = stateCode;
    }

    public String getConstituency() {
        return constituency;
    }

    public void setConstituency(String constituency) {
        this.constituency = constituency;
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
        if (!(object instanceof LokSabha)) {
            return false;
        }
        LokSabha other = (LokSabha) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.bjm.entities.LokSabha[ id=" + id + " ]";
    }
    
}
