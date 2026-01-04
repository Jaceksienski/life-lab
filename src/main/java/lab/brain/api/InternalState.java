package lab.brain.api;

import java.util.Map;

public record InternalState(
        float energy,
        float integrity,
        float hydration,
        Map<String, Float> extras
) {}
