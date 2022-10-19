package aoc2016;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class day05 {
    public static void main(String[] args) throws IOException, NoSuchAlgorithmException {
        String hex = "0123456789abcdef";
        String input = Files.readAllLines(Paths.get("data/day05.in")).get(0);
        StringBuilder sb = new StringBuilder();
        HashMap<Integer, String> pm = new HashMap<>();
        int num = 0, found = 0;
        while (found < 8) {
            MessageDigest md = MessageDigest.getInstance("MD5");
            String tmp = input + (++num);
            md.update(tmp.getBytes());
            byte[] digest = md.digest();
            if (digest[0] == 0 && digest[1] == 0 && (digest[2] & 0xF0) == 0) {
                sb.append(hex.charAt(digest[2] & 0xF));
                if (sb.length() == 8) System.out.println(sb);
                int pos = digest[2] & 0xF;
                int idx = (digest[3] & 0xF0) >>> 4;
                if (pos < 8 && !pm.containsKey(pos)) {
                    found++;
                    pm.put(pos, String.valueOf(hex.charAt(idx)));
                }
            }
        }
        System.out.println(IntStream.range(0, 8).mapToObj(pm::get).collect(Collectors.joining()));
    }
}
