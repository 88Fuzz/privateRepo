package com.murder.game.drawing.flashlight;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.PolygonRegion;
import com.badlogic.gdx.graphics.g2d.PolygonSprite;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.EarClippingTriangulator;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ShortArray;
import com.murder.game.drawing.DrawablePolygon;
import com.murder.game.level.Level;
import com.murder.game.level.Tile;
import com.murder.game.utils.RotationUtils;
import com.murder.game.utils.RotationUtils.RotationDirection;

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

    protected static final BeamIncrementor BEAM_ADDER = new BeamIncrementor()
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

    protected static final BeamIncrementor BEAM_SUBBER = new BeamIncrementor()
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

    private static final int MIN_BEAM_THRESHOLD = 5;
    private static final int NUMBER_OF_BEAMS = 220;
    private static final int FLASHLIGHT_ANGLE = 110;
    private static final float DELTA_ANGLE = (float) FLASHLIGHT_ANGLE / NUMBER_OF_BEAMS;

    protected static final int BEAM_INCREMENT = 2;
    protected static final float BEAM_LENGTH_MULTIPLIER = 2.7f;

    private float prevRotation;
    private Vector2 prevPosition;
    private float playerSize;
    private float maxbeamLength;
    private PolygonSprite polySprite;
    private float[] vertices;
    private BeamList beams;
    private Beam lowestBeam;

    public Flashlight()
    {
        this(new BeamList(NUMBER_OF_BEAMS), new float[(NUMBER_OF_BEAMS + 9) * 2]);
    }

    protected Flashlight(final BeamList beams, final float[] vertices)
    {
        lowestBeam = new Beam();
        lowestBeam.setEndPos(Float.MAX_VALUE, Float.MAX_VALUE);

        prevRotation = -1;
        prevPosition = new Vector2();

        this.beams = beams;
        this.vertices = vertices;
    }

    public void init(final Level level, final Vector2 position, final float spriteSize, final float rotation)
    {
        this.playerSize = spriteSize;
        maxbeamLength = spriteSize * BEAM_LENGTH_MULTIPLIER;

        prevRotation = rotation;
        prevPosition = position;

        setAllFlashlightBeams(level, position, rotation);
        updateVertices(level, position);
    }

    public void update(final Level level, final Vector2 position, final float rotation)
    {
        boolean updateVertices = false;
        final float roundedRotation = roundToHalf(rotation);

        if(prevRotation != roundedRotation)
        {
            processRotation(level, position, roundedRotation);
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
            updateVertices(level, position);
        }
    }

    // TODO this code is riddled with assumptions of the incrementation of the
    // angles to be 0.5
    private void processRotation(final Level level, final Vector2 position, final float rotation)
    {
        final RotationDirection rotationDirection = RotationUtils.getRotationDirection(prevRotation, rotation);
        final int adjustments = (int) (RotationUtils.getRotationDistance(prevRotation, rotation) / DELTA_ANGLE);
        float baseAngle = roundToHalf(rotation - FLASHLIGHT_ANGLE / 2) + adjustments * DELTA_ANGLE;

        System.out.println("prevRotation " + prevRotation + " rotation " + rotation + " baseAngle " + baseAngle + " adjustments " + adjustments);

        // If the number of beams to update is greater than the number of beams
        // in the flashlight, it is best to just redraw all beams
        if(adjustments > NUMBER_OF_BEAMS)
        {
            setAllFlashlightBeams(level, position, rotation);
            return;
        }

        if(rotationDirection == RotationDirection.COUNTER_CLOCKWISE)
        {
            IF I continuously rotate this guy counter clockwise, it eventually fucks up :(
            System.out.println("counter " + adjustments);
            // Take from the back and add to the front.
            for(int i = 0; i < adjustments; i++)
            {
                final Beam moving = beams.pollTail();

                calculateBeamEnd(level, moving, baseAngle, position);
                beams.addHead(moving);
                baseAngle -= DELTA_ANGLE;
            }
        }
        else
        {
            System.out.println("clock " + adjustments);
            // Take from the front and add to the back.
            for(int i = 0; i < adjustments; i++)
            {
                final Beam moving = beams.pollHead();

                calculateBeamEnd(level, moving, baseAngle, position);
                beams.addTail(moving);
                baseAngle += DELTA_ANGLE;
            }
        }
    }

    private void processPosition(final Level level, final Vector2 position)
    {

    }

    private void calculateBeamEnd(final Level level, final Beam beam, final float angle, final Vector2 position)
    {
        final float x = getXFromDistance(position, angle, beam.getDistance());
        final float y = getYFromDistance(position, angle, beam.getDistance());

        final Tile tile = getTile(level, x, y);
        // If the tile is currently in a valid position (openTile), keep moving
        // the beam forward until it runs out of length or hits an invalid tile.
        if(isValidTile(tile))
        {
            calculateBeamEnd(BEAM_ADDER, level, beam, angle, position);
            return;
        }

        // If the tile is currently in a invalid position (wall), keep moving
        // the beam back until it is a valid tile.
        calculateBeamEnd(BEAM_SUBBER, level, beam, angle, position);
    }

    private void calculateBeamEnd(final BeamIncrementor beamIncrementor, final Level level, final Beam beam, final float angle,
            final Vector2 position)
    {
        beam.setAngle(angle);
        beam.setDistance(getBeamEnd(beamIncrementor, level, position, beam.getAngle(), beam.getEndPos(), beam.getDistance()));

        if(beam.getEndPos().y < lowestBeam.getEndPos().y)
        {
            lowestBeam = beam;
        }
    }

    protected float getBeamEnd(final BeamIncrementor beamIncrementor, final Level level, final Vector2 position, final float angle,
            final Vector2 endPos, final float length)
    {
        float localDistance = length;
        float nextDistance = length;
        float tmpX = position.x;
        float tmpY = position.y;
        Tile tile = null;

        do
        {
            localDistance = nextDistance;
            nextDistance = beamIncrementor.getNextDistance(localDistance);

            if(nextDistance > maxbeamLength)
            {
                nextDistance = localDistance;
                break;
            }

            tmpX = getXFromDistance(position, angle, nextDistance);
            tmpY = getYFromDistance(position, angle, nextDistance);

            tile = getTile(level, tmpX, tmpY);
        }while(beamIncrementor.continueBeam(tile));
        endPos.x = tmpX;
        endPos.y = tmpY;

        return nextDistance;
    }

    private float getXFromDistance(final Vector2 position, final float angle, final float distance)
    {
        return (float) (position.x + distance * Math.sin(Math.toRadians(angle)));
    }

    private float getYFromDistance(final Vector2 position, final float angle, final float distance)
    {
        return (float) (position.y + distance * Math.cos(Math.toRadians(angle)));
    }

    private void setAllFlashlightBeams(final Level level, final Vector2 position, final float rotation)
    {
        final float baseAngle = rotation - FLASHLIGHT_ANGLE / 2;
        Beam currBeam = beams.getHead();
        int counter = 0;

        while(currBeam != null)
        {
            final float angle = baseAngle + DELTA_ANGLE * counter;

            // TODO This is 100% wrong. Based on the tile BEAM_ADDER or
            // BEAM_SUBBER should be used. Not hard coded to a single one
            calculateBeamEnd(BEAM_ADDER, level, currBeam, angle, position);

            currBeam = currBeam.getNextBeam();
            counter++;
        }
    }

    private void updateVertices(final Level level, final Vector2 position)
    {
        int offset = 0;
        vertices[offset++] = 0;
        vertices[offset++] = 0;

        vertices[offset++] = position.x;
        vertices[offset++] = 0;

        Beam starterBeam;
        final boolean lowestBeamStart;
        if(lowestBeam.getEndPos().y < position.y && lowestBeam.getEndPos().x - MIN_BEAM_THRESHOLD < position.x
                && lowestBeam.getEndPos().x + MIN_BEAM_THRESHOLD > position.x)
        {
            lowestBeamStart = true;
            starterBeam = lowestBeam;
        }
        else
        {
            lowestBeamStart = false;
            starterBeam = beams.getHead();
            vertices[offset++] = position.x;
            vertices[offset++] = position.y;
        }

        while(starterBeam != null)
        {
            System.out.println(starterBeam.getEndPos() + " " + starterBeam.getAngle() + " " + starterBeam.getDistance());
            vertices[offset++] = starterBeam.getEndPos().x;
            vertices[offset++] = starterBeam.getEndPos().y;

            starterBeam = starterBeam.getNextBeam();
            if(lowestBeamStart)
            {
                if(starterBeam == null)
                {
                    starterBeam = beams.getHead();

                    vertices[offset++] = position.x;
                    vertices[offset++] = position.y;
                }
                else if(starterBeam == lowestBeam)
                    starterBeam = null;
            }
        }
        System.out.println("");

        if(lowestBeamStart)
        {
            vertices[offset++] = lowestBeam.getEndPos().x;
            vertices[offset++] = lowestBeam.getEndPos().y;
        }
        else
        {
            vertices[offset++] = position.x;
            vertices[offset++] = position.y;
        }

        vertices[offset++] = position.x;
        vertices[offset++] = 0;

        final Rectangle levelBounds = level.getLevelBounds();
        // levelBounds.width += beamLength;
        // levelBounds.height += beamLength;
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
        return(tile != null && !tile.isLocked());
    }

    private float roundToHalf(float x)
    {
        return (float) (Math.ceil(x * 2) * 0.5);
    }
}
