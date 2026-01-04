package lab.map.camera;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.viewport.Viewport;
import lab.sim.Config;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CameraController extends InputAdapter {
    private final OrthographicCamera cam;
    private final Viewport worldViewport;
    private float scrollYAccum = 0f;

    @Override
    public boolean scrolled(float amountX, float amountY) {
        if (!isMouseOverWorld()) return false;
        scrollYAccum += amountY;
        return true;
    }

    public void update(float dt) {
        float speed = Config.CAMERA_MOVE_SPEED * dt * cam.zoom;

        if (Gdx.input.isKeyPressed(Input.Keys.W)) cam.position.y += speed;
        if (Gdx.input.isKeyPressed(Input.Keys.S)) cam.position.y -= speed;
        if (Gdx.input.isKeyPressed(Input.Keys.A)) cam.position.x -= speed;
        if (Gdx.input.isKeyPressed(Input.Keys.D)) cam.position.x += speed;

        if (isMouseOverWorld() && Gdx.input.isButtonPressed(Input.Buttons.RIGHT)) {
            float dx = -Gdx.input.getDeltaX() * dt * Config.CAMERA_DRAG_SPEED * cam.zoom;
            float dy =  Gdx.input.getDeltaY() * dt * Config.CAMERA_DRAG_SPEED * cam.zoom;
            cam.position.add(dx, dy, 0);
        }

        if (scrollYAccum != 0f) {
            cam.zoom *= (float) Math.pow(Config.CAMERA_ZOOM_STEP, scrollYAccum);
            cam.zoom = MathUtils.clamp(cam.zoom, Config.CAMERA_ZOOM_MIN, Config.CAMERA_ZOOM_MAX);
            scrollYAccum = 0f;
        }
    }

    private boolean isMouseOverWorld() {
        int mx = Gdx.input.getX();
        int myTop = Gdx.input.getY();
        int myBottom = Gdx.graphics.getHeight() - myTop;

        int sx = worldViewport.getScreenX();
        int sy = worldViewport.getScreenY();
        int sw = worldViewport.getScreenWidth();
        int sh = worldViewport.getScreenHeight();

        return mx >= sx && mx < sx + sw && myBottom >= sy && myBottom < sy + sh;
    }
}