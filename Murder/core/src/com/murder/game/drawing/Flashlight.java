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
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.EarClippingTriangulator;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ShortArray;
import com.murder.game.constants.TextureConstants;
import com.murder.game.level.Level;
import com.murder.game.level.Tile;

public class Flashlight implements DrawablePolygon
{
    private float playerSize;
    private float beamIncrement;
    private int numberOfBeams;
    private float flashlightAngle;
    private float beamLength;
    private PolygonSprite polySprite;
    private List<Sprite> flashlightSprites;
    private float[] vertices;

    public Flashlight()
    {
        beamIncrement = 2;
        numberOfBeams = 220;
        flashlightAngle = 110;
        vertices = new float[(numberOfBeams + 9) * 2];
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

    public void draw(final SpriteBatch batch)
    {
        // for(final Sprite beam: flashlightSprites)
        // {
        // beam.draw(batch);
        // }
    }

    public void update(final Level level, final Vector2 position, final float rotation)
    {
        // TODO constantly allocating beams is most likely not efficient
        final float beams[] = new float[(numberOfBeams + 1) * 2];
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

            if((int) beamEndPos.x == (int) position.x && beamEndPos.y < lowestBeamValue)
            {
                lowestBeamValue = beamEndPos.y;
                lowestBeamPos = i * 2;
            }
        }

        vertices[offset++] = beams[lowestBeamPos];
        vertices[offset++] = beams[lowestBeamPos + 1];

        int lcv = lowestBeamPos + 2;
        while(lcv != lowestBeamPos)
        {
            vertices[offset++] = beams[lcv++];
            if(lcv > (numberOfBeams + 1) * 2 - 1)
                lcv = 0;
        }

        vertices[offset++] = beams[lowestBeamPos];
        vertices[offset++] = beams[lowestBeamPos + 1];

        vertices[offset++] = position.x;
        vertices[offset++] = 0;

        // TODO 100000 should be the size of the level, not just random numbers
        vertices[offset++] = 100000;
        vertices[offset++] = 0;

        vertices[offset++] = 100000;
        vertices[offset++] = 100000;

        vertices[offset++] = 0;
        vertices[offset++] = 100000;

        vertices[offset++] = 0;
        vertices[offset++] = 0;

        updatePolySprite();
    }

    private float getBeamLength(final Level level, final Vector2 position, final float angle)
    {
        for(float length = playerSize / 2 + beamIncrement; length < beamLength; length += beamIncrement)
        {
            final float x = (float) (position.x + length * Math.sin(Math.toRadians(angle)));
            final float y = (float) (position.y + length * Math.cos(Math.toRadians(angle)));

            final Tile tile = level.getTile((int) (x / playerSize), (int) (y / playerSize));
            if(tile == null || tile.isLocked())
                return length - beamIncrement;
        }

        return beamLength;
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
        // pix.setColor(0xCCAAFFEF);
        pix.setColor(0x000000FF);
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