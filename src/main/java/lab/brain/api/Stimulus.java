package lab.brain.api;

import java.util.OptionalInt;
import java.util.Set;

public record Stimulus(
        SenseChannel channel,
        int dx, int dy,
        Set<TraitId> traits,
        float strength,
        OptionalInt trackHint
) {}
