package com.murder.game.texture.loader;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.murder.game.utils.RandomUtils;

/**
 * Base information for textures in a packed atlas. Each group of textures
 * should implement this class.
 */
public abstract class BaseTextureLoader
{
    private TextureAtlas atlas;

    public BaseTextureLoader()
    {
        atlas = new TextureAtlas(Gdx.files.internal(getTexturePackName()));
    }

    /**
     * Returns an AtlasRegion that should be used to create a sprite.
     * 
     * @return
     */
    public AtlasRegion getAtlasRegion()
    {
        final String[] availableRegions = getAvailableRegions();
        final String region = availableRegions[RandomUtils.getRandomInt(0, availableRegions.length)];

        return atlas.findRegion(region);
    }

    /**
     * Return the name of the packed textures to be loaded into the
     * TextureAtlas.
     * 
     * @return
     */
    protected abstract String getTexturePackName();

    /**
     * Return the available texture names that are packed into the texture pack
     * returned from {@link getTexturePackName}
     * 
     * @return
     */
    protected abstract String[] getAvailableRegions();
}