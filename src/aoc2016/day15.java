package aoc2016;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class day15 {

    public static int invmod(int a, int b) {
        int m = b, t, q;
        int x = 0, y = 1;
        if (b == 1) return 0;
        while (a > 1) {
            q = a / b;
            t = b;
            b = a % b;
            a = t;
            t = x;
            x = y - q * x;
            y = t;
        }
        if (y < 0) y += m;
        return y;
    }

    public static int xcrt(List<Integer> as, List<Integer> ns) {
        int result = 0;
        int product = 1;
        for (int n : ns) product *= n;
        for (int i = 0; i < as.size(); i++) {
            int b = product / ns.get(i);
            result += as.get(i) * b * invmod(b, ns.get(i));
        }
        return product - (result % product);
    }

    public static void main(String[] args) throws IOException {
        int idx = 1;
        List<Integer> as = new ArrayList<>();
        List<Integer> ns = new ArrayList<>();
        for (String line : Files.readAllLines(Paths.get("data/day15.in"))) {
            line = line.replaceAll("[.,]", "");
            String[] parts = line.split(" ");
            int modulus = Integer.parseInt(parts[3]);
            int rem = (Integer.parseInt(parts[11]) + idx) % modulus;
            idx += 1;
            ns.add(modulus);
            as.add(rem);
        }
        System.out.println(xcrt(as, ns));
        ns.add(11);
        as.add(as.size() + 1);
        System.out.println(xcrt(as, ns));
    }
}
