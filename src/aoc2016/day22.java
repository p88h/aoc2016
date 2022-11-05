package aoc2016;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class day22 {

    static class State {
        final int fx, fy; // free space position
        final int dx, dy; // data space position

        public State(int fx, int fy, int dx, int dy) {
            this.fx = fx;
            this.fy = fy;
            this.dx = dx;
            this.dy = dy;
        }

        // move free space box to destination (fx+ox, fx+oy)
        State move(int ox, int oy) {
            if (fx + ox == dx && fy + oy == dy) {
                return new State(fx + ox, fy + oy, fx, fy);
            } else {
                return new State(fx + ox, fy + oy, dx, dy);
            }
        }

        @Override
        public int hashCode() {
            return (fy << 24) + (fx << 16) + (dx << 8) + dy;
        }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof State)) return false;
            State other = (State) obj;
            return other.dx == dx && other.dy == dy && other.fx == fx && other.fy == fy;
        }

        public boolean done() {
            return (dx == 0 && dy == 0);
        }

        @Override
        public String toString() {
            return String.format("dx=%d dy=%d fx=%d fy=%d",dx,dy,fx,fy);
        }
    }

    static class Board {
        Map<Integer, Integer> sizeMap = new HashMap<>();
        Map<Integer, Integer> capaMap = new HashMap<>();

        int key(int x, int y) {
            return y * 1024 + x;
        }

        void add(int x, int y, int s, int c) {
            int k = key(x, y);
            sizeMap.put(k, s);
            capaMap.put(k, c);
        }

        // Does position denoted at _src_ have enough space to accommodate data at _dst_
        boolean movable(State src, State dst) {
            int sk = key(src.fx, src.fy), dk = key(dst.fx, dst.fy);
            if (!capaMap.containsKey(sk)) return false;
            if (!sizeMap.containsKey(dk)) return false;
            return capaMap.get(sk) > sizeMap.get(dk);
        }
    }


    public static void main(String[] args) throws IOException {
        Pattern rules = Pattern.compile("/dev/grid/node-x(\\d+)-y(\\d+) *(\\d+)T *(\\d+)T *(\\d+)T *(\\d+)%");
        List<Integer> sizes = new ArrayList<>();
        List<Integer> capas = new ArrayList<>();
        int total = 0;
        int sx = 0, sy = 0, mx = 0;
        Board board = new Board();
        for (String line : Files.readAllLines(Paths.get("data/day22.in"))) {
            Matcher m = rules.matcher(line);
            if (m.matches()) {
                int x = Integer.parseInt(m.group(1));
                int y = Integer.parseInt(m.group(2));
                int used = Integer.parseInt(m.group(4));
                sizes.add(used);
                int avail = Integer.parseInt(m.group(5));
                capas.add(avail);
                if (used > 0 && used <= avail) total--;
                board.add(x, y, used, used + avail);
                if (used == 0) {
                    sx = x;
                    sy = y;
                }
                if (y == 0 && x > mx) mx = x;
            }
        }
        Collections.sort(sizes);
        Collections.sort(capas);
        int idx = 0;
        for (int size : sizes) {
            while (idx < capas.size() && capas.get(idx) < size) idx++;
            if (size > 0) total += capas.size() - idx;
        }
        System.out.println(total);
        State start = new State(sx, sy, mx, 0);
        List<State> stack = Collections.singletonList(start);
        Set<State> visited = new HashSet<>();
        visited.add(start);
        int dst = 0;
        while (!stack.isEmpty()) {
            List<State> next = new ArrayList<>();
            dst += 1;
            for (State s : stack) {
                for (State c : Arrays.asList(s.move(1, 0), s.move(-1, 0), s.move(0, 1), s.move(0, -1))) {
                    if (board.movable(s, c) && !visited.contains(c)) {
                        next.add(c);
                        visited.add(c);
                    }
                    if (c.done()) {
                        System.out.println(dst);
                        return;
                    }
                }
            }
            stack = next;
        }
    }
}
