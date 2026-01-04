package lab.brain.plugin.skills;

import lab.brain.api.*;
import lab.brain.core.*;
import lab.brain.keys.BrainKeys;
import lab.brain.plugin.ltm.LongTermMemory;

import java.util.*;

public final class SkillsPlugin implements BrainPlugin {
    @Override public String id() { return "skills"; }
    @Override public int order() { return 80; }

    private final int maxStuckTicks = 5;

    @Override
    public void onEvent(BrainEvent event, BrainHub hub) {
        switch (event.type()) {
            case DECIDE -> proposeSkillStep(hub);
            case ACTION_OUTCOME -> onOutcome(hub);
            default -> {}
        }
    }

    private void proposeSkillStep(BrainHub hub) {
        ActiveSkill active = hub.get(BrainKeys.ACTIVE_SKILL).orElse(null);

        if (active != null) {
            if (active.done()) {
                hub.remove(BrainKeys.ACTIVE_SKILL);
                return;
            }

            ActionIntent want = active.currentIntent(id());
            ActionCandidate match = findMatchingCandidate(hub.candidates(), want);
            if (match != null) {
                ActionIntent actual = new ActionIntent(match.type(), match.params(), want.confidence(), id());
                hub.propose(actual, 10_000.0);
            } else {
                active.stuck();
                if (active.stuckTicks >= maxStuckTicks) {
                    hub.remove(BrainKeys.ACTIVE_SKILL);
                } else {
                    ActionCandidate insp = findType(hub.candidates(), "INSPECT");
                    if (insp != null) {
                        hub.propose(new ActionIntent(insp.type(), insp.params(), 0.4f, id()), 0.4);
                    }
                }
            }
            return;
        }

        LongTermMemory ltm = hub.get(BrainKeys.LTM).orElse(null);
        InternalState st = hub.internal().orElse(null);
        if (ltm == null || st == null) return;

        if (st.energy() < 0.3f) {
            var e = ltm.get("skill.raiseEnergy").orElse(null);
            if (e != null && e.value() instanceof Skill s) {
                hub.put(BrainKeys.ACTIVE_SKILL, new ActiveSkill(s));
            }
        }
    }

    private void onOutcome(BrainHub hub) {
        ActiveSkill active = hub.get(BrainKeys.ACTIVE_SKILL).orElse(null);
        Outcome o = hub.outcome().orElse(null);
        if (active == null || o == null) return;

        if (o.success()) {
            active.advance();
            if (active.done()) hub.remove(BrainKeys.ACTIVE_SKILL);
        } else {
            active.stuck();
            if (active.stuckTicks >= maxStuckTicks) hub.remove(BrainKeys.ACTIVE_SKILL);
        }
    }

    private ActionCandidate findType(List<ActionCandidate> cands, String type) {
        for (var c : cands) if (type.equalsIgnoreCase(c.type())) return c;
        return null;
    }

    private ActionCandidate findMatchingCandidate(List<ActionCandidate> cands, ActionIntent want) {
        for (var c : cands) {
            if (!want.type().equalsIgnoreCase(c.type())) continue;
            if (paramsCompatible(want.params(), c.params())) return c;
        }
        return null;
    }

    private boolean paramsCompatible(Map<String, Object> wantParams, Map<String, Object> candParams) {
        for (var e : wantParams.entrySet()) {
            Object v = candParams.get(e.getKey());
            if (!Objects.equals(v, e.getValue())) return false;
        }
        return true;
    }
}
