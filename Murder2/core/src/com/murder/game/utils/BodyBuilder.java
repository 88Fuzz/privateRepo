package com.murder.game.utils;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;
import com.murder.game.constants.box2d.BodyType;
import com.murder.game.constants.drawing.DisplayConstants;

public class BodyBuilder
{
    // TODO remove this method when no longer used
    public static Body createBox(final World world, final float x, final float y, final int width, final int height, final boolean isStatic,
            final boolean fixedRotation, final short category, final short groupIndex, final short maskBits)
    {
        Body pBody;
        BodyDef def = new BodyDef();

        if(isStatic)
            def.type = BodyDef.BodyType.StaticBody;
        else
            def.type = BodyDef.BodyType.DynamicBody;

        def.position.set(x / DisplayConstants.PIXELS_PER_METER, y / DisplayConstants.PIXELS_PER_METER);
        def.fixedRotation = fixedRotation;
        pBody = world.createBody(def);
        // pBody.setUserData("wall");

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width / 2 / DisplayConstants.PIXELS_PER_METER, height / 2 / DisplayConstants.PIXELS_PER_METER);

        FixtureDef fd = new FixtureDef();
        fd.shape = shape;
        fd.density = 1.0f;
        // fd.filter.categoryBits = Constants.BIT_WALL;
        // fd.filter.maskBits = Constants.BIT_PLAYER | Constants.BIT_WALL |
        // Constants.BIT_SENSOR;
        fd.filter.categoryBits = category;
        fd.filter.maskBits = maskBits;
        fd.filter.groupIndex = groupIndex;
        pBody.createFixture(fd);
        shape.dispose();
        return pBody;
    }

    public static Body createBody(final World world, final BodyType bodyType, final Vector2 position, final float rotation)
    {
        final BodyDef bodyDef = new BodyDef();
        bodyDef.fixedRotation = true;
        bodyDef.position.set(position.x / DisplayConstants.PIXELS_PER_METER, position.y / DisplayConstants.PIXELS_PER_METER);
        bodyDef.type = bodyType.getBox2dBodyType();

        final Shape shape;
        switch(bodyType.getBodyShape())
        {
        case CIRCLE:
            shape = new CircleShape();
            shape.setRadius(bodyType.getWidth() / 2 / DisplayConstants.PIXELS_PER_METER);
            break;
        case SQUARE:
            shape = new PolygonShape();
            ((PolygonShape) shape).setAsBox(bodyType.getWidth() / 2 / DisplayConstants.PIXELS_PER_METER,
                    bodyType.getHeight() / 2 / DisplayConstants.PIXELS_PER_METER);
            break;
        default:
            throw new IllegalArgumentException("I don't know what shape to make. Yo.");
        }

        final FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = bodyType.getDensity();
        fixtureDef.filter.categoryBits = bodyType.getCategoryBits();
        fixtureDef.filter.maskBits = bodyType.getMaskBits();
        fixtureDef.filter.groupIndex = bodyType.getGroupIndex();

        final Body body = world.createBody(bodyDef).createFixture(fixtureDef).getBody();
        /*
         * Box2d angles have a rotational original pointed to the right. And
         * positive angles move counter-clockwise.
         */
        body.setTransform(body.getPosition(), (rotation + 90) * MathUtils.degreesToRadians);
//        body.setTransform(body.getPosition(), rotation * MathUtils.degreesToRadians);

        return body;
    }
}
