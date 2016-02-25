package com.murder.game.drawing;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.murder.game.constants.TextureConstants;
import com.murder.game.drawing.Item.InventoryItem;
import com.murder.game.level.Level;
import com.murder.game.level.Tile;
import com.murder.game.level.Tile.TileType;

public class Actor extends Drawable
{
    private static final List<Vector2> TESTING_EDGES = new LinkedList<Vector2>();
    private static final int SPRITE_SIZE = 200;
    private static final int CIRCLE_RADIUS = (int) (SPRITE_SIZE / 2.1);
    private static final float MAX_VELOCITY = 460;
    private static final float SQRT_TWO = 1.41421356237f;
    private static final float SPIN = 45;

    static
    {
        TESTING_EDGES.add(new Vector2(0, CIRCLE_RADIUS));
        TESTING_EDGES.add(new Vector2(0, -1 * CIRCLE_RADIUS));
        TESTING_EDGES.add(new Vector2(CIRCLE_RADIUS, 0));
        TESTING_EDGES.add(new Vector2(-1 * CIRCLE_RADIUS, 0));
        TESTING_EDGES.add(new Vector2(CIRCLE_RADIUS / SQRT_TWO, CIRCLE_RADIUS / SQRT_TWO));
        TESTING_EDGES.add(new Vector2(-1 * CIRCLE_RADIUS / SQRT_TWO, CIRCLE_RADIUS / SQRT_TWO));
        TESTING_EDGES.add(new Vector2(CIRCLE_RADIUS / SQRT_TWO, -1 * CIRCLE_RADIUS / SQRT_TWO));
        TESTING_EDGES.add(new Vector2(-1 * CIRCLE_RADIUS / SQRT_TWO, -1 * CIRCLE_RADIUS / SQRT_TWO));
    }

    public enum Direction
    {
        UP,
        DOWN,
        LEFT,
        RIGHT;
    }

    private Set<InventoryItem> inventory;
    private float velocity;
    private float rotation;
    private Vector2 velocityVector;
    private Level level;
    private Flashlight flashlight;

    public Actor(final Vector2 position, final float rotation)
    {
        this.position = position;
        this.rotation = rotation;
        flashlight = new Flashlight();
        inventory = new HashSet<InventoryItem>();
        tilePosition = new Vector2();
        velocityVector = new Vector2();
        velocity = MAX_VELOCITY;
    }

    public void init(final TextureAtlas textureAtlas, final Level level)
    {
        sprite = new Sprite(textureAtlas.findRegion(TextureConstants.CIRCLE_TEXTURE));
        sprite.setOriginCenter();
        flashlight.init(textureAtlas, SPRITE_SIZE);
        this.level = level;
        centerSpritePosition();
        setTilePosition();
    }

    @Override
    public void draw(final SpriteBatch batch, final Matrix4 matrix)
    {
        sprite.draw(batch);
        flashlight.draw(batch);
    }

    @Override
    public void update(final float dt)
    {
        final Vector2 directionalVelocity = new Vector2();
        if(rotation == 0)
        {
            final float mag = velocityVector.len();
            if(mag != 0)
            {
                directionalVelocity.x = velocity * velocityVector.x / mag;
                directionalVelocity.y = velocity * velocityVector.y / mag;
            }
        }
        else
        {
            directionalVelocity.x = (float) (velocity * Math.sin(rotation));
            directionalVelocity.y = (float) (velocity * Math.cos(rotation));
        }

        final Vector2 newPos = position.cpy();
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
            final Item item = tile.getItem();
            if(item != null)
            {
                inventory.add(item.getInventoryItem());
            }
        }

        flashlight.update(level, position, rotation);
    }

    public void moveDirection(final Direction direction)
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

    public void stopMoveDirection(final Direction direction)
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
        rotation += direction * SPIN;
    }

    private boolean checkNewPosition(final Vector2 newPosition)
    {
        final Vector2 testPos = new Vector2();
        final Vector2 testTilePos = new Vector2();
        for(final Vector2 edge: TESTING_EDGES)
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

    public Vector2 getTilePosition()
    {
        return tilePosition.cpy();
    }

    public Vector2 getPosition()
    {
        return new Vector2(position.x + sprite.getWidth() / 2, position.y + sprite.getHeight() / 2);
    }

    public Flashlight getFlashlight()
    {
        return flashlight;
    }

    private boolean isTileValid(final Vector2 tilePos)
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