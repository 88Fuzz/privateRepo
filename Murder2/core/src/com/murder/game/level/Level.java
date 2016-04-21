package com.murder.game.level;

import java.util.Iterator;
import java.util.List;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.murder.game.constants.box2d.BodyType;
import com.murder.game.drawing.Drawable;
import com.murder.game.drawing.Text;
import com.murder.game.drawing.manager.FontManager;
import com.murder.game.drawing.manager.TextureManager;
import com.murder.game.serialize.MyVector2;
import com.murder.game.state.StateManager.StateId;

public class Level extends Drawable
{
    private static final String TILES = "tiles";
    private static final String TEXTS = "texts";
    private static final String ITEMS = "items";
    private static final String LEVEL_ID = "levelId";
    private static final String NEXT_LEVEL_ID = "nextLevelId";
    private static final String NEXT_STATE_ID = "nextStateId";

    @JsonIgnore
    private int numberOfTiles;
    private String levelId;
    private StateId nextStateId;
    private String nextLevelId;
    private List<List<Tile>> tiles;
    private List<Text> texts;
    private List<Item> items;

    @JsonCreator
    public Level(@JsonProperty(TILES) final List<List<Tile>> tiles, @JsonProperty(TEXTS) final List<Text> texts,
            @JsonProperty(ITEMS) final List<Item> items, @JsonProperty(LEVEL_ID) final String levelId,
            @JsonProperty(NEXT_LEVEL_ID) final String nextLevelId, @JsonProperty(NEXT_STATE_ID) final StateId nextStateId)
    {
        super(BodyType.NONE, new MyVector2(), 0);
        this.tiles = tiles;
        this.texts = texts;
        this.items = items;
        this.levelId = levelId;
        this.nextLevelId = nextLevelId;
        this.nextStateId = nextStateId;
    }

    public void init(final World physicsWorld, final TextureManager textureManager, final FontManager fontManager)
    {
        numberOfTiles = 0;
        for(final List<Tile> tileList: tiles)
        {
            numberOfTiles += tileList.size();
            for(final Tile tile: tileList)
            {
                tile.init(physicsWorld, textureManager);
            }
        }

        for(final Text text: texts)
        {
            text.init(fontManager);
        }

        for(final Item item: items)
        {
            item.init(physicsWorld, textureManager);
        }
    }

    @Override
    public void draw(final SpriteBatch batch)
    {
        drawTiles(batch);
        drawTexts(batch);
        drawItems(batch);
    }

    private void drawTiles(final SpriteBatch batch)
    {
        for(final List<Tile> tileList: tiles)
        {
            for(final Tile tile: tileList)
            {
                tile.draw(batch);
            }
        }
    }

    private void drawTexts(final SpriteBatch batch)
    {
        for(final Text text: texts)
        {
            text.draw(batch);
        }
    }

    private void drawItems(final SpriteBatch batch)
    {
        for(final Item item: items)
        {
            item.draw(batch);
        }
    }

    @Override
    public void update(final float dt)
    {
        updateCurrent(dt);
    }

    @Override
    public void updateCurrent(final float dt)
    {
        updateTiles(dt);
        updateTexts(dt);
        updateItems(dt);
    }

    private void updateTiles(final float dt)
    {
        for(final List<Tile> tileList: tiles)
        {
            for(final Tile tile: tileList)
            {
                tile.update(dt);
            }
        }
    }

    private void updateTexts(final float dt)
    {
        for(final Text text: texts)
        {
            text.update(dt);
        }
    }

    private void updateItems(final float dt)
    {
        for(Iterator<Item> it = items.iterator(); it.hasNext();)
        {
            final Item item = it.next();
            item.update(dt);

            if(item.isPickedUp())
                it.remove();
        }
    }

    @JsonIgnore
    public Rectangle getLevelBounds()
    {
        final BodyType bodyType = tiles.get(0).get(0).getBodyType();
        final float halfWidth = bodyType.getWidth() / 2;
        final float halfHeight = bodyType.getHeight() / 2;

        return new Rectangle(0 - halfWidth, 0 - halfHeight, tiles.size() * bodyType.getWidth() - halfWidth,
                tiles.get(0).size() * bodyType.getHeight() - halfHeight);
    }

    public Tile getTile(final int x, final int y)
    {
        if(x >= tiles.size() || x < 0 || y < 0)
            return null;

        final List<Tile> list = tiles.get(x);
        if(y >= list.size())
            return null;

        return list.get(y);
    }

    public int getNumberOfTiles()
    {
        return numberOfTiles;
    }

    public List<List<Tile>> getTiles()
    {
        return tiles;
    }

    public List<Text> getTexts()
    {
        return texts;
    }

    public List<Item> getItems()
    {
        return items;
    }

    public String getLevelId()
    {
        return levelId;
    }

    public String getNextLevelId()
    {
        return nextLevelId;
    }

    public StateId getNextStateId()
    {
        return nextStateId;
    }
}
