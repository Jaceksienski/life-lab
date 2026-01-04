package lab.brain.core;

import java.util.Objects;

public final class Key<T> {
    private final String name;
    private final Class<T> type;

    private Key(String name, Class<T> type) {
        this.name = Objects.requireNonNull(name);
        this.type = Objects.requireNonNull(type);
    }

    public static <T> Key<T> of(String name, Class<T> type) {
        return new Key<>(name, type);
    }

    public String name() { return name; }
    public Class<T> type() { return type; }

    @Override public String toString() { return "Key(" + name + ":" + type.getSimpleName() + ")"; }
    @Override public int hashCode() { return name.hashCode(); }
    @Override public boolean equals(Object o) {
        return (o instanceof Key<?> k) && this.name.equals(k.name);
    }
}
