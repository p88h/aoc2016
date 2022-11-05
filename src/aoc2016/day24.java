package aoc2016;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class day24 {
    static class State {
        final int x, y, z;

        State(int x, int y, int z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        @Override
        public int hashCode() {
            return (x << 16) + (y << 8) + z;
        }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof State)) return false;
            State other = (State) obj;
            return other.x == x && other.y == y && other.z == z;
        }
    }

    static class Maze {
        final List<String> map;
        int sx, sy;

        Maze(List<String> map) {
            this.map = map;
            for (int i = 0; i < map.size(); i++) {
                int p = map.get(i).indexOf('0');
                if (p >= 0) {
                    sx = p;
                    sy = i;
                }
            }
        }

        State move(State s, int dx, int dy) {
            char c = map.get(s.y+dy).charAt(s.x+dx);
            if (c == '#') return null;
            int mask = (c != '.') ? (1 << (c - '0')) : 0;
            return new State(s.x + dx, s.y + dy, s.z | mask);
        }
    }

    public static void main(String[] args) throws IOException {
        Maze m = new Maze(Files.readAllLines(Paths.get("data/day24.in")));
        int[][] dirs = new int[][]{{-1, 0}, {1, 0}, {0, 1}, {0, -1}};
        State start = new State(m.sx, m.sy, 1);
        List<State> stack = Collections.singletonList(start);
        Set<State> visited = new HashSet<>();
        visited.add(start);
        int dist = 0;
        boolean first = true;
        while (!stack.isEmpty()) {
            List<State> next = new ArrayList<>();
            dist += 1;
            for (State s : stack) {
                for (int[] d : dirs) {
                    State n = m.move(s, d[0], d[1]);
                    if (n != null && !visited.contains(n)) {
                        next.add(n);
                        visited.add(n);
                        if (n.z == 255 && first) {
                            System.out.println(dist);
                            first = false;
                        }
                        if (n.z == 255 && n.x == m.sx && n.y == m.sy) {
                            System.out.println(dist);
                            return;
                        }
                    }
                }
            }
            stack = next;
        }
    }

}