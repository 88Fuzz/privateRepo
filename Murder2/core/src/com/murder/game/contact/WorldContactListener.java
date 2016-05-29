package com.murder.game.contact;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.murder.game.constants.box2d.BodyType;
import com.murder.game.constants.level.ItemType;
import com.murder.game.drawing.drawables.Actor;
import com.murder.game.drawing.drawables.Drawable;
import com.murder.game.level.Door;
import com.murder.game.level.Item;

public class WorldContactListener implements ContactListener
{
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

        // TODO This can be structured much better
        if(userDataA instanceof Drawable && userDataB instanceof Item)
        {
            final Drawable drawable = (Drawable) userDataA;
            final Item item = (Item) userDataB;

            // Player has picked up an item
            if(drawable.getBodyType() == BodyType.PLAYER)
                ((Actor) drawable).addItem(item.pickUpItem());
        }
        else if(userDataA instanceof Drawable && userDataB instanceof Door)
        {
            final Drawable drawable = (Drawable) userDataA;
            final Door door = (Door) userDataB;

            // Player is trying to unlock a door
            if(drawable.getBodyType() == BodyType.PLAYER)
            {
                final Actor player = (Actor) drawable;
                // TODO figure out if this will cause concurrent modification
                // exception
                for(final ItemType item: player.getItems())
                {
                    if(door.unlockDoor(item))
                    {
                        player.removeItem(item);
                        break;
                    }
                }
            }
        }
        else if(userDataA instanceof Drawable && userDataB instanceof Drawable)
        {
            final Drawable drawableA = (Drawable) userDataA;
            final Drawable drawableB = (Drawable) userDataB;

            if(drawableA.getBodyType() == BodyType.PLAYER && drawableB.getBodyType() == BodyType.EXIT)
                ((Actor) drawableA).setOnExit(true);
        }
    }

    @Override
    public void endContact(final Contact contact)
    {
        // TODO Auto-generated method stub
    }

    @Override
    public void preSolve(final Contact contact, final Manifold oldManifold)
    {
        // TODO Auto-generated method stub
    }

    @Override
    public void postSolve(final Contact contact, final ContactImpulse impulse)
    {
        // TODO Auto-generated method stub
    }
}
