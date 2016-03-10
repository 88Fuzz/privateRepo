package com.murder.game.drawing;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.murder.game.drawing.fonts.FontGenerator;
import com.murder.game.drawing.fonts.FontGenerator.FontType;
import com.murder.game.state.serial.MyVector2;

public class Text extends Drawable
{
    private BitmapFont font;
    private FontType fontType;
    private String text;

    public Text(final MyVector2 position, final FontType fontType, final String text)
    {
        this.position = position;
        this.fontType = fontType;
        this.text = text;
    }

    public void init(final FontGenerator fontGenerator)
    {
        font = fontGenerator.getFont(fontType);
    }

    @Override
    public void draw(final SpriteBatch batch)
    {
        font.draw(batch, text, position.x, position.y);
    }

    @Override
    public void update(final float dt)
    {
        // TODO Auto-generated method stub
    }
}