package com.murder.game.drawing.drawables;

import java.util.HashSet;
import java.util.Set;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.World;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.murder.game.constants.box2d.BodyType;
import com.murder.game.constants.level.ItemType;
import com.murder.game.serialize.MyVector2;
import com.murder.game.utils.LightBuilder;
import com.murder.game.utils.RandomUtils;

import box2dLight.RayHandler;

public class Actor extends Drawable
{
    // private static final float MAX_VELOCITY = 460 / 20;
    private static final float MAX_VELOCITY = 1260;
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
    protected float spriteUpdateTimer;

    @JsonCreator
    public Actor(@JsonProperty(BODY_TYPE) BodyType bodyType, @JsonProperty(POSITION) final MyVector2 position,
            @JsonProperty(ROTATION) final float rotation)
    {
        super(bodyType, position, rotation);
        this.move = false;
        this.position = position;
        this.inventory = new HashSet<ItemType>();
        // tilePosition = new MyVector2();
        unitVelocityVector = new MyVector2();
        velocity = MAX_VELOCITY;
        this.onExit = false;
    }

    public void init(final World physicsWorld, final RayHandler rayHandler)
    {
        super.init(physicsWorld);
        createLight(rayHandler);
        inventory.clear();

        setSprite();
        spriteUpdateTimer = MAX_SPRITE_UPDATE_TIMER;
        // sprite = new
        // Sprite(textureAtlas.findRegion(TextureConstants.CIRCLE_TEXTURE));
        // sprite = new
        // Sprite(textureAtlas.findRegion(TextureConstants.SINGLE_PIXEL_TEXTURE));
        // sprite.setOriginCenter();
        // centerSpritePosition();
        // setTilePosition();
    }

    protected void setSprite()
    {
        sprite = new Sprite(bodyType.getTextureLoader().getAtlasRegion());
        spriteUpdateTimer = MAX_SPRITE_UPDATE_TIMER;
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
        // final MyVector2 directionalVelocity = new MyVector2();
        // float distanceTraveled = 0;
        float xVelocity = 0;
        float yVelocity = 0;
        if(move)
        {
            xVelocity = (float) (velocity * Math.sin(Math.toRadians(rotation)));
            yVelocity = (float) (velocity * Math.cos(Math.toRadians(rotation)));
            // distanceTraveled = velocity * dt;
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

        spriteUpdateTimer -= dt;
        if(spriteUpdateTimer <= 0)
        {
            setSprite();
        }

        // rotate(.3f);

        // final MyVector2 newPos = position.cpy();
        // // Check x and y independently in case player is moving up against a
        // // wall and with a wall.
        // if(directionalVelocity.x != 0)
        // {
        // newPos.x = position.x + directionalVelocity.x * dt;
        // if(!checkNewPosition(newPos))
        // {
        // newPos.x = position.x;
        // }
        // }
        //
        // if(directionalVelocity.y != 0)
        // {
        // newPos.y = position.y + directionalVelocity.y * dt;
        // if(!checkNewPosition(newPos))
        // {
        // newPos.y = position.y;
        // }
        // }
        //
        // position.x = newPos.x;
        // position.y = newPos.y;
        //
        // setTilePosition();
        // centerSpritePosition();
        //
        // final Tile tile = level.getTile((int) tilePosition.x, (int)
        // tilePosition.y);
        // if(tile != null)
        // {
        // final Item item = tile.getItem(true);
        // if(item != null)
        // {
        // inventory.add(item.getInventoryItem());
        // }
        // }
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

    // private boolean checkNewPosition(final MyVector2 newPosition)
    // {
    // final MyVector2 testPos = new MyVector2();
    // final MyVector2 testTilePos = new MyVector2();
    // for(final MyVector2 edge: TESTING_EDGES)
    // {
    // testPos.x = newPosition.x + edge.x;
    // testPos.y = newPosition.y + edge.y;
    //
    // testTilePos.x = testPos.x / (SPRITE_SIZE);
    // testTilePos.y = testPos.y / (SPRITE_SIZE);
    //
    // if(!isTileValid(testTilePos))
    // {
    // return false;
    // }
    // }
    // return true;
    // }

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

    public void setOnExit(final boolean onExit)
    {
        this.onExit = onExit;
    }

    // public MyVector2 getTilePosition()
    // {
    // return tilePosition.cpy();
    // }

    // public MyVector2 getPosition()
    // {
    // return new MyVector2(position.x + sprite.getWidth() / 2, position.y +
    // sprite.getHeight() / 2);
    // }
    //
    // @JsonIgnore
    // public MyVector2 getCenterPosition()
    // {
    // return position;
    // }

    // private boolean isTileValid(final MyVector2 tilePos)
    // {
    // final Tile tile = level.getTile((int) tilePos.x, (int) tilePos.y);
    // return isValidMove(tile);
    // }
    //
    // private boolean isValidMove(final Tile tile)
    // {
    // if(tile == null || tile.isLocked())
    // {
    // if(tile != null && tile.getTileType() == TileType.DOOR)
    // {
    // if(inventory.contains(tile.getUnlockingItem()))
    // {
    // tile.setLock(false);
    // return true;
    // }
    // }
    //
    // return false;
    // }
    // return true;
    // }
}
