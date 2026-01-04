package lab.brain.plugin.value;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public final class ValueModel {

    public static final class Stats {
        public int n;
        public double q;
        public double var;
    }

    private final Map<Long, Stats> table = new ConcurrentHashMap<>();

    public Stats stats(String actionType, int pointer) {
        long key = pack(actionType, pointer);
        return table.computeIfAbsent(key, k -> new Stats());
    }

    private long pack(String actionType, int pointer) {
        long hi = (long) actionType.hashCode();
        long lo = (pointer & 0xffffffffL);
        return (hi << 32) ^ lo;
    }
}
