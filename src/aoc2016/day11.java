package aoc2016;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class day11 {
    static class Floor {
        public final HashSet<Integer> cpus;
        public final HashSet<Integer> gens;
        public Floor() {
            cpus = new HashSet<>();
            gens = new HashSet<>();
        }
        public Floor(Floor other) {
            cpus = new HashSet<>(other.cpus);
            gens = new HashSet<>(other.gens);
        }
        public int size() {
            return cpus.size() + gens.size();
        }
        public long signature() {
            long value = 0;
            for (long cpu : cpus) value += 1L << cpu;
            value <<= 7;
            for (long gen : gens) value += 1L << gen;
            return value;
        }
        public String toString() {
            StringBuilder sb = new StringBuilder();
            for (long cpu : cpus) sb.append("C").append(cpu).append(" ");
            for (long gen : gens) sb.append("G").append(gen).append(" ");
            return sb.toString();
        }
        boolean valid() {
            if (gens.isEmpty()) return true;
            for (Integer kind : cpus) if (!gens.contains(kind)) return false;
            return true;
        }
    }

    static class State {
        public final List<Floor> floors = new ArrayList<>();
        public int level;

        public State() {
            level = 0;
        }

        public State(State other) {
            for (Floor f : other.floors) floors.add(new Floor(f));
            level = other.level;
        }

        public long signature() {
            long value = 0;
            for (Floor f : floors) {
                value <<= 14;
                value += f.signature();
            }
            return value * 4 + level;
        }

        public String toString() {
            int idx = 0;
            StringBuilder sb = new StringBuilder();
            for (Floor f: floors) {
                if (idx == level) {
                    sb.append("[").append(idx).append("]: ");
                } else {
                    sb.append(idx).append(": ");
                }
                sb.append(f.toString());
                idx += 1;
            }
            return sb.toString();
        }

        boolean valid() {
            for (Floor f : floors) if (!f.valid()) return false;
            return true;
        }

        List<State> moves(int direction) {
            List<State> valid = new ArrayList<>();
            if (level + direction < 0 || level + direction == floors.size()) return valid;
            // Can we move at least one CPU?
            Floor current = floors.get(level);
            for (Integer kind: current.cpus) {
                State c1 = new State(this);
                c1.level += direction;
                c1.floors.get(level).cpus.remove(kind);
                c1.floors.get(c1.level).cpus.add(kind);
                if (c1.valid()) {
                    valid.add(c1);
                    for (Integer other: current.cpus) {
                        // skip invalid and duplicate pairings.
                        if (other.compareTo(kind) <= 0) continue;
                        State c2 = new State(c1);
                        c2.floors.get(level).cpus.remove(other);
                        c2.floors.get(c2.level).cpus.add(other);
                        if (c2.valid()) valid.add(c2);
                    }
                }
                // the only way to move c+g is if both are same kind
                if (current.gens.contains(kind)) {
                    State cg = new State(c1);
                    cg.floors.get(level).gens.remove(kind);
                    cg.floors.get(cg.level).gens.add(kind);
                    if (cg.valid()) valid.add(cg);
                }
            }
            for (Integer kind: current.gens) {
                State g1 = new State(this);
                g1.level += direction;
                g1.floors.get(level).gens.remove(kind);
                g1.floors.get(g1.level).gens.add(kind);
                if (g1.valid()) valid.add(g1);
                for (Integer other: current.gens) {
                    // skip invalid and duplicate pairings.
                    if (other.compareTo(kind) <= 0) continue;
                    State g2 = new State(g1);
                    g2.floors.get(level).gens.remove(other);
                    g2.floors.get(g2.level).gens.add(other);
                    if (g2.valid()) valid.add(g2);
                }
            }
            return valid;
        }

        List<State> allMoves() {
            return Stream.concat(moves(1).stream(), moves(-1).stream()).collect(Collectors.toList());
        }
    }

    static int explore(State init, int limit) {
        List<State> stack = Collections.singletonList(init);
        HashSet<Long> visited = new HashSet<>();
        visited.add(init.signature());
        int dist = 0;
        while (!stack.isEmpty()) {
            dist += 1;
            ArrayList<State> next = new ArrayList<>();
            for (State s : stack) {
                for (State n : s.allMoves()) {
                    long sgn = n.signature();
                    if (!visited.contains(sgn)) {
                        next.add(n);
                        visited.add(sgn);
                    }
                    if (n.floors.get(3).size() == limit) return dist;
                }
            }
            System.out.printf("Distance: %d size: %d\n", dist, next.size());
            stack = next;
        }
        return -1;
    }

    public static void main(String[] args) throws IOException {
        int idx = 1;
        State init = new State();
        Map<String, Integer> typmap = new HashMap<>();
        for (String line : Files.readAllLines(Paths.get("data/day11.in"))) {
            line = line.replaceAll("[.,]", "");
            String prev = "";
            Floor floor = new Floor();
            for (String token : line.split(" ")) {
                if (token.equals("microchip")) {
                    String typ = prev.split("-")[0];
                    Integer id = typmap.getOrDefault(typ, typmap.size());
                    typmap.put(typ, id);
                    System.out.printf("Floor %d %s=>%d CPU\n", idx, typ, id);
                    floor.cpus.add(id);
                }
                if (token.equals("generator")) {
                    Integer id = typmap.getOrDefault(prev, typmap.size());
                    typmap.put(prev, id);
                    System.out.printf("Floor %d %s=>%d GEN\n", idx, prev, id);
                    floor.gens.add(id);
                }
                prev = token;
            }
            idx += 1;
            init.floors.add(floor);
        }
        System.out.println(explore(init, typmap.size()*2));
        for (String elem : Arrays.asList("elerium", "dilithium")) {
            int id = typmap.size();
            typmap.put(elem, id);
            init.floors.get(0).cpus.add(id);
            init.floors.get(0).gens.add(id);

        }
        System.out.println(explore(init, typmap.size()*2));
    }
}
