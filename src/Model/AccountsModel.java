package Model;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AccountsModel {

   

    private int AccountNumber;
    private int ClientId; // NOTE: FR key
    private String accountType;
    private Date openDate;
    private int balance;
    private boolean isActive;

    public AccountsModel(ResultSet rs) throws SQLException {
        this.AccountNumber = rs.getInt("AccountNumber");
        this.ClientId = rs.getInt("ClientId");
        this.accountType = rs.getString("AccountType");
        this.openDate = rs.getDate("OpenData");
        this.balance = rs.getInt("Balance");
        this.isActive = rs.getBoolean("IsActive");
    }

    public AccountsModel(int accountNumber, int clientId, String accountType, Date openDate, int balance,
            boolean isActive) {
        AccountNumber = accountNumber;
        ClientId = clientId;
        this.accountType = accountType;
        this.openDate = openDate;
        this.balance = balance;
        this.isActive = isActive;
    }

    public int getAccountNumber() {
        return AccountNumber;
    }

    public void setAccountNumber(int accountNumber) {
        AccountNumber = accountNumber;
    }

    public int getClientId() {
        return ClientId;
    }

    public void setClientId(int clientId) {
        ClientId = clientId;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public Date getOpenDate() {
        return openDate;
    }

    public void setOpenDate(Date openDate) {
        this.openDate = openDate;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }

    @Override
    public String toString() {
        return "AccountsModel [AccountNumber=" + AccountNumber + ", ClientId=" + ClientId + ", accountType="
                + accountType + ", balance=" + balance + ", isActive=" + isActive + ", openDate=" + openDate + "]";
    }
}
