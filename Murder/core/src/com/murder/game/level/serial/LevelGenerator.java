package com.murder.game.level.serial;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.murder.game.drawing.Actor;
import com.murder.game.level.Item;
import com.murder.game.level.Level;
import com.murder.game.level.Tile;
import com.murder.game.level.Item.InventoryItem;
import com.murder.game.level.Tile.TileType;

public class LevelGenerator
{
    private static final String FILE_EXTENSION = ".json";
    private static final ObjectMapper SERIALIZER = new ObjectMapper();

    public LevelSerialize getLevel(final String levelId)
    {
         final LevelSerialize loadedLevel = loadLevelFromFile(levelId);
         if(loadedLevel != null)
         return loadedLevel;
        
         throw new RuntimeException("");

//        final int tileSize = 200;
//        final int xLevelSize = 10;
//        final int yLevelSize = 5;
//
//        final List<List<Tile>> tiles = new ArrayList<List<Tile>>();
//        for(int i = 0; i < xLevelSize; i++)
//        {
//            final List<Tile> innerList = new ArrayList<Tile>();
//
//            for(int j = 0; j < yLevelSize; j++)
//            {
//                if(j == 0 || j == yLevelSize - 1 || i == 0 || i == xLevelSize - 1)
//                {
//                    innerList.add(new Tile(TileType.WALL, new MyVector2(i * tileSize, j * tileSize), null));
//                    continue;
//                }
//                final Item item = null;
//                // if(i == 2 && j == 6)
//                // {
//                // item = new Item(InventoryItem.GREEN_KEY, new MyVector2(i *
//                // tileSize, j * tileSize));
//                // }
//                // else
//                // {
//                // item = null;
//                // }
//
//                // if(i == 4 && j == 1)
//                // {
//                // tiles.get(i - 1).get(j).setTileType(TileType.DOOR_MAT);
//                // innerList.add(new Tile(TileType.DOOR, new MyVector2(i *
//                // tileSize, j * tileSize), item));
//                // }
//                // else
//                // {
//                innerList.add(new Tile(TileType.FLOOR, new MyVector2(i * tileSize, j * tileSize), item));
//                // }
//            }
//
//            tiles.add(innerList);
//        }
//
//        tiles.get(xLevelSize - 1).get(2).setTileType(TileType.EXIT);
//        final Actor player = new Actor(new MyVector2(tileSize * 2 - tileSize / 2, tileSize * 3 - tileSize / 2), 90);
//        // final Actor player = new Actor(
//        // new MyVector2(tileSize * 7 + 100 - tileSize / 2, tileSize * 3
//        // - 50 - tileSize / 2), 90);
//
//        // addWalls(tiles);
//        return writeLevel(new LevelSerialize(new Level(tiles, levelId, levelId), player), levelId);
    }

    // private void addWalls(final List<List<Tile>> tiles)
    // {
    // int x = 2;
    // // for(int i = 0; i < tiles.get(x).size() - 2; i++)
    // // {
    // // tiles.get(x).get(i).setTileType(TileType.WALL);
    // // }
    //
    // x += 2;
    // for(int i = tiles.get(x).size() - 2; i > 1; i--)
    // {
    // tiles.get(x).get(i).setTileType(TileType.WALL);
    // }
    //
    // x += 2;
    // for(int i = 0; i < tiles.get(x).size() - 2; i++)
    // {
    // tiles.get(x).get(i).setTileType(TileType.WALL);
    // }
    //
    // x += 2;
    // for(int i = tiles.get(x).size() - 2; i > 1; i--)
    // {
    // tiles.get(x).get(i).setTileType(TileType.WALL);
    // }
    // }

    private LevelSerialize writeLevel(final LevelSerialize level, final String levelId)
    {
        try
        {
            SERIALIZER.writeValue(new File(levelId + FILE_EXTENSION), level);
        }
        catch(final IOException e)
        {
            e.printStackTrace();
        }
        return level;
    }

    private LevelSerialize loadLevelFromFile(final String levelId)
    {
        try
        {
            final List<String> lines = Files.readAllLines(Paths.get(levelId + FILE_EXTENSION), StandardCharsets.UTF_8);
            if(!lines.isEmpty())
            {
                return SERIALIZER.readValue(lines.get(0), LevelSerialize.class);
            }
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }

        return null;
    }
}