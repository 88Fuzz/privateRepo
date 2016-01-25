package com.squared.space.game.drawing;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class Actor implements Drawable
{
    private static final float MOVEMENT_SPEED = 174;
    private Sprite sprite;
    private final Vector2 position;
    private final Vector2 velocity;

    public Actor()
    {
        position = new Vector2();
        velocity = new Vector2();
    }

    public void init(final Vector2 position, final Sprite sprite)
    {
        sprite.setPosition(position.x, position.y);
        this.position.x = position.x;
        this.position.y = position.y;
        this.sprite = sprite;
    }

    public void startMoveRight()
    {
        velocity.x += MOVEMENT_SPEED;
    }

    public void stopMoveRight()
    {
        velocity.x -= MOVEMENT_SPEED;
    }

    public void startMoveLeft()
    {
        velocity.x -= MOVEMENT_SPEED;
    }

    public void stopMoveLeft()
    {
        velocity.x += MOVEMENT_SPEED;
    }
    
    public Vector2 getPosition()
    {
        return position;
    }

    @Override
    public void draw(final SpriteBatch batch)
    {
        sprite.draw(batch);
    }

    @Override
    public void update(final float dt)
    {
        position.x += velocity.x * dt;
        position.y += velocity.y * dt;

        sprite.setPosition(position.x, position.y);
    }
}