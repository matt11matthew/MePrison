import java.util.ArrayList;
import java.util.List;

/**
 * Created by Matthew E on 11/17/2017.
 */
public class Generate {

    public static void main(String[] args) {
        List<String> printString = new ArrayList<>();
        printString.add("ranks:");
        String[] letters = new String[] {
                "a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z"
        };
        int costIncrease = 2000;
        int index = 1;
        for (String letter : letters) {
            printString.add("  " + letter + ":");
            printString.add("    tag: " + "\"7[&a" + letter.toUpperCase() + "&7] &f\"" );
            if (letter.equalsIgnoreCase("a")) {

                printString.add("    cost: -1");
            } else {
                printString.add("    cost: " + costIncrease * index);
            }
            index++;
        }
        for (String s : printString) {

            System.out.println(s);
        }
    }
}
