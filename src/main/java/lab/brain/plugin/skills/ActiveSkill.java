package lab.brain.plugin.skills;

import lab.brain.api.ActionIntent;

public final class ActiveSkill {
    public final Skill skill;
    public int index;
    public int stuckTicks;

    public ActiveSkill(Skill skill) {
        this.skill = skill;
        this.index = 0;
        this.stuckTicks = 0;
    }

    public boolean done() { return index >= skill.steps().size(); }

    public ActionIntent currentIntent(String decidedBy) {
        var step = skill.steps().get(index);
        return new ActionIntent(step.type(), step.params(), 0.95f, decidedBy);
    }

    public void advance() { index++; stuckTicks = 0; }
    public void stuck() { stuckTicks++; }
}
