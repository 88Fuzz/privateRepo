package com.murder.game.level;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.murder.game.constants.TextureConstants;
import com.murder.game.drawing.Drawable;
import com.murder.game.drawing.Item;
import com.murder.game.drawing.Item.InventoryItem;
import com.murder.game.utils.GraphicsUtils;

public class Tile extends Drawable
{
    public enum TileType
    {
        WALL(TextureConstants.WALL_TILE, true, true),
        FLOOR(TextureConstants.FLOOR_TILE, false, false),
        EXIT(TextureConstants.EXIT_TILE, false, false),
        DOOR(TextureConstants.DOOR_TILE, false, true),
        NONE("None", true, true);

        private final String textureName;
        private final boolean blocking;
        private final boolean defaultLocking;

        TileType(final String textureName, final boolean blocking, final boolean defaultLocking)
        {
            this.textureName = textureName;
            this.blocking = blocking;
            this.defaultLocking = defaultLocking;
        }

        public String getTextureName()
        {
            return textureName;
        }

        public boolean isBlocking()
        {
            return blocking;
        }

        public boolean getDefaultLocking()
        {
            return defaultLocking;
        }
    }

    private TextureAtlas textureAtlas;
    private String roomId;
    private TileType tileType;
    private Item item;
    private boolean locked;

    public Tile(final TextureAtlas textureAtlas, final TileType tileType, final Vector2 position, final String roomId,
            final Item item)
    {
        this.roomId = roomId;
        this.position.set(position);
        this.textureAtlas = textureAtlas;
        this.sprite = new Sprite();
        this.item = item;
        this.locked = tileType.getDefaultLocking();
        setTileType(tileType);
        sprite.setPosition(position.x, position.y);
        if(item != null)
        {
            item.init(textureAtlas);
        }
    }

    @Override
    public void draw(final SpriteBatch batch, final Matrix4 matrix)
    {
        if(tileType != TileType.NONE)
            sprite.draw(batch);

        if(item != null)
            item.draw(batch, matrix);
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

    public Item getItem()
    {
        final Item retItem = item;
        item = null;
        return retItem;
    }

    public InventoryItem getUnlockingItem()
    {
        return InventoryItem.GREEN_KEY;
    }

    public void setLock(final boolean lock)
    {
        this.locked = lock;
    }

    public boolean isLocked()
    {
        return tileType.isBlocking() || locked;
    }
}