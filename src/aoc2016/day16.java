package aoc2016;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class day16 {
    interface BitSequence {
        BitSequence inverse();

        void append(StringBuilder sb);

        int size();
    }

    static class StringSequence implements BitSequence {
        private final String value;
        private StringSequence reversed;

        StringSequence(String value, StringSequence original) {
            this.value = value;
            this.reversed = original;
        }

        @Override
        public BitSequence inverse() {
            if (reversed == null) {
                StringBuilder tmp = new StringBuilder();
                for (int i = value.length() - 1; i >= 0; i--) tmp.append(value.charAt(i) == '0' ? '1' : '0');
                reversed = new StringSequence(tmp.toString(), this);
            }
            return reversed;
        }

        @Override
        public void append(StringBuilder sb) {
            sb.append(value);
        }

        @Override
        public int size() {
            return value.length();
        }
    }

    static class LiteralSequence implements BitSequence {
        private final int value;
        private LiteralSequence reversed;

        LiteralSequence(int value, LiteralSequence original) {
            this.value = value;
            this.reversed = original;
        }

        @Override
        public BitSequence inverse() {
            if (reversed == null) reversed = new LiteralSequence(1 - value, this);
            return reversed;
        }

        @Override
        public void append(StringBuilder sb) {
            sb.append(value);
        }

        @Override
        public int size() {
            return 1;
        }
    }

    static class SuperSequence implements BitSequence {
        private final List<BitSequence> parts;
        private BitSequence reversed;
        private final int cached_size;

        SuperSequence(List<BitSequence> parts, SuperSequence original) {
            this.parts = parts;
            this.reversed = original;
            int cs = 0;
            for (BitSequence bs : parts) cs += bs.size();
            cached_size = cs;
        }

        @Override
        public BitSequence inverse() {
            if (reversed == null) {
                List<BitSequence> result = new ArrayList<>();
                for (int i = parts.size() - 1; i >= 0; i--) result.add(parts.get(i).inverse());
                reversed = new SuperSequence(result, this);
            }
            return reversed;
        }

        @Override
        public void append(StringBuilder sb) {
            for (BitSequence bs : parts) bs.append(sb);
        }

        @Override
        public int size() {
            return cached_size;
        }
    }

    static String checksum(String bits) {
        StringBuilder tmp = new StringBuilder();
        for (int i = 0; i < bits.length(); i += 2) tmp.append((bits.charAt(i) == bits.charAt(i + 1)) ? '1' : '0');
        return tmp.toString();
    }

    static String collapse(String bits) {
        while (bits.length() % 2 == 0) bits = checksum(bits);
        return bits;
    }

    static String expand(String from, int limit) {
        BitSequence root = new StringSequence(from, null);
        BitSequence zero = new LiteralSequence(0, null);
        while (root.size() < limit) {
            root = new SuperSequence(Arrays.asList(root, zero, root.inverse()), null);
        }
        StringBuilder sb = new StringBuilder();
        root.append(sb);
        return sb.substring(0, limit);
    }

    public static void main(String[] args) throws IOException {
        String input = Files.readAllLines(Paths.get("data/day16.in")).get(0);
        System.out.println(collapse(expand(input, 272)));
        System.out.println(collapse(expand(input, 35651584)));
    }
}
