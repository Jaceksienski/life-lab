package lab;

import lab.sim.Config;

import java.util.ArrayDeque;
import java.util.Deque;

public class LogBuffer {
    private final Deque<String> lines = new ArrayDeque<>();

    public void log(String s) {
        lines.addLast(s);
        while (lines.size() > Config.LOG_MAX_LINES) lines.removeFirst();
    }

    public String joinAll() {
        StringBuilder sb = new StringBuilder();
        for (String l : lines) sb.append(l).append('\n');
        return sb.toString();
    }
}
