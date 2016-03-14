package com.murder.game.drawing;

import java.util.LinkedList;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.PolygonRegion;
import com.badlogic.gdx.graphics.g2d.PolygonSprite;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.EarClippingTriangulator;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ShortArray;
import com.murder.game.drawing.Actor.RotationDirection;
import com.murder.game.level.Level;
import com.murder.game.level.Tile;

public class Flashlight implements DrawablePolygon
{
    private interface BeamIncrementor
    {
        /**
         * Gets the next distance the beam is moving.
         * 
         * @param distance
         * @return
         */
        public float getNextDistance(final float distance);

        /**
         * Based on the tile the beam is currently at, return true if the beam
         * should keep moving or false if the beam should stop and use the
         * previous distance as the final distance.
         * 
         * @param tile
         * @return
         */
        public boolean continueBeam(final Tile tile);
    }

    private class Beam
    {
        private float angle;
        private Vector2 endPos = new Vector2();
        private float distance;

        public Beam()
        {
            angle = -1;
            endPos = new Vector2();
            distance = 0;
        }

        public void setAngle(final float angle)
        {
            this.angle = angle;
        }

        public float getAngle()
        {
            // TODO round this to some decimal place
            return angle;
        }

        public void setEndPos(final float x, final float y)
        {
            endPos.x = x;
            endPos.y = y;
        }

        public Vector2 getEndPos()
        {
            return endPos;
        }

        public void setDistance(final float distance)
        {
            this.distance = distance;
        }

        public float getDistance()
        {
            return distance;
        }
    }

    private static final BeamIncrementor beamAdder = new BeamIncrementor()
    {
        @Override
        public float getNextDistance(final float distance)
        {
            return distance + BEAM_INCREMENT;
        }

        @Override
        public boolean continueBeam(final Tile tile)
        {
            if(isValidTile(tile))
                return true;
            return false;
        }
    };

    private static final BeamIncrementor beamSubber = new BeamIncrementor()
    {
        @Override
        public float getNextDistance(final float distance)
        {
            return distance - BEAM_INCREMENT;
        }

        @Override
        public boolean continueBeam(final Tile tile)
        {
            if(isValidTile(tile))
                return false;
            return true;
        }
    };

    private static final int MIN_BEAM_POS_THRESHOLD = 5;
    private static final int BEAM_INCREMENT = 2;
    private static final int NUMBER_OF_BEAMS = 220;
    private static final int FLASHLIGHT_ANGLE = 110;

    private float prevRotation;
    private Vector2 prevPosition;
    private float playerSize;
    private float beamLength;
    private PolygonSprite polySprite;
    private float[] vertices;
    // private float[] beams;
    private LinkedList<Beam> beams;
    private float lowestBeamValue;
    private int lowestBeamPosition;

    public Flashlight()
    {
        prevRotation = -1;
        prevPosition = new Vector2();

        vertices = new float[(NUMBER_OF_BEAMS + 9) * 2];
        beams = new LinkedList<Beam>();
        for(int i = 0; i < NUMBER_OF_BEAMS; i++)
        {
            beams.add(new Beam());
        }
        // beams = new float[(numberOfBeams + 1) * 2];
    }

    public void init(final TextureAtlas textureAtlas, final float spriteSize, final Vector2 position, final float rotation)
    {
        this.playerSize = spriteSize;
        beamLength = spriteSize * 2.7f;
        // Set up the beams here so no special logic is needed in the
        // processRotation and processPosition methods
    }

    public void update(final Level level, final Vector2 position, final float rotation, final RotationDirection rotationDirection)
    {
        boolean updateVertices = false;
        final float roundedRotation = roundToHalf(rotation);
        // TODO do this with position too

        if(rotationDirection != RotationDirection.NONE)
        {
            processRotation(level, position, roundedRotation, rotationDirection);
            prevRotation = roundedRotation;
            updateVertices = true;
        }

        if(!position.equals(prevPosition))
        {
            processPosition(level, position);
            prevPosition.x = position.x;
            prevPosition.y = position.y;
            updateVertices = true;
        }

        if(updateVertices)
        {
            updateVertices(position);
        }
    }

    // TODO this code is riddled with assumptions of the incrementation of the
    // angles to be 0.5
    private void processRotation(final Level level, final Vector2 position, final float rotation, final RotationDirection rotationDirection)
    {
        final float deltaAngle = FLASHLIGHT_ANGLE / NUMBER_OF_BEAMS;
        final float baseAngle = roundToHalf(rotation - FLASHLIGHT_ANGLE / 2);
        final int adjustments = (int) Math.abs((prevRotation - rotation) * deltaAngle);

        if(rotationDirection == RotationDirection.COUNTER_CLOCKWISE)
        {
            // Take from the back and add to the front.
            for(int i = 0; i < adjustments; i++)
            {
                final Beam moving = beams.pollLast();
                final Beam first = beams.getFirst();

                moving.setAngle(first.getAngle() + baseAngle);
                moving.setDistance(getBeamEnd(level, position, moving.getAngle(), moving.getEndPos(), first.getDistance()));
                beams.addLast(moving);
            }
        }
        else
        {
            // Take from the front and add to the back.
            for(int i = 0; i < adjustments; i++)
            {
                final Beam moving = beams.pollFirst();
                final Beam last = beams.getLast();

                moving.setAngle(last.getAngle() + baseAngle);
                moving.setDistance(getBeamEnd(level, position, moving.getAngle(), moving.getEndPos(), last.getDistance()));
                beams.addLast(moving);

            }
        }
    }

    private void processPosition(final Level level, final Vector2 position)
    {

    }

    // public void update(final Level level, final Vector2 position, final float
    // rotation)
    // {
    // final float baseAngle = rotation - flashlightAngle / 2;
    // final float deltaAngle = flashlightAngle / numberOfBeams;
    // int i;
    // int offset = 0;
    // vertices[offset++] = 0;
    // vertices[offset++] = 0;
    //
    // vertices[offset++] = position.x;
    // vertices[offset++] = 0;
    //
    // // beams[0] = position.x;
    // // beams[1] = position.y;
    // int lowestBeamPos = 0;
    // float lowestBeamValue = position.y;
    // for(i = 1; i < numberOfBeams + 1; i++)
    // {
    // final float angle = baseAngle + deltaAngle * (i - 1);
    //
    // final Vector2 beamEndPos = getBeamEnd(level, position, angle);
    // // beams[i * 2] = beamEndPos.x;
    // // beams[i * 2 + 1] = beamEndPos.y;
    //
    // if(beamEndPos.y < lowestBeamValue && beamEndPos.x -
    // MIN_BEAM_POS_THRESHOLD < position.x
    // && beamEndPos.x + MIN_BEAM_POS_THRESHOLD > position.x)
    // {
    // lowestBeamValue = beamEndPos.y;
    // lowestBeamPos = i * 2;
    // }
    // }
    //
    // // System.arraycopy(beams, lowestBeamPos, vertices, offset, beams.length
    // // - lowestBeamPos);
    // // offset += beams.length - lowestBeamPos;
    // // System.arraycopy(beams, 0, vertices, offset, lowestBeamPos);
    // // offset += lowestBeamPos;
    //
    // // vertices[offset++] = beams[lowestBeamPos];
    // // vertices[offset++] = beams[lowestBeamPos + 1];
    //
    // vertices[offset++] = position.x;
    // vertices[offset++] = 0;
    //
    // final Rectangle levelBounds = level.getLevelBounds();
    // levelBounds.width += beamLength;
    // levelBounds.height += beamLength;
    // vertices[offset++] = levelBounds.width;
    // vertices[offset++] = 0;
    //
    // vertices[offset++] = levelBounds.width;
    // vertices[offset++] = levelBounds.height;
    //
    // vertices[offset++] = 0;
    // vertices[offset++] = levelBounds.height;
    //
    // vertices[offset++] = 0;
    // vertices[offset++] = 0;
    //
    // updatePolySprite();
    // }

    private float getBeamEnd(final Level level, final Vector2 position, final float angle, final Vector2 endPos, final float length)
    {
        final Tile tile = getTile(level, endPos.x, endPos.y);
        // If the tile is currently in a valid position (openTile), keep moving
        // the beam forward until it runs out of length or hits an invalid tile.
        if(isValidTile(tile))
        {
            return getBeamEnd(beamAdder, level, position, angle, endPos, length);
        }

        // If the tile is currently in a invalid position (wall), keep moving
        // the beam back until it is a valid tile.
        return getBeamEnd(beamSubber, level, position, angle, endPos, length);
    }

    private float getBeamEnd(final BeamIncrementor beamIncrementor, final Level level, final Vector2 position, final float angle,
            final Vector2 endPos, final float length)
    {
        float localLength = length;
        float nextDistance = length;
        float tmpX = position.x;
        float tmpY = position.y;
        Tile tile = null;

        do
        {
            localLength = nextDistance;
            nextDistance = beamIncrementor.getNextDistance(localLength);
            if(nextDistance > beamLength)
                break;

            tmpX = (float) (position.x + nextDistance * Math.sin(Math.toRadians(angle)));
            tmpY = (float) (position.y + nextDistance * Math.cos(Math.toRadians(angle)));

            tile = getTile(level, tmpX, tmpY);
        }while(beamIncrementor.continueBeam(tile));

        endPos.x = tmpX;
        endPos.y = tmpY;

        
        You need to figure out a way to move the data in beams to vertices to draw the flashlight. YO

            if(beamEndPos.y < lowestBeamValue && beamEndPos.x - MIN_BEAM_POS_THRESHOLD < position.x
                    && beamEndPos.x + MIN_BEAM_POS_THRESHOLD > position.x)
            {
                
            }

        return localLength;
    }

    private void updateVertices(final Vector2 position)
    {
        int offset = 0;
        vertices[offset++] = 0;
        vertices[offset++] = 0;

        vertices[offset++] = position.x;
        vertices[offset++] = 0;

        // beams[0] = position.x;
        // beams[1] = position.y;
        int lowestBeamPos = 0;
        float lowestBeamValue = position.y;
        for(i = 1; i < numberOfBeams + 1; i++)
        {
            final float angle = baseAngle + deltaAngle * (i - 1);

            final Vector2 beamEndPos = getBeamEnd(level, position, angle);
            // beams[i * 2] = beamEndPos.x;
            // beams[i * 2 + 1] = beamEndPos.y;

            if(beamEndPos.y < lowestBeamValue && beamEndPos.x - MIN_BEAM_POS_THRESHOLD < position.x
                    && beamEndPos.x + MIN_BEAM_POS_THRESHOLD > position.x)
            {
                lowestBeamValue = beamEndPos.y;
                lowestBeamPos = i * 2;
            }
        }

        // System.arraycopy(beams, lowestBeamPos, vertices, offset, beams.length
        // - lowestBeamPos);
        // offset += beams.length - lowestBeamPos;
        // System.arraycopy(beams, 0, vertices, offset, lowestBeamPos);
        // offset += lowestBeamPos;

        // vertices[offset++] = beams[lowestBeamPos];
        // vertices[offset++] = beams[lowestBeamPos + 1];

        vertices[offset++] = position.x;
        vertices[offset++] = 0;

        final Rectangle levelBounds = level.getLevelBounds();
        levelBounds.width += beamLength;
        levelBounds.height += beamLength;
        vertices[offset++] = levelBounds.width;
        vertices[offset++] = 0;

        vertices[offset++] = levelBounds.width;
        vertices[offset++] = levelBounds.height;

        vertices[offset++] = 0;
        vertices[offset++] = levelBounds.height;

        vertices[offset++] = 0;
        vertices[offset++] = 0;

        updatePolySprite();
    }

    private void updatePolySprite()
    {
        final Pixmap pix = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pix.setColor(0xCCAAFFEF);
        // pix.setColor(0x000000FF);
        pix.fill();
        final Texture textureSolid = new Texture(pix);
        final TextureRegion textureRegion = new TextureRegion(textureSolid);

        final EarClippingTriangulator triangulator = new EarClippingTriangulator();
        final ShortArray triangleIndices = triangulator.computeTriangles(vertices);
        final PolygonRegion polyReg = new PolygonRegion(textureRegion, vertices, triangleIndices.toArray());

        polySprite = new PolygonSprite(polyReg);
    }

    @Override
    public void draw(final PolygonSpriteBatch polyBatch)
    {
        polySprite.draw(polyBatch);
    }

    private Tile getTile(final Level level, final float tmpX, final float tmpY)
    {
        return level.getTile((int) (tmpX / playerSize), (int) (tmpY / playerSize));
    }

    private static boolean isValidTile(final Tile tile)
    {
        return(tile != null && tile.isLocked());
    }

    private float roundToHalf(float x)
    {
        return (float) (Math.ceil(x * 2) * 0.5);
    }
}