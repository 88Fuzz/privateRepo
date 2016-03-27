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

    // TODO change this to .5? maybe one? or just keep at 5
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
        prevRotation = -1;
        prevPosition = new Vector2();

        this.beams = beams;
        this.vertices = vertices;
    }

    public void init(final Level level, final Vector2 position, final float spriteSize, final float rotation)
    {
        lowestBeam = new Beam();
        lowestBeam.setEndPos(position.x, position.y);

        this.playerSize = spriteSize;
        maxbeamLength = spriteSize * BEAM_LENGTH_MULTIPLIER;

        prevRotation = rotation;
        prevPosition.x = position.x;
        prevPosition.y = position.y;

        setAllFlashlightBeams(level, position, rotation);
        updateVertices(level, position);
    }

    public void update(final Level level, final Vector2 position, final float rotation)
    {
        boolean updateVertices = false;
        boolean positionUpdated = false;
        final float roundedRotation = roundToHalf(rotation);
        
        System.out.println("prevPosition " + prevPosition + " new position " + position);
        if(!(prevPosition.x == position.x && prevPosition.y == position.y))
//        if(!prevPosition.equals(position))
        {
            System.out.println("POSITION CHANGED");
            positionUpdated = true;
            lowestBeam = new Beam();
            lowestBeam.getEndPos().x = position.x;
            lowestBeam.getEndPos().y = position.y;
        }

        if(prevRotation != roundedRotation)
        {
            processRotation(level, position, roundedRotation, positionUpdated);
            prevRotation = roundedRotation;
            updateVertices = true;
        }
        /*
         * when the position moves, there is most likely (but not always) a rotation change too. When updating the rest of the 
         * flashlight beams, it shouldn't update the beams that were moved from the rotation.
         */
        else if(positionUpdated)
        {
            processPosition(level, position, RotationDirection.CLOCKWISE, 0);
            updateVertices = true;
        }

        if(updateVertices)
        {
            updateVertices(level, position);
        }
        prevPosition.x = position.x;
        prevPosition.y = position.y;
    }

    // TODO this code is riddled with assumptions of the incrementation of the
    // angles to be 0.5
    private void processRotation(final Level level, final Vector2 position, final float rotation, final boolean positionUpdated)
    {
        final RotationDirection rotationDirection = RotationUtils.getRotationDirection(prevRotation, rotation);
        final int adjustments = (int) (RotationUtils.getRotationDistance(prevRotation, rotation) / DELTA_ANGLE);

        System.out.println("prevRotation " + prevRotation + " rotation " + rotation + " adjustments " + adjustments);
        System.out.println("prevPosition " + prevPosition + " position " + position);

        // If the number of beams to update is greater than the number of beams
        // in the flashlight, it is best to just redraw all beams
        if(adjustments > NUMBER_OF_BEAMS)
        {
            setAllFlashlightBeams(level, position, rotation);
            return;
        } 

        if(rotationDirection == RotationDirection.COUNTER_CLOCKWISE)
        {
            float baseAngle = roundToHalf(rotation - FLASHLIGHT_ANGLE / 2) + adjustments * DELTA_ANGLE;
//            System.out.println("counter " + baseAngle);
            // Take from the back and add to the front.
            for(int i = 0; i < adjustments; i++)
            {
                final Beam moving = beams.pollTail();

                calculateBeamEnd(level, moving, baseAngle, position);
                beams.addHead(moving);
                baseAngle -= DELTA_ANGLE;
            }
//            System.out.println("");
        }
        else
        {
            float baseAngle = roundToHalf(rotation + FLASHLIGHT_ANGLE / 2) - adjustments * DELTA_ANGLE;
//            System.out.println("clock " + baseAngle);
            // Take from the front and add to the back.
            for(int i = 0; i < adjustments; i++)
            {
                final Beam moving = beams.pollHead();

                calculateBeamEnd(level, moving, baseAngle, position);
                beams.addTail(moving);
                baseAngle += DELTA_ANGLE;
            }
        }

        if(positionUpdated)
            processPosition(level, position, rotationDirection, adjustments);
    }

    private void processPosition(final Level level, final Vector2 position, final RotationDirection rotationDirection, final int adjustments)
    {
        int localAdjustments = NUMBER_OF_BEAMS;
        //If the flashlight has been rotated clockwise, every adjustments number of beams has already been updated from the head.
        if(rotationDirection == RotationDirection.CLOCKWISE)
        {
            Beam beam = beams.getTail();
            while(beam != null && adjustments < localAdjustments)
            {
                calculateBeamEnd(level, beam, beam.getAngle(), position);

                beam = beam.getPrevBeam();
                localAdjustments--;
            }
        }
        else
        {
            Beam beam = beams.getHead();
            while(beam != null && adjustments < localAdjustments)
            {
                calculateBeamEnd(level, beam, beam.getAngle(), position);

                beam = beam.getNextBeam();
                localAdjustments--;
            }
            
        }
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

        // If the flashlight beam is lower than the position, around the x value
        // of the position. The flashlight will not render correctly so we need
        // to record it.

        if(isLowestBeam(beam.getEndPos(), position, lowestBeam.getEndPos().y))
        {
            System.out.println("SETTING LOWEST BEAM: beam " + beam.getEndPos() + " angle " + angle + " lowestBeam y " + lowestBeam.getEndPos().y);
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
//        System.out.println("ALL");
        final float baseAngle = rotation - FLASHLIGHT_ANGLE / 2;
        Beam currBeam = beams.getHead();
        int counter = 0;

        while(currBeam != null)
        {
            final float angle = baseAngle + DELTA_ANGLE * counter;

            // TODO This is 100% wrong. Based on the tile BEAM_ADDER or
            // BEAM_SUBBER should be used. Not hard coded to a single one
            calculateBeamEnd(level, currBeam, angle, position);

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

        //I don't think this is needed anymore
//        if(!isLowestBeam(lowestBeam.getEndPos(), position, position.y))
//        {
//            System.out.println("BWAAAAAAAAH " + lowestBeam.getEndPos());
//            lowestBeam = new Beam();
//            lowestBeam.setEndPos(position.x, position.y);
//        }

        System.out.println("lowestBeam " + lowestBeam.getEndPos() + " position " + position);
        if(lowestBeam.getEndPos().y != position.y)
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

        
//        prevRotation 90.0 rotation 54.5 adjustments 71
//        prevPosition [303.3091:502.39383] position [303.3091:502.39383]
//        lowestBeam [300.0:500.0] position [303.3091:502.39383]
//        [300.0:500.0] -1.0 0.0 true
//        Resetting to head
//        [303.3091:798.3938] 0.0 296.0 true
//        [305.89218:798.38257] 0.5 296.0 true
//        [308.47504:798.34875] 1.0 296.0 true
//        [311.0575:798.2924] 1.5 296.0 true
//        [313.63937:798.2135] 2.0 296.0 true
//        [316.22046:798.1121] 2.5 296.0 true
//        [318.9052:799.9854] 3.0 298.0 true
//        [321.5016:799.838] 3.5 298.0 true
//        [324.09653:799.6679] 4.0 298.0 true
//        [326.6899:799.4752] 4.5 298.0 true
//        [329.28152:799.2598] 5.0 298.0 true
//        [331.87115:799.0219] 5.5 298.0 true
//        [334.4586:798.76135] 6.0 298.0 true
//        
//        when the position changes, the lowestBeam doesn't change and it's fucking everything up because it's never existed and can't complete the while loop below
//
//        When the position changes, the lowestBeam should be set to the new position, otherwise the lowestBeam should stay the same value.

        while(starterBeam != null)
        {
            System.out.println(starterBeam.getEndPos() + " " + starterBeam.getAngle() + " " + starterBeam.getDistance() + " " + lowestBeamStart);
            vertices[offset++] = starterBeam.getEndPos().x;
            vertices[offset++] = starterBeam.getEndPos().y;

            starterBeam = starterBeam.getNextBeam();
            if(lowestBeamStart)
            {
                if(starterBeam == null)
                {
                    System.out.println("Resetting to head");
                    starterBeam = beams.getHead();

                    vertices[offset++] = position.x;
                    vertices[offset++] = position.y;
                }

                // In the chance that the lowest beam is also the first beam,
                // this cannot be an else if
                if(starterBeam == lowestBeam)
                {
                    System.out.println("Resetting to null");
                    starterBeam = null;
                }
            }
        }
//        System.out.println("");

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

        if(polySprite == null)
            polySprite = new PolygonSprite(polyReg);
        else
            polySprite.setRegion(polyReg);
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

    private boolean isLowestBeam(final Vector2 newPosition, final Vector2 basePosition, final float minValue)
    {
        if(newPosition.x - MIN_BEAM_THRESHOLD < basePosition.x && newPosition.x + MIN_BEAM_THRESHOLD > basePosition.x && newPosition.y < minValue)
            return true;

        return false;
    }
}