package View;

import java.sql.Date;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import Controller.AccountController;
import Controller.ClientController;
import Controller.TellerController;
import Controller.TransactionController;
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
        String welcomeVar = "WELCOME TO JG BANKING SYSTEM";
        String welcome = ""
                + "##################################\n"
                + welcomeVar + "\n"
                + "##################################\n";
        print(welcome);

    }

    // NOTE: CHANGE USER LANG
    public void chooseLanguage() {
        int opt = 0;
        String language = "";

        String languageOption = ""
                + "Choose your language:\n"
                + "Press 1 to continue in English\n"
                + "Press 2 to continue in French\n"
                + "Enter your choice:\n";

        print(languageOption);
        opt = isANumber();

        while (opt != 1 && opt != 2) {
            print("INVALID INPUT, PLEASE ENTER AGAIN\n");
            print(languageOption);
            opt = isANumber();
        }
        language = (opt == 1) ? "English" : "French";
        print("Continuing in " + language + " ...\n");
        addDelay(1);

    }

    // NOTE: TELLER LOGIN
    public void tellerLogin() {

        int attempts = 3;

        String username;
        int password;
        String str = ""
                + "====================================\n"
                + "TELLER LOGIN:\n"
                + "====================================\n";

        print(str);
        print("ENTER USERNAME: ");
        username = input.next();
        print("ENTER Password: ");
        password = isANumber();

        currentTeller = tc.verifyLogin(username, password);
        while (currentTeller == null && attempts > 0) {

            print("WARNING: TELLER DOES NOT EXIST. " + (attempts--) + " REMAINING ATTEMPTS.\n");
            print("ENTER USERNAME: ");
            username = input.next();
            print("ENTER PASSWORD: ");
            password = isANumber();
            currentTeller = tc.verifyLogin(username, password);
        }
        if (attempts < 1) {
            print("INVALID CREDENTIALS, PLEASE TRY AGAIN NEXT TIME");
            logout();
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
        boolean goBack = false;
        String str = ""
                + "====================================\n"
                + "M A I N - M E NU\n"
                + "====================================";
        while (!goBack) {
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
            opt = isANumber();
            switch (opt) {
                case 1 -> createNewClient();
                case 2 -> modifyExistingClient();
                case 3 -> viewClient();
                case 4 -> doTransaction();
                case 5 -> goBack = true;
                case 6 -> viewAllTransaction();
                case 7 -> logout();
                default -> {
                    print("INPUT OUT OF RANGE, TRY AGAIN:\n");
                }
            }
        }

        tellerLogin();
        clientMenu();
    }

    private void createNewClient() {
        ClientsModel client = null;
        String str = ""
                + "====================================\n"
                + "CREATE NEW CLIENT\n"
                + "====================================";
        System.out.println(str);
        print("ENTER ID: ");
        int id = isANumber();
        while(isClientExist(id)){
            System.out.println("CLIENT ALREADY EXIST, TRY AGAIN: ");
            print("ENTER ID: ");
            id = isANumber();
        }
        print("ENTER FIRST NAME: ");
        String firstName = onlyCharacters();
        print("ENTER LAST NAME: ");
        String lastName = onlyCharacters();
        print("ENTER IDENTIFICATION (I-1234): ");
        String identification = identificationFormat();
        input.nextLine();
        print("ENTER ADDRESS: ");
        String address = input.nextLine();
        client = new ClientsModel(id, firstName, lastName, identification, address);
        print("WOULD YOU LIKE TO OPEN AN ACCOUNT NOW? (Y/N)");
        String answer = yesOrNoAnswer();
        if ("Y".equalsIgnoreCase(answer)) {
            AccountsModel account = addAccount(id);
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
    private String accountType() {
        boolean redo = false;
        String result = "";
        do {
            String str = ""
                    + "PRESS 1 FOR SAVING\n"
                    + "PRESS 2 FOR CHECKING\n"
                    + "PRESS 3 FOR INVESTMENT\n"
                    + "ENTER YOUR CHOICE : ";

            print(str);
            int opt = isANumber();
            switch (opt) {
                case 1 -> {
                    result = "SAVING";
                    redo = false;
                }
                case 2 -> {
                    result = "CHECKING";
                    redo = false;
                }
                case 3 -> {
                    result = "INVESTMENT";
                    redo = false;
                }
                default -> {
                    print("INPUT OUT OF RANGE, TRY AGAIN:\n");
                    redo = true;
                }
            }

        } while (redo);

        return result;
    }

    // NOTE: ADD ACCOUNT
    private AccountsModel addAccount(int clientId) {
        AccountsModel account = null;
        String str = ""
                + "====================================\n"
                + "ADD ACCOUNT\n"
                + "====================================";

        System.out.println(str);
        print("ENTER ACCOUNT NUMBER: ");
        int accountNumber = isANumber();
        while(isAccountExist(accountNumber)){
            System.out.println("ACCOUNT ALREADY EXIST, TRY AGAIN: ");
            print("ENTER ACCOUNT NUMBER: ");
            accountNumber = isANumber();
        }

        String accountType = accountType();
        print("ENTER BALANCE AMOUNT: ");
        int balance = isANumber();
        while (balance < 0) {
            print("SORRY, NEGATIVE BALANCE IS NOT ALLOWED\n");
            print("ENTER BALANCE AMOUNT: ");
            balance = isANumber();
        }
        boolean isActive = isActive(balance);

        account = new AccountsModel(accountNumber, clientId, accountType, getCurrentDate(), balance, isActive);
        return account;
    }

    // NOTE: VIEW CLIENT
    public void viewClient() {
        String str = ""
                + "====================================\n"
                + "CLIENT DETAILS\n"
                + "====================================";
        System.out.println(str);
        print("PLEASE ENTER CLIENT ID: ");
        int clientId = isANumber();
        ClientsModel client = cc.getClientById(clientId);
        if (client != null) {
            print("====================================\n");
            System.out.println("CLIENT ID: " + client.getId());
            System.out.println("FIRST NAME: " + client.getFirstName());
            System.out.println("LAST NAME: " + client.getLastName());
            System.out.println("IDENTIFICATION: " + client.getIdentification());
            System.out.println("ADDRESS: " + client.getAddress());
            List<AccountsModel> accounts = ac.showAllAccountOnThisClient(clientId);
            iterateAccountList(accounts);
        } else {
            print("CLIENT DOES NOT EXIST...TRY AGAIN!\n");
            addDelay(1);
            clientMenu();
        }
        navigation();
    }

    private void modifyExistingClient() {
        boolean isValid = true;
        String str = ""
                +"====================================\n"
                +"MODIFY EXISTING CLIENT\n"
                +"====================================";
                
        System.out.println(str);
        String modOutput = ""
                +"PRESS 1 TO UPDATE CLIENT INFO\n"
                + "PRESS 2 TO OPEN NEW ACCOUNT\n"
                + "PRESS 3 TO DEACTIVATE AN ACCOUNT\n"
                + "PRESS 4 TO GO BACK TO THE PREVIOUS MENU\n"
                + "PRESS 5 TO LOGOUT\n"
                + "ENTER YOUR CHOICE: ";
        print(modOutput);
        int opt = isANumber();
        switch (opt) {
            case 1 -> updateClientInfo();
            case 2 -> openNewAccount();
            case 3 -> deleteAccount();
            case 4 -> print("REDIRECTING TO THE MAIN MENU\n");
            case 5 -> logout();
            default -> {
                System.out.println("INPUT IS OUT OF RANGE, TRY AGAIN: ");
                isValid = false;
            }
        }
        if (!isValid) {
            modifyExistingClient();
        }
    }

    private void updateClientInfo() {
        print("PLEASE ENTER CLIENT ID: ");
        int clientId = isANumber();

        // NOTE: GETS THE MATCHING CLIENT ID
        ClientsModel client = cc.getClientById(clientId);
        if (client != null) {
            print("ENTER NEW FIRST NAME: ");
            String firstName = onlyCharacters();
            print("ENTER NEW LAST NAME: ");
            String lastName = onlyCharacters();
            print("ENTER NEW IDENTIFICATION (I-1234): ");
            String identification = identificationFormat();
            input.nextLine();
            print("ENTER NEW ADDRESS: ");
            String address = input.nextLine();
            
            client.setFirstName(firstName);
            client.setLastName(lastName);
            client.setIdentification(identification);
            client.setAddress(address);
            cc.createOrUpdateClient(client);
            print("SUCCESSFUL, RETURNING TO THE PREVIOUS MENU\n");
            modifyExistingClient();
        } else {
            print("CLIENT DOES NOT EXIST...TRY AGAIN\n!");
            addDelay(1);
            updateClientInfo();
        }

    }

    public void openNewAccount() {
        print("PLEASE ENTER CLIENT ID: ");
        int clientId = isANumber();
        ClientsModel client = cc.getClientById(clientId);
        if (client != null) {

            client.addAccount(addAccount(clientId));
            cc.createOrUpdateClient(client);
            print("SUCCESSFUL, RETURNING TO THE PREVIOUS MENU\n");
            modifyExistingClient();
        } else {
            print("CLIENT DOES NOT EXIST...TRY AGAIN!\n");
            addDelay(1);
            updateClientInfo();
        }

    }

    private void deleteAccount() {
        print("PLEASE ENTER CLIENT ID: ");
        int clientId = isANumber();
        ClientsModel client = cc.getClientById(clientId);
        if (client != null) {
            print("WARNING! ONLY ACCOUNT WITH 0 BALANCE CAN BE DEACTIVATED\n");
            List<AccountsModel> accounts = ac.showAllAccountOnThisClient(clientId);
            iterateAccountList(accounts);

            print("PLEASE ENTER ACCOUNT NUMBER TO DEACTIVATE: ");
            int accountNumber = isANumber();
            boolean hasFoundAccount = false;
            // REFACTOR:
            for (AccountsModel account : accounts) {
                if (account.getAccountNumber() == accountNumber) {
                    if (account.getBalance() == 0) {
                        print("ARE YOU SURE YOU WANT TO DEACTIVATE ACCOUNT:(Y/N) ");
                    if (yesOrNoAnswer().equalsIgnoreCase("y")) {
                        account.setActive(false);
                        ac.createOrUpdateAccount(account);
                        hasFoundAccount = true;
                        print("SUCCESSFUL, RETURNING TO THE PREVIOUS MENU\n");
                        break;
                    }
                    } else {
                        print("FAILED TO DEACTIVATE\n");
                        print("ACCOUNT HAS " + account.getBalance() + " REMAINING BALANCE\n");
                        break;
                    }
                }
            }
            if (!hasFoundAccount) {
                print("ACCOUNT NUMBER DOES NOT EXIST! PLEASE TRY AGAIN!\n");
                deleteAccount();
            }

            modifyExistingClient();
        } else {
            print("CLIENT DOES NOT EXIST...TRY AGAIN\n!");
            addDelay(1);
            modifyExistingClient();
        }

    }

    // NOTE: CREATE TRANSACTION
    private void doTransaction() {
        String str = ""
                +"====================================\n"
                +"T R A N S A C T I O N\n"
                +"====================================";
        System.out.println(str);
        print("PLEASE ENTER CLIENT ID: ");
        int clientId = isANumber();
        ClientsModel client = cc.getClientById(clientId);
        if (client != null) {
            Boolean goBack = false;
            System.out.println("CLIENT NAME : " + client.getFirstName() + " " + client.getLastName());
            while (!goBack) {
                String menuOptions = "PRESS 1 TO WITHDRAW\n"
                        + "PRESS 2 TO DEPOSIT\n"
                        + "PRESS 3 TO TRANSFER\n"
                        + "PRESS 4 TO DEACTIVATE\n"
                        + "PRESS 5 TO REACTIVATE\n"
                        + "PRESS 6 TO TO BACK TO THE PREVIOUS MENU\n"
                        + "PRESS 7 TO LOGOUT\n"
                        + "ENTER YOUR CHOICE : ";
                System.out.println("====================================");
                print(menuOptions);
                int choice = isANumber();
                switch (choice) {
                    case 1 -> withdraw(client);
                    case 2 -> deposit(client);
                    case 3 -> transferAmount(client);
                    case 4 -> deactivateAccount(client);
                    case 5 -> reactivateAccount(client);
                    case 6 -> {
                        goBack = true;
                    }
                    case 7 -> logout();
                    default -> print("INPUT IS OUT OF RANGE, TRY AGAIN\n");
                }
            }

        } else {
            print("CLIENT DOES NOT EXIST...TRY AGAIN!\n");
            addDelay(1);
            doTransaction();
        }
        clientMenu();
    }

    public void withdraw(ClientsModel client) {
        boolean hasFoundAccount = false;
        print("PLEASE ENTER ACCOUNT NUMBER: ");
        int accountNumber = isANumber();
        List<AccountsModel> accounts = ac.showAllAccountOnThisClient(client.getId());
        for (AccountsModel account : accounts) {
            if (account.getAccountNumber() == accountNumber && account.isActive()) {
                print("CURRENT BALANCE: $" + account.getBalance() + "\n");
                print("ENTER THE WITHDRAW AMOUNT: ");
                int amount = isANumber();
                while (!(isAmountValid(amount))) {
                    print("AMOUNT HAVE TO BE MORE THAN 0\n");
                    print("ENTER THE RIGHT DEPOSIT AMOUNT: ");
                    amount = isANumber();
                }

                int withdrawAmount = account.getBalance() - amount;
                while (!ac.updateAccountBalance(account, withdrawAmount)) {
                    print("YOU CURRENT BALANCE IS $" + account.getBalance() + "\n");
                    print("ENTER THE RIGHT WITHDRAW AMOUNT: ");
                    amount = isANumber();
                    withdrawAmount = account.getBalance() - amount;
                }
                trc.insertWithdrawTransaction(account, withdrawAmount);

                System.out.println("SUCCESSFULLY WITHDRAWN $" + amount);
                System.out.println("YOUR NEW BALANCE IS $" + account.getBalance());
                hasFoundAccount = true;
                break;

            }
        }
        if (!hasFoundAccount) {
            print("CLIENT'S ACCOUNT NUMBER DOES NOT EXIST || ACCOUNT IS NOT ACTIVE , PLEASE TRY AGAIN!\n");

        }

    }

    public void deposit(ClientsModel client) {
        boolean hasFoundAccount = false;
        int newBalance = 0;
        print("PLEASE ENTER ACCOUNT NUMBER: ");
        int accountNumber = isANumber();
        List<AccountsModel> accounts = ac.showAllAccountOnThisClient(client.getId());
        for (AccountsModel account : accounts) {
            if (account.getAccountNumber() == accountNumber && account.isActive()) {
                print("CURRENT BALANCE: $" + account.getBalance() + "\n");
                print("ENTER THE DEPOSIT AMOUNT: ");
                int depositAmount = isANumber();
                while (!(isAmountValid(depositAmount))) {
                    print("AMOUNT HAVE TO BE MORE THAN 0\n");
                    print("ENTER THE RIGHT DEPOSIT AMOUNT: ");
                    depositAmount = isANumber();
                }
                newBalance = account.getBalance() + depositAmount;
                while (!ac.updateAccountBalance(account, newBalance) && depositAmount < 1) {
                    print("YOU CURRENT BALANCE IS $" + account.getBalance() + "\n");
                    print("ENTER THE RIGHT DEPOSIT AMOUNT: ");
                    newBalance = account.getBalance() + depositAmount;
                }
                trc.insertDepositTransaction(account, depositAmount);

                System.out.println("SUCCESSFULLY DEPOSITED $" + depositAmount);
                System.out.println("YOUR NEW BALANCE IS $" + account.getBalance());
                hasFoundAccount = true;
                break;

            }
        }
        if (!hasFoundAccount) {
            print("CLIENT'S ACCOUNT NUMBER DOES NOT EXIST || ACCOUNT IS NOT ACTIVE , PLEASE TRY AGAIN!\n");

        }
    }

    private void transferAmount(ClientsModel client) {
        boolean hasFoundAccount = false;
        print("PLEASE ENTER ACCOUNT NUMBER: ");
        int accountNumber = isANumber();
        List<AccountsModel> accounts = ac.showAllAccountOnThisClient(client.getId());
        for (AccountsModel account : accounts) {
            if (account.getAccountNumber() == accountNumber && account.isActive()) {
                print("PEASE ENTER DESIGNATION ACCOUNT NUMBER: ");
                int accountNumber2 = isANumber();

                AccountsModel account2 = ac.getAccountByAccountNumber(accountNumber2);
                while (account2 == null || accountNumber == accountNumber2 || !account2.isActive()) {
                    print("ACCOUNT DOES NOT EXIT || YOU CAN'T TRANSFER ON THE SAME ACCOUNT || DESIGNATION ACCOUNT NOT ACTIVE, PLEASE TRY AGAIN\n");
                    print("PLEASE ENTER DESIGNATION ACCOUNT NUMBER: ");
                    accountNumber2 = isANumber();
                    account2 = ac.getAccountByAccountNumber(accountNumber2);
                }
             
                print("CURRENT BALANCE: $" + account.getBalance() + "\n");
                print("ENTER THE AMOUNT TO BE TRANSFER: ");
                int amount = isANumber();
                while (!isAmountValid(amount) || account.getBalance() < amount) {
                    print("CANNOT TRANSFER 0 OR LESS THAN ACCOUNT BALANCE\n");
                    print("ENTER THE RIGHT AMOUNT TO BE TRANSFER: ");
                    amount = isANumber();
                }

                ac.updateAccountBalance(account, account.getBalance() - amount);
                ac.updateAccountBalance(account2, account2.getBalance() + amount);
                trc.insertTransferTransaction(account, account2, amount);

                System.out.println(
                        "SUCCESSFULLY TRANSFER $" + amount + " To ACCOUNT NUMBER: " + account2.getAccountNumber());
                System.out.println("YOUR NEW BALANCE IS $" + account.getBalance());
                hasFoundAccount = true;
                break;

            }
        }
        if (!hasFoundAccount) {
            print("CLIENT'S ACCOUNT NUMBER DOES NOT EXIST || ACCOUNT IS NOT ACTIVE , PLEASE TRY AGAIN!\n");

        }

    }

    public void deactivateAccount(ClientsModel client) {
        boolean hasFoundAccount = false;
        print("PLEASE ENTER ACCOUNT NUMBER TO DEACTIVATE: ");
        int accountNumber = isANumber();
        List<AccountsModel> accounts = ac.showAllAccountOnThisClient(client.getId());
        for (AccountsModel account : accounts) {
            if (account.getAccountNumber() == accountNumber) {
                if (account.getBalance() == 0) {
                    print("ARE YOU SURE YOU WANT TO DEACTIVATE ACCOUNT:(Y/N) ");
                    if (yesOrNoAnswer().equalsIgnoreCase("y")) {
                        account.setActive(false);
                        ac.createOrUpdateAccount(account);
                        hasFoundAccount = true;
                        print("SUCCESSFUL, RETURNING TO THE PREVIOUS MENU\n");
                        break;
                    }
                } else {
                    hasFoundAccount = true;
                    print("FAILED TO DEACTIVATE\n");
                    print("ACCOUNT HAS " + account.getBalance() + " REMAINING BALANCE\n");
                }
            }

        }
        if (!hasFoundAccount) {
            print("CLIENT'S ACCOUNT NUMBER DOES NOT EXIST || ACCOUNT IS NOT ACTIVE , PLEASE TRY AGAIN!\n");


        }
    }

    public void reactivateAccount(ClientsModel client) {
        boolean hasFoundAccount = false;
        print("PLEASE ENTER ACCOUNT NUMBER TO DEACTIVATE: ");
        int accountNumber = isANumber();
        List<AccountsModel> accounts = ac.showAllAccountOnThisClient(client.getId());
        for (AccountsModel account : accounts) {
            if (account.getAccountNumber() == accountNumber) {
                if (account.getBalance() == 0) {
                    print("ARE YOU SURE YOU WANT TO ACTIVATE ACCOUNT:(Y/N) ");
                    if (yesOrNoAnswer().equalsIgnoreCase("y")) {
                        account.setActive(true);
                        ac.createOrUpdateAccount(account);
                        hasFoundAccount = true;
                        print("SUCCESSFUL, RETURNING TO THE PREVIOUS MENU\n");
                        break;
                    }
                } else {
                    hasFoundAccount = true;
                    print("FAILED TO REACTIVATE\n");
                }
            }

        }
        if (!hasFoundAccount) {
            print("CLIENT'S ACCOUNT NUMBER DOES NOT EXIST || ACCOUNT IS NOT ACTIVE , PLEASE TRY AGAIN!\n");
        }
    }

    public void viewAllTransaction() {
        String str = ""
                +"====================================\n"
                +"TRANSACTION REPORT\n"
                +"====================================";
                
        System.out.println(str);
        trc.getAllTransaction();
        while (trc.hasNext()) {
            TransactionModel transaction = trc.next();
            System.out.println("====================================");
            print("TRANSACTION ID  " + transaction.getTransactionId() + "\n");
            print("TO ACCOUNT NUMBER: " + transaction.getToAccountNumber() + "\n");
            print("FROM ACCOUNT NUMBER: " + transaction.getFromAccountNumber() + "\n");
            print("TRANSACTION DETAIL: " + transaction.getTransactionDetail() + "\n");
            print("VALUE: " + transaction.getValue() + "\n");

        }
        System.out.println("====================================");
    }

    // NOTE: UTILITY METHODS

    // NOTE: PRINT DATA
    public static <T> void print(T data) {
        System.out.print(data);
    }

    // NOTE: IF BALANCE IS ZERO, DEACTIVATE
    private boolean isActive(int balance) {
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
    private Date getCurrentDate() {
        long now = System.currentTimeMillis();
        return  new Date(now);
    }

    // NOTE: CHECK IF THE DEPOSIT AMOUNT IS ZERO
    public boolean isAmountValid(int amount) {
        return amount > 0;
    }

    // NOTE: Iterate through account
    private void iterateAccountList(List<AccountsModel> accounts) {
        for (AccountsModel account : accounts) {
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
    public void navigation() {
        boolean redo = true;
        while (redo) {
            String msg = "PRESS 1 PREVIOUS MENU\n"
                    + "PRESS 2 LOGOUT\n"
                    + "ENTER YOUR CHOICE: ";
            print(msg);
            int option = 0;
         
                option = isANumber();
                switch (option) {
                    case 1 -> {print("REDIRECTING TO THE MAIN MENU\n"); redo = false;}
                    case 2 -> logout();
                    default -> {
                        print("INPUT IS OUT OF RANGE, TRY AGAIN\n");
                        System.out.println("====================================");
                    }
                }
        }

    }

    // NOTE: VERIFY INPUT IS A NUMBER
    public int isANumber() {
        while (true) {
            try {
                return input.nextInt();
            } catch (InputMismatchException e) {
                input.next();
                print("INVALID INPUT, TRY AGAIN: ");

            }
        }
    }
    // NOTE: CHECK IF ACCOUNT EXIST ALREADY
    public boolean isAccountExist(int accountNumber){
        return (ac.getAccountByAccountNumber(accountNumber) != null);

    }

     // NOTE: CHECK IF CLIENT EXIST ALREADY
     public boolean isClientExist(int clientId){
        return (cc.getClientById(clientId) != null);

    }

    // NOTE: REGEX FOR IDENTIFICATION I- FOLLOWED BY 4 DIGITS
    private String identificationFormat() {
        String regex = "(^I-)(\\d{4})";
        String s = input.next();
        while (!s.matches(regex)) {
            print("ENTER I- FOLLOWED BY 4 DIGITS, TRY AGAIN: ");
            s = input.next();
        }
        return s;
    }

    // NOTE: REGEX TO ONLY ACCEPT LETTERS
    private String onlyCharacters() {
        String regex = "[a-zA-Z]+";
        String s = input.next();
        while (!s.matches(regex)) {
            print("NUMBERS NOT ALLOWED, TRY AGAIN: ");
            s = input.next();
        }
        return s;
    }

    // NOTE: REGEX FOR Y OR N
    private String yesOrNoAnswer() {
        String regex = "^[Yy|Nn]";
        String s = input.next();
        while (!s.matches(regex)) {
            print("INVALID INPUT, ENTER Y OR N : ");
            s = input.next();
        }
        return s;
    }

    // NOTE: EXIT APPLICATION
    private void logout() {
        System.out.println("SIGNING OFF!, HAVE A GREAT DAY!");
        System.exit(0);
    }

    public void start(){
        printWelcome();
        chooseLanguage();
        tellerLogin();
        clientMenu();
    }

    // DEBUG:
    // public static void main(String[] args) {

    //     MainView v = new MainView();
    //     v.printWelcome();
    //     v.chooseLanguage();
    //     v.tellerLogin();
    //     v.clientMenu();

    // }
}
