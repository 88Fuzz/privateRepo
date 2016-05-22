package com.murder.game.constants.box2d;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.murder.game.constants.drawing.TextureType;

public enum BodyType
{
    // TODO this shit needs to be reworked so that each color variant is not a
    // different body type, green door vs red door vs yellow door.
    PLAYER(BodyShape.CIRCLE, TextureType.CIRCLE_TEXTURE, Color.WHITE, 100f, 100f, 1f, CollisionType.PLAYER.getCollisionValue(),
            (short) (CollisionType.DOOR.getCollisionValue() | CollisionType.MONSTER.getCollisionValue() | CollisionType.WALL.getCollisionValue()
                    | CollisionType.KEY.getCollisionValue() | CollisionType.EXIT.getCollisionValue()),
            CollisionType.DEFAULT_GROUP_INDEX, BodyDef.BodyType.DynamicBody, false, new Vector2(1, 1), true),
    MOB(BodyShape.CIRCLE, TextureType.MOB_TEXTURE, Color.FOREST, 180f, 180f, 1f, CollisionType.MONSTER.getCollisionValue(),
            (short) (CollisionType.DOOR.getCollisionValue() | CollisionType.PLAYER.getCollisionValue() | CollisionType.MONSTER.getCollisionValue()
                    | CollisionType.WALL.getCollisionValue()),
            CollisionType.DEFAULT_GROUP_INDEX, BodyDef.BodyType.DynamicBody, false, new Vector2(1, 1), true),
    WALL(BodyShape.SQUARE, TextureType.SINGLE_PIXEL_TEXTURE, Color.BROWN, 200f, 200f, 1f, CollisionType.WALL.getCollisionValue(),
            (short) (CollisionType.MONSTER.getCollisionValue() | CollisionType.PLAYER.getCollisionValue()), CollisionType.DEFAULT_GROUP_INDEX,
            BodyDef.BodyType.StaticBody, false, new Vector2(1, 1), false),
    FLOOR(BodyShape.SQUARE, TextureType.FLOOR_TEXTURE, Color.CLEAR, 200f, 200f, 1f, CollisionType.FLOOR.getCollisionValue(), (short) 0,
            CollisionType.DEFAULT_GROUP_INDEX, BodyDef.BodyType.StaticBody, false, new Vector2(1, 1), true),
    GREEN_DOOR(BodyShape.SQUARE, TextureType.SINGLE_PIXEL_TEXTURE, Color.GREEN, 200f, 200f, 1f, CollisionType.DOOR.getCollisionValue(),
            (short) (CollisionType.MONSTER.getCollisionValue() | CollisionType.PLAYER.getCollisionValue()), CollisionType.DEFAULT_GROUP_INDEX,
            BodyDef.BodyType.StaticBody, false, new Vector2(1, 1), false),
    YELLOW_DOOR(BodyShape.SQUARE, TextureType.SINGLE_PIXEL_TEXTURE, Color.YELLOW, 200f, 200f, 1f, CollisionType.DOOR.getCollisionValue(),
            (short) (CollisionType.MONSTER.getCollisionValue() | CollisionType.PLAYER.getCollisionValue()), CollisionType.DEFAULT_GROUP_INDEX,
            BodyDef.BodyType.StaticBody, false, new Vector2(1, 1), false),
    GREEN_KEY(BodyShape.SQUARE, TextureType.KEY_TEXTURE, Color.GREEN, 200f, 100f, 1f, CollisionType.KEY.getCollisionValue(),
            CollisionType.PLAYER.getCollisionValue(), CollisionType.DEFAULT_GROUP_INDEX, BodyDef.BodyType.StaticBody, true, new Vector2(1, 1), true),
    YELLOW_KEY(BodyShape.SQUARE, TextureType.KEY_TEXTURE, Color.YELLOW, 200f, 100f, 1f, CollisionType.KEY.getCollisionValue(),
            CollisionType.PLAYER.getCollisionValue(), CollisionType.DEFAULT_GROUP_INDEX, BodyDef.BodyType.StaticBody, true, new Vector2(1, 1), true),
    GREEN_MAT(BodyShape.SQUARE, TextureType.SINGLE_PIXEL_TEXTURE, Color.GREEN, 200f, 20f, 1f, (short) 0, (short) 0, CollisionType.DEFAULT_GROUP_INDEX,
            BodyDef.BodyType.StaticBody, true, new Vector2(1, 1), true),
    YELLOW_MAT(BodyShape.SQUARE, TextureType.SINGLE_PIXEL_TEXTURE, Color.YELLOW, 200f, 20f, 1f, (short) 0, (short) 0,
            CollisionType.DEFAULT_GROUP_INDEX, BodyDef.BodyType.StaticBody, true, new Vector2(1, 1), true),
    EXIT(BodyShape.SQUARE, TextureType.EXIT_TEXTURE, Color.CLEAR, 200f, 200f, 1f, CollisionType.EXIT.getCollisionValue(),
            CollisionType.PLAYER.getCollisionValue(), CollisionType.DEFAULT_GROUP_INDEX, BodyDef.BodyType.StaticBody, true, new Vector2(1, 1), true),
    NONE(BodyShape.SQUARE, TextureType.SINGLE_PIXEL_TEXTURE, Color.BLACK, 0f, 0f, 1f, (short) 0, (short) 0, CollisionType.DEFAULT_GROUP_INDEX,
            BodyDef.BodyType.StaticBody, false, new Vector2(1, 1), false);

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
    private final boolean sensor;
    /*
     * Size multiplier is used to create the floor tile under a sensor.
     */
    private final Vector2 sizeMultiplier;
    private final boolean traversable;

    private BodyType(final BodyShape bodyShape, final TextureType textureType, final Color color, final float width, final float height,
            final float density, final short categoryBits, final short maskBits, final short groupIndex, final BodyDef.BodyType box2dBodyType,
            final boolean sensor, final Vector2 sizeMultiplier, final boolean traversable)
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
        this.sensor = sensor;
        this.sizeMultiplier = sizeMultiplier;
        this.traversable = traversable;
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

    public boolean isSensor()
    {
        return sensor;
    }

    public Vector2 getSizeMultiplier()
    {
        return sizeMultiplier;
    }

    public boolean isTraversable()
    {
        return traversable;
    }
}