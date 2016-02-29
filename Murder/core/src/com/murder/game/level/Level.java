package com.murder.game.level;

import java.util.List;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Rectangle;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.murder.game.drawing.Drawable;

public class Level extends Drawable
{
    private static final String TILES = "tiles";
    private List<List<Tile>> tiles;

    @JsonCreator
    public Level(@JsonProperty(TILES) final List<List<Tile>> tiles)
    {
        this.tiles = tiles;
    }

    public void init(final TextureAtlas textureAtlas)
    {
        for(final List<Tile> tileList: tiles)
        {
            for(final Tile tile: tileList)
            {
                tile.init(textureAtlas);
            }
        }
    }

    @Override
    public void draw(final SpriteBatch batch)
    {
        for(final List<Tile> tileList: tiles)
        {
            for(final Tile tile: tileList)
            {
                tile.draw(batch);
            }
        }
    }

    @Override
    public void update(final float dt)
    {
        for(final List<Tile> tileList: tiles)
        {
            for(final Tile tile: tileList)
            {
                tile.update(dt);
            }
        }
    }

    public List<List<Tile>> getTiles()
    {
        return tiles;
    }

    @JsonIgnore
    public Rectangle getLevelBounds()
    {
        // TODO this width is hard coded. FIX THAT.
        return new Rectangle(0, 0, tiles.size() * 200, tiles.get(0).size() * 200);
    }

    public Tile getTile(final int x, final int y)
    {
        final List<Tile> list = tiles.get(x);
        return list.get(y);
    }
}