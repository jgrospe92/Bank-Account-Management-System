package Test;

import java.sql.Date;

import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import Controller.TransactionController;
import Model.AccountsModel;
import Model.TransactionModel;

public class TransactionControllerTest {
    @Test
    public void testGetAllTransaction() {
      
    }

    @Test
    public void testHasNext() {
        TransactionController tc = new TransactionController();
        tc.hasNext();
    }

    @Test
    public void testInsertDepositTransaction() {

    }

    @Test
    public void testInsertTransferTransaction() {

    }

    @Test
    public void testInsertWithdrawTransaction() {
        
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testNext() {
        TransactionController tc = new TransactionController();
        tc.next();
    }
}
 