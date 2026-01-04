package lab.brain.plugin.planner;

import lab.brain.api.*;
import lab.brain.core.*;
import lab.brain.keys.BrainKeys;
import lab.brain.plugin.ltm.LongTermMemory;
import lab.brain.plugin.skills.ActiveSkill;
import lab.brain.plugin.skills.Skill;

public final class PlannerPlugin implements BrainPlugin {
    @Override public String id() { return "planner"; }
    @Override public int order() { return 120; }

    @Override
    public void onEvent(BrainEvent event, BrainHub hub) {
        if (event.type() == BrainEventType.ACTION_OUTCOME) updateNoProgress(hub);
        if (event.type() == BrainEventType.DECIDE) maybeActivateExplorationSkill(hub);
    }

    private void updateNoProgress(BrainHub hub) {
        Outcome o = hub.outcome().orElse(null);
        if (o == null) return;

        int n = hub.get(BrainKeys.NO_PROGRESS_TICKS).orElse(0);

        boolean progress = (o.deltaEnergy() > 0.01f) || (o.deltaIntegrity() > 0.01f);
        n = progress ? 0 : n + 1;

        hub.put(BrainKeys.NO_PROGRESS_TICKS, n);
    }

    private void maybeActivateExplorationSkill(BrainHub hub) {
        int n = hub.get(BrainKeys.NO_PROGRESS_TICKS).orElse(0);
        if (n < 30) return;

        if (hub.get(BrainKeys.ACTIVE_SKILL).isPresent()) return;

        LongTermMemory ltm = hub.get(BrainKeys.LTM).orElse(null);
        if (ltm == null) return;

        var entry = ltm.get("skill.explore").orElse(null);
        if (entry != null && entry.value() instanceof Skill explore) {
            hub.put(BrainKeys.ACTIVE_SKILL, new ActiveSkill(explore));
        }
    }
}
