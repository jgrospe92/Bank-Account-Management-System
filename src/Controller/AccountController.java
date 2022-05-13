package Controller;

import DbHelper.AccountDAO;
import Model.AccountsModel;

public class AccountController {
    

    
    public void createOrUpdateAccount(AccountsModel account){
        AccountDAO.saveOrUpdateAccount(account);

    }
}
