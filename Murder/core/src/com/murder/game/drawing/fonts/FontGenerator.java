package com.murder.game.drawing.fonts;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;

public class FontGenerator
{
    public enum FontType
    {
        BLUEBIRD_12,
        BLUEBIRD_48;
    }

    private final Map<FontType, BitmapFont> fontMap;

    public FontGenerator()
    {
        fontMap = new HashMap<FontType, BitmapFont>();
        final FreeTypeFontGenerator fontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("font/Bluebird.otf"));
        final FreeTypeFontParameter parameter = new FreeTypeFontParameter();
        parameter.color = Color.WHITE;

        // BLUEBIRD_12
        parameter.size = 12;
        fontMap.put(FontType.BLUEBIRD_12, fontGenerator.generateFont(parameter));

        // BLUEBIRD_48
        parameter.size = 48;
        fontMap.put(FontType.BLUEBIRD_48, fontGenerator.generateFont(parameter));

        fontGenerator.dispose();
    }

    public BitmapFont getFont(final FontType fontType)
    {
        return fontMap.get(fontType);
    }
}