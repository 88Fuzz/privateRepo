package com.murder.game.level.generator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.murder.game.constants.box2d.BodyType;
import com.murder.game.constants.drawing.FontType;
import com.murder.game.constants.level.ItemType;
import com.murder.game.drawing.Actor;
import com.murder.game.drawing.Text;
import com.murder.game.level.Item;
import com.murder.game.level.Level;
import com.murder.game.level.Tile;
import com.murder.game.serialize.LevelSerialize;
import com.murder.game.serialize.MyVector2;
import com.murder.game.state.StateManager.StateId;

public class LevelGenerator
{
    private static final int TILE_SIZE = 200;
    private static final String DIRECTORY = "levels/";
    private static final String FILE_EXTENSION = ".json";
    private static final ObjectMapper SERIALIZER = new ObjectMapper();

    public static LevelSerialize getLevel(final String levelId)
    {
        // final LevelSerialize loadedLevel = loadLevelFromFile("Level05");
        // //final LevelSerialize loadedLevel = loadLevelFromFile(levelId);
        // if(loadedLevel != null)
        // return loadedLevel;
        //
        // throw new RuntimeException("File Not Found");

        return generateLevel5();
    }

    public static LevelSerialize generateLevel5()
    {
        final int xLevelSize = 10;
        final int yLevelSize = 8;
        final int xPlayerStart = 2;
        final int yPlayerStart = 1;
        final int playerStartRotation = 0;

        final List<List<Tile>> tiles = new ArrayList<List<Tile>>();
        final List<Item> items = new ArrayList<Item>();
        for(int i = 0; i < xLevelSize; i++)
        {
            final List<Tile> innerList = new ArrayList<Tile>();

            for(int j = 0; j < yLevelSize; j++)
            {
                if(j == 0 || j == yLevelSize - 1 || i == 0 || i == xLevelSize - 1)
                {
                    innerList.add(new Tile(BodyType.WALL, new MyVector2(i * TILE_SIZE, j * TILE_SIZE), 0));
                    continue;
                }
                else if(j == 1 && i == 1)
                {
                    innerList.add(new Tile(BodyType.FLOOR, new MyVector2(i * TILE_SIZE, j * TILE_SIZE), 0));
                    items.add(new Item(ItemType.GREEN_KEY, new MyVector2(i * TILE_SIZE, j * TILE_SIZE), -90));
                }
                else if(j == 3 && i == 2)
                {
                    innerList.add(new Tile(BodyType.GREEN_DOOR, new MyVector2(i * TILE_SIZE, j * TILE_SIZE), 0));
                }
                else if(j == 3 && i <= 4)
                {
                    innerList.add(new Tile(BodyType.WALL, new MyVector2(i * TILE_SIZE, j * TILE_SIZE), 0));
                }
                else if(j == 6 && i == 5)
                {
                    innerList.add(new Tile(BodyType.EXIT, new MyVector2(i * TILE_SIZE, j * TILE_SIZE), -90));
                }
                else if(j >= 3 && i == 5)
                {
                    innerList.add(new Tile(BodyType.WALL, new MyVector2(i * TILE_SIZE, j * TILE_SIZE), 0));
                }
                else if(j >= 3 && i == 6)
                {
                    innerList.add(new Tile(BodyType.WALL, new MyVector2(i * TILE_SIZE, j * TILE_SIZE), 0));
                }
                else
                {
                    innerList.add(new Tile(BodyType.FLOOR, new MyVector2(i * TILE_SIZE, j * TILE_SIZE), 0));
                }
            }

            tiles.add(innerList);
        }
        final Actor player = new Actor(BodyType.PLAYER, new MyVector2(xPlayerStart * TILE_SIZE, yPlayerStart * TILE_SIZE), playerStartRotation);

        return writeLevel(new LevelSerialize(new Level(tiles, new LinkedList<Text>(), items, "Level05", "Level05", StateId.GAME_STATE), player),
                "Level05");
    }

    public static LevelSerialize generateLevel4()
    {
        final int xLevelSize = 12;
        final int yLevelSize = 12;
        final int xPlayerStart = 1;
        final int yPlayerStart = 4;

        final List<List<Tile>> tiles = new ArrayList<List<Tile>>();
        final List<Item> items = new ArrayList<Item>();
        for(int i = 0; i < xLevelSize; i++)
        {
            final List<Tile> innerList = new ArrayList<Tile>();

            for(int j = 0; j < yLevelSize; j++)
            {
                if(j == 0 || j == yLevelSize - 1 || i == 0 || i == xLevelSize - 1)
                {
                    innerList.add(new Tile(BodyType.WALL, new MyVector2(i * TILE_SIZE, j * TILE_SIZE), 0));
                    continue;
                }
                else if(j == 8 && i >= 4 && i <= 8)
                {
                    innerList.add(new Tile(BodyType.WALL, new MyVector2(i * TILE_SIZE, j * TILE_SIZE), 0));
                }
                else if(j <= 8 && i == 3)
                {
                    innerList.add(new Tile(BodyType.WALL, new MyVector2(i * TILE_SIZE, j * TILE_SIZE), 0));
                }
                else if(j >= 3 && j <= 8 && i == 8)
                {
                    innerList.add(new Tile(BodyType.WALL, new MyVector2(i * TILE_SIZE, j * TILE_SIZE), 0));
                }
                else if(j >= 3 && j <= 5 && i == 6)
                {
                    innerList.add(new Tile(BodyType.WALL, new MyVector2(i * TILE_SIZE, j * TILE_SIZE), 0));
                }
                else if(j >= 3 && j <= 6 && i == 5)
                {
                    innerList.add(new Tile(BodyType.WALL, new MyVector2(i * TILE_SIZE, j * TILE_SIZE), 0));
                }
                else if(i == 6 && j == 6)
                {
                    innerList.add(new Tile(BodyType.EXIT, new MyVector2(i * TILE_SIZE, j * TILE_SIZE), -90));
                }
                else
                {
                    innerList.add(new Tile(BodyType.FLOOR, new MyVector2(i * TILE_SIZE, j * TILE_SIZE), 0));
                }
            }

            tiles.add(innerList);
        }
        final Actor player = new Actor(BodyType.PLAYER, new MyVector2(xPlayerStart * TILE_SIZE, yPlayerStart * TILE_SIZE), 180);

        return writeLevel(new LevelSerialize(new Level(tiles, new LinkedList<Text>(), items, "Level04", "Level04", StateId.GAME_STATE), player),
                "Level04");
    }

    public static LevelSerialize generateLevel3()
    {
        final int xLevelSize = 8;
        final int yLevelSize = 12;
        final int xPlayerStart = 1;
        final int yPlayerStart = yLevelSize - 2;

        final List<List<Tile>> tiles = new ArrayList<List<Tile>>();
        final List<Item> items = new ArrayList<Item>();
        for(int i = 0; i < xLevelSize; i++)
        {
            final List<Tile> innerList = new ArrayList<Tile>();

            for(int j = 0; j < yLevelSize; j++)
            {
                if(j == 0 || j == yLevelSize - 1 || i == 0 || i == xLevelSize - 1)
                {
                    innerList.add(new Tile(BodyType.WALL, new MyVector2(i * TILE_SIZE, j * TILE_SIZE), 0));
                    continue;
                }
                else if(j == 8 && i <= 4)
                {
                    innerList.add(new Tile(BodyType.WALL, new MyVector2(i * TILE_SIZE, j * TILE_SIZE), 0));
                }
                else if(j == 5 && i >= 3)
                {
                    innerList.add(new Tile(BodyType.WALL, new MyVector2(i * TILE_SIZE, j * TILE_SIZE), 0));
                }
                else if(j == 2 && i <= 4)
                {
                    innerList.add(new Tile(BodyType.WALL, new MyVector2(i * TILE_SIZE, j * TILE_SIZE), 0));
                }
                else if(i == 1 && j == 1)
                {
                    innerList.add(new Tile(BodyType.EXIT, new MyVector2(i * TILE_SIZE, j * TILE_SIZE), -90));
                }
                else
                {
                    innerList.add(new Tile(BodyType.FLOOR, new MyVector2(i * TILE_SIZE, j * TILE_SIZE), 0));
                }
            }

            tiles.add(innerList);
        }
        final Actor player = new Actor(BodyType.PLAYER, new MyVector2(xPlayerStart * TILE_SIZE, yPlayerStart * TILE_SIZE), -90);

        return writeLevel(new LevelSerialize(new Level(tiles, new LinkedList<Text>(), items, "Level03", "Level03", StateId.GAME_STATE), player),
                "Level03");
    }

    public static LevelSerialize generateLevel2()
    {
        final int xLevelSize = 6;
        final int yLevelSize = 10;
        final int xPlayerStart = 1;
        final int yPlayerStart = yLevelSize - 2;

        final List<List<Tile>> tiles = new ArrayList<List<Tile>>();
        final List<Item> items = new ArrayList<Item>();
        for(int i = 0; i < xLevelSize; i++)
        {
            final List<Tile> innerList = new ArrayList<Tile>();

            for(int j = 0; j < yLevelSize; j++)
            {
                if(j == 0 || j == yLevelSize - 1 || i == 0 || i == xLevelSize - 1)
                {
                    if(i == xLevelSize - 2 && j == 0)
                    {
                        innerList.add(new Tile(BodyType.EXIT, new MyVector2(i * TILE_SIZE, j * TILE_SIZE), 0));
                    }
                    else
                    {
                        innerList.add(new Tile(BodyType.WALL, new MyVector2(i * TILE_SIZE, j * TILE_SIZE), 0));
                    }
                    continue;
                }
                innerList.add(new Tile(BodyType.FLOOR, new MyVector2(i * TILE_SIZE, j * TILE_SIZE), 0));
            }

            tiles.add(innerList);
        }
        final Actor player = new Actor(BodyType.PLAYER, new MyVector2(xPlayerStart * TILE_SIZE, yPlayerStart * TILE_SIZE), -90);

        final List<Text> texts = new ArrayList<Text>();
        texts.add(new Text(new MyVector2((float) (xPlayerStart * TILE_SIZE + TILE_SIZE / 1.5), yPlayerStart * TILE_SIZE), FontType.BLUEBIRD_48,
                "Two fingers to rotate", 0));
        return writeLevel(new LevelSerialize(new Level(tiles, texts, items, "Level02", "Level02", StateId.GAME_STATE), player), "Level02");
    }

    public static LevelSerialize generateLevel1()
    {
        final int xLevelSize = 10;
        final int yLevelSize = 5;

        final List<List<Tile>> tiles = new ArrayList<List<Tile>>();
        final List<Item> items = new ArrayList<Item>();
        for(int i = 0; i < xLevelSize; i++)
        {
            final List<Tile> innerList = new ArrayList<Tile>();

            for(int j = 0; j < yLevelSize; j++)
            {
                if(j == 0 || j == yLevelSize - 1 || i == 0 || i == xLevelSize - 1)
                {
                    if(i == xLevelSize - 1 && j == 2)
                    {
                        innerList.add(new Tile(BodyType.EXIT, new MyVector2(i * TILE_SIZE, j * TILE_SIZE), -90));
                    }
                    else
                    {
                        innerList.add(new Tile(BodyType.WALL, new MyVector2(i * TILE_SIZE, j * TILE_SIZE), 0));
                    }
                    continue;
                }
                innerList.add(new Tile(BodyType.FLOOR, new MyVector2(i * TILE_SIZE, j * TILE_SIZE), 0));
                // if(i == 2 && j == 6)
                // {
                // item = new Item(InventoryItem.GREEN_KEY, new MyVector2(i *
                // tileSize, j * tileSize));
                // }
                // else
                // {
                // item = null;
                // }

                // if(i == 4 && j == 1)
                // {
                // tiles.get(i - 1).get(j).setTileType(TileType.DOOR_MAT);
                // innerList.add(new Tile(TileType.DOOR, new MyVector2(i *
                // tileSize, j * tileSize), item));
                // }
                // else
                // {
                // innerList.add(new Tile(TileType.FLOOR, new MyVector2(i *
                // tileSize, j * tileSize), item));
                // }
            }

            tiles.add(innerList);
        }

        // tiles.get(xLevelSize - 1).get(2).setTileType(TileType.EXIT);
        // final Actor player = new Actor(
        // new MyVector2(tileSize * 7 + 100 - tileSize / 2, tileSize * 3
        // - 50 - tileSize / 2), 90);
        final Actor player = new Actor(BodyType.PLAYER, new MyVector2(200, 400), -90);

        // addWalls(tiles);
        final List<Text> texts = new ArrayList<Text>();
        texts.add(new Text(new MyVector2(400, 400), FontType.BLUEBIRD_48, "Tap to move", 0));
        return writeLevel(new LevelSerialize(new Level(tiles, texts, items, "Level01", "Level01", StateId.GAME_STATE), player), "Level01");
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