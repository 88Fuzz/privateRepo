package com.murder.game.level;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.World;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.murder.game.constants.box2d.BodyType;
import com.murder.game.drawing.drawables.Drawable;
import com.murder.game.drawing.drawables.Mob;
import com.murder.game.level.pathfinder.PathFinderState;
import com.murder.game.serialize.MyVector2;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({ @Type(value = Tile.class, name = "Tile"), @Type(value = Door.class, name = "Door"), @Type(value = Exit.class, name = "Exit") })
public class Tile extends Drawable
{
    protected static final String BODY_TYPE = "bodyType";
    protected static final String POSITION = "position";
    protected static final String ROTATION = "rotation";

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

    // The following are variables for pathfinding
    private Map<String, Float> distanceToStart;
    private Map<String, Float> distanceToEnd;
    private Map<String, PathFinderState> pathFinderState;
    private Map<String, Tile> parentTile;
    private Map<String, Tile> childTile;
    private List<Mob> mobs;

    @JsonCreator
    public Tile(@JsonProperty(BODY_TYPE) final BodyType bodyType, @JsonProperty(POSITION) final MyVector2 position,
            @JsonProperty(ROTATION) final float rotation)
    {
        super(bodyType, position, rotation);
        distanceToStart = new HashMap<String, Float>();
        distanceToEnd = new HashMap<String, Float>();
        pathFinderState = new HashMap<String, PathFinderState>();
        parentTile = new HashMap<String, Tile>();
        childTile = new HashMap<String, Tile>();
        // this.locked = tileType.getDefaultLocking();
        // this.tileType = tileType;
        // sprite.setPosition(position.x, position.y);
    }

    public void init(final World physicsWorld, final List<Mob> mobs)
    {
        super.init(physicsWorld);

        this.mobs = mobs;
        distanceToStart.clear();
        distanceToEnd.clear();
        pathFinderState.clear();
        parentTile.clear();
        childTile.clear();
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

    /**
     * Set the G value for a tile's key. The key allows for multiple paths to be
     * calculated on a single level (ie multiple enemies finding a path to the
     * player)
     * 
     * @param pathKey
     * @param value
     */
    public void setDistanceToStart(final String pathKey, final float value)
    {
        distanceToStart.put(pathKey, value);
    }

    public float getDistanceToStart(final String pathKey)
    {
        final Float value = distanceToStart.get(pathKey);
        return (value == null) ? Float.MAX_VALUE : value;
    }

    /**
     * Set the H value for a tile's key. The key allows for multiple paths to be
     * calculated on a single level (ie multiple enemies finding a path to the
     * player)
     * 
     * @param pathKey
     * @param value
     */
    public void setDistanceToEnd(final String pathKey, final float value)
    {
        distanceToEnd.put(pathKey, value);
    }

    public float getDistanceToEnd(final String pathKey)
    {
        final Float value = distanceToEnd.get(pathKey);
        return (value == null) ? Float.MAX_VALUE : value;
    }

    /**
     * Set the F value (typically H + G) for a tile's key. The key allows for
     * multiple paths to be calculated on a single level (ie multiple enemies
     * finding a path to the player)
     * 
     * @param pathKey
     * @return
     */
    public float getFValue(final String pathKey)
    {
        // TODO, constantly creating floats bad??
        final Float startValue = distanceToStart.get(pathKey);
        if(startValue == null)
            return Float.MAX_VALUE;

        final Float endValue = distanceToEnd.get(pathKey);
        if(endValue == null)
            return Float.MAX_VALUE;

        return startValue + endValue;
    }

    /**
     * Sets the state value for a tile for a given pathfinding key. The key
     * allows for multiple paths to be calculated on a single level (ie multiple
     * enemies finding a path to the player)
     * 
     * @param pathKey
     * @param state
     */
    public void setPathFinderState(final String pathKey, final PathFinderState state)
    {
        if(state == PathFinderState.OPEN)
        {
            sprite.setColor(Color.BLUE);
        }
        else if(state == PathFinderState.CLOSED)
        {
            sprite.setColor(Color.RED);
        }
        else
        {
            sprite.setColor(Color.VIOLET);

        }
        pathFinderState.put(pathKey, state);
    }

    public PathFinderState getPathFinderState(final String pathKey)
    {
        final PathFinderState value = pathFinderState.get(pathKey);

        return (value == null) ? PathFinderState.NONE : value;
    }

    /**
     * Sets the parent Tile for for a given pathfinding key. The key allows for
     * multiple paths to be calculated on a single level (ie multiple enemies
     * finding a path to the player)
     * 
     * @param pathKey
     * @param tile
     */
    public void setParentTile(final String pathKey, final Tile tile)
    {
        parentTile.put(pathKey, tile);
    }

    public Tile getParentTile(final String pathKey)
    {
        return parentTile.get(pathKey);
    }

    /**
     * Sets the child Tile for for a given pathfinding key. The key allows for
     * multiple paths to be calculated on a single level (ie multiple enemies
     * finding a path to the player)
     * 
     * @param pathKey
     * @param tile
     */
    public void setChildTile(final String pathKey, final Tile tile)
    {
        childTile.put(pathKey, tile);
    }

    public Tile getChildTile(final String pathKey)
    {
        return childTile.get(pathKey);
    }

    // TODO this is stupid and should be deleted.
    public void setColor()
    {
        sprite.setColor(Color.CHARTREUSE);
    }

    public void clearPathInformation(final String pathKey)
    {
        childTile.remove(pathKey);
        parentTile.remove(pathKey);

        distanceToStart.remove(pathKey);
        distanceToEnd.remove(pathKey);
        pathFinderState.remove(pathKey);
    }

    protected void updateMobs()
    {
        for(final Mob mob: mobs)
        {
            mob.findPath();
        }
    }
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