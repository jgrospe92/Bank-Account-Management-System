package Controller;

import java.util.ArrayList;
import java.util.List;

import DbHelper.TransactionDAO;
import Model.AccountsModel;
import Model.TransactionModel;

public class TransactionController {
    
    List<TransactionModel> storedTransaction = new ArrayList<>();
    private int index = -1;
    private int transactionId = 0;
  

    // NOTE: ITERATION METHODS 
    public boolean hasNext(){
        return index < storedTransaction.size() -1;
    }

    public TransactionModel next(){
        index++;
        return storedTransaction.get(index);
    }

    public boolean insertWithdrawTransaction(AccountsModel account, int amount){

        TransactionModel transaction = new TransactionModel(transactionId++, 0, 
        account.getAccountNumber(), account.getAccountType(), amount);
        return TransactionDAO.insertWithdrawTransaction(transaction);
    }

    public boolean insertDepositTransaction(TransactionModel transaction){
        return TransactionDAO.insertDepositTransaction(transaction);
    }


    public List<TransactionModel> getAllTransaction(){
        storedTransaction = TransactionDAO.getAllTransaction();
        return storedTransaction;
    }
}
