/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package healthcarion.domain;

import java.sql.Timestamp;
import java.util.Date;

/**
 *
 * @author Sameer
 */
public class PatientHabitationHistory {

    public PatientHabitationHistory() {
        patient = new Patient();
        zone = new Zone();
    }

    public PatientHabitationHistory(Integer id, Patient patient, Zone zone,Boolean isCurrentState, Timestamp lastTimestamp) {
        this.id = id;
        this.patient = patient;
        this.zone = zone;
        this.lastTimestamp = lastTimestamp;
        this.isCurrentState = isCurrentState;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public Zone getZone() {
        return zone;
    }

    public void setZone(Zone zone) {
        this.zone = zone;
    }

    public Boolean getIsCurrentState() {
        return isCurrentState;
    }

    public void setIsCurrentState(Boolean isCurrentState) {
        this.isCurrentState = isCurrentState;
    }

    public Timestamp getLastTimestamp() {
        return lastTimestamp;
    }

    public void setLastTimestamp(Timestamp lastTimestamp) {
        this.lastTimestamp = lastTimestamp;
    }

    private Integer id;
    private Patient patient;
    private Zone zone;
    private Timestamp lastTimestamp;
    private Boolean isCurrentState;
    
}
