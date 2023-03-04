import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Random;

public class xkcdpwgen {

    private static final String WORDLIST = "words.txt";

    private final List<String> words;

    public xkcdpwgen() throws IOException {
        this(WORDLIST);
    }

    public xkcdpwgen(String wordListPath) throws IOException {
        this.words = Files.readAllLines(Paths.get(wordListPath));
    }

    public String generatePassword(int numWords, boolean capitalize, int numNumbers, int numSymbols) {
        Random rand = new Random();
        StringBuilder password = new StringBuilder();

        for (int i = 0; i < numWords; i++) {
            int index = rand.nextInt(this.words.size());
            String word = this.words.get(index);
            if (capitalize) {
                word = word.substring(0, 1).toUpperCase() + word.substring(1);
            }
            password.append(word);
        }

        for (int i = 0; i < numNumbers; i++) {
            password.insert(rand.nextInt(password.length() + 1), rand.nextInt(10));
        }

        String symbols = "~!@#$%^&*.:;";
        for (int i = 0; i < numSymbols; i++) {
            password.insert(rand.nextInt(password.length() + 1), symbols.charAt(rand.nextInt(symbols.length())));
        }

        return password.toString();
    }

    public static void main(String[] args) {
        try {
            xkcdpwgen generator = new xkcdpwgen();

            int numWords = 4;
            boolean capitalize = false;
            int numNumbers = 0;
            int numSymbols = 0;

            for (int i = 0; i < args.length; i++) {
                if (args[i].equals("-w") || args[i].equals("--words")) {
                    numWords = Integer.parseInt(args[++i]);
                } else if (args[i].equals("-c") || args[i].equals("--caps")) {
                    capitalize = true;
                } else if (args[i].equals("-n") || args[i].equals("--numbers")) {
                    numNumbers = Integer.parseInt(args[++i]);
                } else if (args[i].equals("-s") || args[i].equals("--symbols")) {
                    numSymbols = Integer.parseInt(args[++i]);
                }
            }

            String password = generator.generatePassword(numWords, capitalize, numNumbers, numSymbols);
            System.out.println(password);
        } catch (IOException e) {
            System.err.println("Failed to load word list file: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.err.println("Invalid command line argument: " + e.getMessage());
        }
    }
}
