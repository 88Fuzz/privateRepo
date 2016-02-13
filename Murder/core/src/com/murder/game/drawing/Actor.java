package com.murder.game.drawing;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.murder.game.constants.TextureConstants;
import com.murder.game.level.Level;
import com.murder.game.level.Tile;

public class Actor implements Drawable
{
    private static final float MAX_VELOCITY = 460;
    private static final float SQRT_TWO = 1.41421356237f;

    public enum Direction
    {
        UP,
        DOWN,
        LEFT,
        RIGHT;
    }

    private Sprite sprite;
    private Vector2 position;
    private Vector2 tilePosition;
    private float velocity;
    private float rotation;
    private Vector2 velocityVector;
    private Level level;

    public Actor(final Vector2 position, final float rotation)
    {
        this.position = position;
        this.rotation = rotation;
        tilePosition = new Vector2();
        velocityVector = new Vector2();
        velocity = MAX_VELOCITY;
    }

    public void init(final TextureAtlas textureAtlas, final Level level)
    {
        sprite = new Sprite(textureAtlas.findRegion(TextureConstants.CIRCLE_TEXTURE));
        sprite.setOriginCenter();
        this.level = level;
        setSpritePosition();
        setTilePosition();
    }

    @Override
    public void draw(final SpriteBatch batch)
    {
        sprite.draw(batch);
    }

    @Override
    public void update(final float dt)
    {
        final Vector2 directionalVelocity = new Vector2();

        if(rotation == 0)
        {
            final float mag = velocityVector.len();
            if(mag == 0)
                return;

            directionalVelocity.x = velocity * velocityVector.x / mag;
            directionalVelocity.y = velocity * velocityVector.y / mag;
        }
        else
        {
            directionalVelocity.x = (float) (velocity * Math.sin(rotation));
            directionalVelocity.y = (float) (velocity * Math.cos(rotation));
        }

        final Vector2 newPos = position.cpy();
        newPos.x = position.x + directionalVelocity.x * dt;
        newPos.y = position.y + directionalVelocity.y * dt;

        Vector2 newTilePos = new Vector2();
        // Assume the actor is a circle.
        if(directionalVelocity.y != 0)
        {
            // TODO These can be structured a lot better. YO
            // TODO something you should try now is ALWAYS move the player. 
            // Once the movement is done, check the 8? corners of the circle to see if they are in a wall.
            // If in a wall, move the player out of the wall
            if(directionalVelocity.y > 0)
            {
                newTilePos.x = (int) (newPos.x / sprite.getWidth());
                newTilePos.y = (int) ((newPos.y + (sprite.getHeight() / 2)) / sprite.getHeight());
                if(!isTileValid(newTilePos))
                {
                    //TODO this should actually set the player to the edge of the wall and not the original player position
                    newPos.y = position.y;
                }
                else
                {
                    final float originalXDiagonal = (float) (newPos.x + 0.5 * sprite.getWidth() / 2 * SQRT_TWO);

                    newTilePos.y = (int) ((newPos.y + 0.5 * sprite.getHeight() / 2 * SQRT_TWO) / sprite.getHeight());
                    newTilePos.x = (int) (originalXDiagonal / sprite.getWidth());

                    if(!isTileValid(newTilePos))
                    {
                        newPos.y = position.y;
                    }
                    else
                    {
                        newTilePos.x = (int) ((originalXDiagonal - 0.5 * sprite.getWidth()) / sprite.getWidth());

                        if(!isTileValid(newTilePos))
                        {
                            newPos.y = position.y;
                        }
                    }
                }
            }
            else
            {
                newTilePos.x = (int) (newPos.x / sprite.getWidth());
                newTilePos.y = (int) ((newPos.y - (sprite.getHeight() / 2)) / sprite.getHeight());
                if(!isTileValid(newTilePos))
                {
                    newPos.y = position.y;
                }
                else
                {
                    final float originalXDiagonal = (float) (newPos.x + 0.5 * sprite.getWidth() / 2 * SQRT_TWO);
                    newTilePos.y = (int) ((newPos.y - 0.5 * sprite.getHeight() / 2 * SQRT_TWO) / sprite.getHeight());
                    newTilePos.x = (int) (originalXDiagonal / sprite.getWidth());

                    if(!isTileValid(newTilePos))
                    {
                        newPos.y = position.y;
                    }
                    else
                    {
                        newTilePos.x = (int) ((originalXDiagonal - 0.5 * sprite.getWidth()) / sprite.getWidth());

                        if(!isTileValid(newTilePos))
                        {
                            newPos.y = position.y;
                        }
                    }
                }
            }
        }

        if(directionalVelocity.x != 0)
        {
            if(directionalVelocity.x > 0)
            {
                newTilePos.x = (int) ((newPos.x + (sprite.getWidth() / 2)) / sprite.getWidth());
                newTilePos.y = (int) (newPos.y / sprite.getHeight());
                if(!isTileValid(newTilePos))
                {
                    newPos.x = position.x;
                }
                else
                {
                    final float originalYDiagonal = (float) (newPos.y + 0.5 * sprite.getHeight() / 2 * SQRT_TWO);
                    newTilePos.y = (int) (originalYDiagonal / sprite.getHeight());
                    newTilePos.x = (int) ((newPos.x + 0.5 * sprite.getWidth() / 2 * SQRT_TWO) / sprite.getWidth());

                    if(!isTileValid(newTilePos))
                    {
                        newPos.x = position.x;
                    }
                    else
                    {
                        newTilePos.y = (int) ((originalYDiagonal - 0.5 * sprite.getHeight()) / sprite.getHeight());

                        if(!isTileValid(newTilePos))
                        {
                            newPos.x = position.x;
                        }
                    }
                }
            }
            else
            {
                newTilePos.x = (int) ((newPos.x - (sprite.getWidth() / 2)) / sprite.getWidth());
                newTilePos.y = (int) (newPos.y / sprite.getHeight());
                if(!isTileValid(newTilePos))
                {
                    newPos.x = position.x;
                }
                else
                {
                    final float originalYDiagonal = (float) (newPos.y + 0.5 * sprite.getHeight() / 2 * SQRT_TWO);
                    newTilePos.y = (int) (originalYDiagonal / sprite.getHeight());
                    newTilePos.x = (int) ((newPos.x - 0.5 * sprite.getWidth() / 2 * SQRT_TWO) / sprite.getWidth());

                    if(!isTileValid(newTilePos))
                    {
                        newPos.x = position.x;
                    }
                    else
                    {
                        newTilePos.y = (int) ((originalYDiagonal - 0.5 * sprite.getHeight()) / sprite.getHeight());

                        if(!isTileValid(newTilePos))
                        {
                            newPos.x = position.x;
                        }
                    }
                }
            }
        }

        position.x = newPos.x;
        position.y = newPos.y;

        setTilePosition();
        setSpritePosition();
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

    public Vector2 getTilePosition()
    {
        return tilePosition.cpy();
    }

    public Vector2 getPosition()
    {
        return new Vector2(position.x + sprite.getWidth() / 2, position.y + sprite.getHeight() / 2);
    }

    private void setTilePosition()
    {
        tilePosition.x = (int) (position.x / sprite.getWidth());
        tilePosition.y = (int) (position.y / sprite.getHeight());
    }

    private void setSpritePosition()
    {
        sprite.setPosition(position.x - sprite.getWidth() / 2, position.y - sprite.getHeight() / 2);
    }

    private boolean isTileValid(final Vector2 tilePos)
    {
        final Tile tile = level.getTile((int) tilePos.x, (int) tilePos.y);
        return isValidMove(tile);
    }

    private boolean isValidMove(final Tile tile)
    {
        if(tile == null || tile.getTileType().isBlocking())
            return false;
        return true;
    }
}