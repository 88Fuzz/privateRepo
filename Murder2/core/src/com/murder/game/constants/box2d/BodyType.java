package com.murder.game.constants.box2d;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.murder.game.constants.drawing.TextureType;

public enum BodyType
{
    PLAYER(BodyShape.CIRCLE, TextureType.CIRCLE_TEXTURE, new Color(1, 1, 1, 0.2f),
            /* Color.WHITE, */ 100f, 100f, 1f,
            CollisionType.PLAYER.getCollisionValue(), (short) (CollisionType.DOOR.getCollisionValue() | CollisionType.MONSTER.getCollisionValue()
                    | CollisionType.WALL.getCollisionValue() | CollisionType.KEY.getCollisionValue()),
            CollisionType.DEFAULT_GROUP_INDEX, BodyDef.BodyType.DynamicBody),
    MONSTER(BodyShape.CIRCLE, TextureType.CIRCLE_TEXTURE, Color.FOREST, 100f, 100f, 1f, CollisionType.MONSTER.getCollisionValue(),
            (short) (CollisionType.DOOR.getCollisionValue() | CollisionType.PLAYER.getCollisionValue() | CollisionType.WALL.getCollisionValue()),
            CollisionType.DEFAULT_GROUP_INDEX, BodyDef.BodyType.DynamicBody),
    WALL(BodyShape.SQUARE, TextureType.SINGLE_PIXEL_TEXTURE, new Color(1, 1, 0, 0.2f),
            /* Color.BROWN, */ 200f, 200f, 1f, CollisionType.WALL.getCollisionValue(),
            (short) (CollisionType.MONSTER.getCollisionValue() | CollisionType.PLAYER.getCollisionValue()), CollisionType.DEFAULT_GROUP_INDEX,
            BodyDef.BodyType.StaticBody),
    DOOR(BodyShape.SQUARE, TextureType.SINGLE_PIXEL_TEXTURE, Color.GREEN, 200f, 200f, 1f, CollisionType.DOOR.getCollisionValue(),
            (short) (CollisionType.MONSTER.getCollisionValue() | CollisionType.PLAYER.getCollisionValue()), CollisionType.DEFAULT_GROUP_INDEX,
            BodyDef.BodyType.StaticBody),
    EXIT(BodyShape.SQUARE, TextureType.SINGLE_PIXEL_TEXTURE, Color.CLEAR, 200f, 200f, 1f, CollisionType.EXIT.getCollisionValue(), (short) 0,
            CollisionType.DEFAULT_GROUP_INDEX, BodyDef.BodyType.StaticBody),
    KEY(BodyShape.SQUARE, TextureType.SINGLE_PIXEL_TEXTURE, Color.CLEAR, 100f, 100f, 1f, CollisionType.KEY.getCollisionValue(),
            CollisionType.PLAYER.getCollisionValue(), CollisionType.DEFAULT_GROUP_INDEX, BodyDef.BodyType.StaticBody);

    private final BodyShape bodyShape;
    private final TextureType textureType;
    private final Color color;
    private final float width; // In pixels
    private final float height; // In pixels
    private final float density; // In box2d units

    /*
     * CategoryBits is the type of body for collision detection
     */
    private final short categoryBits;

    /*
     * MaskBits are the types of categoryBits that can collide with the Body.
     */
    private final short maskBits;

    /*
     * GroupIndex makes categoryBits and maskBits do weird things.
     * 
     * if either fixture has a groupIndex of zero, use the category/mask rules
     * as above
     * 
     * if both groupIndex values are non-zero but different, use the
     * category/mask rules as above
     * 
     * if both groupIndex values are the same and positive, collide
     * 
     * if both groupIndex values are the same and negative, don't collide
     */
    private final short groupIndex;
    private final BodyDef.BodyType box2dBodyType;

    private BodyType(final BodyShape bodyShape, final TextureType textureType, final Color color, final float width, final float height,
            final float density, final short categoryBits, final short maskBits, final short groupIndex, final BodyDef.BodyType box2dBodyType)
    {
        this.bodyShape = bodyShape;
        this.textureType = textureType;
        this.color = color;
        this.width = width;
        this.height = height;
        this.density = density;
        this.categoryBits = categoryBits;
        this.maskBits = maskBits;
        this.groupIndex = groupIndex;
        this.box2dBodyType = box2dBodyType;
    }

    public BodyShape getBodyShape()
    {
        return bodyShape;
    }

    public Color getColor()
    {
        return color;
    }

    public float getWidth()
    {
        return width;
    }

    public float getHeight()
    {
        return height;
    }

    public float getDensity()
    {
        return density;
    }

    public short getCategoryBits()
    {
        return categoryBits;
    }

    public short getMaskBits()
    {
        return maskBits;
    }

    public short getGroupIndex()
    {
        return groupIndex;
    }

    public BodyDef.BodyType getBox2dBodyType()
    {
        return box2dBodyType;
    }

    public TextureType getTextureType()
    {
        return textureType;
    }
}