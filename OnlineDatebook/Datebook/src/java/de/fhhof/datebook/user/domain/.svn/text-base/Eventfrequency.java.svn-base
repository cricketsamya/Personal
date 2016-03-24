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
@Table(name = "dbeventfrequency")
@NamedQueries({
    @NamedQuery(name = "Eventfrequency.findAll", query = "SELECT e FROM Eventfrequency e"),
    @NamedQuery(name = "Eventfrequency.findByIntFrequencyID", query = "SELECT e FROM Eventfrequency e WHERE e.intFrequencyID = :intFrequencyID"),
    @NamedQuery(name = "Eventfrequency.findByStrFrequencydescription", query = "SELECT e FROM Eventfrequency e WHERE e.strFrequencydescription = :strFrequencydescription")})
public class Eventfrequency implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "intFrequencyID", nullable = false)
    private Integer intFrequencyID;
    @Basic(optional = false)
    @Column(name = "strFrequencydescription", nullable = false, length = 45)
    private String strFrequencydescription;

    public Eventfrequency() {
    }

    public Eventfrequency(Integer intFrequencyID) {
        this.intFrequencyID = intFrequencyID;
    }

    public Eventfrequency(Integer intFrequencyID, String strFrequencydescription) {
        this.intFrequencyID = intFrequencyID;
        this.strFrequencydescription = strFrequencydescription;
    }

    public Integer getIntFrequencyID() {
        return intFrequencyID;
    }

    public void setIntFrequencyID(Integer intFrequencyID) {
        this.intFrequencyID = intFrequencyID;
    }

    public String getStrFrequencydescription() {
        return strFrequencydescription;
    }

    public void setStrFrequencydescription(String strFrequencydescription) {
        this.strFrequencydescription = strFrequencydescription;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (intFrequencyID != null ? intFrequencyID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Eventfrequency)) {
            return false;
        }
        Eventfrequency other = (Eventfrequency) object;
        if ((this.intFrequencyID == null && other.intFrequencyID != null) || (this.intFrequencyID != null && !this.intFrequencyID.equals(other.intFrequencyID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "de.fhhof.datebook.user.domain.Eventfrequency[intFrequencyID=" + intFrequencyID + "]";
    }

}
