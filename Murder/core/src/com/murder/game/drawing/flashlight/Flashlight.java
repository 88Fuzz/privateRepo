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
        
        /**
         * TODO remove.
         * @return
         */
        public String getName();
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
        
        @Override
        public String getName()
        {
            return "Adder";
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
        
        @Override
        public String getName()
        {
            return "Subber";
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
    private float maxBeamLength;
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
        maxBeamLength = spriteSize * BEAM_LENGTH_MULTIPLIER;

        prevRotation = rotation;
        prevPosition.x = position.x;
        prevPosition.y = position.y;

        setAllFlashlightBeams(level, position, rotation, 0);
        updateVertices(level, position);
    }

    //TODO this distanceTraveled shit breaks horribly when frame rate drops below 40 fps
    public void update(final Level level, final Vector2 position, final float rotation, final float distanceTraveled)
    {
        boolean updateVertices = false;
        boolean positionUpdated = false;
        final float roundedRotation = roundToHalf(rotation);

        if(!(prevPosition.x == position.x && prevPosition.y == position.y))
        {
            positionUpdated = true;
            //TODO you can have some static Beam that will never be in the beams list instead of always declaring a new beam.
            lowestBeam = new Beam();
            lowestBeam.getEndPos().x = position.x;
            lowestBeam.getEndPos().y = position.y;
        }

        if(prevRotation != roundedRotation)
        {
            processRotation(level, position, roundedRotation, positionUpdated, distanceTraveled);
            prevRotation = roundedRotation;
            updateVertices = true;
        }
        /*
         * when the position moves, there is most likely (but not always) a rotation change too. When updating the rest of the 
         * flashlight beams, it shouldn't update the beams that were moved from the rotation.
         */
        else if(positionUpdated)
        {
            System.out.println("position only");
            processPosition(level, position, RotationDirection.CLOCKWISE, 0, distanceTraveled);
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
    private void processRotation(final Level level, final Vector2 position, final float rotation, final boolean positionUpdated, final float distanceTraveled)
    {
        final RotationDirection rotationDirection = RotationUtils.getRotationDirection(prevRotation, rotation);
        final int adjustments = (int) (RotationUtils.getRotationDistance(prevRotation, rotation) / DELTA_ANGLE);

        System.out.println("prevRotation " + prevRotation + " rotation " + rotation + " adjustments " + adjustments);
        System.out.println("prevPosition " + prevPosition + " position " + position);

        // If the number of beams to update is greater than the number of beams
        // in the flashlight, it is best to just redraw all beams
        if(adjustments > NUMBER_OF_BEAMS)
        {
            setAllFlashlightBeams(level, position, rotation, distanceTraveled);
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

                calculateBeamEnd(level, moving, baseAngle, position, distanceTraveled);
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

                calculateBeamEnd(level, moving, baseAngle, position, distanceTraveled);
                beams.addTail(moving);
                baseAngle += DELTA_ANGLE;
            }
        }

        if(positionUpdated)
            processPosition(level, position, rotationDirection, adjustments, distanceTraveled);
    }

    private void processPosition(final Level level, final Vector2 position, final RotationDirection rotationDirection, final int adjustments, final float distanceTraveled)
    {
        int localAdjustments = NUMBER_OF_BEAMS;
        //If the flashlight has been rotated clockwise, every adjustments number of beams has already been updated from the head.
        if(rotationDirection == RotationDirection.CLOCKWISE)
        {
            Beam beam = beams.getTail();
            while(beam != null && adjustments < localAdjustments)
            {
                calculateBeamEnd(level, beam, beam.getAngle(), position, distanceTraveled);

                beam = beam.getPrevBeam();
                localAdjustments--;
            }
        }
        else
        {
            Beam beam = beams.getHead();
            while(beam != null && adjustments < localAdjustments)
            {
                calculateBeamEnd(level, beam, beam.getAngle(), position, distanceTraveled);

                beam = beam.getNextBeam();
                localAdjustments--;
            }
            
        }
    }

    private void calculateBeamEnd(final Level level, final Beam beam, final float angle, final Vector2 position, final float distanceTraveled)
    {
        final float x = getXFromDistance(position, angle, beam.getDistance());
        final float y = getYFromDistance(position, angle, beam.getDistance());

        final Tile tile = getTile(level, x, y);
        // If the tile is currently in a valid position (openTile), keep moving
        // the beam forward until it runs out of length or hits an invalid tile.
        if(isValidTile(tile))
        {
            calculateBeamEnd(BEAM_ADDER, level, beam, angle, position, distanceTraveled);
            return;
        }

        // If the tile is currently in a invalid position (wall), keep moving
        // the beam back until it is a valid tile.
        calculateBeamEnd(BEAM_SUBBER, level, beam, angle, position, distanceTraveled);
    }

    private void calculateBeamEnd(final BeamIncrementor beamIncrementor, final Level level, final Beam beam, final float angle,
            final Vector2 position, final float distanceTraveled)
    {
        beam.setAngle(angle);
        beam.setDistance(getBeamEnd(beamIncrementor, level, position, beam.getAngle(), beam.getEndPos(), beam.getDistance(), distanceTraveled));

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
            final Vector2 endPos, final float length, final float distanceTraveled)
    {
        float nextDistance = length - distanceTraveled;
        float localDistance = nextDistance;
        float tmpX = endPos.x;
        float tmpY = endPos.y;
        Tile tile = null;

        do
        {
            localDistance = nextDistance;
            nextDistance = beamIncrementor.getNextDistance(localDistance);

            if(nextDistance > maxBeamLength)
            {
                System.out.println("MAX DISTANCE with angle " + angle + " incrementor " + beamIncrementor.getName() + " local " + localDistance + " max " +
                        maxBeamLength + " x " + tmpX + " y " + tmpY + " position " + position);
                nextDistance = localDistance;
                break;
            }

            tmpX = getXFromDistance(position, angle, nextDistance);
            tmpY = getYFromDistance(position, angle, nextDistance);

            tile = getTile(level, tmpX, tmpY);
        }while(beamIncrementor.continueBeam(tile));
        endPos.x = tmpX;
        endPos.y = tmpY;
//        System.out.println(" ending endPos " + endPos);

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

    this is somehow horribly broken at the start
    private void setAllFlashlightBeams(final Level level, final Vector2 position, final float rotation, final float distanceTraveled)
    {
//        System.out.println("ALL");
        final float baseAngle = rotation - FLASHLIGHT_ANGLE / 2;
        Beam currBeam = beams.getHead();
        int counter = 0;

        while(currBeam != null)
        {
            currBeam.setDistance(playerSize);
            final float angle = baseAngle + DELTA_ANGLE * counter;
            calculateBeamEnd(level, currBeam, angle, position, distanceTraveled);

            currBeam = currBeam.getNextBeam();
            counter++;
        }
    }

    private void updateVertices(final Level level, final Vector2 position)
    {
        final Rectangle levelBounds = level.getLevelBounds();
        levelBounds.x -= maxBeamLength;
        levelBounds.y -= maxBeamLength;
        levelBounds.width += maxBeamLength;
        levelBounds.height += maxBeamLength;

        int offset = 0;
        vertices[offset++] = levelBounds.x;
        vertices[offset++] = levelBounds.y;

        vertices[offset++] = position.x;
        vertices[offset++] = levelBounds.y;

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

        while(starterBeam != null)
        {
            System.out.println(starterBeam.getEndPos() + " " + starterBeam.getAngle() + " " + starterBeam.getDistance() + " " + lowestBeamStart );
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
        vertices[offset++] = levelBounds.y;

        vertices[offset++] = levelBounds.width;
        vertices[offset++] = levelBounds.y;

        vertices[offset++] = levelBounds.width;
        vertices[offset++] = levelBounds.height;

        vertices[offset++] = levelBounds.x;
        vertices[offset++] = levelBounds.height;

        vertices[offset++] = levelBounds.x;
        vertices[offset++] = levelBounds.y;

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