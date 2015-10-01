package com.libgdx.airplane.game.drawable.weapons;

/**
 * Interface for any objects that can be damaged.
 * 
 */
public interface Hittable
{
    /**
     * Called if the object gets hit. TODO: implement hit with damage amount and
     * damage type.
     * 
     * @return The amount of damage done in hit.
     */
    public float hit();

    /**
     * Called if the object should be killed immediately.
     * 
     * @return The amount of damage done by kill.
     */
    public float kill();

}
