package com.murder.game.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class GraphicsUtils
{
    public static void applyTextureRegion(final Sprite sprite, final TextureRegion region)
    {
        sprite.setRegion(region);
        sprite.setSize(region.getRegionWidth(), region.getRegionHeight());
        sprite.setOrigin(region.getRegionWidth() / 2, region.getRegionHeight() / 2);
    }

    // Libgdx is fucking stupid and has the touch positions based on origin on
    // top left. All others have origin on bottom left. Flip y
    public static int flipY(int y)
    {
        return Gdx.graphics.getHeight() - y;
    }

    public static Vector2 getNormalizedScreenTouch(int screenX, int screenY, Vector2 cameraPosition)
    {
        screenY = GraphicsUtils.flipY(screenY);
        return new Vector2(screenX + cameraPosition.x, screenY + cameraPosition.y);
    }
}