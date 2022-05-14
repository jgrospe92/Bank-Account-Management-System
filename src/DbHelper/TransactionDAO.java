package DbHelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import Model.TransactionModel;

public class TransactionDAO {

    public static List<TransactionModel> getAllTransaction() {
        List<TransactionModel> result = new ArrayList<>();
        try {
            Connection con = DbConnector.createConnection();
            String sql = "SELECT * FROM Transactions";
            PreparedStatement stmt = con.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                TransactionModel transaction = new TransactionModel(rs);
                result.add(transaction);
            }
            stmt.close();
            return result;
        } catch (SQLException e) {
            System.out.println("Error retrieving account [" + e.getMessage() + "]");
        }
        return null;
    }

    public static boolean insertWithdrawTransaction(TransactionModel transaction) {
        if (transaction == null) {
            return true;
        }
        Connection con = DbConnector.createConnection();
        try {

            con.setAutoCommit(false);

            String sql = "INSERT INTO Transactions (transactionId, toAccountNumber, fromAccountNumber, transactionDetail, value) "
                    + "VALUES(?,?,?,?,?)";
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, transaction.getTransactionId());
            stmt.setInt(2, transaction.getToAccountNumber());
            stmt.setInt(3, transaction.getFromAccountNumber());
            stmt.setString(4, transaction.getTransactionDetail());
            stmt.setInt(5, transaction.getValue());
            stmt.executeUpdate();
            stmt.close();
            con.commit();
            return true;

        } catch (SQLException e) {
            System.out.println("Error retrieving transaction data [" + e.getMessage() + "]");

            try {
                con.rollback();
                System.out.println("ROLLING BACK CHANGES");
            } catch (SQLException ex) {
                System.out.println("Error rolling back [" + ex.getMessage() + "]");
            }
            return false;
        }
    }

    public static int sequenceOnTransactionId() {
        int result = 0;
        try {
            Connection con = DbConnector.createConnection();
            String sql = "SELECT * FROM Transactions ORDER BY 1";
            PreparedStatement stmt = con.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
           
            while (rs.next()) {
                TransactionModel transaction = new TransactionModel(rs);
                result = transaction.getTransactionId();
            }

            stmt.close();
            return result;
        } catch (SQLException e) {
            System.out.println("Error retrieving transaction ID [" + e.getMessage() + "]");
            return 0;
        }
    }

    public static boolean insertDepositTransaction(TransactionModel transaction) {
        if (transaction == null) {
            return true;
        }
        Connection con = DbConnector.createConnection();
        try {

            con.setAutoCommit(false);

            String sql = "INSERT INTO Transactions (transactionId, toAccountNumber, fromAccountNumber, transactionDetail, value) "
                    + "VALUES(?,?,?,?,?)";
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, transaction.getTransactionId());
            stmt.setInt(2, transaction.getToAccountNumber());
            stmt.setInt(3, transaction.getFromAccountNumber());
            stmt.setString(4, transaction.getTransactionDetail());
            stmt.setInt(5, transaction.getValue());
            stmt.executeUpdate();
            stmt.close();
            con.commit();
            return true;

        } catch (SQLException e) {
            System.out.println("Error retrieving transaction data [" + e.getMessage() + "]");

            try {
                con.rollback();
                System.out.println("ROLLING BACK CHANGES");
            } catch (SQLException ex) {
                System.out.println("Error rolling back [" + ex.getMessage() + "]");
            }
            return false;
        }
    }

    public static boolean insertTransferTransaction(TransactionModel transaction) {
        if (transaction == null) {
            return true;
        }
        Connection con = DbConnector.createConnection();
        try {

            con.setAutoCommit(false);

            String sql = "INSERT INTO Transactions (transactionId, toAccountNumber, fromAccountNumber, transactionDetail, value) "
                    + "VALUES(?,?,?,?,?)";
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, transaction.getTransactionId());
            stmt.setInt(2, transaction.getToAccountNumber());
            stmt.setInt(3, transaction.getFromAccountNumber());
            stmt.setString(4, transaction.getTransactionDetail());
            stmt.setInt(5, transaction.getValue());
            stmt.executeUpdate();
            stmt.close();
            con.commit();
            return true;

        } catch (SQLException e) {
            System.out.println("Error retrieving transaction data [" + e.getMessage() + "]");

            try {
                con.rollback();
                System.out.println("ROLLING BACK CHANGES");
            } catch (SQLException ex) {
                System.out.println("Error rolling back [" + ex.getMessage() + "]");
            }
            return false;
        }
    }

}
