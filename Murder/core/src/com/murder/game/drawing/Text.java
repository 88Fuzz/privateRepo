package com.murder.game.drawing;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;

public class Text extends Drawable
{
    final BitmapFont font;

    public Text()
    {
        final FreeTypeFontGenerator fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("font/Bluebird.otf"));
        final FreeTypeFontParameter parameter = new FreeTypeFontParameter();
        parameter.size = 12;
        parameter.color = Color.WHITE;
        // TODO all fonts should be loaded into a key value pair. Key being font type and value being BitmapFont. This constructor will take the key.
        font = fontGenerator.generateFont(parameter);
        fontGenerator.dispose();
    }

    @Override
    public void draw(final SpriteBatch batch)
    {
        font.draw(batch, "TEST", 100, 100);
    }

    @Override
    public void update(final float dt)
    {
        // TODO Auto-generated method stub
    }
}