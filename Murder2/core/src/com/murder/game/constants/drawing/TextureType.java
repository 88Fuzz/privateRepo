package com.murder.game.constants.drawing;

public enum TextureType
{
    EXIT_TEXTURE("images/ExitTexture.png"),
    FLOOR_TEXTURE("images/FloorTexture.png"),
    SINGLE_PIXEL_TEXTURE("images/SinglePixel.png"),
    KEY_TEXTURE("images/KeyTexture.png"),
    CIRCLE_TEXTURE("images/CircleTexture.png"),
    MOB_TEXTURE("images/MobTexture.png");

    private final String fileName;

    private TextureType(final String fileName)
    {
        this.fileName = fileName;
    }

    public String getFileName()
    {
        return fileName;
    }
}