package lab.brain.plugin.ltm;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public final class LongTermMemory {
    public record Entry(Object value, float strength, long lastTick) {}

    private final Map<String, Entry> db = new ConcurrentHashMap<>();

    public void put(String key, Object value, float strength, long tick) {
        db.merge(key, new Entry(value, strength, tick),
                (a,b) -> new Entry(b.value, a.strength + b.strength, tick));
    }

    public Optional<Entry> get(String key) {
        return Optional.ofNullable(db.get(key));
    }

    public Map<String, Entry> snapshot() {
        return Map.copyOf(db);
    }

    public void decay(float amount) {
        for (var it = db.entrySet().iterator(); it.hasNext(); ) {
            var mapEntry = it.next(); // Map.Entry<String, Entry>
            Entry old = mapEntry.getValue();

            float s = old.strength() - amount;
            if (s <= 0f) {
                it.remove();
            } else {
                mapEntry.setValue(new Entry(old.value(), s, old.lastTick()));
            }
        }
    }

}
