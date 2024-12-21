package memorizingtool;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class FileProcessor {

    public <T> LinkedList<T> read(String fileName, Class<T> type) throws IOException {
        if (!Files.exists(Paths.get(fileName))) {
            System.out.println("File not found!");
            return null;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(fileName), 2048)) {
            if (type == Long.class) {
                return (LinkedList<T>) reader.lines()
                        .map(Long::parseLong)
                        .collect(Collectors.toCollection(() -> new LinkedList<>()));
            } else if (type == String.class) {
                return (LinkedList<T>) reader.lines()
                        .collect(Collectors.toCollection(() -> new LinkedList<>()));
            } else if (type == Boolean.class) {
                return (LinkedList<T>) reader.lines()
                        .map(Boolean::parseBoolean)
                        .collect(Collectors.toCollection(() -> new LinkedList<>()));
            } else {
                throw new IllegalArgumentException("Unsupported type: " + type);
            }
        }
    }

    public <T> void write(String fileName, List<T> data, Class<T> type) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName), 2048)) {
            if (type == Long.class || type == String.class || type == Boolean.class) {
                for (T value : data) {
                    writer.write(String.valueOf(value));
                    writer.newLine();
                }
            } else {
                throw new IllegalArgumentException("Unsupported type: " + type);
            }
        }
    }
}