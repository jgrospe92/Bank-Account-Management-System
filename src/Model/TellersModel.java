package Model;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TellersModel {
    
    private static int tellerId;
    private static String username;
    private static int password;
    private static String firstName;
    private static String lastName;
    private static Date lastLogin;

    // NOTE: SINGLETON ?
    private static TellersModel tellerInstance = null;

    // NOTE: Teller is static

    public TellersModel(ResultSet rs) throws SQLException{
        this.tellerId = rs.getInt("TellerId");
        this.username = rs.getString("Username");
        this.password = rs.getInt("Password");
        this.firstName = rs.getString("FirstName");
        this.lastName = rs.getString("LastName");
        this.lastLogin = rs.getDate("LastLogin");

    }

    public static int getTellerId() {
        return tellerId;
    }

    public static void setTellerId(int tellerId) {
        TellersModel.tellerId = tellerId;
    }

    public static String getUsername() {
        return username;
    }

    public static void setUsername(String username) {
        TellersModel.username = username;
    }

    public static int getPassword() {
        return password;
    }

    public static void setPassword(int password) {
        TellersModel.password = password;
    }

    public static String getFirstName() {
        return firstName;
    }

    public static void setFirstName(String firstName) {
        TellersModel.firstName = firstName;
    }

    public static String getLastName() {
        return lastName;
    }

    public static void setLastName(String lastName) {
        TellersModel.lastName = lastName;
    }

    public static Date getLastLogin() {
        return lastLogin;
    }

    public static void setLastLogin(Date lastLogin) {
        TellersModel.lastLogin = lastLogin;
    }

    
}
