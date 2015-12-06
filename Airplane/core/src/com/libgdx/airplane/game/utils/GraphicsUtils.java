package com.libgdx.airplane.game.utils;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class GraphicsUtils
{
    public static void applyTextureRegion(final Sprite sprite, final TextureRegion region)
    {
        sprite.setRegion(region);
        sprite.setColor(1, 1, 1, 1);
        sprite.setSize(region.getRegionWidth(), region.getRegionHeight());
        sprite.setOrigin(region.getRegionWidth()/2, region.getRegionHeight()/2);
    }
    
    public static void applySpriteToBody(final Sprite sprite, final Vector2 bodySize)
    {
        sprite.setSize(bodySize.x, bodySize.y);
        sprite.setOrigin(sprite.getWidth() / 2, sprite.getHeight() / 2);
    }
}
