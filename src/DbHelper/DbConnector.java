package DbHelper;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DbConnector {
    
    private static Connection con;
    // Singleton class
    public static Connection createConnection(){
        if(con == null){
            try {
                Class.forName("org.sqlite.JDBC");
                con = DriverManager.getConnection("jdbc:sqlite:src/DataBase/Bankdb.db");
            }
            catch (ClassNotFoundException e){
                System.out.println("SQL DRIVER IS NOT FOUND [" +e+"]");
                System.exit(0);
            }
            catch (SQLException ex){
                System.out.println("SQL EXCEPTION [" +ex+"]");
            } 
        }
        return con;
    }

}
