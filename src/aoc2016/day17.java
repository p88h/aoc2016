package aoc2016;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

public class day17 {
    static class Path {
        final String steps;
        final int x, y;

        Path(String steps, int x, int y) {
            this.steps = steps;
            this.x = x;
            this.y = y;
        }

        Path update(char direction, int dx, int dy) {
            return new Path(steps + direction, x + dx, y + dy);
        }
    }

    static List<Path> traverse(Path path) throws NoSuchAlgorithmException {
        List<Path> ret = new ArrayList<>();
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(path.steps.getBytes());
        byte[] dig = md.digest();
        if ((dig[0] & 0xF0) > 0xA0 && path.y > 0) ret.add(path.update('U', 0, -1));
        if ((dig[0] & 0xF) > 0xA && path.y < 3) ret.add(path.update('D', 0, 1));
        if ((dig[1] & 0xF0) > 0xA0 && path.x > 0) ret.add(path.update('L', -1, 0));
        if ((dig[1] & 0xF) > 0xA && path.x < 3) ret.add(path.update('R', 1, 0));
        return ret;
    }

    public static void main(String[] args) throws IOException, NoSuchAlgorithmException {
        String input = Files.readAllLines(Paths.get("data/day17.in")).get(0);
        List<Path> stack = Collections.singletonList(new Path(input, 0, 0));
        boolean first = true;
        int len = 0;
        while (!stack.isEmpty()) {
            List<Path> next = new ArrayList<>();
            for (Path p : stack) {
                if (p.x == 3 && p.y == 3) {
                    if (first) System.out.println(p.steps.substring(input.length()));
                    len = p.steps.length() - input.length();
                    first = false;
                } else {
                    next.addAll(traverse(p));
                }
            }
            stack = next;
        }
        System.out.println(len);
    }
}
