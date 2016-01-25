package com.squared.space.game.drawing;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

public class Text implements Drawable
{
    private static final float BASE_UPDATE_TIME = .06f;
    // TODO use this if writing to the screen
    // https://github.com/libgdx/libgdx/wiki/Gdx-freetype
    // private final FreeTypeFontGenerator fontGenerator;
    private Label label;
    private String text;
    private int position;
    private float updateTime;

    public Text()
    {
        label = null;
        text = null;
        updateTime = BASE_UPDATE_TIME;
        position = 1;
    }

    public void setLabel(final Label label)
    {
        this.label = label;
    }

    public void setText(final String text)
    {
        this.text = text;
        position = 1;
        if(label != null)
        {
            setLabelText();
        }
    }

    @Override
    public void draw(final SpriteBatch batch)
    {
        label.draw(batch, 1.0f);
    }

    @Override
    public void update(final float dt)
    {
        if(updateTime < 0)
        {
            updateTime = BASE_UPDATE_TIME;
            if(!isFinishedText())
            {
                position++;
                setLabelText();
            }
        }
        updateTime -= dt;
    }

    public void finishText()
    {
        position = text.length();
        setLabelText();
    }

    public boolean isFinishedText()
    {
        return position == text.length();
    }

    private void setLabelText()
    {
        if(position <= text.length())
            label.setText(text.substring(0, position));
    }
}