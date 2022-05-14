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

    public static AccountsModel getAccountByAccountNumber(int accountNumber){
        try {
            Connection con = DbConnector.createConnection();
            String sql = "SELECT * "+
                        "FROM Accounts WHERE accountNumber=?";
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

    public static boolean updateBalance(AccountsModel account, int amount){
        if (account == null) {return true;}
        Connection con = DbConnector.createConnection();
        //int newBalance = account.getBalance() - withdrawAmount;
        System.out.println(amount);
        try{
            con.setAutoCommit(false);

            String sql = "UPDATE Accounts SET Balance = ? WHERE AccountNumber = ?";
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(2, account.getAccountNumber());
           if (amount >= 0){
                account.setBalance(amount);
                stmt.setInt(1, amount);
                stmt.executeUpdate();
                stmt.close();
                con.commit();
                return true;
            }
            else {
                try {
                    con.rollback();
                    System.out.println("AMOUNT CANNOT BE NEGATIVE");
                    System.out.println("ROLLING BACK CHANGES");
                } catch (SQLException ex) {
                    System.out.println("Error rolling back [" + ex.getMessage() + "]");
                }
            }
          
        } catch (SQLException e) {
            System.out.println("Error retrieving account when saving or updating [" + e.getMessage() + "]");
        }
        return false;
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

    public static List<AccountsModel> getAllAccount(Integer clientId){
        if (clientId == null) {return null;}
        List<AccountsModel> result = new ArrayList<>();
        Connection con = DbConnector.createConnection();
        try {
            con.setAutoCommit(false);
            String sql = "SELECT accountNumber, A.ClientId, accountType, openDate, balance, isActive "
                        +"FROM Accounts A JOIN Clients C ON A.ClientId = C.ClientId "
                        +"WHERE A.clientId=?";
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, clientId);
            ResultSet rs = stmt.executeQuery();
            while(rs.next()){
                AccountsModel account = new AccountsModel(rs);
                result.add(account);
            }
            stmt.close();
            con.commit();
            return result;
        }catch (SQLException e){
            System.out.println("Error retrieving accounts Data [" + e.getMessage() + "]");
            try {
                con.rollback();
                System.out.println("ROLLING BACK CHANGES");
            } catch (SQLException ex) {
                System.out.println("Error rolling back [" + ex.getMessage() + "]");
            }
            return null;
         
        }
        
    }

    

    public static String getDateTime(){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("YYYY-MM-dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        String date = dtf.format(now);
        return date;
    }

    
}
