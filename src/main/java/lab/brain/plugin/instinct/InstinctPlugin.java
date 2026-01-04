package lab.brain.plugin.instinct;

import lab.brain.api.*;
import lab.brain.core.*;

import java.util.*;

public final class InstinctPlugin implements BrainPlugin {
    @Override public String id() { return "instinct"; }
    @Override public int order() { return 55; }

    @Override
    public void onEvent(BrainEvent event, BrainHub hub) {
        if (event.type() != BrainEventType.DECIDE) return;

        InternalState st = hub.internal().orElse(null);
        if (st == null) return;

        List<ActionCandidate> cands = hub.candidates();
        if (cands.isEmpty()) return;

        if (st.integrity() < 0.2f) {
            var esc = findCandidate(cands, "ESCAPE");
            if (esc != null) {
                hub.propose(new ActionIntent(esc.type(), esc.params(), 0.8f, id()), 0.8);
                return;
            }
        }

        if (st.energy() < 0.3f) {
            var insp = findCandidate(cands, "INSPECT");
            if (insp != null) {
                hub.propose(new ActionIntent(insp.type(), insp.params(), 0.6f, id()), 0.6);
                return;
            }
        }

        ActionCandidate bestMove = null;
        for (var c : cands) {
            if ("MOVE".equalsIgnoreCase(c.type())) {
                if (bestMove == null || c.estimatedCost() < bestMove.estimatedCost()) bestMove = c;
            }
        }
        if (bestMove != null) {
            hub.propose(new ActionIntent(bestMove.type(), bestMove.params(), 0.35f, id()), 0.35);
            return;
        }

        ActionCandidate best = cands.stream()
                .min(Comparator.comparingDouble(ActionCandidate::estimatedCost))
                .orElse(cands.get(0));
        hub.propose(new ActionIntent(best.type(), best.params(), 0.25f, id()), 0.25);
    }

    private ActionCandidate findCandidate(List<ActionCandidate> cands, String type) {
        for (var c : cands) {
            if (type.equalsIgnoreCase(c.type())) return c;
        }
        return null;
    }
}
