/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package healthcarion.dao;

import healtcarion.utils.DatabaseUtil;
import healtcarion.utils.Util;
import healthcarion.domain.Patient;
import healthcarion.domain.PatientHabitationHistory;
import healthcarion.domain.Zone;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Sameer
 */
public class DatabaseDao {

    public Patient putPatient(Patient oPatient) {
        Integer iResult = 0;
        ResultSet rs = null;
        try {
            rs = DatabaseUtil.getData("SELECT id,first_name,last_name FROM patient WHERE first_name = '" + oPatient.getFirstName() + "' AND last_name ='" + oPatient.getLastName() + "'");
            if (rs.next()) {
                iResult = DatabaseUtil.executeSQL("UPDATE patient SET first_name ='" + oPatient.getFirstName() + "',last_name ='" + oPatient.getLastName() + "' WHERE id =" + rs.getNString("id"));
            }

        } catch (SQLException sqlExp) {
            Util.log("Error in updating Patient Data");
        }
        if (iResult != null && iResult == 0) {
            try {
                iResult = DatabaseUtil.executeSQL("INSERT INTO patient(first_name,last_name) VALUES ('" + oPatient.getFirstName() + "'" + ",'" + oPatient.getLastName() + "')");
                rs = DatabaseUtil.getData("CALL IDENTITY();");
                if(rs.next()){
                    oPatient.setId(rs.getInt(1));
                }
            } catch (SQLException sqlExp) {
                Util.log("Error in inserting Patient Data");
            }
        }
        return oPatient;
    }

    public Patient getPatient(Integer id) {
        ResultSet rs = null;
        Patient oPatient = null;
        try {
            rs = DatabaseUtil.getData("SELECT id,first_name,last_name FROM patient WHERE id = " + id);
            while (rs.next()) {
                oPatient = new Patient(rs.getInt("id"), rs.getString("first_name"), rs.getString("last_name"));
            }
        } catch (SQLException sqlExp) {
            Util.log("Error in retrieving Patient Data");
        }
        return oPatient;
    }

    public Zone putHabitation(Zone oZone) {
        Integer iResult = null;
        try {
            iResult = DatabaseUtil.executeSQL("UPDATE zone SET name ='" + oZone.getName() + "' WHERE name = '" + oZone.getName() + "' OR id=" + oZone.getId());
        } catch (SQLException sqlExp) {
            Util.log("Error in updating zone Data");
        }
        if (iResult != null && iResult == 0) {
            try {
                iResult = DatabaseUtil.executeSQL("INSERT INTO zone(name) VALUES ('" + oZone.getName() + "')");
                 ResultSet         rs = DatabaseUtil.getData("CALL IDENTITY();");
                if(rs.next()){
                    oZone.setId(rs.getInt(1));
                }
            } catch (SQLException sqlExp) {
                Util.log("Error in inserting zone Data");
            }
        }
        return oZone;
    }

    public Zone getHabitation(Integer id) {
        ResultSet rs = null;
        Zone oZone = null;
        try {
            rs = DatabaseUtil.getData("SELECT id,name FROM zone WHERE id = " + id);
            while (rs.next()) {
                oZone = new Zone(rs.getInt("id"), rs.getString("name"));
            }
        } catch (SQLException sqlExp) {
            Util.log("Error in retrieving Zone Data");
        }
        return oZone;
    }

    public List<Patient> getPatientsInHabitation(Zone oZone) {
        ResultSet rs = null;
        List<Patient> oList = new ArrayList<Patient>();
        Patient oPatient = null;
        try {
            rs = DatabaseUtil.getData("SELECT id,first_name,last_name FROM patient p,zone z,patientzone pz "
                    + "WHERE p.id = pz.patient_id AND z.id = pz.zone_id AND z.id ='" + oZone.getId() + "'");
            while (rs.next()) {
                oPatient = new Patient(rs.getInt("id"), rs.getString("first_name"), rs.getString("last_name"));
                oList.add(oPatient);
            }
        } catch (SQLException sqlExp) {
            Util.log("Error in retrieving PatientsInHabitation Data");
        }
        return oList;
    }

    public Boolean isPatientInHabitation(Patient oPatient) {
        ResultSet rs = null;
        Boolean isPatientInHabitation = false;
        try {
            rs = DatabaseUtil.getData("SELECT id,first_name,last_name FROM patient p,patienthabitationhistory hist,patientzone pz "
                    + "WHERE p.id = pz.patient_id AND hist.zone_id = pz.zone_id AND p.id =" + oPatient.getId() + " AND hist.patient_id = p.id AND hist.iscurrentstate = 1 AND pz.zone_type='A'");
            if (rs.next()) {
                isPatientInHabitation = true;
            }
        } catch (SQLException sqlExp) {
            Util.log("Error in retrieving isPatientInHabitation Data");
        }
        return isPatientInHabitation;
    }

    public List<PatientHabitationHistory> getHabitationHistory(Patient oPatient) {
        ResultSet rs = null;
        PatientHabitationHistory oPatientHabitationHistory = null;
        Patient tmpPatient = null;
        Zone tmpZone = null;

        List<PatientHabitationHistory> listPatientHabitationHistory = new ArrayList<PatientHabitationHistory>();
        try {
            rs = DatabaseUtil.getData("SELECT p.id patient_id,p.first_name first_name,p.last_name last_name,"
                    + "z.id zone_id,z.name zone_name,lastTs lastTs,hist.iscurrentstate iscurrentstate FROM patient p,zone z,patienthabitationhistory hist "
                    + "WHERE p.id = hist.patient_id AND z.id = hist.zone_id AND p.id = " + oPatient.getId()+" ORDER BY lastTs desc");
            while (rs.next()) {
                Boolean isCurrentState = false;
                tmpPatient = new Patient(rs.getInt("patient_id"), rs.getString("first_name"), rs.getString("last_name"));
                tmpZone = new Zone(rs.getInt("zone_id"), rs.getString("name"));
                if (rs.getInt("iscurrentstate") == 1) {
                    isCurrentState = true;
                }
                oPatientHabitationHistory = new PatientHabitationHistory(rs.getInt("id"), tmpPatient, tmpZone, isCurrentState, rs.getTimestamp("lastTs"));
                listPatientHabitationHistory.add(oPatientHabitationHistory);
            }
        } catch (SQLException sqlExp) {
            Util.log("Error in retrieving PatientHabitationHistory Data");
        }
        return listPatientHabitationHistory;
    }
}
