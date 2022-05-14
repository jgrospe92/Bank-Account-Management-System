package Controller;

import java.util.List;

import DbHelper.AccountDAO;
import Model.AccountsModel;
import Model.ClientsModel;

public class AccountController {
    

 

    public void createOrUpdateAccount(AccountsModel account){
        AccountDAO.saveOrUpdateAccount(account);

    }

    public List<AccountsModel> showAllAccountOnThisClient(int clientId){
        return AccountDAO.getAllAccount(clientId);
    }
}
