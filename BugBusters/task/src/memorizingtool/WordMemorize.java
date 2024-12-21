package memorizingtool;

import java.util.*;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class WordMemorize extends BaseMemorize<String> {

    public WordMemorize() {
        super(String.class);

        commands.put("/concat", new Class<?>[]{int.class, int.class});
        commands.put("/swapCase", new Class<?>[]{int.class});
        commands.put("/upper", new Class<?>[]{int.class});
        commands.put("/lower", new Class<?>[]{int.class});
        commands.put("/reverse", new Class<?>[]{int.class});
        commands.put("/length", new Class<?>[]{int.class});
        commands.put("/join", new Class<?>[]{String.class});
        commands.put("/regex", new Class<?>[]{String.class});
    }

    void help() {
        super.help();
        System.out.println(
                "===================================================================================================================\n" +
                        "Word-specific commands:\n" +
                        "===================================================================================================================\n" +
                        "/concat [<int> INDEX1] [<int> INDEX2] Concatenate two specified strings\n" +
                        "/swapCase [<int> INDEX] Output swapped case version of the specified string\n" +
                        "/upper [<int> INDEX] Output uppercase version of the specified string\n" +
                        "/lower [<int> INDEX] Output lowercase version of the specified string\n" +
                        "/reverse [<int> INDEX] Output reversed version of the specified string\n" +
                        "/length [<int> INDEX] Get the length of the specified string\n" +
                        "/join [<string> DELIMITER] Join all the strings with the specified delimiter\n" +
                        "/regex [<string> PATTERN] Search for all elements that match the specified regular expression " +
                        "pattern\n" +
                        "===================================================================================================================");
    }

    void concat(int i, int j) {
        try {
            System.out.println("Concatenated string: " + list.get(i) + list.get(j));
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Index out of bounds!");
        }
    }

    void swapCase(int i) {
        try {
            System.out.printf("\"%s\" string with swapped case: ", list.get(i));
            for (char c : (list.get(i)).toCharArray()) {
                if (Character.isUpperCase(c)) {
                    System.out.print(Character.toLowerCase(c));
                } else if (Character.isLowerCase(c)) {
                    System.out.print(Character.toUpperCase(c));
                } else {
                    System.out.print(c);
                }
            }
            System.out.println();
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Index out of bounds!");
        }
    }

    void upper(int i) {
        try {
            System.out.printf("Uppercase \"%s\" string: %s\n", list.get(i), (list.get(i)).toUpperCase());
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Index out of bounds!");
        }
    }

    void lower(int i) {
        try {
            System.out.printf("Lowercase \"%s\" string: %s\n", list.get(i), (list.get(i)).toLowerCase());
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Index out of bounds!");
        }
    }

    void reverse(int i) {
        try {
            System.out.printf("Reversed \"%s\" string: %s\n", list.get(i), new StringBuilder(list.get(i)).reverse());
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Index out of bounds!");
        }
    }

    void length(int i) {
        try {
            System.out.printf("Length of \"%s\" string: %d\n", list.get(i), (list.get(i)).length());
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Index out of bounds!");
        }
    }

    void join(String delimiter) {
        System.out.printf("Joined string: %s\n", String.join(delimiter, list));
    }

    void regex(String regex) {
        List<String> matchingElements = new ArrayList<>();
        Pattern pattern;
        try {
            pattern = Pattern.compile(regex);
        } catch (PatternSyntaxException e) {
            System.out.println("Incorrect regex pattern provided");
            return;
        }
        for (String element : list) {
            if (pattern.matcher(element).matches()) {
                matchingElements.add(element);
            }
        }
        if (matchingElements.isEmpty()) {
            System.out.println("There are no strings that match provided regex");
            return;
        }
        System.out.println("Strings that match provided regex:");
        System.out.println(Arrays.toString(matchingElements.toArray()));
    }
}
