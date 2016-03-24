/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.fhhof.datebook.user.domain;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author Sumedh
 */
@Entity
@Table(name = "dbeventtype")
@NamedQueries({
    @NamedQuery(name = "Eventtype.findAll", query = "SELECT e FROM Eventtype e"),
    @NamedQuery(name = "Eventtype.findByIntEventtypeID", query = "SELECT e FROM Eventtype e WHERE e.intEventtypeID = :intEventtypeID"),
    @NamedQuery(name = "Eventtype.findByStrEventtype", query = "SELECT e FROM Eventtype e WHERE e.strEventtype = :strEventtype")})
public class Eventtype implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "intEventtypeID", nullable = false)
    private Integer intEventtypeID;
    @Basic(optional = false)
    @Column(name = "strEventtype", nullable = false, length = 45)
    private String strEventtype;

    public Eventtype() {
    }

    public Eventtype(Integer intEventtypeID) {
        this.intEventtypeID = intEventtypeID;
    }

    public Eventtype(Integer intEventtypeID, String strEventtype) {
        this.intEventtypeID = intEventtypeID;
        this.strEventtype = strEventtype;
    }

    public Integer getIntEventtypeID() {
        return intEventtypeID;
    }

    public void setIntEventtypeID(Integer intEventtypeID) {
        this.intEventtypeID = intEventtypeID;
    }

    public String getStrEventtype() {
        return strEventtype;
    }

    public void setStrEventtype(String strEventtype) {
        this.strEventtype = strEventtype;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (intEventtypeID != null ? intEventtypeID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Eventtype)) {
            return false;
        }
        Eventtype other = (Eventtype) object;
        if ((this.intEventtypeID == null && other.intEventtypeID != null) || (this.intEventtypeID != null && !this.intEventtypeID.equals(other.intEventtypeID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "de.fhhof.datebook.user.domain.Eventtype[intEventtypeID=" + intEventtypeID + "]";
    }

}
