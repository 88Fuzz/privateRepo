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
import com.murder.game.drawing.drawables.Drawable;
import com.murder.game.drawing.drawables.Mob;
import com.murder.game.drawing.drawables.Text;
import com.murder.game.drawing.manager.FontManager;
import com.murder.game.serialize.MyVector2;

import box2dLight.RayHandler;

public class Level extends Drawable
{
    private static final String TILES = "tiles";
    private static final String TEXTS = "texts";
    private static final String ITEMS = "items";
    private static final String LEVEL_ID = "levelId";

    @JsonIgnore
    private int numberOfTiles;
    private List<List<Tile>> tiles;
    private List<Text> texts;
    private List<Item> items;
    private String levelId;

    @JsonCreator
    public Level(@JsonProperty(TILES) final List<List<Tile>> tiles, @JsonProperty(TEXTS) final List<Text> texts,
            @JsonProperty(ITEMS) final List<Item> items, @JsonProperty(LEVEL_ID) final String levelId)
    {
        super(BodyType.NONE, new MyVector2(), 0);
        this.tiles = tiles;
        this.texts = texts;
        this.items = items;
        this.levelId = levelId;
    }

    public void init(final World physicsWorld, final FontManager fontManager, final List<Mob> mobs, final RayHandler rayHandler)
    {
        numberOfTiles = 0;
        for(final List<Tile> tileList: tiles)
        {
            numberOfTiles += tileList.size();
            for(final Tile tile: tileList)
            {
                if(tile instanceof Exit)
                    ((Exit) tile).init(physicsWorld, mobs, rayHandler);
                else 
                    tile.init(physicsWorld, mobs);
            }
        }

        for(final Text text: texts)
        {
            text.init(fontManager);
        }

        for(final Item item: items)
        {
            item.init(physicsWorld);
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

    public void dispose()
    {
        // Find something to dispose
    }
}