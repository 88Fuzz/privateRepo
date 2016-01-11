package com.pixel.wars.game.drawing;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class Text implements Drawable
{
    // TODO use this if writing to the screen
    // https://github.com/libgdx/libgdx/wiki/Gdx-freetype
    // private final FreeTypeFontGenerator fontGenerator;
    private final Vector2 position;
    private final BitmapFont font;
    private String text;

    public Text(final String text, final Color color, final int x, final int y, final float scale)
    {
        this.text = text;
        this.font = new BitmapFont(Gdx.files.internal("images/arial-15.fnt"), false);
        this.position = new Vector2();
        position.x = x;
        position.y = y;

        // TODO dispose front
        font.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
        font.setColor(color);
        font.getData().scaleX = scale;
        font.getData().scaleY = scale;
//        ((BitmapFont) font.getData()).setScale(scale);
    }

    @Override
    public void draw(final SpriteBatch batch)
    {
        font.draw(batch, text, position.x, position.y);
    }
}