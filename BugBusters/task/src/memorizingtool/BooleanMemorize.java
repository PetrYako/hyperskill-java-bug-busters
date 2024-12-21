package memorizingtool;

public class BooleanMemorize extends BaseMemorize<Boolean> {

    public BooleanMemorize() {
        super(Boolean.class);

        commands.put("/flip", new Class<?>[]{int.class});
        commands.put("/negateAll", new Class<?>[]{});
        commands.put("/and", new Class<?>[]{int.class, int.class});
        commands.put("/or", new Class<?>[]{int.class, int.class});
        commands.put("/logShift", new Class<?>[]{int.class});
        commands.put("/convertTo", new Class<?>[]{String.class});
        commands.put("/morse", new Class<?>[]{});
    }

    void help() {
        super.help();
        System.out.println(
                "===================================================================================================================\n" +
                        "Boolean-specific commands:\n" +
                        "===================================================================================================================\n" +
                        "/flip [<int> INDEX] - Flip the specified boolean\n" +
                        "/negateAll - Negate all the booleans in memory\n" +
                        "/and [<int> INDEX1] [<int> INDEX2] - Calculate the bitwise AND of the two specified elements\n" +
                        "/or [<int> INDEX1] [<int> INDEX2] - Calculate the bitwise OR of the two specified elements\n" +
                        "/logShift [<int> NUM] - Perform a logical shift of elements in memory by the specified amount\n" +
                        "/convertTo [string/number] - Convert the boolean(bit) sequence in memory to the specified type\n" +
                        "/morse - Convert the boolean(bit) sequence to Morse code\n" +
                        "===================================================================================================================");
    }

    void flip(int index) {
        try {
            list.set(index, !list.get(index));
            System.out.println("Element on " + index + " position flipped");
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Index out of bounds!");
        }
    }

    void negateAll() {
        list.replaceAll(e -> !e);
        System.out.println("All elements negated");
    }

    void and(int i, int j) {
        try {
            boolean a = list.get(i);
            boolean b = list.get(j);
            boolean res = a && b;
            System.out.printf("Operation performed: (%b && %b) is %b\n", a, b, res);
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Index out of bounds!");
        }
    }

    void or(int i, int j) {
        try {
            boolean a = list.get(i);
            boolean b = list.get(j);
            boolean res = a || b;
            System.out.printf("Operation performed: (%b || %b) is %b\n", a, b, res);
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Index out of bounds!");
        }
    }

    void logShift(int n) {
        int outputValue = n;
        int size = list.size();

        if (size == 0) {
            return;
        }
        n %= size;
        if (n < 0) {
            n += size;
        }
        for (int i = 0; i < n; i++) {
            Boolean last = list.get(size - 1);
            for (int j = size - 1; j > 0; j--) {
                list.set(j, list.get(j - 1));
            }
            list.set(0, last);
        }
        System.out.println("Elements shifted by " + outputValue);
    }

    void convertTo(String type) {
        if (list.isEmpty()) {
            System.out.println("No data memorized");
            return;
        }
        StringBuilder binary = new StringBuilder();
        for (boolean b : list) {
            if (b) {
                binary.append("1");
            } else {
                binary.append("0");
            }
        }
        switch (type.toLowerCase()) {
            case "number":
                System.out.println("Converted: " + Long.parseLong(binary.toString(), 2));
                break;
            case "string":
                int byteSize = Byte.SIZE;
                StringBuilder sb = new StringBuilder();
                if (binary.length() % byteSize != 0) {
                    System.out.println("Amount of elements is not divisible by 8, so the last " + binary.length() % byteSize + " of " +
                            "them will be ignored");
                }
                for (int i = 0; i < binary.length(); i += byteSize) {
                    String segment = binary.substring(i, Math.min(i + byteSize, binary.length()));
                    int asciiValue = Integer.parseInt(segment, 2);
                    sb.append((char) asciiValue);
                }
                String asciiSequence = sb.toString();
                System.out.println("Converted: " + asciiSequence);
                break;
            default:
                System.out.println("Incorrect argument, possible arguments: string, number");
                break;
        }
    }

    void morse() {
        if (list.isEmpty()) {
            System.out.println("No data memorized");
            return;
        }
        StringBuilder morseCode = new StringBuilder("Morse code: ");
        for (boolean b : list) {
            if (b) {
                morseCode.append(".");
            } else {
                morseCode.append("_");
            }
        }
        System.out.println(morseCode);
    }
}
