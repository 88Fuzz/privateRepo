package com.murder.game.drawing;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.murder.game.constants.drawing.FontType;
import com.murder.game.drawing.manager.FontManager;
import com.murder.game.serialize.MyVector2;

public class Text extends NonBodyDrawable
{
    private BitmapFont font;
    private FontType fontType;
    private String text;

    public Text(final MyVector2 position, final FontType fontType, final String text, final float rotation)
    {
        super(position, rotation);
        this.fontType = fontType;
        this.text = text;
    }

    public void init(final FontManager fontManager)
    {
        font = fontManager.getFont(fontType);
    }

    @Override
    public void draw(final SpriteBatch batch)
    {
        font.draw(batch, text, position.x, position.y);
    }

    @Override
    protected void updateCurrent(float dt)
    {
        // do nothing
    }
}