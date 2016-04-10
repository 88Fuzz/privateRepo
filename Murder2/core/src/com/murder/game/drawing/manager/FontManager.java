package com.murder.game.drawing.manager;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.murder.game.constants.drawing.FontType;

public class FontManager
{
    private final Map<FontType, BitmapFont> fontMap;
    private FreeTypeFontGenerator fontGenerator;

    public FontManager()
    {
        fontMap = new HashMap<FontType, BitmapFont>();
    }

    // TODO maybe don't lazy load?
    public BitmapFont getFont(final FontType fontType)
    {
        BitmapFont bitmapFont = fontMap.get(fontType);

        if(bitmapFont == null)
            bitmapFont = loadFont(fontType);

        return bitmapFont;
    }

    private BitmapFont loadFont(final FontType fontType)
    {
        dispose();

        fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal(fontType.getFileName()));
        final FreeTypeFontParameter parameter = new FreeTypeFontParameter();
        parameter.color = Color.WHITE;
        parameter.size = fontType.getSize();

        final BitmapFont bitmapFont = fontGenerator.generateFont(parameter);
        fontMap.put(fontType, bitmapFont);

        return bitmapFont;
    }

    public void dispose()
    {
        if(fontGenerator != null)
        {
            fontGenerator.dispose();
            fontGenerator = null;
        }
    }
}