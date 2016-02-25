package com.murder.game.level;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Rectangle;
import com.murder.game.constants.TextureConstants;
import com.murder.game.drawing.Drawable;

public class Room extends Drawable
{
    private String roomId;
    private Rectangle rectangle;
    private Sprite blackBox;

    public Room(final String roomId, final Rectangle rectangle, final TextureAtlas textureAtlas)
    {
        this.roomId = roomId;
        this.rectangle = rectangle;
        blackBox = new Sprite(textureAtlas.findRegion(TextureConstants.SINGLE_PIXEL_TEXTURE));
        blackBox.setBounds(rectangle.x, rectangle.y, rectangle.width, rectangle.height);
        turnOffLight();
    }

    public void turnOnLight()
    {
        blackBox.setColor(0, 0, 0, 0);
    }

    public void turnOffLight()
    {
        blackBox.setColor(0, 0, 0, 1);
    }

    @Override
    public void draw(final SpriteBatch batch, final Matrix4 matrix)
    {
        blackBox.draw(batch);
    }

    @Override
    public void update(float dt)
    {}
}