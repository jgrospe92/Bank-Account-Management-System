package DbHelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import Model.AccountsModel;

public class AccountDAO {

    public AccountsModel getAccount(int accountNumber){
        Connection con = DbConnector.createConnection();
        String sql = "SELECT AccountNumber, ClientId, AccountType, OpenDate, Balance, IsActive"+
                    "FROM Accounts WHERE AccountNumber=?";
        try {
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, accountNumber);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()){
                return new AccountsModel(rs);
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving product [" + e.getMessage() + "]");
        }
        return null;
    }
    
}
