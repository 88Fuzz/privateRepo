package com.murder.game.level;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.murder.game.constants.TextureConstants;
import com.murder.game.drawing.Drawable;
import com.murder.game.level.Item.InventoryItem;
import com.murder.game.utils.GraphicsUtils;

public class Tile extends Drawable
{
    private static final String TILE_TYPE = "tileType";
    private static final String POSITION = "position";
    private static final String ITEM = "item";

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

    private TileType tileType;
    private Item item;
    @JsonIgnore
    private TextureAtlas textureAtlas;
    @JsonIgnore
    private boolean locked;

    @JsonCreator
    public Tile(@JsonProperty(TILE_TYPE) final TileType tileType, @JsonProperty(POSITION) final Vector2 position,
            @JsonProperty(ITEM) final Item item)
    {
        this.position.set(position);
        this.sprite = new Sprite();
        this.item = item;
        this.locked = tileType.getDefaultLocking();
        this.tileType = tileType;
        sprite.setPosition(position.x, position.y);
    }

    public void init(final TextureAtlas textureAtlas)
    {
        this.textureAtlas = textureAtlas;
        setTileType(tileType);

        if(item != null)
        {
            item.init(textureAtlas);
        }
    }

    @Override
    public void draw(final SpriteBatch batch)
    {
        if(tileType != TileType.NONE)
            sprite.draw(batch);

        if(item != null)
            item.draw(batch);
    }

    @Override
    public void update(final float dt)
    {}

    public void setTileType(final TileType tileType)
    {
        this.tileType = tileType;
        if(textureAtlas != null && tileType != TileType.NONE)
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

    @JsonIgnore
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