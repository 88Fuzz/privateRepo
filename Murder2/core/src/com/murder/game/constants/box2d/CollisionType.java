package com.murder.game.constants.box2d;

public enum CollisionType
{
    PLAYER((short) (1 << 0)),
    MONSTER((short) (1 << 1)),
    WALL((short) (1 << 2)),
    DOOR((short) (1 << 3)),
    FLOOR((short) (1 << 4)),
    EXIT((short) (1 << 5)),
    KEY((short) (1 << 6));

    public static final short DEFAULT_GROUP_INDEX = 1;

    private final short collisionValue;

    private CollisionType(final short collisionValue)
    {
        this.collisionValue = collisionValue;
    }

    public short getCollisionValue()
    {
        return collisionValue;
    }
}