package lab.brain.api;

import java.util.Optional;
import java.util.Set;

public record Outcome(
        boolean success,
        float deltaEnergy,
        float deltaIntegrity,
        float deltaHydration,
        Set<TraitId> revealedTraits,
        Optional<PerceptionFrame> newFrame
) {}
