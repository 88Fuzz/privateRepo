package com.murder.game.serialize;

import com.badlogic.gdx.math.Vector2;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 
 * Wrapper around Vector2 so that it can be serialized to json easier
 *
 */
@JsonIgnoreProperties({ "zero", "unit" })
public class MyVector2 extends Vector2
{
    private static final String X = "x";
    private static final String Y = "y";

    private static final long serialVersionUID = 913902788239530931L;

    public MyVector2()
    {
        this(0, 0);
    }

    public MyVector2(final MyVector2 other)
    {
        this(other.x, other.y);
    }

    @JsonCreator
    public MyVector2(@JsonProperty(X) final float x, @JsonProperty(Y) final float y)
    {
        super(x, y);
    }

    public float getX()
    {
        return x;
    }

    public float getY()
    {
        return y;
    }

    @Override
    public MyVector2 cpy()
    {
        return new MyVector2(this);
    }
}