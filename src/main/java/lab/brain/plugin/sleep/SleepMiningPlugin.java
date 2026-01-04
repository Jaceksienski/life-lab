package lab.brain.plugin.sleep;

import lab.brain.core.*;
import lab.brain.keys.BrainKeys;
import lab.brain.plugin.ltm.LongTermMemory;
import lab.brain.plugin.skills.Skill;
import lab.brain.plugin.stm.RingBuffer;

import java.util.*;

public final class SleepMiningPlugin implements BrainPlugin {
    @Override public String id() { return "sleepMining"; }
    @Override public int order() { return 200; }

    @Override
    public void onEvent(BrainEvent event, BrainHub hub) {
        if (event.type() != BrainEventType.SLEEP_START) return;

        RingBuffer<Object> stm = hub.get(BrainKeys.STM).orElse(null);
        LongTermMemory ltm = hub.get(BrainKeys.LTM).orElse(null);
        long tick = hub.perception().map(p -> p.tick()).orElse(event.tick());
        if (stm == null || ltm == null) return;

        if (ltm.get("skill.explore").isEmpty()) {
            Skill explore = new Skill("explore", List.of(
                    new Skill.ActionStep("MOVE", Map.of("dir", "E")),
                    new Skill.ActionStep("MOVE", Map.of("dir", "N")),
                    new Skill.ActionStep("INSPECT", Map.of()),
                    new Skill.ActionStep("MOVE", Map.of("dir", "W")),
                    new Skill.ActionStep("INSPECT", Map.of())
            ));
            ltm.put("skill.explore", explore, 2.0f, tick);
        }

        if (ltm.get("skill.raiseEnergy").isEmpty()) {
            Skill raiseEnergy = new Skill("raiseEnergy", List.of(
                    new Skill.ActionStep("MOVE", Map.of("dir", "E")),
                    new Skill.ActionStep("INSPECT", Map.of()),
                    new Skill.ActionStep("CONSUME", Map.of())
            ));
            ltm.put("skill.raiseEnergy", raiseEnergy, 1.5f, tick);
        }

        ltm.put("sleep.lastSTMSize", stm.size(), 0.5f, tick);
    }
}
