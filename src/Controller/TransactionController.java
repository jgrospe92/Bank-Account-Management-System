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
        transactionId = TransactionDAO.sequenceOnTransactionId();
        TransactionModel transaction = new TransactionModel(++transactionId, 0, 
        account.getAccountNumber(), "WITHDRAW", amount);
        return TransactionDAO.insertWithdrawTransaction(transaction);
    }

    public boolean insertDepositTransaction(AccountsModel account, int amount){
        transactionId = TransactionDAO.sequenceOnTransactionId();
        TransactionModel transaction = new TransactionModel(++transactionId, account.getAccountNumber(), 
       0, "DEPOSIT", amount);
        return TransactionDAO.insertDepositTransaction(transaction);
    }

    public boolean insertTransferTransaction(AccountsModel account, AccountsModel fromAccount , int amount){
        transactionId = TransactionDAO.sequenceOnTransactionId();
        TransactionModel transaction = new TransactionModel(++transactionId, account.getAccountNumber(), 
        fromAccount.getAccountNumber(), "TRANSFER", amount);
        return TransactionDAO.insertTransferTransaction(transaction);
    }


    public List<TransactionModel> getAllTransaction(){
        storedTransaction = TransactionDAO.getAllTransaction();
        return storedTransaction;
    }

    
}
