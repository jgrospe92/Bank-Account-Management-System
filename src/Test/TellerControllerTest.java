package Test;

import static org.junit.Assert.assertThrows;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;

import Controller.TellerController;
import DbHelper.DbConnector;
import DbHelper.TellerDAO;
import Model.TellersModel;

public class TellerControllerTest {

    TellerController tc;

    @BeforeEach
    public void setUp(){
        tc = new TellerController();
    }


    @Test
    public void testFetchAllTellerShouldThrowNullWhenEmpty() {
        assertThrows(NullPointerException.class, () -> tc.fetchAllTeller());

    }

    @Test
    public void testFindTeller() {
        tc = new TellerController();
        tc.findTeller(123);
    }

    @Test
    public void testGetTellerLastLoginDate() {

    }

    @Test
    public void testGetTellers() {

    }

    @Test
    @DisplayName("Should return exception error when Teller list is empty")
    public void testHasNext() {
        TellerController tc = new TellerController();
        Assertions.assertThrows(NullPointerException.class, () -> {
            tc.hasNext();
        });

    }

    @Test
    public void testNext() {

    }

    @Test
    public void testUpdateLogin() {

    }

    @Test
    public void testVerifyLogin() {

    }
}
