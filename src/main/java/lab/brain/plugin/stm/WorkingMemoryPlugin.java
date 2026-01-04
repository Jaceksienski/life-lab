package lab.brain.plugin.stm;

import lab.brain.core.*;
import lab.brain.keys.BrainKeys;

public final class WorkingMemoryPlugin implements BrainPlugin {
    @Override public String id() { return "stm"; }
    @Override public int order() { return 10; }

    @Override
    public void onEvent(BrainEvent event, BrainHub hub) {
        RingBuffer<Object> stm = hub.getOrCreate(BrainKeys.STM, () -> new RingBuffer<>(5000));

        switch (event.type()) {
            case PERCEPTION -> hub.perception().ifPresent(stm::add);
            case ACTION_OUTCOME -> hub.outcome().ifPresent(stm::add);
            default -> {}
        }
    }
}
