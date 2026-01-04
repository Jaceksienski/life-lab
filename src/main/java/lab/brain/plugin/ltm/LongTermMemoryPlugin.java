package lab.brain.plugin.ltm;

import lab.brain.core.*;
import lab.brain.keys.BrainKeys;
import lab.brain.core.BrainPlugin;

public final class LongTermMemoryPlugin implements BrainPlugin {
    @Override public String id() { return "ltm"; }
    @Override public int order() { return 30; }

    @Override
    public void onEvent(BrainEvent event, BrainHub hub) {
        hub.getOrCreate(BrainKeys.LTM, LongTermMemory::new);

        if (event.type() == BrainEventType.SLEEP_END) {
            hub.get(BrainKeys.LTM).ifPresent(ltm -> ltm.decay(0.02f));
        }
    }
}
