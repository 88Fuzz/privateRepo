package com.murder.game.drawing;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.World;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.murder.game.constants.box2d.BodyType;
import com.murder.game.constants.drawing.DisplayConstants;
import com.murder.game.serialize.MyVector2;
import com.murder.game.utils.BodyBuilder;

public abstract class Drawable
{
    @JsonIgnore
    protected Sprite sprite;
    @JsonIgnore
    protected Body body;
    protected MyVector2 position;
    // protected MyVector2 tilePosition;
    private float rotation;
    private BodyType bodyType;

    protected Drawable()
    {}

    protected Drawable(final BodyType bodyType, final MyVector2 position, final float rotation)
    {
        this.position = position;
        this.rotation = rotation;
        // this.tilePosition = new MyVector2();
        this.bodyType = bodyType;
    }

    protected void init(final World world, final TextureManager textureManager)
    {
        this.sprite = new Sprite(textureManager.getTexture(bodyType.getTextureType()), (int) bodyType.getWidth(), (int) bodyType.getHeight());
        this.sprite.setColor(bodyType.getColor());
        this.sprite.setOriginCenter();
        this.body = BodyBuilder.createBody(world, bodyType, position, rotation);
        adjustSprite();
    }

    /**
     * Method called when the object should be drawn on the screen.
     * 
     * @param batch
     */
    public abstract void draw(final SpriteBatch batch);

    /**
     * Method called with the time since the last call to update.
     * 
     * @param dt
     */
    protected abstract void updateCurrent(final float dt);

    public void update(final float dt)
    {
        updateCurrent(dt);
        adjustSprite();
    }

    private void adjustSprite()
    {
        sprite.setPosition(body.getPosition().x * DisplayConstants.PIXELS_PER_METER - bodyType.getWidth() / 2,
                body.getPosition().y * DisplayConstants.PIXELS_PER_METER - bodyType.getHeight() / 2);
        sprite.setRotation(body.getAngle() * MathUtils.radiansToDegrees);
    }

    public void dispose()
    {
        for(final Fixture fixture: body.getFixtureList())
        {
            fixture.getShape().dispose();
        }
    }

    public BodyType getBodyType()
    {
        return bodyType;
    }

    public Vector2 getBodyPosition()
    {
        return body.getPosition();
    }

    public MyVector2 getPosition()
    {
        return position;
    }

    public float getRotation()
    {
        return rotation;
    }
    // protected void setTilePosition()
    // {
    // tilePosition.x = (int) (position.x / sprite.getWidth());
    // tilePosition.y = (int) (position.y / sprite.getHeight());
    // }
    //
    // protected void centerSpritePosition()
    // {
    // sprite.setPosition(position.x - sprite.getWidth() / 2, position.y -
    // sprite.getHeight() / 2);
    // }
    //
    // protected void setSpritePosition()
    // {
    // sprite.setPosition(position.x, position.y);
    // }
    //
    // public MyVector2 getPosition()
    // {
    // return position.cpy();
    // }
}
