package com.murder.game.utils;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.physics.box2d.Body;

import box2dLight.ConeLight;
import box2dLight.RayHandler;

public class LightBuilder
{
    public static ConeLight createConeLight(final RayHandler rayHandler, final Body body, final Color c, final float dist, final float dir,
            final float cone)
    {
        ConeLight cl = new ConeLight(rayHandler, 120, c, dist, 0, 0, dir, cone);
        cl.setSoftnessLength(0f);
        cl.attachToBody(body);
        cl.setXray(false);
        return cl;
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