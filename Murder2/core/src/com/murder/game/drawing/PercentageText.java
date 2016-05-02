package com.murder.game.drawing;

import com.badlogic.gdx.Gdx;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.murder.game.constants.drawing.FontType;
import com.murder.game.serialize.MyVector2;

public class PercentageText extends Text
{
    @JsonCreator
    public PercentageText(final float offsetX, final float offsetY, final FontType fontType, final String text, final float rotation)
    {
        // Divide by two for the x and y coordinates because the camera has a
        // default position of (0,0) being the center of the screen.
        super(new MyVector2(offsetX * Gdx.graphics.getWidth()/2, offsetY * Gdx.graphics.getHeight()/2), fontType, text, rotation);
        System.out.println("position " + new MyVector2(offsetX * Gdx.graphics.getWidth(), offsetY * Gdx.graphics.getHeight()));
    }
}