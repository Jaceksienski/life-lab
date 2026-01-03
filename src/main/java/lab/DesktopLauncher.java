package lab.desktop;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import lab.sim.TileMapApp;

public class DesktopLauncher {
    public static void main(String[] args) {
        Lwjgl3ApplicationConfiguration cfg = new Lwjgl3ApplicationConfiguration();
        cfg.setTitle("Life Lab - TileMap");
        cfg.setWindowedMode(1000, 700);
        cfg.useVsync(true);

        new Lwjgl3Application(new TileMapApp(), cfg);
    }
}
