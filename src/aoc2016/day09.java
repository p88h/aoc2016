package aoc2016;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class day09 {
    static long computelen(String input, int a, int b, boolean rec) {
        long len = 0;
        for (int i = a; i <= b; i++) {
            if (input.charAt(i) == '(') {
                int j = input.indexOf('x', i);
                int k = input.indexOf(')', i);
                int n = Integer.parseInt(input.substring(i + 1, j));
                int m = Integer.parseInt(input.substring(j + 1, k));
                if (rec) {
                    len += computelen(input, k + 1, k + n, rec) * (long) m;
                } else {
                    len += (long) n * m;
                }
                i = k + n;
            } else {
                len += 1;
            }
        }
        return len;
    }

    public static void main(String[] args) throws IOException {
        String input = Files.readAllLines(Paths.get("data/day09.in")).get(0);
        System.out.println(computelen(input, 0, input.length() - 1, false));
        System.out.println(computelen(input, 0, input.length() - 1, true));
    }
}
