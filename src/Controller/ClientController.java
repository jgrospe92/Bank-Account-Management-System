package Controller;

import java.util.List;

import DbHelper.AccountDAO;
import DbHelper.ClientDAO;
import Model.AccountsModel;
import Model.ClientsModel;

public class ClientController {

    private List<ClientsModel> accounts;
    private int index = -1;

    public boolean hasNext(){
        return index < accounts.size() -1;
    }

    public ClientsModel next(){
        index++;
        return accounts.get(index);
    }
    
    public ClientsModel getClientById(int clientId){
        return ClientDAO.getClientById(clientId);
    }

    public void createOrUpdateClient(ClientsModel client){
        ClientDAO.saveOrUpdateClient(client);
    }

    public void fetchAllAccounts(){
        accounts = ClientDAO.getAllAccounts();
    }

    public List<ClientsModel> getClientAccounts(){
        return accounts;
    }
    
}
