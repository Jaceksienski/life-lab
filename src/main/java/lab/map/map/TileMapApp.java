package lab.sim;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import lab.map.camera.CameraController;
import lab.map.map.TileMap;
import lab.map.map.TileMapRenderer;

public class TileMapApp extends ApplicationAdapter {

    private ShapeRenderer shape;

    private OrthographicCamera cam;
    private CameraController camController;

    private TileMap map;
    private TileMapRenderer mapRenderer;

    @Override
    public void create() {
        shape = new ShapeRenderer();

        map = new TileMap(Config.MAP_W, Config.MAP_H, Config.TILE_SIZE);
        mapRenderer = new TileMapRenderer(map);

        cam = new OrthographicCamera();
        cam.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        // ustaw kamerę na środek mapy
        cam.position.set(
                (map.width * map.tileSize) / 2f,
                (map.height * map.tileSize) / 2f,
                0f
        );
        cam.update();

        camController = new CameraController(cam);
    }

    @Override
    public void render() {
        float dt = Gdx.graphics.getDeltaTime();

        camController.update(dt);
        cam.update();

        Gdx.gl.glClearColor(Config.CLEAR_R, Config.CLEAR_G, Config.CLEAR_B, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        shape.setProjectionMatrix(cam.combined);
        mapRenderer.render(shape, cam);
    }

    @Override
    public void resize(int width, int height) {
        // ważne: aktualizuj viewport kamery, żeby zoom i culling działały poprawnie po zmianie okna
        cam.setToOrtho(false, width, height);
    }

    @Override
    public void dispose() {
        shape.dispose();
    }
}
