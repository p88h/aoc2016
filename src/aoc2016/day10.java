package aoc2016;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class day10 {

    interface Receiver {
        void receive(int value);
    }

    private static class Output implements Receiver {
        private final Integer id;
        private int value;

        private Output(Integer id) {
            this.id = id;
        }

        @Override
        public void receive(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    private static class Bot implements Receiver {
        private final Integer id;
        Receiver rLow, rHigh;
        Integer a, b;
        Integer low, high;

        public Bot(Integer id) {
            this.id = id;
        }

        public void setup(Receiver l, Receiver h) {
            rLow = l;
            rHigh = h;
        }

        @Override
        public void receive(int value) {
            if (a == null) a = value;
            else b = value;
        }

        public List<Integer> prepare() {
            if (a != null && b != null) {
                low = Math.min(a, b);
                high = Math.max(a, b);
                a = b = null;
                return Arrays.asList(low, high, id);
            }
            return null;
        }

        public void send() {
            if (low != null && high != null) {
                rLow.receive(low);
                rHigh.receive(high);
                low = high = null;
            }
        }
    }

    private static class Factory {
        HashMap<Integer, Bot> bots = new HashMap<>();
        HashMap<Integer, Output> outputs = new HashMap<>();
        List<List<Integer>> log = new ArrayList<>();

        public Bot getBot(int id) {
            if (!bots.containsKey(id)) bots.put(id, new Bot(id));
            return bots.get(id);
        }

        public Output getOutput(int id) {
            if (!outputs.containsKey(id)) outputs.put(id, new Output(id));
            return outputs.get(id);
        }

        public Receiver get(String type, int id) {
            if (type.equals("output")) {
                return getOutput(id);
            } else {
                return getBot(id);
            }
        }

        int prepareAll() {
            int cnt = 0;
            for (Bot b : bots.values()) {
                List<Integer> entry = b.prepare();
                if (entry != null) {
                    log.add(entry);
                    cnt++;
                }
            }
            return cnt;
        }

        void sendAll() {
            for (Bot b : bots.values()) b.send();
        }

        int find(int l, int h) {
            for (List<Integer> entry : log) {
                if (entry.get(0) == l && entry.get(1) == h) {
                    return entry.get(2);
                }
            }
            return -1;
        }

    }

    public static void main(String[] args) throws IOException {
        Pattern rules = Pattern.compile("bot (\\d+) gives low to (\\w+) (\\d+) and high to (\\w+) (\\d+)");
        Pattern input = Pattern.compile("value (\\d+) goes to bot (\\d+)");
        Factory factory = new Factory();
        for (String line : Files.readAllLines(Paths.get("data/day10.in"))) {
            Matcher m = rules.matcher(line);
            if (m.matches()) {
                Bot b = factory.getBot(Integer.parseInt(m.group(1)));
                Receiver l = factory.get(m.group(2), Integer.parseInt(m.group(3)));
                Receiver h = factory.get(m.group(4), Integer.parseInt(m.group(5)));
                b.setup(l, h);
            }
            m = input.matcher(line);
            if (m.matches()) {
                Bot b = factory.getBot(Integer.parseInt(m.group(2)));
                b.receive(Integer.parseInt(m.group(1)));
            }
        }
        while (factory.prepareAll() > 0) {
            factory.sendAll();
        }
        System.out.println(factory.find(17, 61));
        int p = 1;
        for (int id : Arrays.asList(0, 1, 2)) p = p * factory.getOutput(id).getValue();
        System.out.println(p);
    }

}
