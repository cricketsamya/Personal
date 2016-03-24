/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package healthcarion;

import healtcarion.utils.DatabaseUtil;
import healthcarion.dao.DatabaseDao;
import healthcarion.domain.Patient;
import healthcarion.domain.PatientHabitationHistory;
import healthcarion.domain.Zone;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.ResultSet;
import java.util.List;

/**
 *
 * @author Sameer
 */
public class StartUp {

    public static void display() {
        try {

            ResultSet rs = DatabaseUtil.getData("SELECT hist.patient_id patient_id,p.first_name first_name,z.name name "
                    + "FROM patient p LEFT OUTER JOIN patienthabitationhistory hist on p.id = hist.patient_id "
                    + "LEFT OUTER JOIN zone z "
                    + "  ON hist.zone_id = z.id WHERE hist.iscurrentstate = 1");
            Integer i = 0;
            while (rs.next()) {
                if (i == 0) {
                    System.out.println("Patient ID\tFirstName\t\tZONE\t");
                    i++;
                }
                System.out.println(rs.getString("patient_id") + "\t" + rs.getString("first_name") + "\t" + rs.getString("name"));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }


    }

    public static void alarm() {
        try {

            ResultSet rs = DatabaseUtil.getData("SELECT hist.patient_id patient_id,p.first_name first_name,p.last_name last_name,z.name name FROM patienthabitationhistory hist LEFT OUTER JOIN "
                    + "patientzone pz  "
                    + "ON hist.zone_id = pz.zone_id LEFT OUTER JOIN patient p ON hist.patient_id = p.id LEFT OUTER JOIN zone z ON hist.zone_id = z.id WHERE pz.patient_id is null AND hist.iscurrentstate = 1");
            Integer i = 0;
            while (rs.next()) {
                if (i == 0) {
                    System.out.println("*********");
                    System.out.println("WARNING");
                    System.out.println("*********");
                    System.out.println("FIRST NAME\t\tLASTNAME\t\tZONE\t");
                    i++;
                }
                System.out.println(rs.getString("first_name") + "\t\t" + rs.getString("last_name") + "\t\t" + rs.getString("name"));
            }
        } catch (Exception ex) {
        }


    }

    public static void main(String[] args) throws IOException {
        DatabaseUtil.getDBConnection();

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        DatabaseUtil.generateTables();
        DatabaseUtil.generateSampleData();
        DatabaseDao oDatabaseDao = new DatabaseDao();

        while (true) {
            alarm();
            System.out.println("");
            System.out.println("MENU");
            System.out.println("1. Put patient");
            System.out.println("2. Get patient");
            System.out.println("3. Display history");
            System.out.println("4. Put zone");
            System.out.println("5. Get zone");
            System.out.println("6. Move patient");
            System.out.println("7. getPatientsInHabitation");
            System.out.println("8. isPatientInHabitation");
            System.out.println("9. getHabitationHistory");
            System.out.println("0. Exit");
            System.out.println("Enter option :");
            try {
                Integer option = Integer.parseInt(br.readLine());
                switch (option) {
                    case 1:
                        Patient oPatient = new Patient();
                        System.out.print("Enter patient name : ");
                        oPatient.setFirstName(br.readLine());
                        System.out.print("Enter patient lastname : ");
                        oPatient.setLastName(br.readLine());
                        oPatient = oDatabaseDao.putPatient(oPatient);

                        DatabaseUtil.executeSQL("INSERT INTO patientzone (patient_id, zone_id,zone_type) VALUES (" + oPatient.getId() + ",1,'A')");
                        DatabaseUtil.executeSQL("INSERT INTO patientzone (patient_id,zone_id,zone_type) VALUES (" + oPatient.getId() + ",2,'R')");
                        DatabaseUtil.executeSQL("INSERT INTO patientzone (patient_id,zone_id,zone_type) VALUES (" + oPatient.getId() + ",4,'R')");
                        DatabaseUtil.executeSQL("INSERT INTO patienthabitationhistory (patient_id, zone_id,iscurrentstate) VALUES (" + oPatient.getId() + ",1,1)");
                        System.out.print("Patient added");
                        break;
                    case 2:
                        System.out.println("Enter patient id : ");
                        Integer patientID = Integer.parseInt(br.readLine());
                        oPatient = oDatabaseDao.getPatient(patientID);
                        System.out.println("Patients details");
                        System.out.println(oPatient.getFirstName() + " " + oPatient.getLastName());
                        break;
                    case 3:
                        display();
                        break;
                    case 4:
                        Zone oZone = new Zone();
                        System.out.print("Enter zone name : ");
                        oZone.setName(br.readLine());
                        oDatabaseDao.putHabitation(oZone);
                        System.out.print("Zone added");
                        break;
                    case 5:

                        System.out.println("Enter zone id : ");
                        Integer zoneId = Integer.parseInt(br.readLine());
                        oZone = oDatabaseDao.getHabitation(zoneId);
                        System.out.println("Patients details");
                        System.out.println(oZone.getName());
                        break;
                    case 6:
                        System.out.println("Enter patient Id : ");
                        patientID = Integer.parseInt(br.readLine());
                        System.out.println("Enter Zone Id : ");
                        Integer zoneID = Integer.parseInt(br.readLine());
                        DatabaseUtil.executeSQL("UPDATE patienthabitationhistory SET iscurrentstate = 0 WHERE iscurrentstate = 1 AND patient_id=" + patientID);
                        DatabaseUtil.executeSQL("INSERT INTO patienthabitationhistory (patient_id, zone_id,iscurrentstate) VALUES (" + patientID + "," + zoneID + ",1)");
                        break;
                    case 7:
                        System.out.print("Enter zone id : ");
                        zoneId = Integer.parseInt(br.readLine());
                        oZone = oDatabaseDao.getHabitation(zoneId);
                        List<Patient> listPatient = oDatabaseDao.getPatientsInHabitation(oZone);
                        System.out.println("Patients in " + oZone.getName());
                        for (Patient p : listPatient) {
                            System.out.println(p.getFirstName() + " " + p.getLastName());
                        }
                        break;
                    case 8:
                        System.out.println("Enter patient Id : ");
                        patientID = Integer.parseInt(br.readLine());
                        oPatient = oDatabaseDao.getPatient(patientID);
                        Boolean isPatientInHabitation = oDatabaseDao.isPatientInHabitation(oPatient);
                        if (isPatientInHabitation) {
                            System.out.println(oPatient.getFirstName() + " is in habitation ");
                        } else {
                            System.out.println(oPatient.getFirstName() + " is not in habitation ");
                        }
                        break;
                    case 9:
                        System.out.println("Enter patient Id : ");
                        patientID = Integer.parseInt(br.readLine());
                        oPatient = oDatabaseDao.getPatient(patientID);
                        List<PatientHabitationHistory> listPatientHabitationHistory = oDatabaseDao.getHabitationHistory(oPatient);
                        System.out.println("FIRST NAME\t\tLASTNAME\t\tZONE\tTIME\t\t\tCURRENT ZONE");
                        String str = "";
                        for (PatientHabitationHistory p : listPatientHabitationHistory) {
                            str = "";
                            if (p.getIsCurrentState()) {
                                str = "Yes";
                            }
                            System.out.println(p.getPatient().getFirstName() + "\t\t" + p.getPatient().getLastName() + "\t"
                                    + p.getZone().getName() + "\t" + p.getLastTimestamp() + "\t" + str);

                        }

                        break;
                    default:
                        System.exit(0);
                }

            } catch (Exception ex) {
            }

        }

    }
}
