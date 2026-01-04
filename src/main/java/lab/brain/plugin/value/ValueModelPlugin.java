package lab.brain.plugin.value;

import lab.brain.api.*;
import lab.brain.core.*;
import lab.brain.keys.BrainKeys;
import lab.brain.plugin.encoder.EncodedContext;

import java.util.*;

public final class ValueModelPlugin implements BrainPlugin {

    @Override public String id() { return "value"; }
    @Override public int order() { return 60; }

    private final double alpha = 0.10;
    private final double ucbC = 0.7;
    private final double costW = 1.0;

    private ActionIntent lastIntent;
    private int[] lastPointers;

    @Override
    public void onEvent(BrainEvent event, BrainHub hub) {
        ValueModel model = hub.getOrCreate(BrainKeys.VALUE, ValueModel::new);

        switch (event.type()) {
            case DECIDE -> proposeFromModel(hub, model);
            case ACTION_OUTCOME -> updateFromOutcome(hub, model);
            default -> {}
        }
    }

    private void proposeFromModel(BrainHub hub, ValueModel model) {
        EncodedContext ctx = hub.get(BrainKeys.CTX).orElse(null);
        if (ctx == null) return;

        List<ActionCandidate> cands = hub.candidates();
        if (cands.isEmpty()) return;

        int globalN = 1;
        for (int p : ctx.pointers()) {
            var st = model.stats("GLOBAL", p);
            globalN += st.n;
        }
        final double logN = Math.log(globalN + 1.0);

        for (ActionCandidate a : cands) {
            double qSum = 0.0;
            double nSum = 0.0;

            for (int p : ctx.pointers()) {
                var st = model.stats(a.type(), p);
                qSum += st.q;
                nSum += st.n;
            }

            double avgN = nSum / Math.max(1, ctx.pointers().length);
            double ucb = ucbC * Math.sqrt(logN / (1.0 + avgN));

            double score = qSum - costW * a.estimatedCost() + ucb;

            ActionIntent intent = new ActionIntent(a.type(), a.params(), (float) clamp01(sigmoid(score)), id());
            hub.propose(intent, score);
        }

        Proposal best = hub.proposals().stream()
                .max(Comparator.comparingDouble(Proposal::score))
                .orElse(null);
        if (best != null) {
            lastIntent = best.intent();
            lastPointers = ctx.pointers();
        }
    }

    private void updateFromOutcome(BrainHub hub, ValueModel model) {
        if (lastIntent == null || lastPointers == null) return;

        Outcome o = hub.outcome().orElse(null);
        if (o == null) return;

        double r = 1.5 * o.deltaEnergy() + 2.0 * o.deltaIntegrity() + 0.5 * o.deltaHydration();
        if (!o.success()) r -= 0.05;

        double target = r;

        for (int p : lastPointers) {
            var st = model.stats(lastIntent.type(), p);
            st.n++;
            double td = target - st.q;
            st.q += alpha * td;
            st.var = st.var + alpha * ((td * td) - st.var);
        }
    }

    private static double sigmoid(double x) { return 1.0 / (1.0 + Math.exp(-x)); }
    private static double clamp01(double x) { return Math.max(0.0, Math.min(1.0, x)); }
}
