package com.murder.game.constants.level;

import com.murder.game.constants.box2d.BodyType;

public enum ItemType
{
    GREEN_KEY(BodyType.GREEN_KEY),
    YELLOW_KEY(BodyType.YELLOW_KEY);
//    GREEN_MAT(BodyType.GREEN_MAT),
//    YELLOW_MAT(BodyType.YELLOW_MAT);

    private final BodyType bodyType;

    private ItemType(final BodyType bodyType)
    {
        this.bodyType = bodyType;
    }

    public BodyType getBodyType()
    {
        return bodyType;
    }
}