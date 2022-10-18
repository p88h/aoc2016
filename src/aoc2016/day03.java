package aoc2016;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class day03 {

    static int count_triangles(List<Integer> nums) {
        int cnt = 0;
        for (int i = 0; i < nums.size() / 3; i++) {
            int m = Collections.max(nums.subList(i * 3, i * 3 + 3));
            int r = nums.subList(i * 3, i * 3 + 3).stream().mapToInt(Integer::intValue).sum();
            if (m < (r - m)) cnt++;
        }
        return cnt;
    }

    static List<Integer> deinterleave(List<Integer> nums, int b) {
        return IntStream.range(0, nums.size() / 3).map(n -> n * 3 + b).mapToObj(nums::get).collect(Collectors.toList());
    }

    static int count_triangles_interleaved(List<Integer> nums) {
        int cnt = 0;
        for (int b = 0; b < 3; b++) cnt += count_triangles(deinterleave(nums, b));
        return cnt;
    }

    public static void main(String[] args) throws IOException {
        List<Integer> nums = new ArrayList<>();
        for (String line : Files.readAllLines(Paths.get("data/day03.in")))
            for (String ss : line.split("\\s+")) if (!ss.isEmpty()) nums.add(Integer.valueOf(ss));
        System.out.println(count_triangles(nums));
        System.out.println(count_triangles_interleaved(nums));
    }
}
