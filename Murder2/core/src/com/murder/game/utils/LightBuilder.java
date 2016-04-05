package com.murder.game.utils;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.physics.box2d.Body;
import com.murder.game.constants.box2d.BodyType;
import com.murder.game.constants.box2d.CollisionType;

import box2dLight.ConeLight;
import box2dLight.RayHandler;

public class LightBuilder
{
    // TODO delete this when no longer needed
    public static ConeLight createConeLight_old(final RayHandler rayHandler, final Body body, final Color c, final float dist, final float dir,
            final float cone)
    {
        ConeLight cl = new ConeLight(rayHandler, 120, c, dist, 0, 0, dir, cone);
        cl.setSoftnessLength(0f);
        cl.attachToBody(body);
        cl.setXray(false);
        return cl;
    }

    public static ConeLight createConeLight(final RayHandler rayHandler, final Body body, final Color color, final float distance,
            final float direction, final float coneSize)
    {
        final ConeLight coneLight = new ConeLight(rayHandler, 120, color, distance, 0, 0, direction, coneSize);
        coneLight.setSoftnessLength(0f);
        coneLight.attachToBody(body);
        coneLight.setXray(false);
        coneLight.setContactFilter(BodyType.PLAYER.getCategoryBits(), CollisionType.DEFAULT_GROUP_INDEX,
                (short) (CollisionType.DOOR.getCollisionValue() | CollisionType.WALL.getCollisionValue()));
        return coneLight;
    }
    // public static ConeLight createConeLight(RayHandler rayHandler, Body body,
    // Color c, float dist, float dir, float cone)
    // {
    // final ConeLight cl = new ConeLight(rayHandler, 120, c, dist, 0, 0, dir,
    // cone);
    // cl.setSoftnessLength(0f);
    // cl.attachToBody(body);
    // cl.setXray(false);
    // return cl;
    // }
}