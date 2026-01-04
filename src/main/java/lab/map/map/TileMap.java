package lab.map.map;


public class TileMap {
    public final int width, height;
    public final float tileSize;

    private final Tile[][] tiles;

    public TileMap(int width, int height, float tileSize) {
        this.width = width;
        this.height = height;
        this.tileSize = tileSize;

        tiles = new Tile[width][height];
        for (int x = 0; x < width; x++)
            for (int y = 0; y < height; y++)
                tiles[x][y] = new Tile(x, y);
    }

    public Tile get(int x, int y) { return tiles[x][y]; }

    public float tileCenterX(int tx) { return (tx + 0.5f) * tileSize; }
    public float tileCenterY(int ty) { return (ty + 0.5f) * tileSize; }
}