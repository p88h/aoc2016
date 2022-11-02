package aoc2016;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class day19 {
    public static void main(String[] args) throws IOException {
        int num = Integer.parseInt(Files.readAllLines(Paths.get("data/day19.in")).get(0));
        int idx = 0;
        List<Integer> next = new ArrayList<>(num);
        for (int i = 0; i < num; i++) next.add((i + 1) % num);
        while (next.get(idx) != idx) {
            int nidx = next.get(idx);
            next.set(idx, next.get(nidx));
            idx = nidx;
        }
        System.out.println(idx + 1);
        // reset and play again with new rules
        for (int i = 0; i < num; i++) next.set(i, (i + 1) % num);
        int cnt = num;
        int tidx = (num / 2) - 1;
        while (cnt > 1) {
            int nidx = next.get(tidx);
            next.set(tidx, next.get(nidx));
            if (cnt % 2 != 0) tidx = next.get(tidx);
            idx = next.get(idx);
            cnt -= 1;
        }
        System.out.println(idx + 1);
    }
}
