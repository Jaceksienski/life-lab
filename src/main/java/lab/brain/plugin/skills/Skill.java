package lab.brain.plugin.skills;

import java.util.List;
import java.util.Map;

public record Skill(
        String id,
        List<ActionStep> steps
) {
    public record ActionStep(String type, Map<String, Object> params) {}
}
