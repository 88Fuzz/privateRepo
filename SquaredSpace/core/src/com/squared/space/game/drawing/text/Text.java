package com.squared.space.game.drawing.text;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.squared.space.game.drawing.Drawable;

public class Text implements Drawable
{
    private static final float BASE_UPDATE_TIME = .06f;
    // TODO use this if writing to the screen
    // https://github.com/libgdx/libgdx/wiki/Gdx-freetype
    // private final FreeTypeFontGenerator fontGenerator;
    private Text nextText;
    private int textPosition;
    private float updateTime;
    private Vector2 namePosition;
    private String name;

    protected TextureAtlas atlas;
    protected BitmapFont font;
    protected Vector2 position;
    protected String text;

    public Text()
    {
        text = null;
        updateTime = BASE_UPDATE_TIME;
        textPosition = 1;
    }

    public void init(final TextureAtlas atlas, final BitmapFont font, final Vector2 position, final Vector2 namePosition)
    {
        this.atlas = atlas;
        this.font = font;
        this.position = position;
        this.namePosition = namePosition;
    }

    public void setNextText(final Text nextText)
    {
        this.nextText = nextText;
    }

    public void setTexts(final String text, final String name)
    {
        this.text = text;
        this.name = name;
        textPosition = 1;
    }

    public Text getNextText()
    {
        textPosition = 1;
        return nextText;
    }

    @Override
    public void draw(final SpriteBatch batch)
    {
        font.draw(batch, text.substring(0, textPosition), position.x, position.y);
        font.draw(batch, name, namePosition.x, namePosition.y);
    }

    @Override
    public void update(final float dt)
    {
        if(updateTime < 0)
        {
            updateTime = BASE_UPDATE_TIME;
            if(!isFinishedText())
            {
                textPosition++;
            }
        }
        updateTime -= dt;
    }

    public boolean selectionUp()
    {
        return true;
    }

    public boolean selectionDown()
    {
        return true;
    }

    public void finishText()
    {
        textPosition = text.length();
    }

    public boolean isFinishedText()
    {
        return textPosition == text.length();
    }

    public String getText()
    {
        return text;
    }
}