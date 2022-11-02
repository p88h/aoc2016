package aoc2016;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class day21 {

    static class Password {
        private final String pass;
        ArrayList<Character> characters = new ArrayList<>();
        Map<Character, Integer> positions = new HashMap<>();

        public Password(String pass) {
            this.pass = pass;
            for (int i = 0; i < pass.length(); i++) {
                characters.add(pass.charAt(i));
                positions.put(pass.charAt(i), i);
            }
        }

        void swap(int a, int b, Character l, Character r) {
            characters.set(a, r);
            characters.set(b, l);
            positions.put(r, a);
            positions.put(l, b);
        }

        void swap(Character l, Character r) {
            swap(positions.get(l), positions.get(r), l, r);
        }

        void swap(int a, int b) {
            swap(a, b, characters.get(a), characters.get(b));
        }

        void reverse(int a, int b) {
            while (a < b) swap(a++, b--);
        }

        void rotate(int ofs) {
            if (ofs < 0) ofs += pass.length();
            ofs = ofs % pass.length();
            if (ofs == 0) return;
            for (Character c : pass.toCharArray()) {
                int npos = (positions.get(c) + ofs) % pass.length();
                positions.put(c, npos);
                characters.set(npos, c);
            }
        }

        void move(int a, int b) {
            while (a < b) {
                swap(a, a + 1);
                a++;
            }
            while (a > b) {
                swap(a - 1, a);
                a--;
            }
        }

        @Override
        public String toString() {
            return characters.stream().map(String::valueOf).collect(Collectors.joining());
        }

        Password run(List<String> input, boolean reversed) {
            for (String line : input) {
                String[] parts = line.split(" ");
                switch (parts[0]) {
                    case "swap":
                        if (parts[1].equals("letter")) {
                            swap(Character.valueOf(parts[2].charAt(0)), Character.valueOf(parts[5].charAt(0)));
                        } else {
                            swap(Integer.parseInt(parts[2]), Integer.parseInt(parts[5]));
                        }
                        break;
                    case "reverse":
                        reverse(Integer.parseInt(parts[2]), Integer.parseInt(parts[4]));
                        break;
                    case "rotate":
                        int ofs;
                        if (parts[1].equals("based")) {
                            ofs = positions.get(parts[6].charAt(0));
                            if (reversed) {
                                // 0:1 1:3 2:5 3:7 || 4:2 5:4 6:6 7:0
                                ofs = (ofs % 2 != 0) ? (ofs / 2 - ofs) : (3 - ofs / 2);
                                if (ofs == 3) ofs = -1;
                            } else {
                                ofs += (ofs >= 4) ? 2 : 1;
                            }
                        } else {
                            ofs = Integer.parseInt(parts[2]);
                            if (parts[1].equals("left")) ofs *= -1;
                            if (reversed) ofs *= -1;
                        }
                        rotate(ofs);
                        break;
                    case "move":
                        if (reversed) {
                            move(Integer.parseInt(parts[5]), Integer.parseInt(parts[2]));
                        } else {
                            move(Integer.parseInt(parts[2]), Integer.parseInt(parts[5]));
                        }
                        break;
                }
            }
            return this;
        }
    }

    public static void main(String[] args) throws IOException {
        List<String> script = Files.readAllLines(Paths.get("data/day21.in"));
        System.out.println(new Password("abcdefgh").run(script, false));
        Collections.reverse(script);
        System.out.println(new Password("fbgdceah").run(script, true));
    }
}
