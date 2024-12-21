package memorizingtool;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

public abstract class BaseMemorize<T extends Comparable<T>> {
    Class<T> clazz;
    List<T> list;
    Map<String, Class<?>[]> commands;

    boolean finished = false;

    public BaseMemorize(Class<T> clazz) {
        this.list = new ArrayList<>();
        this.commands = new HashMap<>();
        this.clazz = clazz;

        commands.put("/help", new Class<?>[]{});
        commands.put("/menu", new Class<?>[]{});
        commands.put("/add", new Class<?>[]{clazz});
        commands.put("/remove", new Class<?>[]{int.class});
        commands.put("/replace", new Class<?>[]{int.class, clazz});
        commands.put("/replaceAll", new Class<?>[]{clazz, clazz});
        commands.put("/index", new Class<?>[]{clazz});
        commands.put("/sort", new Class<?>[]{String.class});
        commands.put("/frequency", new Class<?>[]{});
        commands.put("/print", new Class<?>[]{int.class});
        commands.put("/printAll", new Class<?>[]{String.class});
        commands.put("/getRandom", new Class<?>[]{});
        commands.put("/count", new Class<?>[]{clazz});
        commands.put("/size", new Class<?>[]{});
        commands.put("/equals", new Class<?>[]{int.class, int.class});
        commands.put("/readFile", new Class<?>[]{String.class});
        commands.put("/writeFile", new Class<?>[]{String.class});
        commands.put("/clear", new Class<?>[]{});
        commands.put("/compare", new Class<?>[]{int.class, int.class});
        commands.put("/mirror", new Class<?>[]{});
        commands.put("/unique", new Class<?>[]{});
    }

    void Run() {
        Scanner scanner = new Scanner(System.in);
        ArrayList<Object> args = new ArrayList<>();
        while (!finished) {
            args.clear();
            System.out.println("Perform action:");
            String[] data = scanner.nextLine().split(" ");

            if (!commands.containsKey(data[0])) {
                System.out.println("No such command");
                continue;
            }

            if (commands.get(data[0]).length != data.length - 1) {
                System.out.println("Incorrect amount of arguments provided to a command");
                continue;
            }

            Class<?>[] expectedParams = commands.get(data[0]);

            try {
                for (int i = 1; i < data.length; i++) {
                    if (expectedParams[i - 1].equals(int.class)) {
                        args.add(Integer.parseInt(data[i]));
                    } else if (expectedParams[i - 1].equals(Long.class)) {
                        args.add(Long.parseLong(data[i]));
                    } else if (expectedParams[i - 1].equals(String.class)) {
                        args.add(data[i]);
                    } else if (expectedParams[i - 1].equals(Boolean.class)) {
                        if (!data[i].equalsIgnoreCase("true") && !data[i].equalsIgnoreCase("false")) {
                            throw new NumberFormatException();
                        }
                        args.add(Boolean.parseBoolean(data[i]));
                    }
                }
            } catch (NumberFormatException e) {
                System.out.println("Some arguments can't be parsed! Expected types: " + Arrays.toString(expectedParams));
                continue;
            }

            Arrays.stream(this.getClass().getDeclaredMethods())
                    .filter(method -> method.getName().equals(data[0].substring(1)))
                    .findFirst()
                    .ifPresent(method -> {
                        try {
                            method.invoke(this, args.toArray());
                        } catch (IllegalAccessException | InvocationTargetException e) {
                            e.printStackTrace();
                        }
                    });
            Arrays.stream(this.getClass().getSuperclass().getDeclaredMethods())
                    .filter(method -> method.getName().equals(data[0].substring(1)))
                    .findFirst()
                    .ifPresent(method -> {
                        try {
                            method.invoke(this, args.toArray());
                        } catch (IllegalAccessException | InvocationTargetException e) {
                            e.printStackTrace();
                        }
                    });
        }
    }

    void unique() {
        Map<T, Long> counts = new HashMap<>();
        for (T i : list) {
            counts.put(i, counts.getOrDefault(i, 0L) + 1);
        }

        ArrayList<T> uniqueList = new ArrayList<>();
        for (Map.Entry<T, Long> entry : counts.entrySet()) {
            uniqueList.add(entry.getKey());
        }
        System.out.println("Unique values: " + uniqueList);
    }

    void mirror() {
        list = list.reversed();
        System.out.println("Data reversed");
    }

    void compare(int i, int j) {
        try {
            int res = list.get(i).compareTo(list.get(j));
            if (res > 0) {
                System.out.println("Result: " + list.get(i) + " > " + list.get(j));
            } else if (res < 0) {
                System.out.println("Result: " + list.get(i) + " < " + list.get(j));
            } else {
                System.out.println("Result: " + list.get(i) + " = " + list.get(j));
            }
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Index out of bounds!");
        }
    }

    void clear() {
        list.clear();
        System.out.println("Data cleared");
    }

    void writeFile(String path) throws IOException {
        FileProcessor fileProcessor = new FileProcessor();
        fileProcessor.write(path, list, clazz);
        System.out.println("Data exported: " + list.size());
    }

    void readFile(String path) throws IOException {
        FileProcessor fileProcessor = new FileProcessor();
        int startSize = list.size();
        list.addAll(fileProcessor.read(path, clazz));
        System.out.println("Data imported: " + (list.size() - startSize));
    }

    void equals(int i, int j) {
        try {
            boolean res = list.get(i).equals(list.get(j));
            System.out.printf("%d and %d elements are%s equal: %s\n", i, j, res ? "" : " not", list.get(i) + (res ? " = " : " != ") + list.get(j));
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Index out of bounds!");
        }
    }

    void size() {
        System.out.println("Amount of elements: " + list.size());
    }

    void count(T value) {
        int amount = 0;
        for (T i : list) {
            if (i.equals(value)) {
                amount++;
            }
        }
        System.out.println("Amount of " + value + ": " + amount);
    }

    void getRandom() {
        if (list.isEmpty()) {
            System.out.println("There are no elements memorized");
            return;
        }

        Random random = new Random();
        T element;
        if (list.size() == 1) {
            element = list.getFirst();
        } else {
            element = list.get(random.nextInt(0, list.size()));
        }
        System.out.println("Random element: " + element);
    }

    void printAll(String type) {
        switch (type) {
            case "asList":
                System.out.println("List of elements:\n" +
                        Arrays.toString(list.toArray()));
                break;
            case "lineByLine":
                System.out.println("List of elements:\n");
                for (T i : list) {
                    System.out.println(i);
                }
                break;
            case "oneLine":
                System.out.println("List of elements:");
                for (int i = 0; i < list.size() - 1; i++) {
                    System.out.print(list.get(i) + " ");
                }
                if (!list.isEmpty())
                    System.out.print(list.getLast());
                System.out.println();
                break;
            default:
                System.out.println("Incorrect argument, possible arguments: asList, lineByLine, oneLine");
                break;
        }
    }

    void print(int index) {
        try {
            System.out.println("Element on " + index + " position is " + list.get(index));
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Index out of bounds!");
        }
    }

    void frequency() {
        if (list.isEmpty()) {
            System.out.println("There are no elements in a list");
            return;
        }
        Map<T, Long> counts = new HashMap<>();
        for (T i : list) {
            if (counts.get(i) == null) {
                counts.put(i, 1L);
            } else {
                counts.put(i, counts.get(i) + 1);
            }
        }

        System.out.println("Frequency:");
        for (Map.Entry<T, Long> entry : counts.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
    }

    void sort(String way) {
        if (way.equals("ascending") || way.equals("descending")) {
            if (way.equals("ascending")) {
                list.sort(Comparator.naturalOrder());
            } else {
                list.sort((Comparator<? super T>) Comparator.naturalOrder().reversed());
            }
            System.out.printf("Memory sorted %s\n", way);
        } else {
            System.out.println("Incorrect argument, possible arguments: ascending, descending");
        }
    }

    void index(T value) {
        int i = list.indexOf(value);
        if (i == -1) {
            System.out.println("There is no such element");
            return;
        }
        System.out.println("First occurrence of " + value + " is on " + i + " position");
    }

    void replaceAll(T from, T to) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).equals(from)) {
                list.set(i, to);
            }
        }
        System.out.println("Each " + from + " element replaced with " + to);
    }

    void replace(int index, T element) {
        try {
            list.set(index, element);
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Index out of bounds!");
            return;
        }
        System.out.println("Element on " + index + " position replaced with " + element);
    }

    void remove(int index) {
        try {
            list.remove(index);
            System.out.println("Element on " + index + " position removed");
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Index out of bounds!");
        }
    }

    void add(T element) {
        try {
            list.add(element);
        } catch (ClassCastException e) {
            System.out.println("Some arguments can't be parsed");
            return;
        }
        System.out.println("Element " + element + " added");
    }

    void menu() {
        this.finished = true;
        list.clear();
        clazz = null;
        commands.clear();
        System.gc();
    }

    void help() {
        System.out.println(
                "===================================================================================================================\n" +
                        "Usage: COMMAND [<TYPE> PARAMETERS]\n" +
                        "===================================================================================================================\n" +
                        "General commands:\n" +
                        "===================================================================================================================\n" +
                        "/help - Display this help message\n" +
                        "/menu - Return to the menu\n" +
                        "\n" +
                        "/add [<T> ELEMENT] - Add the specified element to the list\n" +
                        "/remove [<int> INDEX] - Remove the element at the specified index from the list\n" +
                        "/replace [<int> INDEX] [<T> ELEMENT] - Replace the element at specified index with the new one\n" +
                        "/replaceAll [<T> OLD] [<T> NEW] - Replace all occurrences of specified element with the new " +
                        "one\n" +
                        "\n" +
                        "/index [<T> ELEMENT] - Get the index of the first specified element in the list\n" +
                        "/sort [ascending/descending] - Sort the list in ascending or descending order\n" +
                        "/frequency - The frequency count of each element in the list\n" +
                        "/print [<int> INDEX] - Print the element at the specified index in the list\n" +
                        "/printAll [asList/lineByLine/oneLine] - Print all elements in the list in specified format\n" +
                        "/getRandom - Get a random element from the list\n" +
                        "/count [<T> ELEMENT] - Count the number of occurrences of the specified element in the list\n" +
                        "/size - Get the number of elements in the list\n" +
                        "/equals [<int> INDEX1] [<int> INDEX2] - Check if two elements are equal\n" +
                        "/clear - Remove all elements from the list\n" +
                        "/compare [<int> INDEX1] [<int> INDEX2] Compare elements at the specified indices in the list\n" +
                        "/mirror - Mirror elements' positions in list\n" +
                        "/unique - Unique elements in the list\n" +
                        "/readFile [<string> FILENAME] - Import data from the specified file and add it to the list\n" +
                        "/writeFile [<string> FILENAME] - Export the list data to the specified file");
    }
}
