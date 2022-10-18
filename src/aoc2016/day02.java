package aoc2016;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class day02 {
    private static class Button {
        public Character value;
        public Map<Character, Button> links;

        public Button(char v) {
            this.value = v;
            links = new HashMap<>();
        }
    }

    private static class Grid {
        private final List<Button> buttons = new ArrayList<>();
        private final String order;
        Button current;

        private Grid(String order) {
            this.order = order;
            for (char label : order.toCharArray()) buttons.add(new Button(label));
            current = buttons.get(order.indexOf('5'));
        }

        private void linkup(String labels, char dir, int mod) {
            for (char c : labels.toCharArray()) {
                int x = order.indexOf(c);
                buttons.get(x).links.put(dir, buttons.get(x + mod));
            }
        }

        public static Grid build3x3() {
            Grid grid = new Grid("123456789");
            grid.linkup("456789", 'U', -3);
            grid.linkup("123456", 'D', 3);
            grid.linkup("235689", 'L', -1);
            grid.linkup("124578", 'R', 1);
            return grid;
        }

        static Grid build3rot3() {
            Grid grid = new Grid("  1   234 56789 ABC   D  ");
            grid.linkup("3678ABCD", 'U', -5);
            grid.linkup("1234678B", 'D', 5);
            grid.linkup("346789BC", 'L', -1);
            grid.linkup("235678AB", 'R', 1);
            return grid;
        }

        void move(char c) {
            current = current.links.getOrDefault(c, current);
        }
    }

    public static void main(String[] args) throws IOException {
        Grid a = Grid.build3x3(), b = Grid.build3rot3();
        StringBuilder as = new StringBuilder(), bs = new StringBuilder();
        for (String line : Files.readAllLines(Paths.get("data/day02.in"))) {
            for (Character c : line.toCharArray()) {
                a.move(c);
                b.move(c);
            }
            as.append(a.current.value);
            bs.append(b.current.value);
        }
        System.out.println(as);
        System.out.println(bs);
    }
}
