package com.murder.game.drawing;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.PolygonRegion;
import com.badlogic.gdx.graphics.g2d.PolygonSprite;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.EarClippingTriangulator;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ShortArray;
import com.murder.game.constants.TextureConstants;
import com.murder.game.level.Level;
import com.murder.game.level.Tile;

public class Flashlight implements DrawablePolygon
{
    private class FlashlightProcessor implements Runnable
    {
        private Level level;
        private int lowestBeamPos;
        private float lowestBeamValue;
        private int beamStart;
        private int beamEnd;
        private float baseAngle;
        private float deltaAngle;
        private Vector2 position;

        public void init(final Level level, final int beamStart, final int beamEnd, final float baseAngle, final float deltaAngle,
                final Vector2 position)
        {
            this.level = level;
            this.beamStart = beamStart;
            this.beamEnd = beamEnd;
            this.baseAngle = baseAngle;
            this.deltaAngle = deltaAngle;
            this.position = position;
            this.lowestBeamPos = beamStart;
            this.lowestBeamValue = position.y;
        }

        @Override
        public void run()
        {
            for(int i = beamStart; i < beamEnd + 1; i++)
            {
                final float angle = baseAngle + deltaAngle * (i - 1);

                final Vector2 beamEndPos = getBeamEnd(level, position, angle);
                beams[i * 2] = beamEndPos.x;
                beams[i * 2 + 1] = beamEndPos.y;

                if(beamEndPos.y < lowestBeamValue && beamEndPos.x - MIN_BEAM_POS_THRESHOLD < position.x
                        && beamEndPos.x + MIN_BEAM_POS_THRESHOLD > position.x)
                {
                    lowestBeamValue = beamEndPos.y;
                    lowestBeamPos = i * 2;
                }
            }
        }

        public float getLowestBeamValue()
        {
            return lowestBeamValue;
        }

        public int getLowestBeamPos()
        {
            return lowestBeamPos;
        }
    }

    private static final int MAX_THREAD_POOL_SIZE = 4;
    private static final int MIN_BEAM_POS_THRESHOLD = 5;
    private float playerSize;
    private float beamIncrement;
    private int numberOfBeams;
    private float flashlightAngle;
    private float beamLength;
    private PolygonSprite polySprite;
    private List<Sprite> flashlightSprites;
    private float[] vertices;
    private float[] beams;
    private FlashlightProcessor[] flashlightProcessors;

    public Flashlight()
    {
        beamIncrement = 5;
        // numberOfBeams = 220 / 2;
        numberOfBeams = 12;
        flashlightAngle = 110;
        vertices = new float[(numberOfBeams + 9) * 2];
        beams = new float[(numberOfBeams + 1) * 2];

        flashlightProcessors = new FlashlightProcessor[MAX_THREAD_POOL_SIZE];
        for(int i = 0; i < MAX_THREAD_POOL_SIZE; i++)
        {
            flashlightProcessors[i] = new FlashlightProcessor();
        }
    }

    public void init(final TextureAtlas textureAtlas, final float spriteSize)
    {
        this.playerSize = spriteSize;
        beamLength = spriteSize * 2.7f;
        flashlightSprites = new ArrayList<Sprite>(numberOfBeams);
        for(int i = 0; i < numberOfBeams; i++)
        {
            final Sprite sprite = new Sprite(textureAtlas.findRegion(TextureConstants.SINGLE_PIXEL_TEXTURE));
            sprite.setOrigin(0, 0);
            sprite.setColor(Color.RED);
            flashlightSprites.add(sprite);
        }
    }

    public void update(final Level level, final Vector2 position, final float rotation)
    {
        final float baseAngle = rotation - flashlightAngle / 2;
        final float deltaAngle = flashlightAngle / numberOfBeams;
        final int processorBeams = numberOfBeams / MAX_THREAD_POOL_SIZE;
        int beamStart = 0;
        int beamEnd = 0;
        int offset = 0;

        /*
         * TODO check if the main thread should process one of the beams or just
         * have the executor run all threads.
         */
        final ExecutorService executor = Executors.newFixedThreadPool(MAX_THREAD_POOL_SIZE);
        for(int i = 0; i < MAX_THREAD_POOL_SIZE; i++)
        {
            beamStart = beamEnd + 1;
            beamEnd += processorBeams;
            flashlightProcessors[i].init(level, beamStart, beamEnd, baseAngle, deltaAngle, position);
            executor.execute(flashlightProcessors[i]);
        }

        vertices[offset++] = 0;
        vertices[offset++] = 0;

        vertices[offset++] = position.x;
        vertices[offset++] = 0;

        beams[0] = position.x;
        beams[1] = position.y;

        executor.shutdown();
        while(!executor.isTerminated());
        int lowestBeamPos = 0;
        float lowestBeamValue = position.y;

        for(int i = 0; i < MAX_THREAD_POOL_SIZE; i++)
        {
            if(flashlightProcessors[i].getLowestBeamValue() < lowestBeamValue)
            {
                lowestBeamValue = flashlightProcessors[i].getLowestBeamValue();
                lowestBeamPos = flashlightProcessors[i].getLowestBeamPos();
            }
        }

        System.out.println("lowestValue " + lowestBeamValue + " lowestPos " + lowestBeamPos);
        for(int i = 0; i < beams.length;)
        {
            System.out.print("[" + beams[i++] + ", " + beams[i++] + "] ");
        }
        System.out.println("");

        // for(int i = 1; i < numberOfBeams + 1; i++)
        // {
        // final float angle = baseAngle + deltaAngle * (i - 1);
        //
        // final Vector2 beamEndPos = getBeamEnd(level, position, angle);
        // beams[i * 2] = beamEndPos.x;
        // beams[i * 2 + 1] = beamEndPos.y;
        //
        // if(beamEndPos.y < lowestBeamValue && beamEndPos.x -
        // MIN_BEAM_POS_THRESHOLD < position.x
        // && beamEndPos.x + MIN_BEAM_POS_THRESHOLD > position.x)
        // {
        // lowestBeamValue = beamEndPos.y;
        // lowestBeamPos = i * 2;
        // }
        // }

        System.arraycopy(beams, lowestBeamPos, vertices, offset, beams.length - lowestBeamPos);
        offset += beams.length - lowestBeamPos;
        System.arraycopy(beams, 0, vertices, offset, lowestBeamPos);
        offset += lowestBeamPos;

        vertices[offset++] = beams[lowestBeamPos];
        vertices[offset++] = beams[lowestBeamPos + 1];

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

    private Vector2 getBeamEnd(final Level level, final Vector2 position, final float angle)
    {
        final Vector2 endPos = new Vector2();
        for(float length = playerSize / 2 + beamIncrement; length < beamLength; length += beamIncrement)
        {
            endPos.x = (float) (position.x + length * Math.sin(Math.toRadians(angle)));
            endPos.y = (float) (position.y + length * Math.cos(Math.toRadians(angle)));

            final Tile tile = level.getTile((int) (endPos.x / playerSize), (int) (endPos.y / playerSize));
            if(tile == null || tile.isLocked())
                return endPos;
        }

        return endPos;
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
}