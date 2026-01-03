package lab.map;

public class TileMap {
    public final int width;
    public final int height;
    public final float tileSize;

    private final Tile[][] tiles;

    public TileMap(int width, int height, float tileSize) {
        this.width = width;
        this.height = height;
        this.tileSize = tileSize;

        tiles = new Tile[width][height];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                tiles[x][y] = new Tile(x, y);
            }
        }
    }

    public Tile get(int x, int y) {
        return tiles[x][y];
    }
}
