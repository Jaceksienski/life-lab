package lab.map.map;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import lab.sim.Config;


public class TileMapRenderer {
    private final TileMap map;

    public TileMapRenderer(TileMap map) {
        this.map = map;
    }

    public void render(ShapeRenderer shape, OrthographicCamera cam, int selectedX, int selectedY, int hoverX, int hoverY) {
        float halfW = (cam.viewportWidth * cam.zoom) / 2f;
        float halfH = (cam.viewportHeight * cam.zoom) / 2f;

        float marginW = halfW * Config.RENDER_MARGIN_PERCENT;
        float marginH = halfH * Config.RENDER_MARGIN_PERCENT;

        float minWorldX = cam.position.x - halfW - marginW;
        float maxWorldX = cam.position.x + halfW + marginW;
        float minWorldY = cam.position.y - halfH - marginH;
        float maxWorldY = cam.position.y + halfH + marginH;

        int minTileX = (int) Math.floor(minWorldX / map.tileSize);
        int maxTileX = (int) Math.floor(maxWorldX / map.tileSize);
        int minTileY = (int) Math.floor(minWorldY / map.tileSize);
        int maxTileY = (int) Math.floor(maxWorldY / map.tileSize);

        if (minTileX < 0) minTileX = 0;
        if (minTileY < 0) minTileY = 0;
        if (maxTileX >= map.width) maxTileX = map.width - 1;
        if (maxTileY >= map.height) maxTileY = map.height - 1;

        shape.begin(ShapeRenderer.ShapeType.Filled);
        for (int x = minTileX; x <= maxTileX; x++) {
            for (int y = minTileY; y <= maxTileY; y++) {
                boolean dark = ((x + y) % 2 == 0);
                if (dark) shape.setColor(0.16f, 0.16f, 0.18f, 1f);
                else      shape.setColor(0.18f, 0.18f, 0.20f, 1f);

                shape.rect(x * map.tileSize, y * map.tileSize, map.tileSize - 1f, map.tileSize - 1f);
            }
        }
        shape.end();

        shape.begin(ShapeRenderer.ShapeType.Line);

        if (hoverX >= 0 && hoverY >= 0 && !(hoverX == selectedX && hoverY == selectedY)) {
            shape.setColor(0f, 1f, 1f, 1f);
            shape.rect(hoverX * map.tileSize, hoverY * map.tileSize, map.tileSize, map.tileSize);
        }

        if (selectedX >= 0 && selectedY >= 0) {
            shape.setColor(1f, 1f, 0f, 1f);
            shape.rect(selectedX * map.tileSize, selectedY * map.tileSize, map.tileSize, map.tileSize);
        }

        shape.end();
    }
}

