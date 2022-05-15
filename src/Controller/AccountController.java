package Controller;

import java.util.List;

import DbHelper.AccountDAO;
import Model.AccountsModel;

public class AccountController {
    

    public void createOrUpdateAccount(AccountsModel account){
        AccountDAO.saveOrUpdateAccount(account);
    }
    public List<AccountsModel> showAllAccountOnThisClient(int clientId){
        return AccountDAO.getAllAccount(clientId);
    }

    public boolean updateAccountBalance(AccountsModel account, int withdrawAmount){
        return AccountDAO.updateBalance(account, withdrawAmount);
    }

    public AccountsModel getAccountByAccountNumber(int accountNumber){
        return AccountDAO.getAccountByAccountNumber(accountNumber);
    }
}
