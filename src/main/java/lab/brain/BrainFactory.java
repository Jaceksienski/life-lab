package lab.brain;

import lab.brain.core.BrainCore;

import lab.brain.plugin.encoder.EncoderPlugin;
import lab.brain.plugin.instinct.InstinctPlugin;
import lab.brain.plugin.ltm.LongTermMemoryPlugin;
import lab.brain.plugin.planner.PlannerPlugin;
import lab.brain.plugin.skills.SkillsPlugin;
import lab.brain.plugin.sleep.SleepMiningPlugin;
import lab.brain.plugin.stm.WorkingMemoryPlugin;
import lab.brain.plugin.value.ValueModelPlugin;

import java.util.List;

public final class BrainFactory {
    public static BrainCore createPrototype() {
        return new BrainCore(List.of(
                new WorkingMemoryPlugin(),
                new EncoderPlugin(),
                new LongTermMemoryPlugin(),
                new InstinctPlugin(),
                new ValueModelPlugin(),
                new SkillsPlugin(),
                new PlannerPlugin(),
                new SleepMiningPlugin()
        ));
    }
}
