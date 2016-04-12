package com.murder.game.level;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.World;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.murder.game.constants.box2d.BodyType;
import com.murder.game.drawing.Drawable;
import com.murder.game.drawing.manager.TextureManager;
import com.murder.game.serialize.MyVector2;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({ @Type(value = Tile.class, name = "Tile"), @Type(value = Door.class, name = "Door") })
public class Tile extends Drawable
{
    private static final String BODY_TYPE = "bodyType";
    private static final String POSITION = "position";
    private static final String ROTATION = "rotation";

    // public enum TileType
    // {
    // WALL(TextureConstants.WALL_TILE, true, true),
    // FLOOR(TextureConstants.FLOOR_TILE, false, false),
    // EXIT(TextureConstants.EXIT_TILE, false, false),
    // DOOR_MAT(TextureConstants.DOOR_MAT_TILE, false, false),
    // DOOR(TextureConstants.DOOR_TILE, false, true),
    // NONE("None", true, true);
    //
    // private final String textureName;
    // private final boolean blocking;
    // private final boolean defaultLocking;
    //
    // TileType(final String textureName, final boolean blocking, final boolean
    // defaultLocking)
    // {
    // this.textureName = textureName;
    // this.blocking = blocking;
    // this.defaultLocking = defaultLocking;
    // }
    //
    // public String getTextureName()
    // {
    // return textureName;
    // }
    //
    // public boolean isBlocking()
    // {
    // return blocking;
    // }
    //
    // public boolean getDefaultLocking()
    // {
    // return defaultLocking;
    // }
    // }

    // private TileType tileType;
    // @JsonIgnore
    // private TextureAtlas textureAtlas;
    // @JsonIgnore
    // private boolean locked;

    @JsonCreator
    public Tile(@JsonProperty(BODY_TYPE) final BodyType bodyType, @JsonProperty(POSITION) final MyVector2 position,
            @JsonProperty(ROTATION) final float rotation)
    {
        super(bodyType, position, rotation);
        // this.locked = tileType.getDefaultLocking();
        // this.tileType = tileType;
        // sprite.setPosition(position.x, position.y);
    }

    public void init(final World physicsWorld, final TextureManager textureManager)
    {
        super.init(physicsWorld, textureManager);
        // this.textureAtlas = textureAtlas;
        // setTileType(tileType);
    }

    @Override
    public void draw(final SpriteBatch batch)
    {
        sprite.draw(batch);
        // if(tileType != TileType.NONE)
        // sprite.draw(batch);
    }

    @Override
    public void updateCurrent(final float dt)
    {}

    // public void setTileType(final TileType tileType)
    // {
    // this.tileType = tileType;
    // locked = tileType.getDefaultLocking();
    // if(textureAtlas != null && tileType != TileType.NONE)
    // GraphicsUtils.applyTextureRegion(sprite,
    // textureAtlas.findRegion(tileType.getTextureName()));
    // }
    //
    // public TileType getTileType()
    // {
    // return tileType;
    // }
    //
    // public Item getItem()
    // {
    // return getItem(false);
    // }
    //
    // public Item getItem(boolean removeItem)
    // {
    // final Item retItem = item;
    // if(removeItem)
    // item = null;
    //
    // return retItem;
    // }
    //
    // @JsonIgnore
    // public InventoryItem getUnlockingItem()
    // {
    // return InventoryItem.GREEN_KEY;
    // }
    //
    // public void setLock(final boolean lock)
    // {
    // this.locked = lock;
    // }
    //
    // public boolean isLocked()
    // {
    // return tileType.isBlocking() || locked;
    // }
}