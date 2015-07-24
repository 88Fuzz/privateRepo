package com.circleboy.moveable;

import java.util.LinkedList;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.circleboy.event.abstracts.AbstractDrawableEvent;
import com.circleboy.util.ScalingUtil;

public class Moveable
{
    protected final Vector2 pos;
    protected final Sprite sprite;
    private float baseScreenMovement;
    private float baseMovement;
    private final LinkedList<AbstractDrawableEvent> events;

    public Moveable(final float x, final float y, final Sprite sprite, final float baseScreenMovement,
            final float baseMovement)
    {
        events = new LinkedList<AbstractDrawableEvent>();
        final Vector2 scale = ScalingUtil.getScale();
        sprite.setScale(sprite.getScaleX() * scale.x, sprite.getScaleY() * scale.y);

        Rectangle rect = sprite.getBoundingRectangle();

        pos = new Vector2(-1 * rect.x + x, -1 * rect.y + y);
        sprite.setPosition(pos.x, pos.y);

        this.sprite = sprite;
        this.baseScreenMovement = baseScreenMovement;
        this.baseMovement = baseMovement;
    }

    public void update(final Moveable circle, final float dt, final float movementFactor)
    {
        float movement = (baseScreenMovement * movementFactor + baseMovement) * dt;
        pos.x += movement;
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

    public void setBaseScreenMovement(final float movement)
    {
        baseScreenMovement = movement;
    }

    public void setBaseMovement(final float movement)
    {
        baseMovement = movement;
    }

    public Vector2 getPosition()
    {
        return pos;
    }

    public float getHeight()
    {
        return sprite.getBoundingRectangle().height;
    }

    public float getWidth()
    {
        return sprite.getBoundingRectangle().width;
    }

    /*
     * Used to get the original size of a sprite's texture. Scaling changes the
     * texture's width found by getWidth().
     */
    public float getOriginalSpriteWidth()
    {
        return sprite.getWidth();
    }

    /*
     * Used to get the original size of a sprite's texture. Scaling changes the
     * texture's height found by getHeight().
     */
    public float getOriginalSpriteHeight()
    {
        return sprite.getHeight();
    }

    public Sprite getSprite()
    {
        return sprite;
    }

    public void addEventList(LinkedList<AbstractDrawableEvent> newEvents)
    {
        events.addAll(newEvents);
    }

    public void setText(final String text)
    {
        //Do nothing
    }
}