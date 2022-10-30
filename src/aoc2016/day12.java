package aoc2016;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class day12 {

    interface Operand {
        int get();
    }

    static class Register implements Operand {
        int val;

        public void set(int value) {
            val = value;
        }

        public int get() {
            return val;
        }
    }

    static class Immediate implements Operand {
        final int val;

        Immediate(int val) {
            this.val = val;
        }

        public int get() {
            return val;
        }
    }

    interface Op {
        void execute();
    }

    static class CopyOp implements Op {
        private final Operand src;
        private final Register tgt;

        CopyOp(Operand src, Register tgt) {
            this.src = src;
            this.tgt = tgt;
        }

        public void execute() {
            tgt.set(src.get());
        }
    }

    static class AddOp implements Op {
        private final Operand src;
        private final Register tgt;

        AddOp(Operand src, Register tgt) {
            this.src = src;
            this.tgt = tgt;
        }

        public void execute() {
            tgt.set(tgt.get() + src.get());
        }
    }

    static class JnzOp implements Op {
        private final Operand src;
        private final Register pc;
        private final int pos;

        JnzOp(Operand src, Register pc, int pos) {
            this.src = src;
            this.pc = pc;
            this.pos = pos;
        }

        public void execute() {
            if (src.get() != 0) pc.set(pos);
        }
    }

    static class Machine {
        Map<String, Register> registers = new HashMap<>();
        List<Op> program = new ArrayList<>();
        Register pc = new Register();

        public Machine() {
            reset();
        }

        public void step() {
            program.get(pc.get()).execute();
            pc.val += 1;
        }

        public void run() {
            while (pc.get() < program.size()) step();
        }

        public void reset() {
            for (String rid : Arrays.asList("a", "b", "c", "d")) registers.put(rid, new Register());
            pc = new Register();
        }

        void assemble(String line) {
            String[] parts = line.split(" ");
            if (parts[0].equals("cpy")) {
                if (registers.containsKey(parts[1])) {
                    program.add(new CopyOp(registers.get(parts[1]), registers.get(parts[2])));
                } else {
                    program.add(new CopyOp(new Immediate(Integer.parseInt(parts[1])), registers.get(parts[2])));
                }
            }
            if (parts[0].equals("inc")) program.add(new AddOp(new Immediate(1), registers.get(parts[1])));
            if (parts[0].equals("dec")) program.add(new AddOp(new Immediate(-1), registers.get(parts[1])));
            if (parts[0].equals("jnz")) {
                int dest = program.size() + Integer.parseInt(parts[2]) - 1;
                if (registers.containsKey(parts[1])) {
                    program.add(new JnzOp(registers.get(parts[1]), pc, dest));
                } else {
                    program.add(new JnzOp(new Immediate(Integer.parseInt(parts[1])), pc, dest));
                }
            }
        }
    }

    public static void main(String[] args) throws IOException {
        Machine m = new Machine();
        Machine m2 = new Machine();
        for (String line : Files.readAllLines(Paths.get("data/day12.in"))) {
            m.assemble(line);
            m2.assemble(line);
        }
        m.run();
        System.out.println(m.registers.get("a").get());
        m2.registers.get("c").set(1);
        m2.run();
        System.out.println(m2.registers.get("a").get());
    }
}
