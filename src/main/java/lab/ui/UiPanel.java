package lab.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.kotcrab.vis.ui.VisUI;
import lab.sim.Config;

public class UiPanel {

    public final Stage stage;

    private final Skin skin;

    private final Label infoLabel;
    private final Label logLabel;
    private final ScrollPane logScroll;

    public UiPanel() {
        stage = new Stage(new ScreenViewport());

        skin = VisUI.getSkin();

        Table root = new Table();
        root.setFillParent(true);

        Table right = new Table();
        right.setBackground(skin.newDrawable("window", new Color(0.25f, 0.25f, 0.27f, 1f)));
        right.defaults().pad(8);

        Label infoTitle = new Label("Info", skin);
        infoTitle.setAlignment(Align.left);

        infoLabel = new Label("", skin);
        infoLabel.setWrap(true);

        Label logTitle = new Label("Logi", skin);
        logTitle.setAlignment(Align.left);

        logLabel = new Label("", skin);
        logLabel.setWrap(true);

        logScroll = new ScrollPane(logLabel, skin);
        logScroll.setFadeScrollBars(false);
        logScroll.setScrollingDisabled(true, false);

        right.add(infoTitle).left().growX().row();
        right.add(infoLabel).left().growX().height(140).row();

        right.add(logTitle).left().growX().row();
        right.add(logScroll).grow().row();

        root.add().expand().fill();
        root.add(right).top().right().width(Config.UI_PANEL_WIDTH);

        stage.addActor(root);
    }

    public void updateInfo(String text) {
        infoLabel.setText(text);
    }

    public void updateLogs(String text) {
        logLabel.setText(text);
        logScroll.layout();
        logScroll.setScrollPercentY(1f);
    }

    public void resize(int w, int h) {
        stage.getViewport().update(w, h, true);
    }

    public void dispose() {
        stage.dispose();
        skin.dispose();
    }
}
