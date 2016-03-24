/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package healthcarion;

import healtcarion.utils.DatabaseUtil;
import healtcarion.utils.Util;
import healthcarion.dao.DatabaseDao;
import healthcarion.domain.Patient;
import healthcarion.domain.Zone;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;

/**
 *
 * @author Sameer
 */
public class Test {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException, SQLException {
        DatabaseUtil.getDBConnection();
        DatabaseDao oDatabaseDao = new DatabaseDao();
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        DatabaseUtil.generateTables();
        DatabaseUtil.generateSampleData();
        try {
            Util.log(oDatabaseDao.getPatient(1).getFirstName());
            Util.log(oDatabaseDao.getHabitation(1).getName());
            Zone oZone = new Zone("Ward I");
            Util.log(oDatabaseDao.getPatientsInHabitation(oZone));
            Patient oPatient = oDatabaseDao.getPatient(0);
            Util.log(oDatabaseDao.isPatientInHabitation(oPatient));
            Util.log(oDatabaseDao.getHabitationHistory(oPatient));
        } catch (Exception ex) {
        }
        br.readLine();
    }
}
