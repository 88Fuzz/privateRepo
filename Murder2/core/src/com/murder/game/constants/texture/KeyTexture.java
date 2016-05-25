package com.murder.game.constants.texture;

/**
 * Handles the loading of the key texture.
 */
public class KeyTexture extends MiscTexture
{
    private static final KeyTexture KEY_TEXTURE_LOADER = new KeyTexture();

    private static final String[] TEXTURE_NAMES = { "KeyTexture" };

    // Hid constructor so that getCircleTextureLoader is the only way to get
    // this class to enforce singleton
    private KeyTexture()
    {
        super();
    }

    public static BaseTexture getKeyTextureLoader()
    {
        return KEY_TEXTURE_LOADER;
    }

    @Override
    protected String[] getAvailableRegions()
    {
        return TEXTURE_NAMES;
    }
}