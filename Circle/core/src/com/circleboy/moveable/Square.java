package com.circleboy.moveable;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.circleboy.event.implementations.TextChangeEvent;
import com.circleboy.util.MathUtils;
import com.circleboy.util.definitions.TextureConstants;

public class Square extends Moveable
{
    private static final int MAX_COLOR = 255;
    private String text;
    private final BitmapFont font;
    private TextChangeEvent textEvent;
    private float distanceTraveled;
    private final float maxDistance;

    public Square(final float x, final float y, final Sprite sprite, final float baseScreenMovement,
            final float baseMovement, final TextChangeEvent textEvent, final float travelDistance)
    {
        super(x, y, sprite, baseScreenMovement, baseMovement);
        font = new BitmapFont();
        font.setScale(2.0f, 2.0f);
        this.text = "";
        this.textEvent = textEvent;
        maxDistance = travelDistance;
        distanceTraveled = travelDistance / 2.0f;
    }

    public void draw(SpriteBatch batch)
    {
        super.draw(batch);
        Rectangle rect = sprite.getBoundingRectangle();
        font.draw(batch, text, rect.x, rect.y + rect.height + font.getLineHeight());
    }

    public void update(final Moveable circle, final float dt, final float movementFactor)
    {
        Vector2 oldPos = new Vector2(pos.x, pos.y);
        super.update(circle, dt, movementFactor);
        if(textEvent != null && textEvent.checkEvent(circle, this))
            textEvent = null;

        distanceTraveled += Math.abs(MathUtils.getDistance(oldPos, pos));
        if(distanceTraveled > maxDistance)
        {
            distanceTraveled = 0;
            baseMovement *= -1;
        }
    }

    @Override
    public void setText(String text)
    {
        this.text = text;
    }

    public static Sprite generateSprite(final TextureAtlas atlas, final int minWidth, final int maxWidth,
            final int minHeight, final int maxHeight)
    {
        int width = MathUtils.randInt(minWidth, maxWidth);
        int height = MathUtils.randInt(minHeight, maxHeight);

        Sprite sprite = new Sprite(atlas.findRegion(TextureConstants.SQUARE_KEY));
        float defaultWidth = sprite.getWidth();
        float defaultHeight = sprite.getHeight();

        float widthScale = (float) width / defaultWidth;
        float heightScale = (float) height / defaultHeight;
        sprite.setScale(widthScale, heightScale);

        float red = (float) MathUtils.randInt(MAX_COLOR + 1) / MAX_COLOR;
        float green = (float) MathUtils.randInt(MAX_COLOR + 1) / MAX_COLOR;
        float blue = (float) MathUtils.randInt(MAX_COLOR + 1) / MAX_COLOR;

        sprite.setColor(red, green, blue, 1);
        return sprite;
    }
}