import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.Arrays;

public class Avtomat {
    private String states, alphabet, initialStates, finalStates;
    private List<String> letters = new ArrayList<String>();
    private List<String> transactions = new ArrayList<String>();

    public Avtomat(String filePath) {
        Charset charset = Charset.forName("US-ASCII");
        try (BufferedReader reader = (BufferedReader) Files.newBufferedReader(Paths.get(filePath), charset)) {
            String line = reader.readLine();

            //read by line
            this.alphabet = line;
            line = reader.readLine();
            this.states = line;
            line = reader.readLine();
            this.initialStates = line;
            line = reader.readLine();
            this.finalStates = line;

            //read transaction
            while ((line = reader.readLine()) != null) {
                if (!(transactions.contains(line))) {
                    transactions.add(line);
                }

                String letter = line.substring(2, 3);
                if (!(letters.contains(letter))) {
                    letters.add(letter);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getStates() {
        return states;
    }

    public String getAlphabet() {
        return alphabet;
    }

    public String getInitialStates() {
        return initialStates;
    }

    public String getFinalStates() {
        return finalStates;
    }

    public List<String> getTransactions() {
        return transactions;
    }

    public List<String> getLetters() {
        return letters;
    }


    public boolean checkForFinalState(String currentState) {
        if (finalStates.contains(currentState)) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isFinalState(String currentState) {
        if (finalStates.contains(currentState)) {
            return true;
        } else {
            return false;
        }
    }

    public String hasTransaction(String currentState, String letter) {
        for (String transaction : transactions) {
            if (transaction.contains(currentState + " " + letter)) {
                return transaction;
            }
        }
        return "";
    }


    public List<String> splitTransaction(String transaction, List<String> list) {
        String[] splitted = transaction.split(" ");
        for (String part : splitted) {
            list.add(part);
        }
        return list;
    }

    public String rollback(List<String> transactions, List<String> transactionHistory) {
        String lastTransaction = transactionHistory.get(transactionHistory.size()-1);

        int lastTransactionIndex = transactions.indexOf(lastTransaction);
        int newTransactionIndex;

        List<String> lastStates = new ArrayList<String>();
        lastStates = splitTransaction(lastTransaction, lastStates);

        for (String trans : transactions) {
            if(trans.contains(lastStates.get(0) + " " + lastStates.get(1))){
                newTransactionIndex = transactions.indexOf(trans);
                if(newTransactionIndex > lastTransactionIndex){
                    return trans;
                }
            }
        }
        //новой нету или не имееться перехода по букве
        transactionHistory.remove(transactionHistory.size()-1);
        if(transactionHistory.size() == 0){
            return "0";
        }

        return "-1";
    }


    public boolean readableNFA(String word) {
        List<String> letters = new ArrayList<String>();
        int iterator = 0;
        String currentState = initialStates;
        for (int i = 0; i < word.length(); i++) {
            letters.add(Character.toString(word.charAt(i)));
        }

        String transaction = hasTransaction(currentState, letters.get(iterator)); // letters.get(iterator) = первая буква
        while (transaction != "") {
            List<String> transactionHistory = new ArrayList<>();
            transactionHistory.add(transaction);

            List<String> states = new ArrayList<String>();
            states = splitTransaction(transaction, states);

            currentState = states.get(2);
            if (word.length() - 1 != iterator) { // не последняя буква
                iterator++;
            } else {
                if (isFinalState(currentState)) {
                    return true;
                } else {
                    String resultOfRollBack;
                    do {
                        resultOfRollBack = rollback(transactions, transactionHistory);
                    } while (resultOfRollBack.equals("-1"));

                    if(resultOfRollBack.equals("0")){
                        return false;
                    }
                }
            }
            transaction = hasTransaction(currentState, letters.get(iterator));
        }
        return false;
    }
}
