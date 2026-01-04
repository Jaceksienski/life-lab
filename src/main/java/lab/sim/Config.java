package lab.sim;

public final class Config {
    private Config() {}

    // Okno (desktop)
    public static final int WINDOW_W = 1000;
    public static final int WINDOW_H = 700;

    // Mapa
    public static final int MAP_W = 120;
    public static final int MAP_H = 80;
    public static final float TILE_SIZE = 32f;

    // Kamera
    public static final float CAMERA_MOVE_SPEED = 650f; // bazowo, przemnożone przez dt i zoom
    public static final float CAMERA_DRAG_SPEED = 600f;
    public static final float CAMERA_ZOOM_MIN = 0.35f;
    public static final float CAMERA_ZOOM_MAX = 3.0f;
    public static final float CAMERA_ZOOM_STEP = 1.1f;

    // Rendering optymalizacyjny
    public static final float RENDER_MARGIN_PERCENT = 0.10f;

    // UI
    public static final float UI_PANEL_WIDTH = 340f;
    public static final int LOG_MAX_LINES = 200;

    // Tło
    public static final float CLEAR_R = 0.08f;
    public static final float CLEAR_G = 0.08f;
    public static final float CLEAR_B = 0.10f;

    // Akcja
    public static final float MOVE_DURATION = 0.25f;
}
