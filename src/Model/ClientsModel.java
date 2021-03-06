package Model;

import java.util.List;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ClientsModel {

    // class attributes
    private int id;
    private String firstName;
    private String lastName;
    private String identification;
    private String address;
    private List<AccountsModel> accounts;
    

    public ClientsModel(ResultSet rs) throws SQLException{
        this.id = rs.getInt("ClientId");
        this.firstName = rs.getString("FirstName");
        this.lastName = rs.getString("LastName");
        this.identification = rs.getString("Identification");
        this.address = rs.getString("Address");
        this.accounts = new ArrayList<>();

    }

    public ClientsModel(int id, String firstName, String lastName, String identification, String address) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.identification = identification;
        this.address = address;
        this.accounts = new ArrayList<>();
    }



    public int getId() {
        return id;
    }


    public void setId(int id) {
        this.id = id;
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


    public String getIdentification() {
        return identification;
    }


    public void setIdentification(String identification) {
        this.identification = identification;
    }


    public String getAddress() {
        return address;
    }


    public void setAddress(String address) {
        this.address = address;
    }


    public List<AccountsModel> getAccounts() {
        return accounts;
    }


    public void setAccounts(List<AccountsModel> accounts) {
        this.accounts = accounts;
    }

    public void addAccount(AccountsModel acc){
        accounts.add(acc);
    }
    
    @Override
    public String toString() {
        return "ClientsModel [accounts=" + accounts + ", address=" + address + ", firstName=" + firstName + ", id=" + id
                + ", identification=" + identification + ", lastName=" + lastName + "]";
    }
    
}
