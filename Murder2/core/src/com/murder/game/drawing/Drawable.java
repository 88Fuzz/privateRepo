package com.murder.game.drawing;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.murder.game.constants.box2d.BodyType;
import com.murder.game.constants.drawing.DisplayConstants;
import com.murder.game.drawing.manager.TextureManager;
import com.murder.game.serialize.MyVector2;
import com.murder.game.utils.BodyBuilder;

public abstract class Drawable extends NonBodyDrawable
{
    protected static final String POSITION = "position";
    protected static final String ROTATION = "rotation";
    protected static final String BODY_TYPE = "bodyType";

    private static final float ROTATION_OFFSET = MathUtils.PI / 2;

    @JsonIgnore
    protected float bodyWidth;
    @JsonIgnore
    protected float bodyHeight;
    @JsonIgnore
    protected Body body;
    // protected MyVector2 tilePosition;
    protected BodyType bodyType;

    protected Drawable(final BodyType bodyType, final MyVector2 position, final float rotation)
    {
        super(position, rotation);
        // this.tilePosition = new MyVector2();
        this.bodyType = bodyType;
        this.bodyWidth = bodyType.getWidth();
        this.bodyHeight = bodyType.getHeight();
    }

    protected void init(final World physicsWorld, final TextureManager textureManager)
    {
        this.sprite = getSprite(textureManager, bodyType);
        this.body = generateBody(physicsWorld, bodyType, sprite);
    }

    protected Sprite getSprite(final TextureManager textureManager, final BodyType bodyType)
    {
        final Sprite sprite = new Sprite(textureManager.getTexture(bodyType.getTextureType()),
                (int) (bodyType.getWidth() * bodyType.getSizeMultiplier().x), (int) (bodyType.getHeight() * bodyType.getSizeMultiplier().y));
        if(bodyType.getColor() != Color.CLEAR)
            sprite.setColor(bodyType.getColor());
        sprite.setOriginCenter();

        return sprite;
    }

    protected Body generateBody(final World physicsWorld, final BodyType bodyType, final Sprite sprite)
    {
        final Body body = BodyBuilder.createBody(physicsWorld, bodyType, position, rotation);
        body.setUserData(this);
        adjustSprite(body, sprite);

        return body;
    }

    @Override
    public void update(final float dt)
    {
        super.update(dt);
        adjustSprite(body, sprite);
    }

    // TODO change this back to private
    protected void adjustSprite(final Body body, final Sprite sprite)
    {
        // sprite.setSize(bodyWidth, bodyHeight);
        // sprite.setPosition(body.getPosition().x *
        // DisplayConstants.PIXELS_PER_METER - bodyType.getWidth() *
        // bodyType.getSizeMultiplier().x / 2,
        // body.getPosition().y * DisplayConstants.PIXELS_PER_METER -
        // bodyType.getHeight() * bodyType.getSizeMultiplier().y / 2);
        final float x = body.getPosition().x * DisplayConstants.PIXELS_PER_METER - bodyWidth * bodyType.getSizeMultiplier().x / 2;
        final float y = body.getPosition().y * DisplayConstants.PIXELS_PER_METER - bodyHeight * bodyType.getSizeMultiplier().y / 2;
        sprite.setBounds(x, y, bodyWidth, bodyHeight);
        // sprite.setRotation(body.getAngle() * MathUtils.radiansToDegrees);
        position.x = sprite.getX() + bodyType.getWidth() / 2;
        position.y = sprite.getY() + bodyType.getHeight() / 2;
    }

    public BodyType getBodyType()
    {
        return bodyType;
    }

    @JsonIgnore
    public boolean isTraversable()
    {
        return bodyType.isTraversable();
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

    @JsonIgnore
    public int getTilePositionX()
    {
        return (int) (position.x + DisplayConstants.HALF_TILE_SIZE) / DisplayConstants.TILE_SIZE;
    }

    @JsonIgnore
    public int getTilePositionY()
    {
        return (int) (position.y + DisplayConstants.HALF_TILE_SIZE) / DisplayConstants.TILE_SIZE;
    }

    @JsonIgnore
    public Array<Fixture> getFixtures()
    {
        return body.getFixtureList();
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