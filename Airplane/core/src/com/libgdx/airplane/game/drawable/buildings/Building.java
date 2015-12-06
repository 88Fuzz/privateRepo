package com.libgdx.airplane.game.drawable.buildings;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.World;
import com.libgdx.airplane.game.constants.TextureConstants;
import com.libgdx.airplane.game.drawable.AbstractDrawable;
import com.libgdx.airplane.game.drawable.weapons.Hittable;
import com.libgdx.airplane.game.utils.GraphicsUtils;
import com.libgdx.airplane.game.utils.MapDetails;

public class Building extends AbstractDrawable implements Hittable
{
    private float health;

    public Building(final TextureAtlas atlas, final World physicsWorld, final MapDetails mapDetails,
            final Vector2 position, final float width, final float height)
    {
        super();
        init(atlas, physicsWorld, mapDetails, position, width, height);
    }

    public void init(final TextureAtlas atlas, final World physicsWorld, final MapDetails mapDetails,
            final Vector2 position, final float width, final float height)
    {
        final BodyDef defaultDynamicBodyDef = new BodyDef();
        defaultDynamicBodyDef.type = BodyType.DynamicBody;

        final PolygonShape square = new PolygonShape();
        square.setAsBox(width / 2, height / 2);

        final FixtureDef boxFixtureDef = new FixtureDef();
        boxFixtureDef.shape = square;
        boxFixtureDef.density = 0;
        boxFixtureDef.friction = 1;
        boxFixtureDef.restitution = 1;

        defaultDynamicBodyDef.position.set(position.x, position.y);
        Body body = physicsWorld.createBody(defaultDynamicBodyDef);
        body.createFixture(boxFixtureDef);

        super.init(body, new Vector2(width, height), mapDetails, true);
        GraphicsUtils.applyTextureRegion(sprite, atlas.findRegion(TextureConstants.SINGLE_PIXEL));
        GraphicsUtils.applySpriteToBody(sprite, bodySize);
        sprite.setColor(Color.DARK_GRAY);

//        sprite.setBounds(0, 0, width, height);
//        sprite.setPosition(position.x, position.y);

        // TODO health should be looked up by a building type look up table
        health = 100;
    }

    @Override
    public void update(float dt)
    {
        // Do nothing. It's a fucking building... for now
    }

    @Override
    public float kill()
    {
        final float damageDone = health;
        setAlive(false);
        health = 0;
        return damageDone;
    }

    @Override
    protected void drawCurrent(SpriteBatch batch)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public float hit(int damageTaken)
    {
        // TODO take into account damage type
        health -= damageTaken;
        if(health <= 0)
            kill();

        return damageTaken;
    }

    @Override
    public int getAttackDamage()
    {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void getAttackDamageType()
    {
        // TODO Auto-generated method stub

    }
}