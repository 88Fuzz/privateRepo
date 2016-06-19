package com.murder.game.contact;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.murder.game.drawing.drawables.Drawable;
import com.murder.game.drawing.drawables.Mob;
import com.murder.game.level.Door;
import com.murder.game.level.Exit;
import com.murder.game.level.Item;

public class WorldContactListener implements ContactListener
{
    private Map<Class, ContactChecker> checkerMap;

    public WorldContactListener()
    {
        checkerMap = new HashMap<Class, ContactChecker>();
        checkerMap.put(Door.class, new DoorContactChecker());
        checkerMap.put(Item.class, new ItemContactChecker());
        checkerMap.put(Mob.class, new MobActorContactChecker());
        checkerMap.put(Drawable.class, new DrawableContactChecker());
        checkerMap.put(Exit.class, new ExitContactChecker());
    }

    @Override
    public void beginContact(final Contact contact)
    {
        processContact(contact.getFixtureA(), contact.getFixtureB());
        processContact(contact.getFixtureB(), contact.getFixtureA());
    }

    private void processContact(final Fixture fixtureA, final Fixture fixtureB)
    {
        final Body bodyA = fixtureA.getBody();
        final Body bodyB = fixtureB.getBody();

        if(bodyA == null || bodyB == null)
            return;

        final Object userDataA = bodyA.getUserData();
        final Object userDataB = bodyB.getUserData();

        if(userDataA == null || userDataB == null)
            return;

        if(callContactChecker(userDataA, userDataB))
            return;

        callContactChecker(userDataB, userDataA);
    }

    private boolean callContactChecker(final Object objA, final Object objB)
    {
        final ContactChecker contactChecker = checkerMap.get(objA.getClass());

        return callContactChecker(contactChecker, objA, objB);
    }

    private boolean callContactChecker(final ContactChecker contactChecker, final Object objA, final Object objB)
    {
        if(contactChecker == null)
            return false;

        return contactChecker.check(objA, objB);
    }

    @Override
    public void endContact(final Contact contact) {}

    @Override
    public void preSolve(final Contact contact, final Manifold oldManifold) {}

    @Override
    public void postSolve(final Contact contact, final ContactImpulse impulse) {}
}
