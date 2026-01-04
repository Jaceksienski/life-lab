package lab.brain.core;

import lab.brain.api.*;

import java.util.*;

public final class BrainCore {

    private final BrainHub hub = new BrainHub();
    private final List<BrainPlugin> plugins;

    public BrainCore(List<BrainPlugin> plugins) {
        this.plugins = plugins.stream()
                .sorted(Comparator.comparingInt(BrainPlugin::order))
                .toList();
    }

    public ActionIntent tick(PerceptionFrame frame, InternalState internal, List<ActionCandidate> candidates) {
        hub.setPerception(frame);
        hub.setInternal(internal);
        hub.setCandidates(candidates);

        fire(new BrainEvent(BrainEventType.TICK_START, frame.tick()));
        fire(new BrainEvent(BrainEventType.PERCEPTION, frame.tick()));
        fire(new BrainEvent(BrainEventType.CANDIDATES, frame.tick()));

        hub.clearProposals();
        fire(new BrainEvent(BrainEventType.DECIDE, frame.tick()));

        ActionIntent intent = selectBestIntent(candidates);

        fire(new BrainEvent(BrainEventType.TICK_END, frame.tick()));
        return intent;
    }

    public void onActionOutcome(long tick, Outcome outcome) {
        hub.setOutcome(outcome);
        outcome.newFrame().ifPresent(hub::setPerception);
        fire(new BrainEvent(BrainEventType.ACTION_OUTCOME, tick));
    }

    public void sleep(long tick) {
        fire(new BrainEvent(BrainEventType.SLEEP_START, tick));
        fire(new BrainEvent(BrainEventType.SLEEP_END, tick));
    }

    public BrainHub hub() { return hub; }

    private void fire(BrainEvent ev) {
        for (var p : plugins) p.onEvent(ev, hub);
    }

    private ActionIntent selectBestIntent(List<ActionCandidate> candidates) {
        var props = hub.proposals();
        if (!props.isEmpty()) {
            return props.stream()
                    .max(Comparator.comparingDouble(Proposal::score))
                    .map(Proposal::intent)
                    .orElseGet(() -> fallback(candidates));
        }
        return fallback(candidates);
    }

    private ActionIntent fallback(List<ActionCandidate> candidates) {
        if (candidates.isEmpty()) return new ActionIntent("IDLE", Map.of(), 0.1f, "fallback");
        ActionCandidate best = candidates.stream()
                .min(Comparator.comparingDouble(ActionCandidate::estimatedCost))
                .orElse(candidates.get(0));
        return new ActionIntent(best.type(), best.params(), 0.3f, "fallback");
    }
}
