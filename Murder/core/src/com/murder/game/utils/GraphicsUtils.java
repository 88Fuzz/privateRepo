package com.murder.game.utils;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class GraphicsUtils
{
    public static void applyTextureRegion(final Sprite sprite, final TextureRegion region)
    {
        sprite.setRegion(region);
        sprite.setSize(region.getRegionWidth(), region.getRegionHeight());
        sprite.setOrigin(region.getRegionWidth() / 2, region.getRegionHeight() / 2);
    }
}