package aoc2016;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class day18 {
    static String iterate(String from) {
        StringBuilder sb = new StringBuilder();
        sb.append(from.charAt(1));
        char prev = from.charAt(0);
        char middle = from.charAt(1);
        for (int i = 2; i < from.length(); i++) {
            char cur = from.charAt(i);
            sb.append((cur != prev) ? '^' : '.');
            prev = middle;
            middle = cur;
        }
        sb.append(from.charAt(from.length() - 2));
        return sb.toString();
    }

    public static void main(String[] args) throws IOException {
        String input = Files.readAllLines(Paths.get("data/day18.in")).get(0);
        int count = 0;
        for (int i = 0; i < 40; ++i) {
            // System.out.println(input);
            for (char c: input.toCharArray()) if (c == '.') count++;
            input = iterate(input);
        }
        System.out.println(count);
        for (int i = 0; i < 400000 - 40; ++i) {
            for (char c: input.toCharArray()) if (c == '.') count++;
            input = iterate(input);
        }
        System.out.println(count);
    }
}
