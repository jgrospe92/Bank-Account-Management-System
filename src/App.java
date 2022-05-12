import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class App {
    public static void main(String[] args) throws Exception {
      
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("YYYY/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        String date = dtf.format(now);
        
        System.out.println(date);

        /*
        TELLER LOGIN
        ID = 1111
        Usernane = jgrospe
        Password = 1234 
        */
    }
}
