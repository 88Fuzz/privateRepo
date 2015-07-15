package com.circleboy.moveable;

import java.util.LinkedList;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.circleboy.event.AbstractDrawableEvent;

public class Moveable
{
    private final Vector2 pos;
    private final Sprite sprite;
    private float baseMovement;
    private final LinkedList<AbstractDrawableEvent> events;

    public Moveable(final float x, final float y, final Sprite sprite, final float baseMovement)
    {
        events = new LinkedList<AbstractDrawableEvent>();
        pos = new Vector2(x, y);
        sprite.setPosition(pos.x, pos.y);
        this.sprite = sprite;
        this.baseMovement = baseMovement;
    }

    public void update(final Moveable circle, final float dt, final float movementFactor)
    {
        pos.x += baseMovement * movementFactor * dt;
        sprite.setPosition(pos.x, pos.y);

        processEvents(circle);
    }

    public void draw(SpriteBatch batch)
    {
        sprite.draw(batch);
    }

    private void processEvents(final Moveable circle)
    {
        if(events.size() == 0)
            return;

        AbstractDrawableEvent event = events.getFirst();
        while(event != null)
        {
            if(event.checkEvent(circle, this))
            {
                events.poll();
                if(events.size() == 0)
                    return;

                event = events.getFirst();
            }else
            {
                event = null;
            }
        }
    }

    public void setPosition(float x, float y)
    {
        pos.x = x;
        pos.y = y;
        sprite.setPosition(x, y);
    }

    public void setBaseMovement(final float movement)
    {
        baseMovement = movement;
    }

    public Vector2 getPosition()
    {
        return pos;
    }

    public float getWidth()
    {
        return sprite.getWidth();
    }

    public Sprite getSprite()
    {
        return sprite;
    }

    public void addEventList(LinkedList<AbstractDrawableEvent> newEvents)
    {
        events.addAll(newEvents);
    }
}