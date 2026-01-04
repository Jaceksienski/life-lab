package lab.ai.actions;

import lab.ai.Agent;
import lab.map.map.TileMap;
import lab.sim.Config;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MoveAction implements Action {
    private boolean active;
    private float startX, startY;
    private float targetX, targetY;
    private int targetTileX, targetTileY;
    private float moveT;

    private final TileMap map;
    private Agent agent;

    {
        this.active = false;
        this.moveT = 0f;
    }

    /**
     * Rozpoczyna akcję ruchu do określonej pozycji na mapie.
     * @param agent agent, który ma wykonać ruch
     * @param toTileX docelowa pozycja X w kafelkach
     * @param toTileY docelowa pozycja Y w kafelkach
     */
    public void start(Agent agent, int toTileX, int toTileY) {
        this.agent = agent;
        
        this.targetTileX = toTileX;
        this.targetTileY = toTileY;
        this.startX = agent.x;
        this.startY = agent.y;
        this.targetX = map.tileCenterX(toTileX);
        this.targetY = map.tileCenterY(toTileY);

        this.moveT = 0f;
        this.active = true;
    }

    @Override
    public boolean isActive() {
        return active;
    }

    @Override
    public void update(float dt) {
        if (!active || agent == null) return;

        moveT += dt / Config.MOVE_DURATION;
        if (moveT >= 1f) moveT = 1f;

        float t = smoothstep(moveT);
        agent.x = lerp(startX, targetX, t);
        agent.y = lerp(startY, targetY, t);

        if (moveT >= 1f) {
            agent.tileX = targetTileX;
            agent.tileY = targetTileY;
            agent.x = targetX;
            agent.y = targetY;
            active = false;
        }
    }

    private static float lerp(float a, float b, float t) {
        return a + (b - a) * t;
    }

    private static float smoothstep(float t) {
        t = clamp01(t);
        return t * t * (3f - 2f * t);
    }

    private static float clamp01(float v) {
        return v < 0 ? 0 : (v > 1 ? 1 : v);
    }
}

