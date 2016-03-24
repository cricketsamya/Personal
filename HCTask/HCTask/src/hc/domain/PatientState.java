/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package healthcarion.domain;

/**
 *
 * @author Sameer
 */
public class PatientState {

    public PatientState() {
        patient = new Patient();
        zone = new Zone();
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


    private Integer id;
    private Patient patient;
    private Zone zone;
}
