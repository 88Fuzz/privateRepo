package com.murder.game.drawing;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.murder.game.constants.TextureConstants;
import com.murder.game.drawing.flashlight.Flashlight;
import com.murder.game.level.Item;
import com.murder.game.level.Level;
import com.murder.game.level.Tile;
import com.murder.game.level.Item.InventoryItem;
import com.murder.game.level.Tile.TileType;
import com.murder.game.state.serial.MyVector2;
import com.murder.game.utils.RotationUtils;

public class Actor extends Drawable
{
    private static final String MOVE = "move";
    private static final String POSITION = "position";
    private static final String ROTATION = "rotation";

    private static final List<MyVector2> TESTING_EDGES = new LinkedList<MyVector2>();
    private static final int SPRITE_SIZE = 200;
    private static final int CIRCLE_RADIUS = (int) (SPRITE_SIZE / 2.1);
    private static final float MAX_VELOCITY = 460 / 2;
    private static final float SQRT_TWO = 1.41421356237f;
    private static final float SPIN = 45;

    static
    {
        TESTING_EDGES.add(new MyVector2(0, CIRCLE_RADIUS));
        TESTING_EDGES.add(new MyVector2(0, -1 * CIRCLE_RADIUS));
        TESTING_EDGES.add(new MyVector2(CIRCLE_RADIUS, 0));
        TESTING_EDGES.add(new MyVector2(-1 * CIRCLE_RADIUS, 0));
        TESTING_EDGES.add(new MyVector2(CIRCLE_RADIUS / SQRT_TWO, CIRCLE_RADIUS / SQRT_TWO));
        TESTING_EDGES.add(new MyVector2(-1 * CIRCLE_RADIUS / SQRT_TWO, CIRCLE_RADIUS / SQRT_TWO));
        TESTING_EDGES.add(new MyVector2(CIRCLE_RADIUS / SQRT_TWO, -1 * CIRCLE_RADIUS / SQRT_TWO));
        TESTING_EDGES.add(new MyVector2(-1 * CIRCLE_RADIUS / SQRT_TWO, -1 * CIRCLE_RADIUS / SQRT_TWO));
    }

    public enum MoveDirection
    {
        UP,
        DOWN,
        LEFT,
        RIGHT;
    }

    private float rotation;
    private boolean move;
    @JsonIgnore
    private Set<InventoryItem> inventory;
    @JsonIgnore
    private float velocity;
    @JsonIgnore
    private MyVector2 velocityVector;
    @JsonIgnore
    private Level level;
    @JsonIgnore
    private Flashlight flashlight;

    @JsonCreator
    public Actor(@JsonProperty(POSITION) final MyVector2 position, @JsonProperty(ROTATION) final float rotation,
            @JsonProperty(MOVE) final boolean move)
    {
        this.move = move;
        this.position = position;
        this.rotation = rotation;
        flashlight = new Flashlight();
        inventory = new HashSet<InventoryItem>();
        tilePosition = new MyVector2();
        velocityVector = new MyVector2();
        velocity = MAX_VELOCITY;
        this.position.x = 1665.018f;
        this.position.y = 295.00354f;
    }

    public Actor(final MyVector2 position, final float rotation)
    {
        this(position, rotation, false);
    }

    public void init(final TextureAtlas textureAtlas, final Level level)
    {
        sprite = new Sprite(textureAtlas.findRegion(TextureConstants.CIRCLE_TEXTURE));
        sprite.setOriginCenter();
        this.level = level;
        centerSpritePosition();
        setTilePosition();
        flashlight.init(level, position, SPRITE_SIZE, rotation);
    }

    @Override
    public void draw(final SpriteBatch batch)
    {
        sprite.draw(batch);
    }

    @Override
    public void update(final float dt)
    {
        final MyVector2 directionalVelocity = new MyVector2();
        float distanceTraveled = 0;
        if(move)
        {
            directionalVelocity.x = (float) (velocity * Math.sin(Math.toRadians(rotation)));
            directionalVelocity.y = (float) (velocity * Math.cos(Math.toRadians(rotation)));
            distanceTraveled = velocity * dt;
        }
        else
        {
            final float mag = velocityVector.len();
            if(mag != 0)
            {
                directionalVelocity.x = velocity * velocityVector.x / mag;
                directionalVelocity.y = velocity * velocityVector.y / mag;
            }
        }

        final MyVector2 newPos = position.cpy();
        // Check x and y independently in case player is moving up against a
        // wall and with a wall.
        if(directionalVelocity.x != 0)
        {
            newPos.x = position.x + directionalVelocity.x * dt;
            if(!checkNewPosition(newPos))
            {
                newPos.x = position.x;
            }
        }

        if(directionalVelocity.y != 0)
        {
            newPos.y = position.y + directionalVelocity.y * dt;
            if(!checkNewPosition(newPos))
            {
                newPos.y = position.y;
            }
        }

        position.x = newPos.x;
        position.y = newPos.y;

        setTilePosition();
        centerSpritePosition();

        final Tile tile = level.getTile((int) tilePosition.x, (int) tilePosition.y);
        if(tile != null)
        {
            final Item item = tile.getItem(true);
            if(item != null)
            {
                inventory.add(item.getInventoryItem());
            }
        }

        flashlight.update(level, position, rotation, distanceTraveled);
    }

    public void moveDirection(final MoveDirection direction)
    {
        switch(direction)
        {
        case UP:
            velocityVector.y = 1;
            break;
        case DOWN:
            velocityVector.y = -1;
            break;
        case LEFT:
            velocityVector.x = -1;
            break;
        case RIGHT:
            velocityVector.x = 1;
            break;
        }
    }

    public void stopMoveDirection(final MoveDirection direction)
    {
        switch(direction)
        {
        case UP:
            if(velocityVector.y > 0)
                velocityVector.y = 0;
            break;
        case DOWN:
            if(velocityVector.y < 0)
                velocityVector.y = 0;
            break;
        case LEFT:
            if(velocityVector.x < 0)
                velocityVector.x = 0;
            break;
        case RIGHT:
            if(velocityVector.x > 0)
                velocityVector.x = 0;
            break;
        }
    }

    public void rotate(final int direction)
    {
        setRotation(rotation + direction * SPIN);
    }

    private boolean checkNewPosition(final MyVector2 newPosition)
    {
        final MyVector2 testPos = new MyVector2();
        final MyVector2 testTilePos = new MyVector2();
        for(final MyVector2 edge: TESTING_EDGES)
        {
            testPos.x = newPosition.x + edge.x;
            testPos.y = newPosition.y + edge.y;

            testTilePos.x = testPos.x / (SPRITE_SIZE);
            testTilePos.y = testPos.y / (SPRITE_SIZE);

            if(!isTileValid(testTilePos))
            {
                return false;
            }
        }
        return true;
    }

    public void startMove(final boolean move)
    {
        this.move = move;
    }

    public void setRotation(final float rotation)
    {
        final float tmpRotation = RotationUtils.adjustAngleAbout360(rotation);

        this.rotation = tmpRotation;
    }

    public float getRotation()
    {
        return rotation;
    }

    public MyVector2 getTilePosition()
    {
        return tilePosition.cpy();
    }

    public MyVector2 getPosition()
    {
        return new MyVector2(position.x - sprite.getWidth() / 2, position.y - sprite.getHeight() / 2);
    }

    @JsonIgnore
    public MyVector2 getCenterPosition()
    {
        return position.cpy();
    }

    public Flashlight getFlashlight()
    {
        return flashlight;
    }

    private boolean isTileValid(final MyVector2 tilePos)
    {
        final Tile tile = level.getTile((int) tilePos.x, (int) tilePos.y);
        return isValidMove(tile);
    }

    private boolean isValidMove(final Tile tile)
    {
        if(tile == null || tile.isLocked())
        {
            if(tile != null && tile.getTileType() == TileType.DOOR)
            {
                if(inventory.contains(tile.getUnlockingItem()))
                {
                    tile.setLock(false);
                    return true;
                }
            }

            return false;
        }
        return true;
    }
}