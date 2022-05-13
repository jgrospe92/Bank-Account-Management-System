package View;

import java.util.Scanner;

import Controller.TellerController;
import Model.TellersModel;

public class MainView {

    private TellerController tc = new TellerController();
    private TellersModel currentTeller;
    private Scanner input = new Scanner(System.in);

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
                + "PRESS 3 PERFORM TRANSACTION\n"
                + "PRESS 4 TO DEACTIVATE CLIENT ACCOUNT\n"
                + "PRESS 5 TO SWITCH TELLER\n"
                + "ENTER YOUR CHOICE : ";
        System.out.println(str);
        print(menuOptions);
        opt = input.nextInt();
        switch (opt) {
            case 1 -> print("CREATING NEW CLIENT");
            case 2 -> print("MODIFYING EXISTING CLIENT");
            case 3 -> print("PERFORMING TRANSACTIONS");
            case 4 -> print("DEACTIVATING CLIENT ACCOUNT");
            case 5 -> print("SWITCHING TELLER");
            default -> {
                print("INVALID INPUT\n");
                isValid = false;
            }
        }
        if (!isValid) {
            clientMenu();
        }
    }

    // NOTE: PRINT DATA
    public static <T> void print(T data) {
        System.out.print(data);
    }

    public static void main(String[] args) {

        MainView v = new MainView();
        v.printWelcome();
        v.chooseLanguage();
        v.tellerLogin();
        v.clientMenu();

    }
}
