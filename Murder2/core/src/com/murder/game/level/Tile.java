package com.murder.game.level;

import java.util.List;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.World;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.murder.game.constants.box2d.BodyType;
import com.murder.game.drawing.drawables.DrawPosition;
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

    private DrawPosition drawPosition;
    private List<Mob> mobs;

    @JsonCreator
    public Tile(@JsonProperty(BODY_TYPE) final BodyType bodyType, @JsonProperty(POSITION) final MyVector2 position,
            @JsonProperty(ROTATION) final float rotation)
    {
        this(bodyType, position, rotation, DrawPosition.FLOOR);
    }

    public Tile(final BodyType bodyType, final MyVector2 position, final float rotation, final DrawPosition drawPosition)
    {
        super(bodyType, position, rotation);

        this.drawPosition = drawPosition;
    }

    public void init(final World physicsWorld, final List<Mob> mobs)
    {
        super.init(physicsWorld);

        this.mobs = mobs;
    }

    @Override
    public void draw(final SpriteBatch batch)
    {
        sprite.draw(batch);
    }

    @Override
    public void updateCurrent(final float dt)
    {}

    /**
     * Sets the state value for a tile for a given pathfinding key. The key
     * allows for multiple paths to be calculated on a single level (ie multiple
     * enemies finding a path to the player)
     * 
     * @param pathKey
     * @param state
     */
    // TODO remove this method as well
    public void setPathFinderState(final PathFinderState state)
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
    }

    // TODO this is stupid and should be deleted.
    public void setColor()
    {
        sprite.setColor(Color.CHARTREUSE);
    }

    protected void updateMobs()
    {
        for(final Mob mob: mobs)
        {
            mob.findPath();
        }
    }

    @Override
    public DrawPosition getDrawPosition()
    {
        return drawPosition;
    }
}
