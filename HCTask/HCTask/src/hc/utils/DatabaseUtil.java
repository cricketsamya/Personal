/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package healtcarion.utils;

import healthcarion.dao.DatabaseDao;
import healthcarion.domain.Patient;
import healthcarion.domain.Zone;
import java.sql.Statement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author Sameer
 */
public class DatabaseUtil {

    private static Connection dbConnection = null;

    static {
        dbConnection = getConnection();
    }

    private static Connection getConnection() {
        String DB_CONN_STRING = "jdbc:hsqldb:mem:patientDB";
        String DRIVER_CLASS_NAME = "org.hsqldb.jdbc.JDBCDriver";
        String USER_NAME = "SA";
        String PASSWORD = "";

        Connection result = null;
        try {
            Class.forName(DRIVER_CLASS_NAME).newInstance();
        } catch (Exception ex) {
            Util.log("Check classpath. Cannot load db driver: " + DRIVER_CLASS_NAME);
        }

        try {
            result = DriverManager.getConnection(DB_CONN_STRING, USER_NAME, PASSWORD);
            Util.log("DB Created");
        } catch (SQLException e) {
            Util.log("Driver loaded, but cannot connect to db: " + DB_CONN_STRING);
        }
        return result;
    }

    public static Connection getDBConnection() {
        return dbConnection;
    }

    public static void generateTables() {
        //CREATING TABLES
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            statement = dbConnection.createStatement();
            statement.executeUpdate("CREATE TABLE patient( id INTEGER IDENTITY, first_name VARCHAR(64) NOT NULL, last_name VARCHAR(64) NOT NULL );");
            statement.executeUpdate("CREATE TABLE zone( id INTEGER IDENTITY, name VARCHAR(64));");
            statement.executeUpdate("CREATE TABLE patientzone( id INTEGER IDENTITY, patient_id INTEGER, zone_id INTEGER,zone_type VARCHAR(1),FOREIGN KEY (patient_id) REFERENCES patient(id),FOREIGN KEY (zone_id) REFERENCES zone(id));");
            statement.executeUpdate("CREATE TABLE patienthabitationhistory( id INTEGER IDENTITY, patient_id INTEGER, zone_id INTEGER,iscurrentstate INTEGER, lastTS TIMESTAMP DEFAULT CURRENT_TIMESTAMP,FOREIGN KEY (patient_id) REFERENCES patient(id),FOREIGN KEY (zone_id) REFERENCES zone(id));");
            Util.log("Tables created..!!");
        } catch (SQLException sqExp) {
        } finally {
            try {
                statement.close();
            } catch (SQLException ex) {
            }
        }
    }

    public static void generateSampleData() {
        //CREATING TABLES
        Statement statement = null;
        ResultSet resultSet = null;
        DatabaseDao oDatabaseDao = new DatabaseDao();
        try {
            statement = dbConnection.createStatement();
            oDatabaseDao.putPatient(new Patient("Sameer", "Kulkarni"));
            oDatabaseDao.putPatient(new Patient("John", "Cena"));
            oDatabaseDao.putPatient(new Patient("Eric", "Bishop"));
            oDatabaseDao.putPatient(new Patient("King", "Ace"));
            oDatabaseDao.putPatient(new Patient("Brother", "Big"));

            oDatabaseDao.putHabitation(new Zone("Ward I"));
            oDatabaseDao.putHabitation(new Zone("Ward II"));
            oDatabaseDao.putHabitation(new Zone("Balcony"));
            oDatabaseDao.putHabitation(new Zone("Parking"));
            oDatabaseDao.putHabitation(new Zone("Toilet"));
            oDatabaseDao.putHabitation(new Zone("Basement"));
            oDatabaseDao.putHabitation(new Zone("OT"));

            executeSQL("INSERT INTO patientzone (patient_id, zone_id,zone_type) VALUES (0,1,'A')");
            executeSQL("INSERT INTO patientzone (patient_id, zone_id,zone_type) VALUES (1,1,'A')");
            executeSQL("INSERT INTO patientzone (patient_id, zone_id,zone_type) VALUES (2,0,'A')");
            executeSQL("INSERT INTO patientzone (patient_id, zone_id,zone_type) VALUES (3,0,'A')");
            executeSQL("INSERT INTO patientzone (patient_id, zone_id,zone_type) VALUES (4,1,'A')");

            executeSQL("INSERT INTO patientzone (patient_id,zone_id,zone_type) VALUES (0,2,'R')");
            executeSQL("INSERT INTO patientzone (patient_id,zone_id,zone_type) VALUES (0,4,'R')");
            executeSQL("INSERT INTO patientzone (patient_id,zone_id,zone_type) VALUES (0,6,'R')");
            executeSQL("INSERT INTO patientzone (patient_id,zone_id,zone_type) VALUES (1,4,'R')");
            executeSQL("INSERT INTO patientzone (patient_id,zone_id,zone_type) VALUES (1,6,'R')");
            executeSQL("INSERT INTO patientzone (patient_id,zone_id,zone_type) VALUES (2,4,'R')");
            executeSQL("INSERT INTO patientzone (patient_id,zone_id,zone_type) VALUES (3,2,'R')");
            executeSQL("INSERT INTO patientzone (patient_id,zone_id,zone_type) VALUES (3,4,'R')");
            executeSQL("INSERT INTO patientzone (patient_id,zone_id,zone_type) VALUES (4,2,'R')");



            executeSQL("INSERT INTO patienthabitationhistory (patient_id, zone_id,iscurrentstate) VALUES (0,1,1)");
            executeSQL("INSERT INTO patienthabitationhistory (patient_id, zone_id,iscurrentstate) VALUES (1,1,1)");
            executeSQL("INSERT INTO patienthabitationhistory (patient_id, zone_id,iscurrentstate) VALUES (2,0,1)");
            executeSQL("INSERT INTO patienthabitationhistory (patient_id, zone_id,iscurrentstate) VALUES (3,0,1)");
            executeSQL("INSERT INTO patienthabitationhistory (patient_id, zone_id,iscurrentstate) VALUES (4,1,1)");
            Util.log("Rows INSERTED created..!!");

            
        } catch (SQLException sqExp) {
            sqExp.printStackTrace();
        } finally {
            try {
                statement.close();
            } catch (SQLException ex) {
            }
        }
    }

    public static Integer executeSQL(String strSql) throws SQLException {
        Statement statement = null;
        Integer iResult = null;
        try {

            statement = dbConnection.createStatement();
            iResult = statement.executeUpdate(strSql);

        } catch (SQLException sqlExp) {
            Util.log(sqlExp.getMessage());
            throw sqlExp;
        }
        return iResult;
    }

    public static ResultSet getData(String strSql) throws SQLException {
        Statement statement = null;
        ResultSet resultSet = null;
        try {

            statement = dbConnection.createStatement();
            resultSet = statement.executeQuery(strSql);

        } catch (SQLException sqlExp) {
            Util.log(sqlExp.getMessage());
            throw sqlExp;
        }
        return resultSet;
    }
}
