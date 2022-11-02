package aoc2016;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class day20 {
    static class LongPair implements Comparable<LongPair> {
        private final long first;
        private final long second;

        public LongPair(long first, long second) {
            this.first = first;
            this.second = second;
        }

        @Override
        public int hashCode() {
            return Objects.hash(first, second);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || obj.getClass() != getClass()) return false;
            LongPair other = (LongPair) obj;
            return first == other.first && second == other.second;
        }

        public static LongPair of(long first, long second) {
            return new LongPair(first, second);
        }

        @Override
        public int compareTo(LongPair o) {
            if (first < o.first) return -1;
            if (first > o.first) return 1;
            return Long.compare(second, o.second);
        }
    }

    public static void main(String[] args) throws IOException {
        List<LongPair> data = new ArrayList<>();
        for (String line : Files.readAllLines(Paths.get("data/day20.in"))) {
            String[] parts = line.split("-");
            data.add(LongPair.of(Long.parseLong(parts[0]), Long.parseLong(parts[1])));
        }
        Collections.sort(data);
        long allowed = 0, total = 0;
        for (LongPair p : data) {
            if (p.first > allowed) {
                if (total == 0) System.out.println(allowed);
                total += p.first - allowed;
            }
            if (p.second > allowed) allowed = p.second + 1;
        }
        if (allowed < 4294967295L) total += 4294967295L - allowed;
        System.out.println(total);
    }
}
