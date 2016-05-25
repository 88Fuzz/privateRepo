package com.murder.game.texture.loader;

/**
 * Handles the loading of the key texture.
 */
public class KeyTextureLoader extends MiscTextureLoader
{
    private static final KeyTextureLoader KEY_TEXTURE_LOADER = new KeyTextureLoader();

    private static final String[] TEXTURE_NAMES = { "KeyTexture" };

    // Hid constructor so that getCircleTextureLoader is the only way to get
    // this class to enforce singleton
    private KeyTextureLoader()
    {
        super();
    }

    public static BaseTextureLoader getKeyTextureLoader()
    {
        return KEY_TEXTURE_LOADER;
    }

    @Override
    protected String[] getAvailableRegions()
    {
        return TEXTURE_NAMES;
    }
}