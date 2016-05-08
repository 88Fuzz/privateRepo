package com.murder.game.drawing.manager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.murder.game.constants.drawing.FontType;

public class FontManager
{
    public BitmapFont getFont(final FontType fontType)
    {
        return loadFont(fontType);
    }

    private BitmapFont loadFont(final FontType fontType)
    {
        final FreeTypeFontGenerator fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal(fontType.getFileName()));
        final FreeTypeFontParameter parameter = new FreeTypeFontParameter();
        parameter.color = Color.WHITE;
        parameter.size = fontType.getSize();

        final BitmapFont bitmapFont = fontGenerator.generateFont(parameter);

        fontGenerator.dispose();
        return bitmapFont;
    }
}