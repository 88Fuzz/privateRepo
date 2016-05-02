package com.murder.game.constants.drawing;

public enum FontType
{
    AVOCADO_56("font/avocado.ttf", 56),
    BLOODSUCKERS_48("font/BLOODSUC.TTF", 48),
    COLDNIGHT_48("font/coldnightforalligators.ttf", 48),
    EDOSZ_48("font/edosz.ttf", 48),
    GHASTLYPANIC_48("font/Ghastly_Panic.ttf", 48),
    GYPSYCURSE_48("font/Gypsy_Curse.ttf", 48),
    SUBTLE_48("font/SUBTLE.TTF", 48),
    SUNSET_48("font/SUNSET.TTF", 48),
    TEQUILA_48("font/TEQUILA.TTF", 48),
    HAND_48("font/Hand.ttf", 48);

    public static final String ARIAL_15 = "images/arial-15.fnt";

    private final String fileName;
    private final int size;

    private FontType(final String fileName, final int size)
    {
        this.fileName = fileName;
        this.size = size;
    }

    public String getFileName()
    {
        return fileName;
    }

    public int getSize()
    {
        return size;
    }
}