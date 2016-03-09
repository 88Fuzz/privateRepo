package com.murder.game.drawing;

import java.util.ArrayList;
import java.util.List;

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

    public Flashlight()
    {
        beamIncrement = 2;
        numberOfBeams = 220;
        flashlightAngle = 110;
        vertices = new float[(numberOfBeams + 9) * 2];
        beams = new float[(numberOfBeams + 1) * 2];
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
        int i;
        int offset = 0;
        vertices[offset++] = 0;
        vertices[offset++] = 0;

        vertices[offset++] = position.x;
        vertices[offset++] = 0;

        beams[0] = position.x;
        beams[1] = position.y;
        int lowestBeamPos = 0;
        float lowestBeamValue = position.y;
        for(i = 1; i < numberOfBeams + 1; i++)
        {
            final float angle = baseAngle + deltaAngle * (i - 1);

            final Vector2 beamEndPos = getBeamEnd(level, position, angle);
            beams[i * 2] = beamEndPos.x;
            beams[i * 2 + 1] = beamEndPos.y;

            // System.out.println("lowestBeamValue: " + lowestBeamValue +
            // " beamPos: " + beamEndPos + " positionX: " + position.x +
            // " delta: " + deltaAngle);
            if(beamEndPos.y < lowestBeamValue && beamEndPos.x - MIN_BEAM_POS_THRESHOLD < position.x
                    && beamEndPos.x + MIN_BEAM_POS_THRESHOLD > position.x)
            {
                lowestBeamValue = beamEndPos.y;
                lowestBeamPos = i * 2;
            }
        }

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