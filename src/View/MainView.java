package View;

import java.util.Locale;
import java.util.ResourceBundle;
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

    String language;
    String country;

    Locale currentLocale;
    ResourceBundle messages;

    // NOTE: CONSTANT VALUE:
    String INVALID_INPUT = "INVALID INPUT, PLEASE ENTER AGAIN";

    private TellerController tc = new TellerController();
    private TellersModel currentTeller;
    private Scanner input = new Scanner(System.in);

    private ClientController cc = new ClientController();
    private AccountController ac = new AccountController();
    private TransactionController trc = new TransactionController();

    // NOTE: WELCOME UI
    public void printWelcome() {
        String welcomeVar = messages.getString("INTRO2");
        String welcome = ""
                + "############################################\n"
                + welcomeVar + "\n"
                + "############################################\n";
        print(welcome);

    }

    // NOTE: CHANGE USER LANG
    public void chooseLanguage() {
        int opt = 0;

        String languageOption = ""
                + "Choose your language:\n"
                + "Press 1 to continue in English\n"
                + "Press 2 to continue in French\n"
                + "Enter your choice:\n";

        print(languageOption);
        opt = isANumber2();

        while (opt != 1 && opt != 2) {
            System.out.println(INVALID_INPUT);
            print(languageOption);
            opt = isANumber();
        }
        if (opt == 1) {
            language = "en";
            country = "US";
            currentLocale = new Locale(language, country);
            messages = ResourceBundle.getBundle("i18n.MessagesBundle",currentLocale);
        }else {
            language = "fr";
            country = "CA";
            currentLocale = new Locale(language, country);
            messages = ResourceBundle.getBundle("i18n.MessagesBundle",currentLocale);
        }
       
        System.out.println(messages.getString("INTRO"));
        addDelay(1);

    }

    // NOTE: TELLER LOGIN
    public void tellerLogin() {

        int attempts = 3;

        String username;
        int password;
        String str = ""
                + "==========================================================\n"
                + messages.getString("TELLER_LOGIN")
                + "==========================================================\n";

        print(str);
        print(messages.getString("ENTER_USERNAME"));
        username = input.next();
        print(messages.getString("ENTER_PASSWORD"));
        password = isANumber();

        currentTeller = tc.verifyLogin(username, password);
        while (currentTeller == null && attempts > 0) {

            print(messages.getString("TELLER_NOT_EXIST") + (attempts--) + " " + messages.getString("ATTEMPTS"));
            print(messages.getString("ENTER_USERNAME"));
            username = input.next();
            print(messages.getString("ENTER_PASSWORD"));
            password = isANumber();
            currentTeller = tc.verifyLogin(username, password);
        }
        if (attempts < 1) {
            print(messages.getString("INVALID_CRED"));
            logout();
        }

        loginSuccess();
    }

    private void loginSuccess() {
        print("==========================================================\n");
        print(messages.getString("WC") + currentTeller.getFirstName() + ", " + messages.getString("LAST_LOG")
                + tc.getTellerLastLoginDate(currentTeller.getTellerId()) + "\n");
        tc.updateLogin(currentTeller.getTellerId());

    }

    private void clientMenu() {
        int opt = 0;
        boolean goBack = false;
        String str = ""
                + "==========================================================\n"
                + messages.getString("MAIN")
                + "==========================================================";
        while (!goBack) {
            String menuOptions = messages.getString("MENU_OPT");
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
                    print(messages.getString("INPUT_OUT_OF_RANGE"));
                }
            }
        }

        tellerLogin();
        clientMenu();
    }

    private void createNewClient() {
        ClientsModel client = null;
        String str = ""
                + "==========================================================\n"
                + messages.getString("CC")
                + "==========================================================";
        System.out.println(str);
        print(messages.getString("ENTER_ID"));
        int id = isANumber();
        while(isClientExist(id)){
            System.out.println(messages.getString("CLIENT_EXIST"));
            print(messages.getString("ENTER_ID"));
            id = isANumber();
        }
        print(messages.getString("FN"));
        String firstName = onlyCharacters();
        print(messages.getString("LN"));
        String lastName = onlyCharacters();
        print(messages.getString("IDD"));
        String identification = identificationFormat();
        input.nextLine();
        print(messages.getString("ADDRESS"));
        String address = input.nextLine();
        client = new ClientsModel(id, firstName, lastName, identification, address);
        print(messages.getString("OPEN_ACCOUNT"));
        String answer = yesOrNoAnswer();
        if ("Y".equalsIgnoreCase(answer)) {
            AccountsModel account = addAccount(id);
            client.addAccount(account);
            cc.createOrUpdateClient(client);
            print(messages.getString("ADDED_CLIENT"));
            clientMenu();

        } else {
            print(messages.getString("REDIRECT_MAIN"));
            cc.createOrUpdateClient(client);
            clientMenu();
        }

    }

    // NOTE: RETURNS ACCOUNT TYPE
    private String accountType() {
        boolean redo = false;
        String result = "";
        do {
            String str = messages.getString("ACC_TYPE_OPT");

            print(str);
            int opt = isANumber();
            switch (opt) {
                case 1 -> {
                    result = messages.getString("SAVING");
                    redo = false;
                }
                case 2 -> {
                    result = messages.getString("CHECKING");
                    redo = false;
                }
                case 3 -> {
                    result = messages.getString("INVESTMENT");
                    redo = false;
                }
                default -> {
                    print(messages.getString("INPUT_OUT_OF_RANGE"));
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
                + "==========================================================\n"
                + messages.getString("ADD_ACC_HEADER")
                + "==========================================================";

        System.out.println(str);
        print(messages.getString("ENTER_ACC_NUM"));
        int accountNumber = isANumber();
        while(isAccountExist(accountNumber)){
            System.out.println(messages.getString("ACC_EXIST"));
            print(messages.getString("ENTER_ACC_NUM"));
            accountNumber = isANumber();
        }

        String accountType = accountType();
        print(messages.getString("ENTER_BAL"));
        int balance = isANumber();
        while (balance < 0) {
            print(messages.getString("BAL_NEGATIVE"));
            print(messages.getString("ENTER_BAL"));
            balance = isANumber();
        }
        boolean isActive = isActive(balance);

        account = new AccountsModel(accountNumber, clientId, accountType, getCurrentDate(), balance, isActive);
        return account;
    }

    // NOTE: VIEW CLIENT
    public void viewClient() {
        String str = ""
                + "==========================================================\n"
                + messages.getString("CLIENT_D_HEADER")
                + "==========================================================";
        System.out.println(str);
        print(messages.getString("PL_ENTER_C_ID"));
        int clientId = isANumber();
        ClientsModel client = cc.getClientById(clientId);
        if (client != null) {
            print("==========================================================\n");
            System.out.println(messages.getString("C_ID") + client.getId());
            System.out.println(messages.getString("C_FN") + client.getFirstName());
            System.out.println(messages.getString("C_LN")+ client.getLastName());
            System.out.println(messages.getString("C_IDD") + client.getIdentification());
            System.out.println(messages.getString("C_ADD") + client.getAddress());
            List<AccountsModel> accounts = ac.showAllAccountOnThisClient(clientId);
            iterateAccountList(accounts);
        } else {
            print(messages.getString("CLIENT_NOT_FOUND"));
            addDelay(1);
            clientMenu();
        }
        navigation();
    }

    private void modifyExistingClient() {
        boolean isValid = true;
        String str = ""
                +"==========================================================\n"
                +messages.getString("MOD_HEADER")
                +"==========================================================";
                
        System.out.println(str);
        String modOutput = messages.getString("MOD_OPT");
        print(modOutput);
        int opt = isANumber();
        switch (opt) {
            case 1 -> updateClientInfo();
            case 2 -> openNewAccount();
            case 3 -> print(messages.getString("REDIRECT_MAIN"));
            case 4 -> logout();
            default -> {
                System.out.println(messages.getString("INPUT_OUT_OF_RANGE"));
                isValid = false;
            }
        }
        if (!isValid) {
            modifyExistingClient();
        }
    }

    private void updateClientInfo() {
        print(messages.getString("PL_ENTER_C_ID"));
        int clientId = isANumber();

        // NOTE: GETS THE MATCHING CLIENT ID
        ClientsModel client = cc.getClientById(clientId);
        if (client != null) {
            print(messages.getString("NEW_FN"));
            String firstName = onlyCharacters();
            print(messages.getString("NEW_LN"));
            String lastName = onlyCharacters();
            print(messages.getString("NEW_IDD"));
            String identification = identificationFormat();
            input.nextLine();
            print(messages.getString("NEW_ADD"));
            String address = input.nextLine();

            client.setFirstName(firstName);
            client.setLastName(lastName);
            client.setIdentification(identification);
            client.setAddress(address);
            cc.createOrUpdateClient(client);
            print(messages.getString("SUCC_MAIN"));
            modifyExistingClient();
        } else {
            print(messages.getString("CLIENT_NOT_FOUND"));
            addDelay(1);
            updateClientInfo();
        }

    }

    public void openNewAccount() {
        print(messages.getString("PL_ENTER_C_ID"));
        int clientId = isANumber();
        ClientsModel client = cc.getClientById(clientId);
        if (client != null) {

            client.addAccount(addAccount(clientId));
            cc.createOrUpdateClient(client);
            print(messages.getString("SUCC_MAIN"));
            modifyExistingClient();
        } else {
            print(messages.getString("CLIENT_NOT_FOUND"));
            addDelay(1);
            openNewAccount();
        }

    }

    private void deleteAccount() {
        print(messages.getString("PL_ENTER_C_ID"));
        int clientId = isANumber();
        ClientsModel client = cc.getClientById(clientId);
        if (client != null) {
            print(messages.getString("WARNING_BAL"));
            List<AccountsModel> accounts = ac.showAllAccountOnThisClient(clientId);
            iterateAccountList(accounts);

            print(messages.getString("DEACTIVATE"));
            int accountNumber = isANumber();
            boolean hasFoundAccount = false;
            // REFACTOR:
            for (AccountsModel account : accounts) {
                if (account.getAccountNumber() == accountNumber) {
                    if (account.getBalance() == 0) {
                        print(messages.getString("CONFIRM_DEACTIVATE"));
                    if (yesOrNoAnswer().equalsIgnoreCase("y")) {
                        account.setActive(false);
                        ac.createOrUpdateAccount(account);
                        hasFoundAccount = true;
                        print(messages.getString("SUCC_MAIN"));
                        break;
                    }
                    } else {
                        hasFoundAccount = true;
                        print(messages.getString("FAILED_DEA"));
                        print(messages.getString("ACCOUNT_HAS") + account.getBalance() + " " + messages.getString("REM_BAL"));
                        break;
                    }
                }
            }
            if (!hasFoundAccount) {
                print(messages.getString("ACCOUNT_NOT_FOUND"));
                deleteAccount();
            }

            modifyExistingClient();
        } else {
            print(messages.getString("CLIENT_NOT_FOUND"));
            addDelay(1);
            modifyExistingClient();
        }

    }

    // NOTE: CREATE TRANSACTION
    private void doTransaction() {
        String str = ""
                +"==========================================================\n"
                +messages.getString("TRANSACTION_HEADER")
                +"==========================================================";
        System.out.println(str);
        print(messages.getString("PL_ENTER_C_ID"));
        int clientId = isANumber();
        ClientsModel client = cc.getClientById(clientId);
        if (client != null) {
            Boolean goBack = false;
            System.out.println(messages.getString("CLIENT_NAME")+ client.getFirstName() + " " + client.getLastName());
            while (!goBack) {
                String menuOptions = messages.getString("T_OPT");
                System.out.println("==========================================================");
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
                    default -> print(messages.getString("INPUT_OUT_OF_RANGE"));
                }
            }

        } else {
            print(messages.getString("CLIENT_NOT_FOUND"));
            addDelay(1);
            doTransaction();
        }
        clientMenu();
    }

    public void withdraw(ClientsModel client) {
        boolean hasFoundAccount = false;
        print(messages.getString("PL_ENTER_ACC"));
        int accountNumber = isANumber();
        List<AccountsModel> accounts = ac.showAllAccountOnThisClient(client.getId());
        for (AccountsModel account : accounts) {
            if (account.getAccountNumber() == accountNumber && account.isActive()) {
                print(messages.getString("CURR_BAL") + account.getBalance() + "\n");
                print(messages.getString("ENTER_W_AM"));
                int amount = isANumber();
                while (!(isAmountValid(amount))) {
                    print(messages.getString("AMOUNT_ERROR"));
                    print(messages.getString("ENTER_W_AM"));
                    amount = isANumber();
                }

                int withdrawAmount = account.getBalance() - amount;
                while (!ac.updateAccountBalance(account, withdrawAmount)) {
                    print(messages.getString("YOUR_CUR_BAL") + account.getBalance() + "\n");
                    print(messages.getString("ENTER_W_AM"));
                    amount = isANumber();
                    withdrawAmount = account.getBalance() - amount;
                }
                trc.insertWithdrawTransaction(account, withdrawAmount);

                System.out.println(messages.getString("SUC_WITHDRAW") + amount);
                System.out.println(messages.getString("NEW_BAL") + account.getBalance());
                hasFoundAccount = true;
                break;

            }
        }
        if (!hasFoundAccount) {
            print(messages.getString("ERROR_OF"));

        }

    }

    public void deposit(ClientsModel client) {
        boolean hasFoundAccount = false;
        int newBalance = 0;
        print(messages.getString("PL_ENTER_ACC"));
        int accountNumber = isANumber();
        List<AccountsModel> accounts = ac.showAllAccountOnThisClient(client.getId());
        for (AccountsModel account : accounts) {
            if (account.getAccountNumber() == accountNumber && account.isActive()) {
                print(messages.getString("YOUR_CUR_BAL") + account.getBalance() + "\n");
                print(messages.getString("ENTER_D_AMOUNT"));
                int depositAmount = isANumber();
                while (!(isAmountValid(depositAmount))) {
                    print(messages.getString("AMOUNT_ERROR"));
                    print(messages.getString("ENTER_D_AMOUNT"));
                    depositAmount = isANumber();
                }
                newBalance = account.getBalance() + depositAmount;
                while (!ac.updateAccountBalance(account, newBalance) && depositAmount < 1) {
                    print(messages.getString("YOUR_CUR_BAL") + account.getBalance() + "\n");
                    print(messages.getString("ENTER_D_AMOUNT"));
                    newBalance = account.getBalance() + depositAmount;
                }
                trc.insertDepositTransaction(account, depositAmount);

                System.out.println(messages.getString("SUC_DEPOSIT") + depositAmount);
                System.out.println(messages.getString("NEW_BAL") + account.getBalance());
                hasFoundAccount = true;
                break;

            }
        }
        if (!hasFoundAccount) {
            print(messages.getString("ERROR_OF"));

        }
    }

    private void transferAmount(ClientsModel client) {
        boolean hasFoundAccount = false;
        print(messages.getString("PL_ENTER_ACC"));
        int accountNumber = isANumber();
        List<AccountsModel> accounts = ac.showAllAccountOnThisClient(client.getId());
        for (AccountsModel account : accounts) {
            if (account.getAccountNumber() == accountNumber && account.isActive()) {
                print(messages.getString("TRANSFER_TO"));
                int accountNumber2 = isANumber();

                AccountsModel account2 = ac.getAccountByAccountNumber(accountNumber2);
                while (account2 == null || accountNumber == accountNumber2 || !account2.isActive()) {
                    print(messages.getString("TRANSFER_ERROR"));
                    print(messages.getString("TRANSFER_TO"));
                    accountNumber2 = isANumber();
                    account2 = ac.getAccountByAccountNumber(accountNumber2);
                }
             
                print(messages.getString("NEW_BAL") + account.getBalance() + "\n");
                print(messages.getString("AMOUNT_TO_TRANSFER"));
                int amount = isANumber();
                while (!isAmountValid(amount) || account.getBalance() < amount) {
                    print(messages.getString("TRANSFER_LESS_THAN"));
                    print(messages.getString("AMOUNT_TO_TRANSFER"));
                    amount = isANumber();
                }

                ac.updateAccountBalance(account, account.getBalance() - amount);
                ac.updateAccountBalance(account2, account2.getBalance() + amount);
                trc.insertTransferTransaction(account, account2, amount);

                System.out.println(
                        messages.getString("SUC_TRANSFER") + amount + " " + messages.getString("TO_ACC") + account2.getAccountNumber());
                System.out.println(messages.getString("NEW_BAL") + account.getBalance());
                hasFoundAccount = true;
                break;

            }
        }
        if (!hasFoundAccount) {
            print(messages.getString("ERROR_OF"));

        }

    }

    public void deactivateAccount(ClientsModel client) {
        boolean hasFoundAccount = false;
        print(messages.getString("DEACTIVATE"));
        int accountNumber = isANumber();
        List<AccountsModel> accounts = ac.showAllAccountOnThisClient(client.getId());
        for (AccountsModel account : accounts) {
            if (account.getAccountNumber() == accountNumber) {
                if (account.getBalance() == 0) {
                    print(messages.getString("CONFIRM_DEACTIVATE"));
                    if (yesOrNoAnswer().equalsIgnoreCase("y")) {
                        account.setActive(false);
                        ac.createOrUpdateAccount(account);
                        hasFoundAccount = true;
                        print(messages.getString("SUCC_MAIN"));
                        break;
                    }
                } else {
                    hasFoundAccount = true;
                    print(messages.getString("FAILED_DEA"));
                    print(messages.getString("ACCOUNT_HAS")+ account.getBalance() + " " + messages.getString("REM_BAL"));
                }
            }

        }
        if (!hasFoundAccount) {
            print(messages.getString("ERROR_OF"));


        }
    }

    public void reactivateAccount(ClientsModel client) {
        boolean hasFoundAccount = false;
        print(messages.getString("ENTER_ACC_RE"));
        int accountNumber = isANumber();
        List<AccountsModel> accounts = ac.showAllAccountOnThisClient(client.getId());
        for (AccountsModel account : accounts) {
            if (account.getAccountNumber() == accountNumber) {
                if (account.getBalance() == 0) {
                    print(messages.getString("RE_ACTV_CON"));
                    if (yesOrNoAnswer().equalsIgnoreCase("y")) {
                        account.setActive(true);
                        ac.createOrUpdateAccount(account);
                        hasFoundAccount = true;
                        print(messages.getString("SUCC_MAIN"));
                        break;
                    }
                } else {
                    hasFoundAccount = true;
                    print(messages.getString("FAILED_RE"));
                }
            }

        }
        if (!hasFoundAccount) {
            print(messages.getString("ERROR_OF"));
        }
    }

    public void viewAllTransaction() {
        String str = ""
                +"==========================================================\n"
                +messages.getString("TRANSACTION_RE")
                +"==========================================================";
                
        System.out.println(str);
        trc.getAllTransaction();
        while (trc.hasNext()) {
            TransactionModel transaction = trc.next();
            System.out.println("==========================================================");
            print(messages.getString("TRA_ID") + transaction.getTransactionId() + "\n");
            print(messages.getString("TRA_TO_ACC") + transaction.getToAccountNumber() + "\n");
            print(messages.getString("TR_FROM_ACC")+ transaction.getFromAccountNumber() + "\n");
            print(messages.getString("TRA_DETAILS")+ transaction.getTransactionDetail() + "\n");
            print(messages.getString("TRA_VAL") + transaction.getValue() + "\n");

        }
        System.out.println("==========================================================");
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
            System.out.println("========================================================");
            System.out.println(messages.getString("I_ACC") + account.getAccountNumber());
            System.out.println(messages.getString("I_ACC_TYPE") + account.getAccountType());
            System.out.println(messages.getString("I_OPENDATE") + account.getOpenDate());
            System.out.println(messages.getString("I_BAL") + account.getBalance());
            System.out.println(messages.getString("I_ACTIVE") + account.isActive());
            System.out.println("========================================================");
        }
    }

    // NOTE: NAVIGATION
    public void navigation() {
        boolean redo = true;
        while (redo) {
            String msg = messages.getString("NAV_OPT");
            print(msg);
            int option = 0;
         
                option = isANumber();
                switch (option) {
                    case 1 -> {print(messages.getString("REDIRECT_MAIN")); redo = false;}
                    case 2 -> logout();
                    default -> {
                        print(messages.getString("INPUT_OUT_OF_RANGE"));
                        System.out.println("========================================================");
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
                print(messages.getString("INVALID_INPT"));

            }
        }
    }
    // NOTE: VERIFY INPUT IS A NUMBER
    public int isANumber2() {
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
            print(messages.getString("IDD_FORMAT"));
            s = input.next();
        }
        return s;
    }

    // NOTE: REGEX TO ONLY ACCEPT LETTERS
    private String onlyCharacters() {
        String regex = "[a-zA-Z]+";
        String s = input.next();
        while (!s.matches(regex)) {
            print(messages.getString("NUM_NOT_ALLOWED"));
            s = input.next();
        }
        return s;
    }

    // NOTE: REGEX FOR Y OR N
    private String yesOrNoAnswer() {
        String regex = "^[Yy|Nn]";
        String s = input.next();
        while (!s.matches(regex)) {
            print(messages.getString("Y_N"));
            s = input.next();
        }
        return s;
    }

    // NOTE: EXIT APPLICATION
    private void logout() {
        System.out.println(messages.getString("LOGOUT"));
        System.exit(0);
    }

    public void start(){
        chooseLanguage();
        printWelcome();
        tellerLogin();
        clientMenu();
    }
}
