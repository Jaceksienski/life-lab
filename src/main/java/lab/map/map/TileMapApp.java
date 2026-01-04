package lab.map.map;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.kotcrab.vis.ui.VisUI;
import lab.ai.Agent;
import lab.ai.AgentRenderer;
import lab.map.camera.CameraController;
import lab.map.engine.TilePicker;
import lab.sim.Config;
import lab.ui.LogBuffer;
import lab.ui.UiPanel;

import java.util.Locale;

public class TileMapApp extends ApplicationAdapter {

    private ShapeRenderer shape;

    private OrthographicCamera cam;
    private Viewport worldViewport;
    private CameraController camController;

    private TileMap map;
    private TileMapRenderer mapRenderer;
    private AgentRenderer agentRenderer;

    private UiPanel ui;
    private LogBuffer logs;

    private int selectedX = -1, selectedY = -1;
    private int hoverX = -1, hoverY = -1;

    private Agent agent;

    @Override
    public void create() {
        VisUI.load();

        shape = new ShapeRenderer();

        map = new TileMap(Config.MAP_W, Config.MAP_H, Config.TILE_SIZE);
        mapRenderer = new TileMapRenderer(map);
        agentRenderer = new AgentRenderer(map);

        cam = new OrthographicCamera();
        worldViewport = new ScreenViewport(cam);

        int startTx = map.width / 2;
        int startTy = map.height / 2;
        agent = new Agent(startTx, startTy, map);

        cam.position.set(agent.x, agent.y, 0f);
        cam.update();

        ui = new UiPanel();
        logs = new LogBuffer();
        logs.log("Start");
        logs.log("LPM: select + move agent");
        logs.log("PPM: clear selection");

        camController = new CameraController(cam, worldViewport);

        TilePicker picker = new TilePicker(
                worldViewport, map,
                (tx, ty) -> {
                    selectedX = tx;
                    selectedY = ty;

                    if (tx < 0) {
                        logs.log("Selection cleared");
                    } else {
                        logs.log("Selected tile=(" + tx + "," + ty + ")");
                        agent.requestMoveTo(tx, ty);
                    }
                },
                (tx, ty) -> {
                    hoverX = tx;
                    hoverY = ty;
                }
        );

        Gdx.input.setInputProcessor(new InputMultiplexer(ui.stage, picker, camController));
        resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    @Override
    public void render() {
        float dt = Gdx.graphics.getDeltaTime();

        camController.update(dt);
        agent.update(dt);
        cam.update();

        Gdx.gl.glClearColor(Config.CLEAR_R, Config.CLEAR_G, Config.CLEAR_B, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // World pass
        worldViewport.apply();
        shape.setProjectionMatrix(cam.combined);
        mapRenderer.render(shape, cam, selectedX, selectedY, hoverX, hoverY);
        agentRenderer.render(shape, agent);

        // UI pass
        String hoverStr = (hoverX >= 0) ? "(" + hoverX + "," + hoverY + ")" : "-";
        String selStr = (selectedX >= 0) ? "(" + selectedX + "," + selectedY + ")" : "-";

        String info =
                "FPS: " + Gdx.graphics.getFramesPerSecond() + "\n" +
                        "Zoom: " + String.format(Locale.US, "%.2f", cam.zoom) + "\n" +
                        "Hover: " + hoverStr + "\n" +
                        "Selected: " + selStr + "\n" +
                        "Agent tile: (" + agent.tileX + "," + agent.tileY + ")\n" +
                        "Agent moving: " + agent.isMoving();

        ui.updateInfo(info);
        ui.updateLogs(logs.joinAll());

        ui.stage.act(dt);
        ui.stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        ui.resize(width, height);

//        int worldW = Math.max(1, (int) (width - Config.UI_PANEL_WIDTH));
        int worldW = Math.max(1, width);
        int worldH = Math.max(1, height);

        worldViewport.setScreenBounds(0, 0, worldW, worldH);
        worldViewport.update(worldW, worldH, false);
    }

    @Override
    public void dispose() {
        shape.dispose();
        ui.dispose();
        VisUI.dispose();
    }
}