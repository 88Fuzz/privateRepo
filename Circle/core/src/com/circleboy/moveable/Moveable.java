package com.circleboy.moveable;

import java.util.LinkedList;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.circleboy.event.AbstractDrawableEvent;
import com.circleboy.util.ScalingUtil;

public class Moveable
{
    private final Vector2 pos;
    private final Sprite sprite;
    private float baseScreenMovement;
    private float baseMovement;
    private final LinkedList<AbstractDrawableEvent> events;
    private final BitmapFont font;

    public Moveable(final float x, final float y, final Sprite sprite, final float baseScreenMovement,
            final float baseMovement)
    {
        events = new LinkedList<AbstractDrawableEvent>();
        final Vector2 scale = ScalingUtil.getScale();
        sprite.setScale(scale.x, scale.y);

        Rectangle rect = sprite.getBoundingRectangle();

        pos = new Vector2(-1 * rect.x + x, -1 * rect.y + y);
//        pos = new Vector2(x, y);
        System.out.println("Original pos " + x + " " + y);
        System.out.println("\tSetting position to " + pos);
        System.out.println("\trectangle shows " + rect);
        sprite.setPosition(pos.x, pos.y);

        this.sprite = sprite;
        this.baseScreenMovement = baseScreenMovement;
        this.baseMovement = baseMovement;
        font = new BitmapFont();
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
        font.draw(batch, "butt", pos.x, pos.y + sprite.getHeight());
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
     * Used to get the original size of a sprite's texture. Scaling changes the texture's width found by getWidth().
     */
    public float getOriginalSpriteWidth()
    {
        return sprite.getWidth();
    }

    /*
     * Used to get the original size of a sprite's texture. Scaling changes the texture's height found by getHeight().
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
}