package lab.brain.api;

import java.util.List;

public record PerceptionFrame(
        long tick,
        int selfX,
        int selfY,
        List<Stimulus> stimuli
) {}
