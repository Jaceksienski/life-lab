package lab.ai;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import lab.map.map.TileMap;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AgentRenderer {
    private final TileMap map;

    public void render(ShapeRenderer shape, Agent agent) {
        if (agent == null) return;

        float r = map.tileSize * 0.35f;
        shape.begin(ShapeRenderer.ShapeType.Filled);
        shape.setColor(0.2f, 0.9f, 0.3f, 1f);
        shape.circle(agent.x, agent.y, r);
        shape.end();
    }
}