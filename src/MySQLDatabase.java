import java.sql.*;
import java.util.ArrayList;
import java.util.*;

public class MySQLDatabase {
    String uri = "jdbc:mysql://localhost/ReasearchDb?autoReconnect=true&useSSL=false&useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
    String driver = "com.mysql.cj.jdbc.Driver";
    String user = "root";
    String password = "student";

    Connection conn = null;

    MySQLDatabase() {
    }

    /**
     * Connects to the database
     *
     * @return boolean True if sucessful, false if any errors
     */
    public boolean connect() {
        try {
            Class.forName(driver);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        try {
            conn = DriverManager.getConnection(uri, user, password);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * Close the connection to the databse
     *
     * @return boolean True if sucessful, false if any errors
     */
    public boolean close() {
        try {
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * Get Data
     * Takes SQL string and values for string to create a prepared statement
     *
     * @param sql
     * @param values
     * @return array 2D array list of information
     */
    public ArrayList<ArrayList<String>> getData(String sql, ArrayList<String> values) {
        ArrayList<ArrayList<String>> array = new ArrayList<ArrayList<String>>();
        try {
            ResultSet rs = null;
            if (values != null) {
                PreparedStatement stmt = prepare(sql, values);
                rs = stmt.executeQuery();
            } else {
                Statement stmt = conn.createStatement();
                rs = stmt.executeQuery(sql);
            }
            ResultSetMetaData rsmd = rs.getMetaData();
            int fields = rsmd.getColumnCount();
            int row = 0;

            while (rs.next()) {
                array.add(new ArrayList<String>());
                for (int i = 1; i <= fields; i++) {
                    array.get(row).add(rs.getString(i));
                }
                row++;
            }

            return array;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Takes SQL string and values for string to create a prepared statement
     *
     * @param sql
     * @param values
     * @return -1 if an error
     */
    public int setData(String sql, ArrayList<String> values) {
        try {
            PreparedStatement stmnt = prepare(sql, values);
            return stmnt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            return -1;//Error
        }
    }

    /**
     * Prepared Statement method
     *
     * @param sql
     * @param values
     * @return null
     */
    private PreparedStatement prepare(String sql, ArrayList<String> values) {
        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            int count = 1;
            for (String value : values) {
                stmt.setString(count, value);
                count++;
            }
            return stmt;
        } catch (Exception e) {
            return null;
        }
    }


    /**
     * Start transaction method
     */
    public void startTrans() {
        try {
            conn.setAutoCommit(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Rollback method
     */
    public void rollbackTrans() {
        try {
            conn.rollback();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * End transaction method
     */
    public void endTrans() {
        try {
            conn.commit();
            conn.setAutoCommit(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}