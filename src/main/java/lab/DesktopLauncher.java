package lab;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import lab.sim.Config;
import lab.map.map.TileMapApp;

public class DesktopLauncher {
    static void main(String[] args) {
        Lwjgl3ApplicationConfiguration cfg = new Lwjgl3ApplicationConfiguration();
        cfg.setTitle("Life Lab - TileMap + Camera");
        cfg.setWindowedMode(Config.WINDOW_W, Config.WINDOW_H);
        cfg.useVsync(true);

        new Lwjgl3Application(new TileMapApp(), cfg);
    }
}
