package com.murder.game.level;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.murder.game.constants.level.ItemType;
import com.murder.game.drawing.drawables.Actor;
import com.murder.game.drawing.drawables.DrawPosition;
import com.murder.game.drawing.drawables.Drawable;
import com.murder.game.serialize.MyVector2;

public class Item extends Drawable
{
    private static final String ITEM_TYPE = "itemType";
    private static final String POSITION = "position";
    private static final String ROTATION = "rotation";

    private static final float SHRINK_RATE = 25;
    private static final float PERCENT_MOVEMENT_RATE = .8f;

    private ItemType itemType;
    @JsonIgnore
    private boolean pickedUp;
    private Actor target;

    @JsonCreator
    public Item(@JsonProperty(ITEM_TYPE) final ItemType itemType, @JsonProperty(POSITION) final MyVector2 position,
            @JsonProperty(ROTATION) final float rotation)
    {
        super(itemType.getBodyType(), position, rotation);
        this.itemType = itemType;
        this.pickedUp = false;
    }

    public void init(final World physicsWorld)
    {
        super.init(physicsWorld);
        this.pickedUp = false;
        this.target = null;
    }

    @Override
    public void draw(final SpriteBatch batch)
    {
        sprite.draw(batch);
    }

    @Override
    protected void updateCurrent(final float dt)
    {
        // Do nothing
        if(target != null)
        {
            float xScale = sprite.getScaleX();
            float yScale = sprite.getScaleY();

            xScale *= dt * SHRINK_RATE;
            yScale *= dt * SHRINK_RATE;

            if(xScale <= 0)
                xScale = 0;

            if(yScale <= 0)
                yScale = 0;

            sprite.setScale(xScale, yScale);
        }
    }

    @Override
    protected void adjustSprite(final Body body, final Sprite sprite)
    {
        if(target != null)
        {
            float xTarget = target.getPosition().x - sprite.getWidth() / 2;
            float yTarget = target.getPosition().y - sprite.getHeight() / 2;

            xTarget -= sprite.getX();
            yTarget -= sprite.getY();

            xTarget = getAdjustedValue(xTarget);
            yTarget = getAdjustedValue(yTarget);

            sprite.setPosition(sprite.getX() + xTarget, sprite.getY() + yTarget);
        }
        else
            super.adjustSprite(body, sprite);

    }

    private float getAdjustedValue(final float value)
    {
        final float maxValue = 20;
        float adjustedValue = value * PERCENT_MOVEMENT_RATE;
        if(Math.abs(adjustedValue) > maxValue)
        {
            if(adjustedValue < 0)
                return -1 * maxValue;

            return maxValue;
        }

        return adjustedValue;
    }

    public ItemType pickUpItem(final Actor actor)
    {
        if(actor.getClass().equals(Actor.class))
        {
            target = actor;
            pickedUp = true;
        }
        return itemType;
    }

    public boolean isPickedUp()
    {
        return pickedUp;
    }

    public ItemType getItemType()
    {
        return itemType;
    }

    @Override
    public DrawPosition getDrawPosition()
    {
        return DrawPosition.ITEMS;
    }
}