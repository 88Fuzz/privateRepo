package com.murder.game.level.generator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.murder.game.constants.box2d.BodyType;
import com.murder.game.constants.drawing.FontType;
import com.murder.game.drawing.Actor;
import com.murder.game.drawing.Text;
import com.murder.game.level.Level;
import com.murder.game.level.Tile;
import com.murder.game.serialize.LevelSerialize;
import com.murder.game.serialize.MyVector2;
import com.murder.game.state.StateManager.StateId;

public class LevelGenerator
{
    private static final String DIRECTORY = "levels/";
    private static final String FILE_EXTENSION = ".json";
    private static final ObjectMapper SERIALIZER = new ObjectMapper();

    public static LevelSerialize getLevel(final String levelId)
    {
         final LevelSerialize loadedLevel = loadLevelFromFile(levelId);
         if(loadedLevel != null)
         return loadedLevel;
        
         throw new RuntimeException("File Not Found");

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
//                    // TODO the positions of the bodies are in the middle, that
//                    // may fuck some things up.
//                    if(i == xLevelSize - 1 && j == 2)
//                    {
//                        innerList.add(new Tile(BodyType.EXIT, new MyVector2(i * tileSize, j * tileSize), -90));
//                    }
//                    else
//                    {
//                        innerList.add(new Tile(BodyType.WALL, new MyVector2(i * tileSize, j * tileSize), 0));
//                    }
//                    continue;
//                }
//                innerList.add(new Tile(BodyType.FLOOR, new MyVector2(i * tileSize, j * tileSize), 0));
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
//                // innerList.add(new Tile(TileType.FLOOR, new MyVector2(i *
//                // tileSize, j * tileSize), item));
//                // }
//            }
//
//            tiles.add(innerList);
//        }
//
//        // tiles.get(xLevelSize - 1).get(2).setTileType(TileType.EXIT);
//        // final Actor player = new Actor(
//        // new MyVector2(tileSize * 7 + 100 - tileSize / 2, tileSize * 3
//        // - 50 - tileSize / 2), 90);
//        final Actor player = new Actor(BodyType.PLAYER, new MyVector2(200, 400), -90);
//
//        // addWalls(tiles);
//        final List<Text> texts = new ArrayList<Text>();
//        texts.add(new Text(new MyVector2(400, 400), FontType.BLUEBIRD_48, "Tap to move", 0));
//        return writeLevel(new LevelSerialize(new Level(tiles, texts, levelId, levelId, StateId.GAME_STATE), player), levelId);
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

    private static LevelSerialize writeLevel(final LevelSerialize level, final String levelId)
    {
        // TODO actually write the level
        try
        {
            SERIALIZER.writeValue(Gdx.files.internal(DIRECTORY + levelId + FILE_EXTENSION).file(), level);
        }
        catch(final IOException e)
        {
            e.printStackTrace();
        }
        return level;
    }

    private static LevelSerialize loadLevelFromFile(final String levelId)
    {
        final String tag = "MURDER EXCEPTION";
        try
        {
            return SERIALIZER.readValue(Gdx.files.internal(DIRECTORY + levelId + FILE_EXTENSION).readString(), LevelSerialize.class);
        }
        catch(final Exception e)
        {
            Gdx.app.error(tag, e.getMessage());
            Gdx.app.error(tag, getStackTrace(e));
            throw new RuntimeException(e);
        }
    }

    // public List<Text> getTexts(final String levelId)
    // {
    // return null;
    // }

    private static String getStackTrace(final Throwable e)
    {
        final StringBuilder sb = new StringBuilder();
        for(final StackTraceElement element: e.getStackTrace())
        {
            sb.append(element);
            sb.append("\n");
        }

        return sb.toString();
    }
}