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
@Table(name = "VidhanSabha")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "VidhanSabha.findAll", query = "SELECT v FROM VidhanSabha v"),
    @NamedQuery(name = "VidhanSabha.findById", query = "SELECT v FROM VidhanSabha v WHERE v.id = :id"),
    @NamedQuery(name = "VidhanSabha.findByStateCode", query = "SELECT v FROM VidhanSabha v WHERE v.stateCode = :stateCode"),
    @NamedQuery(name = "VidhanSabha.findByConstituency", query = "SELECT v FROM VidhanSabha v WHERE v.constituency = :constituency")})
public class VidhanSabha implements Serializable {

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

    public VidhanSabha() {
    }

    public VidhanSabha(Integer id) {
        this.id = id;
    }

    public VidhanSabha(Integer id, String stateCode, String constituency) {
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
        if (!(object instanceof VidhanSabha)) {
            return false;
        }
        VidhanSabha other = (VidhanSabha) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.bjm.entities.VidhanSabha[ id=" + id + " ]";
    }
    
}
