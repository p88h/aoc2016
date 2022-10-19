package aoc2016;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class day06 {
    public static void main(String[] args) throws IOException {
        List<HashMap<Character, Integer>> counters = new ArrayList<>();
        for (int i = 0; i < 8; ++i) counters.add(new HashMap<>());
        for (String line : Files.readAllLines(Paths.get("data/day06.in"))) {
            for (int i = 0; i < line.length(); ++i) {
                HashMap<Character, Integer> pmap = counters.get(i);
                pmap.put(line.charAt(i), pmap.getOrDefault(line.charAt(i), 0) + 1);
            }
        }
        StringBuilder sb = new StringBuilder(), sa = new StringBuilder();
        for (int i = 0; i < 8; i++) {
            HashMap<Character, Integer> pmap = counters.get(i);
            sa.append(Collections.max(pmap.entrySet(), Map.Entry.comparingByValue()).getKey());
            sb.append(Collections.min(pmap.entrySet(), Map.Entry.comparingByValue()).getKey());
        }
        System.out.println(sa);
        System.out.println(sb);
    }
}
