package View;


import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.util.logging.SimpleFormatter;

import javax.swing.text.AbstractDocument.BranchElement;

import Controller.AccountController;
import Controller.ClientController;
import Controller.TellerController;
import Controller.TransactionController;
import DbHelper.AccountDAO;
import Model.AccountsModel;
import Model.ClientsModel;
import Model.TellersModel;
import Model.TransactionModel;

public class MainView {

    private TellerController tc = new TellerController();
    private TellersModel currentTeller;
    private Scanner input = new Scanner(System.in);

    private ClientController cc = new ClientController();
    private AccountController ac = new AccountController();
    private TransactionController trc = new TransactionController();

    // NOTE: WELCOME UI
    public void printWelcome() {
        String welcome = """
                ###################################################
                #       WELCOME TO JG BANKING SYSTEM              #
                ###################################################
                            """;
        print(welcome);

    }

    // NOTE: CHANGE USER LANG
    public void chooseLanguage() {
        int opt = 0;
        String language = "";

        String languageOption = """
                Choose your language:
                Press 1 to continue in English
                Press 2 to continue in French
                Enter your choice:
                """;
        print(languageOption);
        opt = input.nextInt();

        while (opt != 1 && opt != 2) {
            print("Invalid input, Please enter again\n");
            print(languageOption);
            opt = input.nextInt();
        }
        language = (opt == 1) ? "English" : "French";
        print("Continuing in " + language + " ...\n");

    }

    // NOTE: TELLER LOGIN
    public void tellerLogin() {

        int attempts = 3;

        String username;
        int password;
        String str = """
                ====================================
                TELLER LOGIN:
                ====================================
                """;
        print(str);
        print("ENTER USERNAME: ");
        username = input.next();
        print("ENTER Password: ");
        password = input.nextInt();

        currentTeller = tc.verifyLogin(username, password);
        while (currentTeller == null && attempts > 0) {

            print("WARNING: Teller Does not Exist. " + (attempts--) + " remaining attempts.\n");
            print("ENTER USERNAME:");
            username = input.next();
            print("ENTER Password: ");
            password = input.nextInt();
            currentTeller = tc.verifyLogin(username, password);
        }
        if (attempts < 1) {
            print("INVALID CREDENTIALS, Please try again next time");
            System.exit(0);
        }

        loginSuccess();
    }

    private void loginSuccess() {
        print("====================================\n");
        print("Welcome, " + currentTeller.getFirstName() + ", " + " Last login "
                + tc.getTellerLastLoginDate(currentTeller.getTellerId()) + "\n");
        tc.updateLogin(currentTeller.getTellerId());

    }

    private void clientMenu() {
        int opt = 0;
        boolean isValid = true;
        String str = """
                ====================================
                M A I N - M E NU
                ====================================
                """;

        String menuOptions = "PRESS 1 TO CREATE NEW CLIENT\n"
                + "PRESS 2 TO MODIFY EXISTING CLIENT\n"
                + "PRESS 3 TO VIEW CLIENT\n"
                + "PRESS 4 TO CREATE TRANSACTION\n"
                + "PRESS 5 TO SWITCH TELLER\n"
                + "PRESS 6 TO VIEW ALL TRANSACTION\n"
                + "PRESS 7 TO LOGOUT\n"
                + "ENTER YOUR CHOICE : ";
        System.out.println(str);
        print(menuOptions);
        opt = input.nextInt();
        switch (opt) {
            case 1 -> createNewClient();
            case 2 -> modifyExistingClient();
            case 3 -> viewClient();
            case 4 -> doTransaction();
            case 5 -> {tellerLogin(); clientMenu();}
            case 6 -> viewAllTransaction();
            case 7 -> logout();
            default -> {
                print("INVALID INPUT\n");
                isValid = false;
            }
        }
        if (!isValid) {
            clientMenu();
        }
    }

    private void createNewClient() {
        ClientsModel client = null;
        String str = """
                ====================================
                CREATE NEW CLIENT
                ====================================
                """;
        System.out.println(str);
        print("ENTER ID: ");
        int id = input.nextInt();
        print("ENTER FIRST NAME: ");
        String firstName = input.next();
        print("ENTER LAST NAME: ");
        String lastName = input.next();
        print("ENTER IDENTIFICATION (C123): ");
        String identification = input.next();
        print("ENTER ADDRESS: ");
        String address = input.next();

        client = new ClientsModel(id, firstName, lastName, identification, address);
        print("WOULD YOU LIKE TO OPEN AN ACCOUNT NOW? (Y/N)");
        String answer = input.next();
        if ("Y".equals(answer.toUpperCase())) {
            AccountsModel account =  addAccount(id);
            client.addAccount(account);
            cc.createOrUpdateClient(client);
            print("NEW CLIENT ADDED, REDIRECTING TO THE MAIN MENU\n");
            clientMenu();
            
        } else {
            print("REDIRECTING TO THE MAIN MENU\n");
            cc.createOrUpdateClient(client);
            clientMenu();
        }
        
    }
    // NOTE: RETURNS ACCOUNT TYPE
    private String accountType(){
        String str = """
                    PRESS 1 FOR SAVING
                    PRESS 2 FOR CHECKING
                    PRESS 3 FOR INVESTMENT
                    ENTER YOUR CHOICE :                 
                    """;
        print(str);
        int opt = input.nextInt();
        return switch(opt){
            case 1 -> "SAVING";
            case 2 -> "CHECKING";
            case 3 -> "INVESTMENT";
            default -> "";
        };
    }
    // NOTE: ADD ACCOUNT
    private AccountsModel addAccount(int clientId) {
        AccountsModel account = null;
        String str = """
                ====================================
                ADD ACCOUNT
                ====================================
                """;
        System.out.println(str);
        print("ENTER ACCOUNT NUMBER: ");
        int accountNumber = input.nextInt();
        String accountType = accountType();
        print("ENTER BALANCE AMOUNT: ");
        int balance = input.nextInt();
        while(balance < 0){
            print("SORRY, NEGATIVE BALANCE IS NOT ALLOWED\n");
            print("ENTER BALANCE AMOUNT: ");
            balance = input.nextInt();
        }
        boolean isActive = isActive(balance);
        
        account = new AccountsModel(accountNumber, clientId, accountType, getCurrentDate() , balance, isActive);
        return account;
    }

    // NOTE: VIEW CLIENT
    public void viewClient(){
        String str = """
            ====================================
            CLIENT DETAILS
            ====================================
            """;
        System.out.println(str);
        print("PLEASE ENTER CLIENT ID: ");
        int clientId = input.nextInt();
        ClientsModel client = cc.getClientById(clientId);
        if(client != null){
            print("====================================\n");
            System.out.println("CLIENT ID: " + client.getId());
            System.out.println("FIRST NAME: " + client.getFirstName());
            System.out.println("LAST NAME: " + client.getLastName());
            System.out.println("IDENTIFICATION: " + client.getIdentification());
            System.out.println("ADDRESS: " + client.getAddress());
            List<AccountsModel> accounts = ac.showAllAccountOnThisClient(clientId);
            iterateAccountList(accounts);
        }else {
            print("CLIENT DOES NOT EXIST...TRY AGAIN!\n");
            addDelay(1);
            clientMenu();
        }
         navigation();
    }
  
    private void modifyExistingClient(){
        boolean isValid = true;
        String str = """
            ====================================
            MODIFY EXISTING CLIENT
            ====================================
            """;
        System.out.println(str);
        // TODO: SIMPLIFY THIS
        String modOutput = "PRESS 1 TO UPDATE CLIENT INFO\n"
                        + "PRESS 2 TO OPEN NEW ACCOUNT\n"
                        + "PRESS 3 TO DEACTIVATE AN ACCOUNT\n"
                        + "PRESS 4 TO GO BACK TO THE PREVIOUS MENU\n"
                        + "PRESS 5 TO LOGOUT\n"
                        + "ENTER YOUR CHOICE: ";
        print(modOutput);
        int opt = input.nextInt();
        switch (opt){
            case 1 -> updateClientInfo();
            case 2 -> openNewAccount();
            case 3 -> deleteAccount();
            case 4 -> clientMenu();
            case 5 -> logout();
            default -> {
                System.out.println("INVALID INPUT\n");
                isValid = false;
            }
        }
        if(!isValid){
            modifyExistingClient();
        } 
    }
    private void updateClientInfo(){
        print("PLEASE ENTER CLIENT ID: ");
        int clientId = input.nextInt();
        
        // NOTE: GETS THE MATCHING CLIENT ID
        ClientsModel client = cc.getClientById(clientId);
        if (client != null){
            print("ENTER NEW FIRST NAME: ");
            String firstName = input.next();
            print("ENTER NEW LAST NAME: ");
            String lastName = input.next();
            print("ENTER NEW IDENTIFICATION (C123): ");
            String identification = input.next();
            print("ENTER NEW ADDRESS: ");
            String address = input.next();
            client.setFirstName(firstName);
            client.setLastName(lastName);
            client.setIdentification(identification);
            client.setAddress(address);
            cc.createOrUpdateClient(client);
            print("SUCCESSFUL, RETURNING TO THE PREVIOUS MENU");
            modifyExistingClient();
        } else {
            print("CLIENT DOES NOT EXIST...TRY AGAIN\n!");
            addDelay(1);
            updateClientInfo();
        }
        

    }

    public void openNewAccount(){
        print("PLEASE ENTER CLIENT ID: ");
        int clientId = input.nextInt();
        ClientsModel client = cc.getClientById(clientId);
        if(client != null){

            client.addAccount(addAccount(clientId));
            cc.createOrUpdateClient(client);
            print("SUCCESSFUL, RETURNING TO THE PREVIOUS MENU\n");
            modifyExistingClient();
        }
        else{
            print("CLIENT DOES NOT EXIST...TRY AGAIN!\n");
            addDelay(1);
            updateClientInfo();
        }
       
    }

    private void deleteAccount(){
        print("PLEASE ENTER CLIENT ID: ");
        int clientId = input.nextInt();
        ClientsModel client = cc.getClientById(clientId);
        if(client != null){
            print("WARNING! ONLY ACCOUNT WITH 0 BALANCE CAN BE DEACTIVATED\n");
            List<AccountsModel> accounts = ac.showAllAccountOnThisClient(clientId);
            iterateAccountList(accounts);
            
            print("PLEASE ENTER ACCOUNT NUMBER TO DEACTIVATE: ");
            int accountNumber = input.nextInt();
            boolean hasFoundAccount = false;
            //REFACTOR:
            for (AccountsModel account : accounts){
                if(account.getAccountNumber() == accountNumber){
                    if (account.getBalance() == 0){
                        account.setActive(false);
                        ac.createOrUpdateAccount(account);
                        hasFoundAccount = true;
                        break;
                    }else{
                        print("FAILED TO DEACTIVATE\n");
                        print("ACCOUNT HAS " + account.getBalance() + " REMAINING BALANCE\n");
                        break;
                    }
                }
            }
            if (!hasFoundAccount){
                print("ACCOUNT NUMBER DOES NOT EXIST! PLEASE TRY AGAIN!\n");
                deleteAccount();
            }

            modifyExistingClient();
        }
        else{
            print("CLIENT DOES NOT EXIST...TRY AGAIN!\n");
            addDelay(1);
            modifyExistingClient();
        }

    }

    // NOTE: CREATE TRANSACTION
    // TODO: MAKE THE REST SIMILAR TO THE GO BACK FUNCTION
    private void doTransaction(){
        String str = """
            ====================================
            T R A N S A C T I O N
            ====================================
            """;
        System.out.println(str);
        print("PLEASE ENTER CLIENT ID: ");
        int clientId = input.nextInt();
        ClientsModel client = cc.getClientById(clientId);
        if (client != null){
            Boolean goBack = false;
            System.out.println("CLIENT NAME : " + client.getFirstName() + " " + client.getLastName());
            while(!goBack){
                String menuOptions = "PRESS 1 TO WITHDRAW\n"
                + "PRESS 2 TO DEPOSIT\n"
                + "PRESS 3 TO TRANSFER\n"
                + "PRESS 4 TO DEACTIVATE\n"
                + "PRESS 5 TO TO BACK TO THE PREVIOUS MENU\n"
                + "PRESS 6 TO LOGOUT\n"
                + "ENTER YOUR CHOICE : ";
                System.out.println("====================================");
                print(menuOptions);
                int choice = input.nextInt();
                switch(choice){
                    case 1 -> withdraw(client);
                    case 2 -> deposit(client);
                    case 3 -> transferAmount(client);
                    case 4 -> deactivateAccount(client);
                    case 5 -> {
                        goBack = true;
                    }
                    case 6 -> logout();
                }
            }
           
        } else {
            print("CLIENT DOES NOT EXIST...TRY AGAIN!\n");
            addDelay(1);
            doTransaction();
        }
        clientMenu();
    }

    public void withdraw(ClientsModel client){
        boolean hasFoundAccount = false;
        print("PLEASE ENTER ACCOUNT NUMBER: ");
        int accountNumber = input.nextInt();
        List<AccountsModel> accounts = ac.showAllAccountOnThisClient(client.getId());
        for (AccountsModel account : accounts){
            if(account.getAccountNumber() == accountNumber){
                print("CURRENT BALANCE: $" +account.getBalance()+"\n");
                print("ENTER THE WITHDRAW AMOUNT: ");
                int amount = input.nextInt();
                while(!(isAmountValid(amount))){
                    print("AMOUNT HAVE TO BE MORE THAN 0\n");
                    print("ENTER THE RIGHT DEPOSIT AMOUNT: ");
                    amount = input.nextInt();
                }

                int withdrawAmount = account.getBalance() - amount;
                while(!ac.updateAccountBalance(account, withdrawAmount)){
                    print("YOU CURRENT BALANCE IS $" + account.getBalance() +"\n");
                    print("ENTER THE RIGHT WITHDRAW AMOUNT: ");
                    amount = input.nextInt();
                    withdrawAmount = account.getBalance() - amount;
                }
                trc.insertWithdrawTransaction(account, withdrawAmount);
                

                System.out.println("SUCCESSFULLY WITHDRAWN $" +amount);
                System.out.println("YOUR NEW BALANCE IS $" + account.getBalance());
                hasFoundAccount = true;
                break;
    
                } 
        }
        if (!hasFoundAccount){
            print("ACCOUNT NUMBER DOES NOT EXIST! PLEASE TRY AGAIN!\n");
            
        }
    }

    public void deposit(ClientsModel client){
        boolean hasFoundAccount = false;
        int newBalance = 0;
        print("PLEASE ENTER ACCOUNT NUMBER: ");
        int accountNumber = input.nextInt();
        List<AccountsModel> accounts = ac.showAllAccountOnThisClient(client.getId());
        for (AccountsModel account : accounts){
            if(account.getAccountNumber() == accountNumber){
                print("CURRENT BALANCE: $" +account.getBalance()+"\n");
                print("ENTER THE DEPOSIT AMOUNT: ");
                int depositAmount = input.nextInt();
                while(!(isAmountValid(depositAmount))){
                    print("AMOUNT HAVE TO BE MORE THAN 0\n");
                    print("ENTER THE RIGHT DEPOSIT AMOUNT: ");
                    depositAmount = input.nextInt();
                }
                newBalance = account.getBalance() + depositAmount;
                while(!ac.updateAccountBalance(account, newBalance) && depositAmount < 1){
                    print("YOU CURRENT BALANCE IS $" + account.getBalance() +"\n");
                    print("ENTER THE RIGHT DEPOSIT AMOUNT: ");
                    newBalance = account.getBalance() + depositAmount;
                }
                trc.insertDepositTransaction(account, depositAmount);

                System.out.println("SUCCESSFULLY DEPOSITED $" +depositAmount);
                System.out.println("YOUR NEW BALANCE IS $" + account.getBalance());
                hasFoundAccount = true;
                break;
    
                } 
        }
        if (!hasFoundAccount){
            print("ACCOUNT NUMBER DOES NOT EXIST! PLEASE TRY AGAIN!\n");
            
        }
    }

    private void transferAmount(ClientsModel client){
        boolean hasFoundAccount = false;
        print("PLEASE ENTER ACCOUNT NUMBER: ");
        int accountNumber = input.nextInt();
        List<AccountsModel> accounts = ac.showAllAccountOnThisClient(client.getId());
        for (AccountsModel account : accounts){
            if(account.getAccountNumber() == accountNumber){
                print("PEASE ENTER DESIGNATION ACCOUNT NUMBER: ");
                int accountNumber2 = input.nextInt();
                AccountsModel account2 = ac.getAccountByAccountNumber(accountNumber2);
                while(account2 == null){
                    print("ACCOUNT DOES NOT EXIT, PLEASE TRY AGAIN\n");
                    print("PLEASE ENTER DESIGNATION ACCOUNT NUMBER:" );
                    accountNumber2 = input.nextInt();
                    account2 = ac.getAccountByAccountNumber(accountNumber2);
                }
                print("CURRENT BALANCE: $" +account.getBalance()+"\n");
                print("ENTER THE AMOUNT TO BE TRANSFER: ");
                int amount = input.nextInt();
                while(!isAmountValid(amount) || account.getBalance() < amount){
                    print("CANNOT TRANSFER 0 OR LESS THAN ACCOUNT BALANCE\n");
                    print("ENTER THE RIGHT AMOUNT TO BE TRANSFER: ");
                    amount = input.nextInt();
                }

                ac.updateAccountBalance(account, account.getBalance() - amount);
                ac.updateAccountBalance(account2, account2.getBalance() + amount);
                trc.insertTransferTransaction(account, account2, amount);

                System.out.println("SUCCESSFULLY TRANSFER $" +amount+ " To ACCOUNT NUMBER: " + account2.getAccountNumber());
                System.out.println("YOUR NEW BALANCE IS $" + account.getBalance());
                hasFoundAccount = true;
                break;
    
                } 
        }
        if (!hasFoundAccount){
            print("ACCOUNT NUMBER DOES NOT EXIST! PLEASE TRY AGAIN!\n");
            
        }
        
    }

    public void deactivateAccount(ClientsModel client){
        boolean hasFoundAccount = false;
        print("PLEASE ENTER ACCOUNT NUMBER TO DEACTIVATE: ");
        int accountNumber = input.nextInt();
        List<AccountsModel> accounts = ac.showAllAccountOnThisClient(client.getId());
        for (AccountsModel account : accounts){
            if(account.getAccountNumber() == accountNumber){
                if(account.getBalance() == 0){
                    print("ARE YOU SURE YOU WANT TO DEACTIVATE ACCOUNT:(Y/N) ");
                    if(input.next().equalsIgnoreCase("y")){
                        account.setActive(false);
                        ac.createOrUpdateAccount(account);
                        hasFoundAccount = true;
                        break;
                    } 
                }
                else {
                    hasFoundAccount = true;
                    print("FAILED TO DEACTIVATE\n");
                    print("ACCOUNT HAS " + account.getBalance() + " REMAINING BALANCE\n");
                }
            }
          
        }
        if (!hasFoundAccount){
            print("ACCOUNT NUMBER DOES NOT EXIST! PLEASE TRY AGAIN!\n");
            
        }
    }
    public void viewAllTransaction(){
        String str = """
            ====================================
            TRANSACTION REPORT
            ====================================
            """;
        System.out.println(str);
        trc.getAllTransaction();
        while(trc.hasNext()){
            TransactionModel transaction = trc.next();
            System.out.println("====================================");
            print("TRANSACTION ID  " + transaction.getTransactionId()+"\n");
            print("TO ACCOUNT NUMBER: " + transaction.getToAccountNumber()+"\n");
            print("FROM ACCOUNT NUMBER: " + transaction.getFromAccountNumber()+"\n");
            print("TRANSACTION DETAIL: " + transaction.getTransactionDetail()+"\n");
            print("VALUE: " + transaction.getValue()+"\n");
          
        }
        System.out.println("====================================");
    }

    // NOTE: UTILITY METHODS

    // NOTE: PRINT DATA
    public static <T> void print(T data) {
        System.out.print(data);
    }

      // NOTE: IF BALANCE IS ZERO, DEACTIVATE
      private boolean isActive(int balance){
        return balance > 0;
    }

    // NOTE: ADD PAUSE
    private void addDelay(int sec) {
        try {
            TimeUnit.SECONDS.sleep(sec);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    // NOTE: GET CURRENT DATE
    private Date getCurrentDate(){
        long now = System.currentTimeMillis();
        Date sqlDate = new Date(now);
        return sqlDate;
    }

    // NOTE: CHECK IF THE DEPOSIT AMOUNT IS ZERO
    public boolean isAmountValid(int amount){
      return amount > 0;
    }

    // NOTE: Iterate through account
    private void iterateAccountList(List<AccountsModel> accounts){
        for (AccountsModel account : accounts){
            System.out.println("====================================");
            System.out.println("ACCOUNT #: " + account.getAccountNumber());
            System.out.println("ACCOUNT TYPE: " + account.getAccountType());
            System.out.println("DATE OPENED: " + account.getOpenDate());
            System.out.println("BALANCE: " + account.getBalance());
            System.out.println("IS ACTIVE: " + account.isActive());
            System.out.println("====================================");
        }
    }
    // NOTE: NAVIGATION
    public void navigation(){
        String msg = "PRESS 1 PREVIOUS MENU\n"
        +"PRESS 2 LOGOUT\n"
        +"ENTER YOUR CHOICE: ";
        print(msg);
        int option = 0;
        try {
            option = input.nextInt();
            switch(option){
                case 1 -> clientMenu();
                case 2 -> logout();
                default -> {
                    print("INVALID INPUT, TRY AGAIN");
                    navigation();
                }
            }
            
        }
        catch (Exception e){
            System.out.println("INVALID INPUT! TRY AGAIN!");
            print("\n");
            navigation();
        }
        
        
    }

    private void logout(){
        System.out.println("SIGNING OFF!, HAVE A GREAT DAY!");
        System.exit(0);
    }

    public static void main(String[] args) {

        MainView v = new MainView();
        v.printWelcome();
        v.chooseLanguage();
        v.tellerLogin();
        v.clientMenu();

    }
}
