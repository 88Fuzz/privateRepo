package com.murder.game.drawing.drawables;

import java.util.HashSet;
import java.util.Set;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.murder.game.constants.box2d.BodyType;
import com.murder.game.constants.level.ItemType;
import com.murder.game.serialize.MyVector2;
import com.murder.game.utils.LightBuilder;

import box2dLight.RayHandler;

public class Actor extends Drawable
{
    private static final float SHRINK_RATE = .5f;
    private static final float MAX_VELOCITY = 660;
    private static final float MAX_SPRITE_UPDATE_TIMER = .2f;

    public enum MoveDirection
    {
        UP,
        DOWN,
        LEFT,
        RIGHT;
    }

    @JsonIgnore
    private boolean move;
    @JsonIgnore
    private Set<ItemType> inventory;
    @JsonIgnore
    protected float velocity;
    @JsonIgnore
    protected MyVector2 unitVelocityVector;
    @JsonIgnore
    private boolean onExit;
    @JsonIgnore
    private boolean mobTouched;
    @JsonIgnore
    protected float spriteUpdateTimer;
    protected Vector2 scale;

    @JsonCreator
    public Actor(@JsonProperty(BODY_TYPE) BodyType bodyType, @JsonProperty(POSITION) final MyVector2 position,
            @JsonProperty(ROTATION) final float rotation)
    {
        super(bodyType, position, rotation);
        this.move = false;
        this.position = position;
        this.inventory = new HashSet<ItemType>();
        unitVelocityVector = new MyVector2();
        velocity = MAX_VELOCITY;
        scale = new Vector2();
    }

    public void init(final World physicsWorld, final RayHandler rayHandler)
    {
        super.init(physicsWorld);
        createLight(rayHandler);
        inventory.clear();

        setSprite();
        spriteUpdateTimer = MAX_SPRITE_UPDATE_TIMER;
        onExit = false;
        mobTouched = false;
        scale.x = 1;
        scale.y = 1;
    }

    protected void setSprite()
    {
        sprite = new Sprite(bodyType.getTextureLoader().getAtlasRegion());
        spriteUpdateTimer = MAX_SPRITE_UPDATE_TIMER;
        sprite.setScale(scale.x, scale.y);
    }

    protected void createLight(final RayHandler rayHandler)
    {
        LightBuilder.createConeLight(rayHandler, body, new Color(1f, 1f, 1f, .84f), 30, body.getAngle());
    }

    @Override
    public void draw(final SpriteBatch batch)
    {
        sprite.draw(batch);
    }

    @Override
    public void updateCurrent(final float dt)
    {
        updateSpriteTimer(dt);

        if(mobTouched)
        {
            scale.x -= (dt * SHRINK_RATE);
            scale.y -= (dt * SHRINK_RATE);

            if(scale.x <= 0)
                scale.x = 0;

            if(scale.y <= 0)
                scale.y = 0;

            sprite.setScale(scale.x, scale.y);
            body.setLinearVelocity(0, 0);
        }
        else
        {
            float xVelocity = 0;
            float yVelocity = 0;
            if(move)
            {
                xVelocity = (float) (velocity * Math.sin(Math.toRadians(rotation)));
                yVelocity = (float) (velocity * Math.cos(Math.toRadians(rotation)));
            }
            else
            {
                final float mag = unitVelocityVector.len();
                if(mag != 0)
                {
                    xVelocity = velocity * unitVelocityVector.x / mag;
                    yVelocity = velocity * unitVelocityVector.y / mag;
                }
            }
            xVelocity *= dt;
            yVelocity *= dt;

            body.setLinearVelocity(xVelocity, yVelocity);
        }
    }

    private void updateSpriteTimer(final float dt)
    {
        spriteUpdateTimer -= dt;
        if(spriteUpdateTimer <= 0)
            setSprite();
    }

    public void moveDirection(final MoveDirection direction)
    {
        switch(direction)
        {
        case UP:
            unitVelocityVector.y = 1;
            break;
        case DOWN:
            unitVelocityVector.y = -1;
            break;
        case LEFT:
            unitVelocityVector.x = -1;
            break;
        case RIGHT:
            unitVelocityVector.x = 1;
            break;
        }
    }

    public void stopMoveDirection(final MoveDirection direction)
    {
        switch(direction)
        {
        case UP:
            if(unitVelocityVector.y > 0)
                unitVelocityVector.y = 0;
            break;
        case DOWN:
            if(unitVelocityVector.y < 0)
                unitVelocityVector.y = 0;
            break;
        case LEFT:
            if(unitVelocityVector.x < 0)
                unitVelocityVector.x = 0;
            break;
        case RIGHT:
            if(unitVelocityVector.x > 0)
                unitVelocityVector.x = 0;
            break;
        }
    }

    public void addItem(final ItemType item)
    {
        inventory.add(item);
    }

    public void removeItem(final ItemType item)
    {
        inventory.remove(item);
    }

    public Set<ItemType> getItems()
    {
        return inventory;
    }

    public void startMove(final boolean move)
    {
        this.move = move;
    }

    public boolean isOnExit()
    {
        return onExit;
    }

    public void onExit()
    {
        this.onExit = !isMobTouched();
    }

    public boolean isMobTouched()
    {
        return mobTouched;
    }

    public void setMobTouched()
    {
        mobTouched = !isOnExit();
    }

    @Override
    public DrawPosition getDrawPosition()
    {
        return DrawPosition.ACTOR;
    }
}