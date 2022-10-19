package aoc2016;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class day04 {
    public static void main(String[] args) throws IOException {
        int total = 0;
        int found = 0;
        for (String line : Files.readAllLines(Paths.get("data/day04.in"))) {
            String[] ss = line.split("[^a-z0-9]");
            String expected = ss[ss.length - 1];
            int sectorId = Integer.parseInt(ss[ss.length - 2]);
            Map<Character, Integer> counts = new HashMap<>();
            for (int i = 0; i < ss.length - 2; i++)
                for (char c : ss[i].toCharArray())
                    counts.put(c, counts.getOrDefault(c, 0) + 1);
            String top = counts.entrySet().stream()
                    .sorted((a, b) -> {
                        if (!Objects.equals(a.getValue(), b.getValue())) {
                            return b.getValue().compareTo(a.getValue());
                        } else {
                            return a.getKey().compareTo(b.getKey());
                        }
                    }).limit(5)
                    .map(e -> String.valueOf(e.getKey()))
                    .collect(Collectors.joining());
            if (top.equals(expected)) {
                total += sectorId;
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < ss.length - 2; i++)
                    for (char c : ss[i].toCharArray()) sb.append((char) ((((c - 'a') + sectorId) % 26) + 'a'));
                if (sb.toString().equals("northpoleobjectstorage")) found = sectorId;
            }
        }
        System.out.println(total);
        System.out.println(found);
    }
}
