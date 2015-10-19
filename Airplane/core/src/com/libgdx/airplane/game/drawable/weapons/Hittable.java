package com.libgdx.airplane.game.drawable.weapons;

/**
 * Interface for any objects that can be damaged. Anything that can take damage
 * can cause damage
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
    public float hit(final int damageTaken);

    /**
     * Called if the object should be killed immediately.
     * 
     * @return The amount of damage done by kill.
     */
    public float kill();

    /**
     * Gets the damage that the Hittable object can inflict.
     * 
     * @return
     */
    public int getAttackDamage();

    /**
     * Gets the damage type that the Hittable object inflicts. 
     * 
     * TODO: this needs
     * to be implemented
     * 
     * @return
     */
    public void getAttackDamageType();
}
