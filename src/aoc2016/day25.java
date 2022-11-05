package aoc2016;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class day25 {
    enum Instruction {
        INC, DEC, JNZ, CPY, TGL, OUT
    }

    static class Operation {
        Instruction ins;
        String a, b;

        Operation(Instruction ins, String a, String b) {
            this.ins = ins;
            this.a = a;
            this.b = b;
        }

        void toggle() {
            if (ins == Instruction.CPY) ins = Instruction.JNZ;
            else if (ins == Instruction.JNZ) ins = Instruction.CPY;
            else if (ins == Instruction.DEC) ins = Instruction.INC;
            else if (ins == Instruction.INC) ins = Instruction.DEC;
            else if (ins == Instruction.TGL) ins = Instruction.INC;
        }

        static Operation fromString(String line) {
            String[] parts = line.split(" ");
            Instruction ins = Instruction.valueOf(parts[0].toUpperCase());
            String arg1 = parts[1];
            String arg2 = parts.length > 2 ? parts[2] : null;
            return new Operation(ins, arg1, arg2);
        }

    }

    static class Machine {
        Map<String, Integer> registers = new HashMap<>();
        final Collection<String> assembly;
        List<Operation> program = new ArrayList<>();
        int pc = 0;
        int prev = 1;
        int seq = 0;

        public Machine(Collection<String> lines) {
            this.assembly = lines;
            for (String line : assembly) program.add(Operation.fromString(line));
            reset();
        }

        int getValue(String operand) {
            if (registers.containsKey(operand)) return registers.get(operand);
            return Integer.parseInt(operand);
        }

        void setValue(String operand, int val) {
            if (registers.containsKey(operand)) registers.put(operand, val);
        }

        public void step() {
            Operation op = program.get(pc);
            switch (op.ins) {
                case CPY:
                    setValue(op.b, getValue(op.a));
                    break;
                case DEC:
                    setValue(op.a, getValue(op.a) - 1);
                    break;
                case INC:
                    setValue(op.a, getValue(op.a) + 1);
                    break;
                case JNZ:
                    if (getValue(op.a) != 0) pc += getValue(op.b) - 1;
                    break;
                case TGL:
                    int ofs = pc + getValue(op.a);
                    if (ofs < program.size()) program.get(ofs).toggle();
                    break;
                case OUT:
                    int val = getValue(op.a);
                    if (val != 1 - prev) pc = program.size();
                    prev = val;
                    seq += 1;
                    if (seq >= 100) pc = program.size();
                    break;
            }
            pc++;
        }

        public void run() {
            while (pc < program.size()) step();
        }

        public void reset() {
            for (String rid : Arrays.asList("a", "b", "c", "d")) registers.put(rid, 0);
            pc = 0;
            prev = 1;
            seq = 0;
        }

    }

    public static void main(String[] args) throws IOException {
        Machine m = new Machine(Files.readAllLines(Paths.get("data/day25.in")));
        for (int a = 0; a < 1000; ++a) {
            m.reset();
            m.registers.put("a", a);
            m.run();
            if (m.seq >= 100) {
                System.out.println(a);
                return;
            }
        }
    }
}
