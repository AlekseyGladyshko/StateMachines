import java.io.File;
import java.util.List;
import java.util.ArrayList;
import java.io.*;

public class Main {
    public static void main(String[] args) throws IOException {
        String firstAvtomat = "Avtomat1.txt";
        String secondAvtomat = "Avtomat2.txt";
        Avtomat firstFA = new Avtomat(firstAvtomat);
        Avtomat secondFA = new Avtomat(secondAvtomat);

        Task task = new Task();
        List<String> letters = task.commonLetters(firstFA, secondFA);
        task.task(letters, firstFA, secondFA);
    }
}
