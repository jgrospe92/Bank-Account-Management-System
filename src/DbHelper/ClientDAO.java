package DbHelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import Model.AccountsModel;
import Model.ClientsModel;

public class ClientDAO {
    
    public static ClientsModel getClientById(int clientId) {
        try {
            Connection con = DbConnector.createConnection();
            String sql = "SELECT *" +
                    "FROM Clients WHERE ClientId=?";
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, clientId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                return new ClientsModel(rs);
            }
            stmt.close();
            return null;
        } catch (SQLException e) {
            System.out.println("Error retrieving Client Data [" + e.getMessage() + "]");
            return null;
        }
      
    }

    public static boolean saveOrUpdateClient(ClientsModel client){
        if (client == null ) {return true;}
        Connection con = DbConnector.createConnection();
        try {
           
            con.setAutoCommit(false);
            String sql = "INSERT INTO Clients (ClientId, firstName, lastName, identification, address)"
                        + "VALUES (?,?,?,?,?) ON CONFLICT(ClientId) "
                        + "DO UPDATE "
                        + "SET FirstName=?,"
                        + "LastName=?,"
                        + "Identification=?,"
                        + "Address=?";
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, client.getId());
            stmt.setString(2, client.getFirstName());
            stmt.setString(3, client.getLastName());
            stmt.setString(4, client.getIdentification());
            stmt.setString(5, client.getAddress());
            // NOTE: IF CONFLICT UPDATE IT WITH THESE VALUES
            stmt.setString(6, client.getFirstName());
            stmt.setString(7, client.getLastName());
            stmt.setString(8, client.getIdentification());
            stmt.setString(9, client.getAddress());
            //TODO:
            stmt.executeUpdate();

            for (AccountsModel account : client.getAccounts()){
                AccountDAO.saveOrUpdateAccount(account);
            }

            stmt.close();
            con.commit();
          
            return true;
        
        } catch (SQLException e){
            System.out.println("Error retrieving Client Data [" + e.getMessage() + "]");
            try {
                con.rollback();
                System.out.println("ROLLING BACK CHANGES");
            } catch (SQLException ex) {
                System.out.println("Error rolling back [" + ex.getMessage() + "]");
            }
            return false;
         
        }
    }

    public static void addAccount(ClientsModel client){
        
        for (AccountsModel account : client.getAccounts()){
            AccountDAO.saveOrUpdateAccount(account);
        }
    }

    public static List<ClientsModel> getAllAccounts(){
        List<ClientsModel> result = new ArrayList<>();

        try {
            Connection con = DbConnector.createConnection();
            ClientsModel client = null;
            String sql = "SELECT c.clinetId AS ClientId, c.firstName AS FirstName, c.lastNamem AS LastName"
                    + "c.identification AS Identification, c.address AS Address,"
                    + "a.accountNumber AS AccountNumber, a.accountType AS AccountType, a.openDate AS OpenDate"
                    + "a.balance AS Balance, a.isActive as IsActive"
                    + "FROM Clients c INNER JOIN Accounts a ON c.clinetId=p.clientId ORDER BY 1";
            PreparedStatement stmt = con.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            int lastClientId = -1;
            while(rs.next()){
                if (rs.getInt("ClientId") != lastClientId){
                    client = new ClientsModel(rs);
                    lastClientId = rs.getInt("ClientId");
                }
                client.addAccount(new AccountsModel(rs));
            }


        } catch (Exception e){
            System.out.println("Error retrieving Teller data [" + e.getMessage() + "]");
            return null;
        }
        return result;
    }

  
}
