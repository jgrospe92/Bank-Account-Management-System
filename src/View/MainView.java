package View;

import java.util.Scanner;

import Controller.TellerController;
import DbHelper.TellerDAO;
import Model.TellersModel;

public class MainView {

    private TellerController tc = new TellerController();
    private TellersModel currentTeller;

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
        Scanner input = new Scanner(System.in);
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
        Scanner input = new Scanner(System.in);
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
        username = input.nextLine();
        print("ENTER Password: ");
        password = input.nextInt();

        currentTeller= tc.verifyLogin(username, password);
        while (currentTeller == null && attempts >= 0) {
            print("WARNING: Teller Does not Exist. " + (--attempts) + "remaining attempts.\n");
            print("ENTER USERNAME:");
            username = input.nextLine();
            print("\nENTER Password: ");
            password = input.nextInt();
            currentTeller = tc.verifyLogin(username, password);
        }
        loginSuccess();
    }
    private void loginSuccess(){
        print("====================================\n");
        print("Welcome, " + currentTeller.getFirstName() + ", " + "Last login " + currentTeller.getLastLogin());
        tc.updateLogin(currentTeller.getTellerId());

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

    }
}
