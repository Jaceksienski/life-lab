package lab.brain.keys;

import lab.brain.core.Key;
import lab.brain.plugin.encoder.EncodedContext;
import lab.brain.plugin.skills.ActiveSkill;
import lab.brain.plugin.stm.RingBuffer;
import lab.brain.plugin.value.ValueModel;
import lab.brain.plugin.ltm.LongTermMemory;

@SuppressWarnings({"rawtypes","unchecked"})
public final class BrainKeys {
    private BrainKeys() {}

    public static final Key<RingBuffer> STM = Key.of("stm", (Class) RingBuffer.class);
    public static final Key<EncodedContext> CTX = Key.of("ctx", EncodedContext.class);
    public static final Key<ValueModel> VALUE = Key.of("valueModel", ValueModel.class);
    public static final Key<LongTermMemory> LTM = Key.of("ltm", LongTermMemory.class);
    public static final Key<ActiveSkill> ACTIVE_SKILL = Key.of("activeSkill", ActiveSkill.class);
    public static final Key<Integer> NO_PROGRESS_TICKS = Key.of("noProgressTicks", Integer.class);
}
