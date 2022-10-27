package aoc2016;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class day07 {
    public static void main(String[] args) throws IOException {
        int count = 0, count2 = 0;
        for (String line : Files.readAllLines(Paths.get("data/day07.in"))) {
            boolean brackets = false, good = false, bad = false, prev = false, match = false;
            Set<String> bab = new HashSet<>(), aba = new HashSet<>();
            for (int i = 0; i < line.length(); ++i) {
                if (line.charAt(i) == '[') brackets = true;
                if (line.charAt(i) == ']') brackets = false;
                boolean same = (i > 0 && line.charAt(i-1) == line.charAt(i));
                // ABBA / BAAB
                if (!same && prev && i > 2 && line.charAt(i-3) == line.charAt(i)) {
                    if (brackets) bad = true; else good = true;
                }
                prev = same;
                // ABA / BAB
                if (!same && i > 1 && line.charAt(i-2) == line.charAt(i)) {
                    if (brackets) {
                        String id = line.substring(i-2, i);
                        bab.add(id);
                        if (aba.contains(id)) match=true;
                    } else {
                        String id = line.substring(i-1, i+1);
                        aba.add(id);
                        if (bab.contains(id)) match=true;
                    }
                }
            }
            if (good && !bad) count++;
            if (match) count2++;
        }
        System.out.println(count);
        System.out.println(count2);
    }

}
