package aoc2016;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class day01 {
    public static void main(String[] args) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get("data/day01.in"));
        Map<Integer, Integer> visited = new HashMap<>();
        String[] moves = lines.get(0).split(", ");
        int dx = 0, dy = 1, cx = 0, cy = 0, fx = 0, fy = 0;
        boolean dup_found = false;
        int MAXX = 1000000;
        for (String m: moves) {
            int d = Integer.parseInt(m.substring(1));
            int nx = dx, ny = dy;
            switch (m.charAt(0)) {
                case 'L':
                    nx = -dy;
                    ny = dx;
                    break;
                case 'R':
                    nx = dy;
                    ny = -dx;
                    break;
            }
            dx = nx;
            dy = ny;
            for (int i = 0; i < d; ++i) {
                if (!dup_found && visited.containsKey(MAXX*cy+cx)) {
                    fx = cx;
                    fy = cy;
                    dup_found = true;
                }
                visited.put(MAXX*cy+cx, 1);
                cx += dx;
                cy += dy;
            }
        }
        System.out.println(Math.abs(cx)+Math.abs(cy));
        System.out.println(Math.abs(fx)+Math.abs(fy));
    }
}
