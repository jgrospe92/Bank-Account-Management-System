package Model;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TransactionModel {

    private int transactionId;
    private int toAccountNumber;
    private int fromAccountNumber;
    private String transactionDetail;
    private int value;

    public TransactionModel(ResultSet rs) throws SQLException {
        this.transactionId = rs.getInt(1);
        this.toAccountNumber = rs.getInt(2);
        this.fromAccountNumber = rs.getInt(3);
        this.transactionDetail = rs.getString(4);
        this.value = rs.getInt(5);
    }

    public TransactionModel(int transactionId, int toAccountNumber, int fromAccountNumber, String transactionDetail,
            int value) {
        this.transactionId = transactionId;
        this.toAccountNumber = toAccountNumber;
        this.fromAccountNumber = fromAccountNumber;
        this.transactionDetail = transactionDetail;
        this.value = value;
    }

    public int getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
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
