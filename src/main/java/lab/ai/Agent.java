package lab.ai;

import lab.ai.actions.Action;
import lab.ai.actions.MoveAction;
import lab.map.map.TileMap;

public class Agent {
    public int tileX, tileY;
    public float x, y;

    private Action currentAction;
    private final TileMap map;

    public Agent(int startTileX, int startTileY, TileMap map) {
        this.map = map;
        this.tileX = startTileX;
        this.tileY = startTileY;
        this.x = map.tileCenterX(tileX);
        this.y = map.tileCenterY(tileY);
        this.currentAction = null;
    }

    public boolean isMoving() {
        return currentAction instanceof MoveAction && currentAction.isActive();
    }

    public void requestMoveTo(int tx, int ty) {
        MoveAction moveAction = new MoveAction(map);
        moveAction.start(this, tx, ty);
        currentAction = moveAction;
    }

    public void update(float dt) {
        if (currentAction != null && currentAction.isActive()) {
            currentAction.update(dt);
        }
    }
}