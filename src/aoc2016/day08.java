package aoc2016;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class day08 {

    interface Display {
        boolean get(int row, int col);
    }

    static class EmptyDisplay implements Display {
        public boolean get(int row, int col) {
            return false;
        }
    }

    static class RectUpdate implements Display {
        private final Display lower;
        private final int width, height;

        RectUpdate(Display sub, int a, int b) {
            lower = sub;
            width = a;
            height = b;
        }

        public boolean get(int row, int col) {
            if (row < height && col < width) return true;
            return lower.get(row, col);
        }
    }

    static class RotateRowUpdate implements Display {
        private final Display lower;
        private final int row, offset;

        RotateRowUpdate(Display sub, int a, int b) {
            lower = sub;
            row = a;
            offset = b;
        }

        public boolean get(int row, int col) {
            if (this.row == row) return lower.get(row, (col + 50 - offset) % 50);
            return lower.get(row, col);
        }
    }

    static class RotateColUpdate implements Display {
        private final Display lower;
        private final int col, offset;

        RotateColUpdate(Display sub, int a, int b) {
            lower = sub;
            col = a;
            offset = b;
        }

        public boolean get(int row, int col) {
            if (this.col == col) return lower.get((row + 6 - offset) % 6, col);
            return lower.get(row, col);
        }
    }

    public static void main(String[] args) throws IOException {
        Pattern rect = Pattern.compile("rect (\\d+)x(\\d+)");
        Pattern srow = Pattern.compile("rotate row y=(\\d+) by (\\d+)");
        Pattern scol = Pattern.compile("rotate column x=(\\d+) by (\\d+)");
        Display disp = new EmptyDisplay();
        for (String line : Files.readAllLines(Paths.get("data/day08.in"))) {
            Matcher m = rect.matcher(line);
            if (m.matches()) disp = new RectUpdate(disp, Integer.parseInt(m.group(1)), Integer.parseInt(m.group(2)));
            m = srow.matcher(line);
            if (m.matches())
                disp = new RotateRowUpdate(disp, Integer.parseInt(m.group(1)), Integer.parseInt(m.group(2)));
            m = scol.matcher(line);
            if (m.matches())
                disp = new RotateColUpdate(disp, Integer.parseInt(m.group(1)), Integer.parseInt(m.group(2)));
        }
        int count = 0;
        for (int y = 0; y < 6; y++) {
            StringBuilder sb = new StringBuilder();
            for (int x = 0; x < 50; x++) {
                if (disp.get(y, x)) {
                    count++;
                    sb.append('#');
                } else sb.append(' ');
            }
            System.out.println(sb);
        }
        System.out.println(count);
    }
}
