package com.murder.game.level;

import java.util.List;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.murder.game.constants.box2d.BodyType;
import com.murder.game.drawing.Drawable;
import com.murder.game.drawing.TextureManager;
import com.murder.game.state.StateManager.StateId;

public class Level extends Drawable
{
    private static final String TILES = "tiles";
    private static final String LEVEL_ID = "levelId";
    private static final String NEXT_LEVEL_ID = "nextLevelId";
    private static final String NEXT_STATE_ID = "nextStateId";

    private String levelId;
    private StateId nextStateId;
    private String nextLevelId;
    private List<List<Tile>> tiles;

    @JsonCreator
    public Level(@JsonProperty(TILES) final List<List<Tile>> tiles, @JsonProperty(LEVEL_ID) final String levelId,
            @JsonProperty(NEXT_LEVEL_ID) final String nextLevelId, @JsonProperty(NEXT_STATE_ID) final StateId nextStateId)
    {
        this.tiles = tiles;
        this.levelId = levelId;
        this.nextLevelId = nextLevelId;
        this.nextStateId = nextStateId;
    }

    public void init(final World physicsWorld, final TextureManager textureManager)
    {
        for(final List<Tile> tileList: tiles)
        {
            for(final Tile tile: tileList)
            {
                // TODO this null check might not be needed in the end
                if(tile != null)
                    tile.init(physicsWorld, textureManager);
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
                if(tile != null)
                    tile.draw(batch);
            }
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
        for(final List<Tile> tileList: tiles)
        {
            for(final Tile tile: tileList)
            {
                if(tile != null)
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
        final BodyType bodyType = tiles.get(0).get(0).getBodyType();
        final float halfWidth = bodyType.getWidth() / 2;
        final float halfHeight = bodyType.getHeight() / 2;

        return new Rectangle(0 - halfWidth, 0 - halfHeight, tiles.size() * bodyType.getWidth() - halfWidth,
                tiles.get(0).size() * bodyType.getHeight() - halfHeight);
    }

    // public Tile getTile(final int x, final int y)
    // {
    // if(x >= tiles.size() || x < 0 || y < 0)
    // return null;
    //
    // final List<Tile> list = tiles.get(x);
    // if(y >= list.size())
    // return null;
    //
    // return list.get(y);
    // }

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
