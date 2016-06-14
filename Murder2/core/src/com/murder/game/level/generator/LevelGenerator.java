package com.murder.game.level.generator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.murder.game.constants.box2d.BodyType;
import com.murder.game.constants.drawing.FontType;
import com.murder.game.constants.level.ItemType;
import com.murder.game.drawing.drawables.Actor;
import com.murder.game.drawing.drawables.Mob;
import com.murder.game.drawing.drawables.Text;
import com.murder.game.effects.text.TextEffect;
import com.murder.game.level.Door;
import com.murder.game.level.Exit;
import com.murder.game.level.Item;
import com.murder.game.level.Level;
import com.murder.game.level.Tile;
import com.murder.game.level.Door.DoorMat;
import com.murder.game.serialize.LevelSerialize;
import com.murder.game.serialize.MyVector2;
import com.murder.game.state.config.GameStateConfig;
import com.murder.game.state.config.StateConfig;
import com.murder.game.state.management.PendingAction;
import com.murder.game.state.management.StateAction;
import com.murder.game.state.management.StateId;

public class LevelGenerator
{
    public static final int TILE_SIZE = 200;
    private static final String DIRECTORY = "levels/";
    private static final String FILE_EXTENSION = ".json";
    private static final ObjectMapper SERIALIZER = new ObjectMapper();

    public static LevelSerialize getLevel(final String levelId)
    {
        // final LevelSerialize loadedLevel = loadLevelFromFile(levelId);
        // if(loadedLevel != null)
        // return loadedLevel;
        //
        // throw new RuntimeException("File Not Found");

        // TODO some levels don't have a wall behind the exit, causing the light
        // to shine past the space
        // return generateTestLevel();
        return generateLevel8();
        // return generateLevel7();
        // return generateLevel6();
        // return generateLevel5();
        // return generateLevel4();
        // return generateLevel3();
        // return generateLevel2();
        // return generateLevel1();
    }

    public static LevelSerialize generateTestLevel()
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
                        innerList.add(new Exit(BodyType.EXIT, new MyVector2(i * TILE_SIZE, j * TILE_SIZE), -90));
                    }
                    else
                    {
                        innerList.add(new Tile(BodyType.WALL, new MyVector2(i * TILE_SIZE, j * TILE_SIZE), 0));
                    }
                }
                else if(i == 4 && j == 3)
                {
                    // innerList.add(new Tile(BodyType.FLOOR, new MyVector2(i *
                    // TILE_SIZE, j * TILE_SIZE), 0));
                    innerList.add(new Door(BodyType.GREEN_DOOR, ItemType.GREEN_KEY, new MyVector2(i * TILE_SIZE, j * TILE_SIZE), 0, DoorMat.DOWN));
                    // innerList.add(new Tile(BodyType.FLOOR, new MyVector2(i *
                    // TILE_SIZE, j * TILE_SIZE), 0));
                }
                else if(i == 4)
                {
                    innerList.add(new Tile(BodyType.WALL, new MyVector2(i * TILE_SIZE, j * TILE_SIZE), 0));
                }
                else if(i == 3 && j == 3)
                {
                    innerList.add(new Tile(BodyType.FLOOR, new MyVector2(i * TILE_SIZE, j * TILE_SIZE), 0));
                    items.add(new Item(ItemType.GREEN_KEY, new MyVector2(i * TILE_SIZE, j * TILE_SIZE), -90));
                }
                else
                {
                    innerList.add(new Tile(BodyType.FLOOR, new MyVector2(i * TILE_SIZE, j * TILE_SIZE), 0));
                }
            }

            tiles.add(innerList);
        }

        final Actor player = new Actor(BodyType.PLAYER, new MyVector2(600, 200), -90);
        final List<Mob> mobs = new LinkedList<Mob>();
        mobs.add(new Mob(BodyType.MOB, new MyVector2(1200, 200), 0));
        mobs.add(new Mob(BodyType.MOB, new MyVector2(1400, 200), 0));

        final List<PendingAction> actions = new LinkedList<PendingAction>();
        actions.add(new PendingAction().withAction(StateAction.POP));
        actions.add(new PendingAction().withAction(StateAction.PUSH).withStateId(StateId.GAME_STATE)
                .withStateConfig(getGameStateConfig("Level08", Color.WHITE)));

        return writeLevel(new LevelSerialize(new Level(tiles, new LinkedList<Text>(), items, "Level07"), player, mobs, actions), "Level07");
    }

    public static LevelSerialize generateLevel8()
    {
        final int xLevelSize = 14;
        final int yLevelSize = 13;

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
                }
                else if(j == 6 && i == 11)
                {
                    innerList.add(new Tile(BodyType.FLOOR, new MyVector2(i * TILE_SIZE, j * TILE_SIZE), 0));
                    items.add(new Item(ItemType.GREEN_KEY, new MyVector2(i * TILE_SIZE, j * TILE_SIZE), -90));
                }
                else if(j == 6 && i == 4)
                {
                    innerList.add(new Door(BodyType.GREEN_DOOR, ItemType.GREEN_KEY, new MyVector2(i * TILE_SIZE, j * TILE_SIZE), 0, DoorMat.LEFT));
                }
                else if(j == 6 && i == 9)
                {
                    innerList.add(new Exit(BodyType.EXIT, new MyVector2(i * TILE_SIZE, j * TILE_SIZE), -90));
                }
                else if((j >= 3 && j <= 9) && (i == 4 || i == 9 || i == 10))
                {
                    innerList.add(new Tile(BodyType.WALL, new MyVector2(i * TILE_SIZE, j * TILE_SIZE), 0));
                }
                else if((j == 3 || j == 9) && (i >= 5 && i <= 8))
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

        final Actor player = new Actor(BodyType.PLAYER, new MyVector2(200, 1200), -90);
        final List<Text> texts = new ArrayList<Text>();

        final List<Mob> mobs = new LinkedList<Mob>();
        mobs.add(new Mob(BodyType.MOB, new MyVector2(1400, 1400), 0));
        mobs.add(new Mob(BodyType.MOB, new MyVector2(1600, 800), 0));

        final List<PendingAction> actions = new LinkedList<PendingAction>();
        actions.add(new PendingAction().withAction(StateAction.POP));
        actions.add(new PendingAction().withAction(StateAction.PUSH).withStateId(StateId.GAME_STATE)
                .withStateConfig(getGameStateConfig("Level09", Color.BLACK)));

        return writeLevel(new LevelSerialize(new Level(tiles, texts, items, "Level08"), player, mobs, actions), "Level08");
    }

    public static LevelSerialize generateLevel7()
    {
        final int xLevelSize = 16;
        final int yLevelSize = 5;

        final List<List<Tile>> tiles = new ArrayList<List<Tile>>();
        final List<Item> items = new ArrayList<Item>();
        for(int i = 0; i < xLevelSize; i++)
        {
            final List<Tile> innerList = new ArrayList<Tile>();

            for(int j = 0; j < yLevelSize; j++)
            {
                if(j == 0 || j == yLevelSize - 1 || i == 0 || i == 1 || i == xLevelSize - 1)
                {
                    if(i == 1 && j == 2)
                    {
                        innerList.add(new Exit(BodyType.EXIT, new MyVector2(i * TILE_SIZE, j * TILE_SIZE), -90));
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

        final Actor player = new Actor(BodyType.PLAYER, new MyVector2(1600, 400), -90);
        final List<Text> texts = new ArrayList<Text>();

        final List<Mob> mobs = new LinkedList<Mob>();
        mobs.add(new Mob(BodyType.MOB, new MyVector2(2800, 400), 0));

        final List<PendingAction> actions = new LinkedList<PendingAction>();
        actions.add(new PendingAction().withAction(StateAction.POP));
        actions.add(new PendingAction().withAction(StateAction.PUSH).withStateId(StateId.GAME_STATE)
                .withStateConfig(getGameStateConfig("Level08", Color.BLACK)));

        return writeLevel(new LevelSerialize(new Level(tiles, texts, items, "Level07"), player, mobs, actions), "Level07");
    }

    public static LevelSerialize generateLevel6()
    {
        final int xLevelSize = 8;
        final int yLevelSize = 13;
        final int xPlayerStart = 5;
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
                }
                else if(j == 6 && i == 2)
                {
                    innerList.add(new Tile(BodyType.FLOOR, new MyVector2(i * TILE_SIZE, j * TILE_SIZE), 0));
                    items.add(new Item(ItemType.YELLOW_KEY, new MyVector2(i * TILE_SIZE, j * TILE_SIZE), -90));
                }
                else if(j == 7 && i == 5)
                {
                    innerList.add(new Tile(BodyType.FLOOR, new MyVector2(i * TILE_SIZE, j * TILE_SIZE), 0));
                    items.add(new Item(ItemType.GREEN_KEY, new MyVector2(i * TILE_SIZE, j * TILE_SIZE), -90));
                }
                else if(j == 3 && i == 5)
                {
                    innerList.add(new Door(BodyType.GREEN_DOOR, ItemType.GREEN_KEY, new MyVector2(i * TILE_SIZE, j * TILE_SIZE), 0, DoorMat.DOWN));
                    // +90 is a magic number that should be given value, like
                    // TILE_SIZE/2 - TILE_SIZE/10
                    // items.add(new Item(ItemType.GREEN_MAT, new MyVector2(i *
                    // TILE_SIZE, (j - 1) * TILE_SIZE + 90), -90));
                }
                else if(j == 9 && i == 5)
                {
                    innerList.add(new Door(BodyType.YELLOW_DOOR, ItemType.YELLOW_KEY, new MyVector2(i * TILE_SIZE, j * TILE_SIZE), 0, DoorMat.UP));
                    // +90 is a magic number that should be given value, like
                    // TILE_SIZE/2 - TILE_SIZE/10
                    final Vector2 butts = new Vector2(0, 90);
                    butts.rotate(180);
                    // items.add(new Item(ItemType.YELLOW_MAT, new MyVector2(i *
                    // TILE_SIZE, (j + 1) * TILE_SIZE + butts.y), -90));
                }
                else if(j <= 9 && j >= 3 && i == 3)
                {
                    innerList.add(new Tile(BodyType.WALL, new MyVector2(i * TILE_SIZE, j * TILE_SIZE), 0));
                }
                else if(j == 9 && i >= 3)
                {
                    innerList.add(new Tile(BodyType.WALL, new MyVector2(i * TILE_SIZE, j * TILE_SIZE), 0));
                }
                else if(j == 6 && i >= 3)
                {
                    innerList.add(new Tile(BodyType.WALL, new MyVector2(i * TILE_SIZE, j * TILE_SIZE), 0));
                }
                else if(j == 3 && i >= 3)
                {
                    innerList.add(new Tile(BodyType.WALL, new MyVector2(i * TILE_SIZE, j * TILE_SIZE), 0));
                }
                else if(j == 5 && i == 5)
                {
                    innerList.add(new Exit(BodyType.EXIT, new MyVector2(i * TILE_SIZE, j * TILE_SIZE), -90));
                }
                else
                {
                    innerList.add(new Tile(BodyType.FLOOR, new MyVector2(i * TILE_SIZE, j * TILE_SIZE), 0));
                }
            }

            tiles.add(innerList);
        }
        final Actor player = new Actor(BodyType.PLAYER, new MyVector2(xPlayerStart * TILE_SIZE, yPlayerStart * TILE_SIZE), playerStartRotation);

        final List<PendingAction> actions = new LinkedList<PendingAction>();
        actions.add(new PendingAction().withAction(StateAction.POP));
        actions.add(new PendingAction().withAction(StateAction.PUSH).withStateId(StateId.GAME_STATE)
                .withStateConfig(getGameStateConfig("Level07", Color.BLACK)));

        return writeLevel(new LevelSerialize(new Level(tiles, new LinkedList<Text>(), items, "Level06"), player, new LinkedList<Mob>(), actions),
                "Level06");
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
                else if(j == 6 && i == 7)
                {
                    innerList.add(new Tile(BodyType.FLOOR, new MyVector2(i * TILE_SIZE, j * TILE_SIZE), 0));
                    items.add(new Item(ItemType.GREEN_KEY, new MyVector2(i * TILE_SIZE, j * TILE_SIZE), -90));
                }
                else if(j == 3 && i == 2)
                {
                    innerList.add(new Door(BodyType.GREEN_DOOR, ItemType.GREEN_KEY, new MyVector2(i * TILE_SIZE, j * TILE_SIZE), 0, DoorMat.DOWN));
                    // +90 is a magic number that should be given value, like
                    // TILE_SIZE/2 - TILE_SIZE/10
                    // items.add(new Item(ItemType.GREEN_MAT, new MyVector2(i *
                    // TILE_SIZE, (j - 1) * TILE_SIZE + 90), -90));
                }
                else if(j == 3 && i <= 4)
                {
                    innerList.add(new Tile(BodyType.WALL, new MyVector2(i * TILE_SIZE, j * TILE_SIZE), 0));
                }
                else if(j == 6 && i == 5)
                {
                    innerList.add(new Exit(BodyType.EXIT, new MyVector2(i * TILE_SIZE, j * TILE_SIZE), -90));
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

        final List<PendingAction> actions = new LinkedList<PendingAction>();
        actions.add(new PendingAction().withAction(StateAction.POP));
        actions.add(new PendingAction().withAction(StateAction.PUSH).withStateId(StateId.GAME_STATE)
                .withStateConfig(getGameStateConfig("Level06", Color.BLACK)));

        return writeLevel(new LevelSerialize(new Level(tiles, new LinkedList<Text>(), items, "Level05"), player, new LinkedList<Mob>(), actions),
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
                    innerList.add(new Exit(BodyType.EXIT, new MyVector2(i * TILE_SIZE, j * TILE_SIZE), -90));
                }
                else
                {
                    innerList.add(new Tile(BodyType.FLOOR, new MyVector2(i * TILE_SIZE, j * TILE_SIZE), 0));
                }
            }

            tiles.add(innerList);
        }
        final Actor player = new Actor(BodyType.PLAYER, new MyVector2(xPlayerStart * TILE_SIZE, yPlayerStart * TILE_SIZE), 180);

        final List<PendingAction> actions = new LinkedList<PendingAction>();
        actions.add(new PendingAction().withAction(StateAction.POP));
        actions.add(new PendingAction().withAction(StateAction.PUSH).withStateId(StateId.GAME_STATE)
                .withStateConfig(getGameStateConfig("Level05", Color.BLACK)));

        return writeLevel(new LevelSerialize(new Level(tiles, new LinkedList<Text>(), items, "Level04"), player, new LinkedList<Mob>(), actions),
                "Level04");
    }

    public static LevelSerialize generateLevel3()
    {
        final int xLevelSize = 8;
        final int yLevelSize = 13;
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
                else if(j == 9 && i <= 4)
                {
                    innerList.add(new Tile(BodyType.WALL, new MyVector2(i * TILE_SIZE, j * TILE_SIZE), 0));
                }
                else if(j == 6 && i >= 3)
                {
                    innerList.add(new Tile(BodyType.WALL, new MyVector2(i * TILE_SIZE, j * TILE_SIZE), 0));
                }
                else if(j == 3 && i <= 4)
                {
                    innerList.add(new Tile(BodyType.WALL, new MyVector2(i * TILE_SIZE, j * TILE_SIZE), 0));
                }
                else if(j == 1 && i == 1)
                {
                    innerList.add(new Exit(BodyType.EXIT, new MyVector2(i * TILE_SIZE, j * TILE_SIZE), -90));
                }
                else
                {
                    innerList.add(new Tile(BodyType.FLOOR, new MyVector2(i * TILE_SIZE, j * TILE_SIZE), 0));
                }
            }

            tiles.add(innerList);
        }
        final Actor player = new Actor(BodyType.PLAYER, new MyVector2(xPlayerStart * TILE_SIZE, yPlayerStart * TILE_SIZE), -90);

        final List<PendingAction> actions = new LinkedList<PendingAction>();
        actions.add(new PendingAction().withAction(StateAction.POP));
        actions.add(new PendingAction().withAction(StateAction.PUSH).withStateId(StateId.GAME_STATE)
                .withStateConfig(getGameStateConfig("Level04", Color.BLACK)));

        return writeLevel(new LevelSerialize(new Level(tiles, new LinkedList<Text>(), items, "Level03"), player, new LinkedList<Mob>(), actions),
                "Level03");
    }

    public static LevelSerialize generateLevel2()
    {
        final int xLevelSize = 6;
        final int yLevelSize = 11;
        final int xPlayerStart = 1;
        final int yPlayerStart = yLevelSize - 2;

        final List<List<Tile>> tiles = new ArrayList<List<Tile>>();
        final List<Item> items = new ArrayList<Item>();
        for(int i = 0; i < xLevelSize; i++)
        {
            final List<Tile> innerList = new ArrayList<Tile>();

            for(int j = 0; j < yLevelSize; j++)
            {
                if(j == 0 || j == 1 || j == yLevelSize - 1 || i == 0 || i == xLevelSize - 1)
                {
                    if(i == xLevelSize - 2 && j == 1)
                    {
                        innerList.add(new Exit(BodyType.EXIT, new MyVector2(i * TILE_SIZE, j * TILE_SIZE), 0));
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
        texts.add(new Text(new MyVector2((float) (xPlayerStart * TILE_SIZE + TILE_SIZE / 1.5), yPlayerStart * TILE_SIZE), FontType.AVOCADO_56,
                "two fingers to rotate", 0, new LinkedList<TextEffect>()));

        final List<PendingAction> actions = new LinkedList<PendingAction>();
        actions.add(new PendingAction().withAction(StateAction.POP));
        actions.add(new PendingAction().withAction(StateAction.PUSH).withStateId(StateId.GAME_STATE)
                .withStateConfig(getGameStateConfig("Level03", Color.BLACK)));

        return writeLevel(new LevelSerialize(new Level(tiles, texts, items, "Level02"), player, new LinkedList<Mob>(), actions), "Level02");
    }

    public static LevelSerialize generateLevel1()
    {
        final int xLevelSize = 11;
        final int yLevelSize = 5;

        final List<List<Tile>> tiles = new ArrayList<List<Tile>>();
        final List<Item> items = new ArrayList<Item>();
        for(int i = 0; i < xLevelSize; i++)
        {
            final List<Tile> innerList = new ArrayList<Tile>();

            for(int j = 0; j < yLevelSize; j++)
            {
                if(j == 0 || j == yLevelSize - 1 || i == 0 || i == xLevelSize - 1 || i == xLevelSize - 2)
                {
                    if(i == xLevelSize - 2 && j == 2)
                    {
                        innerList.add(new Exit(BodyType.EXIT, new MyVector2(i * TILE_SIZE, j * TILE_SIZE), -90));
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
        texts.add(new Text(new MyVector2(400, 400), FontType.AVOCADO_56, "tap to move", 0, new LinkedList<TextEffect>()));

        final List<PendingAction> actions = new LinkedList<PendingAction>();
        actions.add(new PendingAction().withAction(StateAction.POP));
        actions.add(new PendingAction().withAction(StateAction.PUSH).withStateId(StateId.GAME_STATE)
                .withStateConfig(getGameStateConfig("Level02", Color.BLACK)));

        return writeLevel(new LevelSerialize(new Level(tiles, texts, items, "Level01"), player, new LinkedList<Mob>(), actions), "Level01");
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
        // TODO remove logging
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

    private static StateConfig getGameStateConfig(final String string, final Color color)
    {
        return new GameStateConfig(string, color.cpy());
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