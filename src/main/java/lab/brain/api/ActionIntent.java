package lab.brain.api;

import java.util.Map;

public record ActionIntent(
        String type,
        Map<String, Object> params,
        float confidence,
        String decidedBy
) {}
