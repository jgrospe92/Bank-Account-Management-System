package DbHelper;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DbConnector {
    
    // Singleton class
    public static Connection createConnection(){

        try {
            Class.forName("org.sqlite.JDBC");
            Connection con = DriverManager.getConnection("jdbc:sqlite:src/DataBase/Bankdb.db");
          
            return con;
        }
        catch (ClassNotFoundException e){
            System.out.println("SQL DRIVER IS NOT FOUND [" +e+"]");
            System.exit(0);
        }
        catch (SQLException ex){
            System.out.println("SQL EXCEPTION [" +ex+"]");
        } 
        System.out.println("Connection Failed"); 
        return null;
    }

}
