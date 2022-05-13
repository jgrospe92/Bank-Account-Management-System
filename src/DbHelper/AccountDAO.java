package DbHelper;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import Model.AccountsModel;

public class AccountDAO {

    public AccountsModel getAccount(int accountNumber){
        try {
            Connection con = DbConnector.createConnection();
            String sql = "SELECT AccountNumber, ClientId, AccountType, OpenDate, Balance, IsActive"+
                        "FROM Accounts WHERE AccountNumber=?";
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, accountNumber);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()){
                return new AccountsModel(rs);
            }
            return null;
        } catch (SQLException e) {
            System.out.println("Error retrieving account [" + e.getMessage() + "]");
            return null;
        }
     
    }

    public static boolean saveOrUpdateAccount(AccountsModel account){
        if (account == null) {return true;}
        Connection con = DbConnector.createConnection();
        try {

            con.setAutoCommit(false);
       
            String sql = "INSERT INTO Accounts (accountNumber, clientId, accountType, openDate, balance, isActive)"
                        + "VALUES (?,?,?,?,?,?) ON CONFLICT(accountNumber) DO UPDATE "
                        + "SET clientId=?, accountType=?, openDate=?, balance=?, isActive=?";
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, account.getAccountNumber());
            stmt.setInt(2, account.getClientId());
         
            stmt.setString(3, account.getAccountType());
            stmt.setDate(4, account.getOpenDate());
            stmt.setInt(5, account.getBalance());
            stmt.setBoolean(6, account.isActive());
            // IF CONFLICT UPDATE
            stmt.setInt(7, account.getClientId());
            stmt.setString(8, account.getAccountType());
            stmt.setDate(9, account.getOpenDate());
            stmt.setInt(10, account.getBalance());
            stmt.setBoolean(11, account.isActive());
            
            stmt.executeUpdate();
            stmt.close();
            con.commit();
            return true;
        } catch (SQLException e){
            System.out.println("Error retrieving account when saving or updating [" + e.getMessage() + "]");
            try {
                con.rollback();
                System.out.println("ROLLING BACK CHANGES");
            } catch (SQLException ex) {
                System.out.println("Error rolling back [" + ex.getMessage() + "]");
            }
            return false;
        }
    }

 

    public static String getDateTime(){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("YYYY-MM-dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        String date = dtf.format(now);
        return date;
    }

    
}
