package Data;

import com.microsoft.sqlserver.jdbc.SQLServerException;

import java.sql.*;

public class DBConnection {
    /*
        Creates a connection with the database, used by all repositories to run queries
     */

    //The URL to the database
    private String connectionUrl;

    //The connection
    private Connection con;

    //Statement that holds the query
    private Statement stmt;

    //ResultSet to get results
    private ResultSet rs;

    public DBConnection() {
        connectionUrl = "jdbc:sqlserver://localhost;databaseName=Netflix;integratedSecurity=true;";
    }

    //Given a query in String form, create a statement and execute it with the connection.
    //Returns the result in a ResultSet object
    public ResultSet runQuery(String sql) {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

            con = DriverManager.getConnection(connectionUrl);
            //Create a statement that can be scrolled forward and backward
            stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

            try {
                rs = stmt.executeQuery(sql);
            } catch (SQLServerException e) {
                //Update/Delete/Create queries will not give a resultset, which produces an error.
                //Exception can be ignored
            }
            return rs;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    //After the results have been retrieved from the ResultSet, all connections can be closed
    public void closeOperations() {
        if (rs != null) try { rs.close(); } catch(Exception e) {
            System.out.println("Could not close the ResultSet in class DBConnection\n");
            e.printStackTrace();
        }
        if (stmt != null) try { stmt.close(); } catch(Exception e) {
            System.out.println("Could not close the statement in class DBConnection\n");
            e.printStackTrace();
        }
        if (con != null) try { con.close(); } catch(Exception e) {
            System.out.println("Could not close the connection in class DBConnection\n");
            e.printStackTrace();
        }
    }
}
