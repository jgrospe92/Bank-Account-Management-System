package Model;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TellersModel {

    private int tellerId;
    private String username;
    private int password;
    private String firstName;
    private String lastName;
    private Date lastLogin;

    // NOTE: Takes ResultSet
    public TellersModel(ResultSet rs) throws SQLException {
        this.tellerId = rs.getInt("TellerId");
        this.username = rs.getString("Username");
        this.password = rs.getInt("Password");
        this.firstName = rs.getString("FirstName");
        this.lastName = rs.getString("LastName");
        this.lastLogin = rs.getDate("LastLogin");

    }
    // NOTE: Takes user input
    public TellersModel(int tellerId, String username, int password, String firstName, String lastName,
            Date lastLogin) {
        this.tellerId = tellerId;
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.lastLogin = lastLogin;
    }

    public int getTellerId() {
        return tellerId;
    }

    public void setTellerId(int tellerId) {
        this.tellerId = tellerId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getPassword() {
        return password;
    }

    public void setPassword(int password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Date getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(Date lastLogin) {
        this.lastLogin = lastLogin;
    }

    @Override
    public String toString() {
        return "TellersModel [firstName=" + firstName + ", lastLogin=" + lastLogin + ", lastName=" + lastName
                + ", password=" + password + ", tellerId=" + tellerId + ", username=" + username + "]";
    }

}
