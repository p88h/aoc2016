package aoc2016;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class day13 {
    static class IntPair {
        private final int first;
        private final int second;

        public IntPair(int first, int second) {
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
            IntPair other = (IntPair) obj;
            return first == other.first && second == other.second;
        }

        IntPair add(IntPair other) {
            return new IntPair(first + other.first, second + other.second);
        }

        public static IntPair of(int first, int second) {
            return new IntPair(first, second);
        }
    }

    static class Maze {
        private final long favorite;

        Maze(int favorite) {
            this.favorite = favorite;
        }

        boolean open(IntPair pos) {
            long x = pos.first;
            long y = pos.second;
            long v = (x * x) + (3 * x) + (2 * x * y) + y + (y * y) + favorite;
            return (Long.bitCount(v) % 2) == 0;
        }

        IntPair explore(IntPair start, IntPair dest, int limit) {
            List<IntPair> dirs = Arrays.asList(IntPair.of(1, 0), IntPair.of(-1, 0), IntPair.of(0, 1), IntPair.of(0, -1));
            List<IntPair> stack = Collections.singletonList(start);
            HashSet<IntPair> visited = new HashSet<>(stack);
            int steps = 0;
            int counter = 1;
            while (!stack.isEmpty()) {
                List<IntPair> next = new ArrayList<>();
                for (IntPair pos : stack) {
                    if (pos.equals(dest)) return IntPair.of(steps, counter);
                    for (IntPair dir : dirs) {
                        IntPair neighbor = pos.add(dir);
                        if (neighbor.first < 0 || neighbor.second < 0) continue;
                        if (visited.contains(neighbor) || !open(neighbor)) continue;
                        visited.add(neighbor);
                        next.add(neighbor);
                        if (steps < limit) counter++;
                    }
                }
                steps += 1;
                stack = next;
            }
            return IntPair.of(-steps, counter);
        }
    }

    public static void main(String[] args) throws IOException {
        int num = Integer.parseInt(Files.readAllLines(Paths.get("data/day13.in")).get(0));
        Maze maze = new Maze(num);
        IntPair res = maze.explore(IntPair.of(1, 1), IntPair.of(31, 39), 50);
        System.out.println(res.first);
        System.out.println(res.second);
    }
}
