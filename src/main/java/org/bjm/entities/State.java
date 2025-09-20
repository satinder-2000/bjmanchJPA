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
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 *
 * @author singh
 */
@Entity
@Table(name = "State")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "State.findAll", query = "SELECT s FROM State s"),
    @NamedQuery(name = "State.findByPostCodePrefix", query = "SELECT s FROM State s WHERE s.postCodePrefix = :postCodePrefix"),
    @NamedQuery(name = "State.findByName", query = "SELECT s FROM State s WHERE s.name = :name"),
    @NamedQuery(name = "State.findByCode", query = "SELECT s FROM State s WHERE s.code = :code"),
    @NamedQuery(name = "State.findById", query = "SELECT s FROM State s WHERE s.id = :id")})
public class State implements Serializable {

    private static final long serialVersionUID = 1L;
    @Size(max = 8)
    @Column(name = "postCodePrefix")
    private String postCodePrefix;
    @Size(max = 100)
    @Column(name = "name")
    private String name;
    @Size(max = 2)
    @Column(name = "code")
    private String code;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;

    public State() {
    }

    public State(Integer id) {
        this.id = id;
    }

    public String getPostCodePrefix() {
        return postCodePrefix;
    }

    public void setPostCodePrefix(String postCodePrefix) {
        this.postCodePrefix = postCodePrefix;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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
        if (!(object instanceof State)) {
            return false;
        }
        State other = (State) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "org.bjm.entities.State[ id=" + id + " ]";
    }
    
}
