package com.murder.game.level.serial;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.murder.game.drawing.Actor;
import com.murder.game.level.Level;
import com.murder.game.level.room.Room;
import com.murder.game.level.Tile;
import com.murder.game.level.Tile.TileType;

public class LevelGenerator
{
    private TextureAtlas textureAtlas;

    public LevelGenerator(final TextureAtlas textureAtlas)
    {
        this.textureAtlas = textureAtlas;
    }

    public LevelSerialize getLevel(final String levelId)
    {
        final int tileSize = 200;

        final List<List<Tile>> tiles = new ArrayList<List<Tile>>();
        final Map<String, List<Room>> roomMap = new HashMap<String, List<Room>>();
        for(int i = 0; i < 10; i++)
        {
            final List<Tile> innerList = new ArrayList<Tile>();
            final String roomId = "room" + i;
            final List<Room> rooms = new ArrayList<Room>();

            rooms.add(new Room(roomId, new Rectangle(i * tileSize, 0, tileSize, 10 * tileSize), textureAtlas));
            roomMap.put(roomId, rooms);

            for(int j = 0; j < 10; j++)
            {
                if(j == 0 || j == 9 || i == 0 || i == 9)
                {
                    innerList.add(
                            new Tile(textureAtlas, TileType.WALL, new Vector2(i * tileSize, j * tileSize), roomId));
                    continue;
                }

                innerList.add(new Tile(textureAtlas, TileType.FLOOR, new Vector2(i * tileSize, j * tileSize), roomId));
            }

            tiles.add(innerList);
        }
        
        tiles.get(8).get(1).setTileType(TileType.EXIT);
        final Actor player = new Actor(new Vector2(tileSize * 2 - tileSize / 2, tileSize * 2 - tileSize / 2), 0);
        addWalls(tiles);
        return new LevelSerialize(new Level(roomMap, tiles), player);
    }

    private void addWalls(final List<List<Tile>> tiles)
    {
        int x = 2;
        // for(int i = 0; i < tiles.get(x).size() - 2; i++)
        // {
        // tiles.get(x).get(i).setTileType(TileType.WALL);
        // }

        x += 2;
        for(int i = tiles.get(x).size() - 2; i > 1; i--)
        {
            tiles.get(x).get(i).setTileType(TileType.WALL);
        }

        x += 2;
        for(int i = 0; i < tiles.get(x).size() - 2; i++)
        {
            tiles.get(x).get(i).setTileType(TileType.WALL);
        }

        x += 2;
        for(int i = tiles.get(x).size() - 2; i > 1; i--)
        {
            tiles.get(x).get(i).setTileType(TileType.WALL);
        }
    }
}