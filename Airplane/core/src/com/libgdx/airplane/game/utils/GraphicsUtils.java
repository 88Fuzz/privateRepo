package com.libgdx.airplane.game.utils;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class GraphicsUtils
{
    public static void applyTextureRegion(Sprite sprite, TextureRegion region)
    {
        sprite.setRegion(region);
        sprite.setColor(1, 1, 1, 1);
        sprite.setSize(region.getRegionWidth(), region.getRegionHeight());
        sprite.setOrigin(region.getRegionWidth()/2, region.getRegionHeight()/2);
    }
}
