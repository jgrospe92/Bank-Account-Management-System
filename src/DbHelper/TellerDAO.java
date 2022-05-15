package DbHelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import Model.TellersModel;

public class TellerDAO {

    public static TellersModel getTellerById(int tellerId) {
        try {
            Connection con = DbConnector.createConnection();
            String sql = "SELECT *" +
                    "FROM Teller WHERE TellerId=?";
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, tellerId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
               
                return new TellersModel(rs);
            }
            stmt.close();
            return null;
        } catch (SQLException e) {
            System.out.println("Error retrieving Teller Data [" + e.getMessage() + "]");
            return null;
        }
      
    }

    public static TellersModel getTellerByUserAndPass(String username, int pass) {
        try {
            Connection con = DbConnector.createConnection();
            String sql = "SELECT *" +
                    "FROM Teller WHERE username=? AND  password=?";
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, username);
            stmt.setInt(2, pass);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                return new TellersModel(rs);
            }
            stmt.close();
        } catch (SQLException e) {
            System.out.println("Error retrieving Teller Data [" + e.getMessage() + "]");
        }
        return null;
    }

    public static List<TellersModel> getAllTellers() {

        List<TellersModel> tellers = new ArrayList<>();

        try {
            Connection con = DbConnector.createConnection();
            TellersModel teller = null;
            String sql = "SELECT * FROM Teller ORDER BY 1";
            PreparedStatement stmt = con.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while(rs.next()) {
                teller = new TellersModel(rs);
                tellers.add(teller);
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving Teller data [" + e.getMessage() + "]");
            return null;
        }
        return tellers;
    }

    public static void insertNewTeller(TellersModel teller) {

        Connection con = DbConnector.createConnection();
        try {
            con.setAutoCommit(false);

            String sql = "INSERT INTO Teller (tellerId, username, password, firstName, lastName, lastLogin" +
                    "VALUES (?,?,?,?,?,?)";
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, teller.getTellerId());
            stmt.setString(2, teller.getUsername());
            stmt.setInt(3, teller.getPassword());
            stmt.setString(4, teller.getFirstName());
            stmt.setString(5, teller.getLastName());
            stmt.setDate(6, teller.getLastLogin());

            stmt.executeUpdate();
            stmt.close();
            con.commit();
        } catch (SQLException e) {
            System.out.println("Error retrieving teller [" + e.getMessage() + "]");
            try {
                con.rollback();
                System.out.println("ROLLING BACK CHANGES");
            } catch (SQLException ex) {
                System.out.println("Error rolling back [" + ex.getMessage() + "]");
            }
        }
    }

    public  static void updateLoginDate(int id){
        try {
            Connection con = DbConnector.createConnection();
            String sql = "UPDATE Teller SET LastLogin=?"+
                        " WHERE TellerId =?";
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, getDateTime());
            stmt.setInt(2, id);
            stmt.executeUpdate();
            stmt.close();
        }
        catch (SQLException e){
            System.out.println("Error updating last login date [" + e.getMessage() + "]");
        }

    }

    public  static String getFormattedDate(int id){
        String formattedDate = "";
        try {
            Connection con = DbConnector.createConnection();
            String sql = "SELECT LastLogin FROM Teller"+
                        " WHERE TellerId =?";
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            formattedDate = rs.getString("LastLogin");
            stmt.close();
        }
        catch (SQLException e){
            System.out.println("Error retrieving  last login date [" + e.getMessage() + "]");
        }
        return formattedDate;

    }
    private static String getDateTime(){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("YYYY-MM-dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        String date = dtf.format(now);
        return date;
    }
}
