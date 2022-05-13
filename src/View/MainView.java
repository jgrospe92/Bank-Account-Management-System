package View;


import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.util.logging.SimpleFormatter;

import Controller.AccountController;
import Controller.ClientController;
import Controller.TellerController;
import DbHelper.AccountDAO;
import Model.AccountsModel;
import Model.ClientsModel;
import Model.TellersModel;

public class MainView {

    private TellerController tc = new TellerController();
    private TellersModel currentTeller;
    private Scanner input = new Scanner(System.in);

    private ClientController cc = new ClientController();
    private AccountController ac = new AccountController();

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
                + "PRESS 3 TO VIEW CLIENT"
                + "PRESS 4 TO CREATE TRANSACTION\n"
                + "PRESS 5 TO DEACTIVATE CLIENT ACCOUNT\n"
                + "PRESS 6 TO SWITCH TELLER\n"
                + "PRESS 7 TO LOGOUT\n"
                + "ENTER YOUR CHOICE : ";
        System.out.println(str);
        print(menuOptions);
        opt = input.nextInt();
        switch (opt) {
            case 1 -> createNewClient();
            case 2 -> modifyExistingClient();
            case 3 -> print("VIEW CLIENT");
            case 4 -> print("PERFORMING TRANSACTIONS");
            case 5 -> print("DEACTIVATING CLIENT ACCOUNT");
            case 6 -> print("SWITCHING TELLER");
            case 7 -> System.exit(0);
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
            //cc.addAccountToClient(client);
            
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
        boolean isActive = isActive(balance);
        
        account = new AccountsModel(accountNumber, clientId, accountType, getCurrentDate() , balance, isActive);
        return account;
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
            case 3 -> print("DEACTIVATE ACCOUNT");
            case 4 -> clientMenu();
            case 5 -> System.exit(0);
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
            print("CLIENT DOES NOT EXIST...TRY AGAIN\n!");
            addDelay(1);
            updateClientInfo();
        }
       
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
    public static void main(String[] args) {

        MainView v = new MainView();
        v.printWelcome();
        v.chooseLanguage();
        v.tellerLogin();
        v.clientMenu();

    }
}
