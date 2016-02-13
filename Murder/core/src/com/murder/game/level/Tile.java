package com.murder.game.level;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.murder.game.constants.TextureConstants;
import com.murder.game.drawing.Drawable;
import com.murder.game.utils.GraphicsUtils;

public class Tile implements Drawable
{
    public enum TileType
    {
        WALL(TextureConstants.WALL_TILE, true),
        FLOOR(TextureConstants.FLOOR_TILE, false),
        DOOR(TextureConstants.DOOR_TILE, true),
        NONE("None", true);

        private final String textureName;
        private final boolean blocking;

        TileType(final String textureName, final boolean blocking)
        {
            this.textureName = textureName;
            this.blocking = blocking;
        }

        public String getTextureName()
        {
            return textureName;
        }

        public boolean isBlocking()
        {
            return blocking;
        }
    }

    private TextureAtlas textureAtlas;
    private String roomId;
    private TileType tileType;
    private Sprite sprite;
    private Vector2 position;

    public Tile(final TextureAtlas textureAtlas, final TileType tileType, final Vector2 position, final String roomId)
    {
        this.roomId = roomId;
        this.position = position;
        this.textureAtlas = textureAtlas;
        this.sprite = new Sprite();
        setTileType(tileType);
        sprite.setPosition(position.x, position.y);
    }

    @Override
    public void draw(final SpriteBatch batch)
    {
        if(tileType != TileType.NONE)
            sprite.draw(batch);
    }

    @Override
    public void update(final float dt)
    {}

    public String getRoomId()
    {
        return roomId;
    }

    public void setTileType(final TileType tileType)
    {
        this.tileType = tileType;
        if(tileType != TileType.NONE)
            GraphicsUtils.applyTextureRegion(sprite, textureAtlas.findRegion(tileType.getTextureName()));
    }

    public TileType getTileType()
    {
        return tileType;
    }
}