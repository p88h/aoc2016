package aoc2016;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

public class day14 {

    static List<Integer> collectKeys(String input, int stretch, int limit) throws NoSuchAlgorithmException {
        HashMap<Integer, List<Integer>> pm = new HashMap<>();
        List<Integer> results = new ArrayList<>();
        int num = 0, found = 0;
        while (results.size() < limit) {
            MessageDigest md = MessageDigest.getInstance("MD5");
            String tmp = input + (++num);
            md.update(tmp.getBytes());
            byte[] dig = md.digest();
            for (int i = 0; i < stretch; ++i) {
                // This is CPU abuse.
                MessageDigest mds = MessageDigest.getInstance("MD5");
                String hashed = String.format("%032x", new BigInteger(1, dig));
                mds.update(hashed.getBytes());
                dig = mds.digest();
            }
            HashSet<Integer> triples = new HashSet<>(), quintuples = new HashSet<>();
            boolean triple = false;
            for (int p = 0; p < dig.length; p++) {
                // that's a pair
                if (((dig[p] & 0xFF) >> 4) == (dig[p] & 0xF)) {
                    // maybe middle of a quintuple
                    if (p > 0 && p + 1 < dig.length) {
                        // 0F [FF] FF
                        if (dig[p + 1] == dig[p] && (dig[p - 1] & 0xF) == (dig[p] & 0xF)) quintuples.add((int) dig[p]);
                        // FF [FF] F0
                        if (dig[p - 1] == dig[p] && (dig[p + 1] & 0xF0) == (dig[p] & 0xF0))
                            quintuples.add((int) dig[p]);
                    }
                    if (!triple) {
                        // 0F FF
                        if (p > 0 && (dig[p - 1] & 0xF) == (dig[p] & 0xF)) triple = true;
                        // FF F0
                        if (p + 1 < dig.length && (dig[p + 1] & 0xF0) == (dig[p] & 0xF0)) triple = true;
                        if (triple) triples.add((int) dig[p]);
                    }
                }
            }
            for (int q : quintuples) {
                if (pm.containsKey(q)) {
                    List<Integer> prev = pm.get(q);
                    for (int pos : prev)
                        if (pos >= num - 1000) {
                            System.out.printf("%d %d\n", results.size(), pos);
                            results.add(pos);
                        }
                    pm.remove(q);
                }
            }
            for (int t : triples) {
                List<Integer> mod = pm.getOrDefault(t, new ArrayList<>());
                mod.add(num);
                pm.put(t, mod);
            }
        }
        Collections.sort(results);
        return results;
    }

    public static void main(String[] args) throws IOException, NoSuchAlgorithmException {
        String input = Files.readAllLines(Paths.get("data/day14.in")).get(0);
        List<Integer> pos1 = collectKeys(input, 0, 64);
        System.out.println(pos1.get(63));
        List<Integer> pos2 = collectKeys(input, 2016, 64);
        System.out.println(pos2.get(63));
    }
}
