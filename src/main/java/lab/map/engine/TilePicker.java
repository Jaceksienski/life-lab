package lab.map.engine;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;
import lab.map.map.TileMap;
import lombok.RequiredArgsConstructor;

import java.util.function.BiConsumer;

@RequiredArgsConstructor
public class TilePicker extends InputAdapter {
    private final Viewport worldViewport;
    private final TileMap map;

    private final BiConsumer<Integer, Integer> onSelectionChanged;
    private final BiConsumer<Integer, Integer> onHoverChanged;

    private final Vector2 tmp = new Vector2();

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (button == Input.Buttons.RIGHT) {
            onSelectionChanged.accept(-1, -1);
            return false; // nie blokuj kamery
        }

        if (button != Input.Buttons.LEFT) return false;

        int[] tile = screenToTile(screenX, screenY);
        if (tile == null) return false;

        onSelectionChanged.accept(tile[0], tile[1]);
        return true;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        int[] tile = screenToTile(screenX, screenY);
        if (tile == null) {
            onHoverChanged.accept(-1, -1);
            return false;
        }
        onHoverChanged.accept(tile[0], tile[1]);
        return false;
    }

    private int[] screenToTile(int screenX, int screenY) {
        if (!isInWorldViewport(screenX, screenY)) return null;

        tmp.set(screenX, screenY);
        worldViewport.unproject(tmp);

        int tx = (int) Math.floor(tmp.x / map.tileSize);
        int ty = (int) Math.floor(tmp.y / map.tileSize);

        if (tx < 0 || ty < 0 || tx >= map.width || ty >= map.height) return null;
        return new int[]{tx, ty};
    }

    private boolean isInWorldViewport(int screenX, int screenY) {
        int sx = worldViewport.getScreenX();
        int sy = worldViewport.getScreenY();
        int sw = worldViewport.getScreenWidth();
        int sh = worldViewport.getScreenHeight();

        int yBottom = Gdx.graphics.getHeight() - screenY;
        return screenX >= sx && screenX < sx + sw && yBottom >= sy && yBottom < sy + sh;
    }
}