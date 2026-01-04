package lab.brain.api;

import java.util.Map;

public record ActionCandidate(
        String type,
        Map<String, Object> params,
        float estimatedCost
) {}
