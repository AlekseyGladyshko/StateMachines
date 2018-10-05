import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Task {
    int wordMaxLength = 6;

    public List<String> commonLetters(Avtomat firstFA, Avtomat secondFA) throws IOException {
        List<String> letters = firstFA.getLetters();

        letters.retainAll(secondFA.getLetters());
        System.out.println(letters);
        return letters;
    }

    public void task(List<String> letters, Avtomat firstFA, Avtomat secondFA) {
        String word = "";
        for (int i = 1; i <= wordMaxLength; i++) {
            word = compliteWord(word, letters, i);
            int iterations = 0;

            while (iterations < i) {
                //System.out.println(word);

                if(firstFA.readableNFA(word) && secondFA.readableNFA(word)){
                    System.out.println(word + " - is readable by 2 FA");
                    return;
                }

                int lastLetter = word.length() - 1;
                int index = nextLetter(word, letters, lastLetter);

                if (index == -1) {
                    word = delLastLetter(word);
                    iterations++;
                    for (int k = word.length() - 1; k >= 0; k--) {
                        int tempIndex = nextLetter(word, letters, word.length() - 1);
                        if (tempIndex == -1) {
                            word = delLastLetter(word);
                            iterations++;
                        } else {
                            word = changeLastLetter(word, letters);
                            word = compliteWord(word, letters, i);
                            iterations = 0;
                            break;
                        }
                    }
                } else {
                    word = changeLastLetter(word, letters);
                }
            }
        }
    }

    public int nextLetter(String word, List<String> letters, int ind) {
        String lastLetter = Character.toString(word.charAt(ind));
        int index = letters.indexOf(lastLetter);

        if (index >= letters.size() - 1) {
            return -1;
        } else {
            return index + 1;
        }
    }

    public String delLastLetter(String word) {
        return word.substring(0, word.length() - 1);
    }

    public String changeLastLetter(String word, List<String> letters) {
        int index = letters.indexOf(Character.toString(word.charAt(word.length() - 1)));
        word = delLastLetter(word);
        word += letters.get(index + 1);
        return word;
    }

    public String compliteWord(String word, List<String> letters, int length) {
        for (int i = word.length(); i < length; i++) {
            word += letters.get(0);
        }
        return word;
    }
}
