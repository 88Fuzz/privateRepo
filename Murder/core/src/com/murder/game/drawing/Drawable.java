package com.murder.game.drawing;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.murder.game.state.serial.MyVector2;

public abstract class Drawable
{
    @JsonIgnore
    protected Sprite sprite;
    protected MyVector2 position;
    protected MyVector2 tilePosition;

    protected Drawable()
    {
        sprite = new Sprite();
        position = new MyVector2();
        tilePosition = new MyVector2();
    }

    protected void init(final TextureAtlas textureAtlas, final String textureKey, final boolean centerSprite)
    {
        sprite = new Sprite(textureAtlas.findRegion(textureKey));
        sprite.setOriginCenter();
        if(centerSprite)
            centerSpritePosition();
        else
            setSpritePosition();
        setTilePosition();
    }

    protected void init(final TextureAtlas textureAtlas, final String textureKey)
    {
        init(textureAtlas, textureKey, true);
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
    public abstract void update(final float dt);

    protected void setTilePosition()
    {
        tilePosition.x = (int) (position.x / sprite.getWidth());
        tilePosition.y = (int) (position.y / sprite.getHeight());
    }

    protected void centerSpritePosition()
    {
        sprite.setPosition(position.x - sprite.getWidth() / 2, position.y - sprite.getHeight() / 2);
    }

    protected void setSpritePosition()
    {
        sprite.setPosition(position.x, position.y);
    }

    public MyVector2 getPosition()
    {
        return position.cpy();
    }
}