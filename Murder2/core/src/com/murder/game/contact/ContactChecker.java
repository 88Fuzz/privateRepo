package com.murder.game.contact;

/**
 * Interface for objects that can collide and have an action happen.
 */
public interface ContactChecker
{
    /**
     * Object A and object B have collided, check if any actions need to be
     * taken. Return true if the collision is final and not checked in reverse.
     * 
     * @param objA
     * @param objB
     * @return
     */
    boolean check(final Object objA, final Object objB);
}