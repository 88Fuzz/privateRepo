package com.murder.game.drawing;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.murder.game.constants.box2d.BodyType;
import com.murder.game.constants.drawing.DisplayConstants;
import com.murder.game.drawing.manager.TextureManager;
import com.murder.game.serialize.MyVector2;
import com.murder.game.utils.BodyBuilder;

public abstract class Drawable extends NonBodyDrawable
{
    private static final float ROTATION_OFFSET = MathUtils.PI / 2;

    @JsonIgnore
    protected Body body;
    // protected MyVector2 tilePosition;
    private BodyType bodyType;

    protected Drawable(final BodyType bodyType, final MyVector2 position, final float rotation)
    {
        super(position, rotation);
        // this.tilePosition = new MyVector2();
        this.bodyType = bodyType;
    }

    protected void init(final World world, final TextureManager textureManager)
    {
        this.sprite = new Sprite(textureManager.getTexture(bodyType.getTextureType()), (int) (bodyType.getWidth() * bodyType.getSizeMultiplier().x),
                (int) (bodyType.getHeight() * bodyType.getSizeMultiplier().y));
        if(bodyType.getColor() != Color.CLEAR)
            this.sprite.setColor(bodyType.getColor());
        this.sprite.setOriginCenter();
        this.body = BodyBuilder.createBody(world, bodyType, position, rotation);
        this.body.setUserData(this);
        adjustSprite();
    }

    @Override
    public void update(final float dt)
    {
        super.update(dt);
        adjustSprite();
    }

    private void adjustSprite()
    {
        sprite.setPosition(body.getPosition().x * DisplayConstants.PIXELS_PER_METER - bodyType.getWidth() * bodyType.getSizeMultiplier().x / 2,
                body.getPosition().y * DisplayConstants.PIXELS_PER_METER - bodyType.getHeight() * bodyType.getSizeMultiplier().y / 2);
        sprite.setRotation(body.getAngle() * MathUtils.radiansToDegrees);
    }

    public BodyType getBodyType()
    {
        return bodyType;
    }

    @JsonIgnore
    public Vector2 getBodyPosition()
    {
        return body.getPosition();
    }

    public void setRotation(final float rotation)
    {
        if(body != null)
            body.setTransform(body.getPosition().x, body.getPosition().y, ROTATION_OFFSET - rotation);

        super.setRotation(rotation);
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
