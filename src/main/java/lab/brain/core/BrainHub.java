package lab.brain.core;

import lab.brain.api.*;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public final class BrainHub {

    // Blackboard (ustawia core)
    private volatile PerceptionFrame perception;
    private volatile InternalState internalState;
    private volatile List<ActionCandidate> candidates = List.of();
    private volatile Outcome lastOutcome;

    // Proposals (zbierane w DECIDE)
    private final List<Proposal> proposals = new CopyOnWriteArrayList<>();

    // Zasoby pluginów (typowane kluczem)
    private final Map<Key<?>, Object> resources = new ConcurrentHashMap<>();

    // ---- Blackboard getters
    public Optional<PerceptionFrame> perception() { return Optional.ofNullable(perception); }
    public Optional<InternalState> internal() { return Optional.ofNullable(internalState); }
    public List<ActionCandidate> candidates() { return candidates; }
    public Optional<Outcome> outcome() { return Optional.ofNullable(lastOutcome); }

    // ---- Blackboard setters (tylko core)
    void setPerception(PerceptionFrame p) { this.perception = p; }
    void setInternal(InternalState s) { this.internalState = s; }
    void setCandidates(List<ActionCandidate> c) { this.candidates = List.copyOf(c); }
    void setOutcome(Outcome o) { this.lastOutcome = o; }

    // ---- Proposals
    public void clearProposals() { proposals.clear(); }
    public void propose(ActionIntent intent, double score) { proposals.add(new Proposal(intent, score)); }
    public List<Proposal> proposals() { return List.copyOf(proposals); }

    // ---- Resources (typed)
    public <T> void put(Key<T> key, T value) { resources.put(key, value); }

    public <T> Optional<T> get(Key<T> key) {
        Object v = resources.get(key);
        if (v == null) return Optional.empty();
        return Optional.of(key.type().cast(v));
    }

    public <T> T getOrCreate(Key<T> key, java.util.function.Supplier<T> factory) {
        return get(key).orElseGet(() -> {
            T v = factory.get();
            put(key, v);
            return v;
        });
    }

    public void remove(Key<?> key) { resources.remove(key); }

    public boolean has(Key<?> key) { return resources.containsKey(key); }
}
