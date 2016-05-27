package com.murder.game.level;

import java.util.List;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.physics.box2d.World;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.murder.game.constants.box2d.BodyType;
import com.murder.game.drawing.Mob;
import com.murder.game.serialize.MyVector2;
import com.murder.game.utils.LightBuilder;

import box2dLight.RayHandler;

public class Exit extends Tile
{
    private static final int DISTANCE = 15;

    @JsonCreator
    public Exit(@JsonProperty(BODY_TYPE) final BodyType bodyType, @JsonProperty(POSITION) final MyVector2 position,
            @JsonProperty(ROTATION) final float rotation)
    {
        super(bodyType, position, rotation);
    }

    public void init(final World physicsWorld, final List<Mob> mobs, final RayHandler rayHandler)
    {
        super.init(physicsWorld, mobs);
        createLight(rayHandler);
    }

    protected void createLight(final RayHandler rayHandler)
    {
        LightBuilder.createPointLight(rayHandler, body, new Color(1f, 1f, 1f, .84f), DISTANCE);
    }
}