/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package de.fhhof.datebook.user.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 *
 * @author Sumedh
 */
@Entity
@Table(name = "dbuserdetails")
@NamedQueries({
    @NamedQuery(name = "Userdetails.findAll", query = "SELECT u FROM Userdetails u"),
    @NamedQuery(name = "Userdetails.findByIntUserID", query = "SELECT u FROM Userdetails u WHERE u.intUserID = :intUserID"),
    @NamedQuery(name = "Userdetails.findByStrFirstname", query = "SELECT u FROM Userdetails u WHERE u.strFirstname = :strFirstname"),
    @NamedQuery(name = "Userdetails.findByStrLastname", query = "SELECT u FROM Userdetails u WHERE u.strLastname = :strLastname"),
    @NamedQuery(name = "Userdetails.findByStrEmailID", query = "SELECT u FROM Userdetails u WHERE u.strEmailID = :strEmailID"),
    @NamedQuery(name = "Userdetails.findByStrPassword", query = "SELECT u FROM Userdetails u WHERE u.strPassword = :strPassword"),
    @NamedQuery(name = "Userdetails.findByStrEmailIDAndStrPassword", query = "SELECT u FROM Userdetails u WHERE u.strEmailID = :strEmailID AND u.strPassword = :strPassword")})

    public class Userdetails implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "intUserID", nullable = false)
    private Integer intUserID;
    @Basic(optional = false)
    @Column(name = "strFirstname", nullable = false, length = 45)
    private String strFirstname;
    @Basic(optional = false)
    @Column(name = "strLastname", nullable = false, length = 45)
    private String strLastname;
    @Basic(optional = false)
    @Column(name = "strEmailID", nullable = false, length = 50)
    private String strEmailID;
    @Basic(optional = false)
    @Column(name = "strPassword", nullable = false, length = 45)
    private String strPassword;

    @Transient
    private String strErrorMessage;

    @Transient
    private String strConfirmPassword;
    
    public Userdetails() {
        strErrorMessage = "";
        strConfirmPassword = "";
    }

    public Userdetails(Integer intUserID) {
        this.intUserID = intUserID;
    }

    public Userdetails(Integer intUserID, String strFirstname, String strLastname, String strEmailID, String strPassword) {
        this.intUserID = intUserID;
        this.strFirstname = strFirstname;
        this.strLastname = strLastname;
        this.strEmailID = strEmailID;
        this.strPassword = strPassword;
    }

    public Integer getIntUserID() {
        return intUserID;
    }

    public void setIntUserID(Integer intUserID) {
        this.intUserID = intUserID;
    }

    public String getStrFirstname() {
        return strFirstname;
    }

    public void setStrFirstname(String strFirstname) {
        this.strFirstname = strFirstname;
    }

    public String getStrLastname() {
        return strLastname;
    }

    public void setStrLastname(String strLastname) {
        this.strLastname = strLastname;
    }

    public String getStrEmailID() {
        return strEmailID;
    }

    public void setStrEmailID(String strEmailID) {
        this.strEmailID = strEmailID;
    }

    public String getStrErrorMessage() {
        return strErrorMessage;
    }

    public void setStrErrorMessage(String strErrorMessage) {
        this.strErrorMessage = strErrorMessage;
    }

    public String getStrConfirmPassword() {
        return strConfirmPassword;
    }

    public void setStrConfirmPassword(String strConfirmPassword) {
        this.strConfirmPassword = strConfirmPassword;
    }

    public String getStrPassword() {
        return strPassword;
    }

    public void setStrPassword(String strPassword) {
        this.strPassword = strPassword;
    }

    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (intUserID != null ? intUserID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Userdetails)) {
            return false;
        }
        Userdetails other = (Userdetails) object;
        if ((this.intUserID == null && other.intUserID != null) || (this.intUserID != null && !this.intUserID.equals(other.intUserID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "de.fhhof.datebook.user.domain.Userdetails[intUserID=" + intUserID + "]";
    }


}
