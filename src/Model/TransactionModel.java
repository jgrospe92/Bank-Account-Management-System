package Model;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TransactionModel {
    
    private int transactionId;
    private int toAccountNumber;
    private int fromAccountNumber;
    private String transactionDetail;
    private int value;

    public TransactionModel(ResultSet rs) throws SQLException{
        this.transactionId = rs.getInt(1);
        this.toAccountNumber = rs.getInt(2);
        this.fromAccountNumber = rs.getInt(3);
        this.transactionDetail = rs.getString(4);
        this.value = rs.getInt(5);
    }


    public int getToAccountNumber() {
        return toAccountNumber;
    }
    public void setToAccountNumber(int toAccountNumber) {
        this.toAccountNumber = toAccountNumber;
    }
    public int getFromAccountNumber() {
        return fromAccountNumber;
    }
    public void setFromAccountNumber(int fromAccountNumber) {
        this.fromAccountNumber = fromAccountNumber;
    }
    public String getTransactionDetail() {
        return transactionDetail;
    }
    public void setTransactionDetail(String transactionDetail) {
        this.transactionDetail = transactionDetail;
    }
    public int getValue() {
        return value;
    }
    public void setValue(int value) {
        this.value = value;
    }
   
}
