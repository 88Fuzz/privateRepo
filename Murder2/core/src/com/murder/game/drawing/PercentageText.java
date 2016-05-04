package com.murder.game.drawing;

import com.badlogic.gdx.Gdx;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.murder.game.constants.drawing.FontType;
import com.murder.game.serialize.MyVector2;

public class PercentageText extends Text
{
    private static final String OFFSET_X = "offsetX";
    private static final String OFFSET_Y = "offsetY";

    private float offsetX;
    private float offsetY;

    @JsonCreator
    public PercentageText(@JsonProperty(OFFSET_X) final float offsetX, @JsonProperty(OFFSET_Y) final float offsetY,
            @JsonProperty(FONT_TYPE) final FontType fontType, @JsonProperty(TEXT) final String text, @JsonProperty(ROTATION) final float rotation)
    {
        // Divide by two for the x and y coordinates because the camera has a
        // default position of (0,0) being the center of the screen.
        super(new MyVector2(offsetX * Gdx.graphics.getWidth() / 2, offsetY * Gdx.graphics.getHeight() / 2), fontType, text, rotation);
        this.offsetX = offsetX;
        this.offsetY = offsetY;
    }

    public float getOffsetX()
    {
        return offsetX;
    }

    public float getOffsetY()
    {
        return offsetY;
    }

    // Jackson will grab this getter from NonBodyDrawable and write the
    // position. This serialized position somehow finds a way in and overrides
    // what is done in the constructor.
    @Override
    @JsonIgnore
    public MyVector2 getPosition()
    {
        return position;
    }

}