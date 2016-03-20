package com.murder.game.drawing.flashlight;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.badlogic.gdx.math.Vector2;
import com.murder.game.level.Level;
import com.murder.game.level.Tile;

public class FlashlightTest
{
    private static final float TILE_SIZE = 200;
    private static final float COMPARISON_DELTA = 0.0001f;

    @Mock
    private Level mockLevel;
    @Mock
    private Tile mockInvalidTile;
    @Mock
    private Tile mockValidTile;

    private Flashlight target;

    @Before
    public void setUp()
    {
        MockitoAnnotations.initMocks(this);
        target = new Flashlight();
        target.init(null, new Vector2(), TILE_SIZE, 0);

        Mockito.when(mockValidTile.isLocked()).thenReturn(false);
        Mockito.when(mockInvalidTile.isLocked()).thenReturn(true);
    }

    @Test
    public void testGetBeamEndWithAdderBeamUp()
    {
        final float angle = 0;
        final Vector2 endPos = new Vector2();
        Mockito.when(mockLevel.getTile(Matchers.anyInt(), Matchers.anyInt())).thenReturn(mockValidTile);
        Mockito.when(mockLevel.getTile(0, 1)).thenReturn(mockInvalidTile);

        Assert.assertEquals(new Float(TILE_SIZE), new Float(target.getBeamEnd(Flashlight.BEAM_ADDER, mockLevel, new Vector2(0, 0), angle, endPos, 0)),
                COMPARISON_DELTA);
        Assert.assertEquals(new Float(0), new Float(endPos.x), COMPARISON_DELTA);
        Assert.assertEquals(new Float(TILE_SIZE), new Float(endPos.y), COMPARISON_DELTA);
    }

    @Test
    public void testGetBeamEndWithAdderMaxBeamUpLength()
    {
        final float angle = 0;
        final Vector2 endPos = new Vector2();
        Mockito.when(mockLevel.getTile(Matchers.anyInt(), Matchers.anyInt())).thenReturn(mockValidTile);

        Assert.assertEquals(new Float(TILE_SIZE * Flashlight.BEAM_LENGTH_MULTIPLIER),
                new Float(target.getBeamEnd(Flashlight.BEAM_ADDER, mockLevel, new Vector2(0, 0), angle, endPos, 0)), COMPARISON_DELTA);
        Assert.assertEquals(new Float(0), new Float(endPos.x), COMPARISON_DELTA);
        Assert.assertEquals(new Float(TILE_SIZE * Flashlight.BEAM_LENGTH_MULTIPLIER), new Float(endPos.y), COMPARISON_DELTA);
    }

    @Test
    public void testGetBeamEndWithAdderBeamRight()
    {
        final float angle = 90;
        final Vector2 endPos = new Vector2();
        Mockito.when(mockLevel.getTile(Matchers.anyInt(), Matchers.anyInt())).thenReturn(mockValidTile);
        Mockito.when(mockLevel.getTile(1, 0)).thenReturn(mockInvalidTile);

        Assert.assertEquals(new Float(TILE_SIZE), new Float(target.getBeamEnd(Flashlight.BEAM_ADDER, mockLevel, new Vector2(0, 0), angle, endPos, 0)),
                COMPARISON_DELTA);
        Assert.assertEquals(new Float(0), new Float(endPos.y), COMPARISON_DELTA);
        Assert.assertEquals(new Float(TILE_SIZE), new Float(endPos.x), COMPARISON_DELTA);
    }

    @Test
    public void testGetBeamEndWithAdderMaxBeamRightLength()
    {
        final float angle = 90;
        final Vector2 endPos = new Vector2();
        Mockito.when(mockLevel.getTile(Matchers.anyInt(), Matchers.anyInt())).thenReturn(mockValidTile);

        Assert.assertEquals(new Float(TILE_SIZE * Flashlight.BEAM_LENGTH_MULTIPLIER),
                new Float(target.getBeamEnd(Flashlight.BEAM_ADDER, mockLevel, new Vector2(0, 0), angle, endPos, 0)), COMPARISON_DELTA);
        Assert.assertEquals(new Float(0), new Float(endPos.y), COMPARISON_DELTA);
        Assert.assertEquals(new Float(TILE_SIZE * Flashlight.BEAM_LENGTH_MULTIPLIER), new Float(endPos.x), COMPARISON_DELTA);
    }

    @Test
    public void testGetBeamEndWithAdderBeamDown()
    {
        final float angle = 180;
        final Vector2 endPos = new Vector2();
        Mockito.when(mockLevel.getTile(Matchers.anyInt(), Matchers.anyInt())).thenReturn(mockValidTile);
        Mockito.when(mockLevel.getTile(0, -1)).thenReturn(mockInvalidTile);

        Assert.assertEquals(new Float(TILE_SIZE), new Float(target.getBeamEnd(Flashlight.BEAM_ADDER, mockLevel, new Vector2(0, 0), angle, endPos, 0)),
                COMPARISON_DELTA);
        Assert.assertEquals(new Float(0), new Float(endPos.x), COMPARISON_DELTA);
        Assert.assertEquals(new Float(-1 * TILE_SIZE), new Float(endPos.y), COMPARISON_DELTA);
    }

    @Test
    public void testGetBeamEndWithAdderMaxBeamDownLength()
    {
        final float angle = 180;
        final Vector2 endPos = new Vector2();
        Mockito.when(mockLevel.getTile(Matchers.anyInt(), Matchers.anyInt())).thenReturn(mockValidTile);

        Assert.assertEquals(new Float(TILE_SIZE * Flashlight.BEAM_LENGTH_MULTIPLIER),
                new Float(target.getBeamEnd(Flashlight.BEAM_ADDER, mockLevel, new Vector2(0, 0), angle, endPos, 0)), COMPARISON_DELTA);
        Assert.assertEquals(new Float(0), new Float(endPos.x), COMPARISON_DELTA);
        Assert.assertEquals(new Float(-1 * TILE_SIZE * Flashlight.BEAM_LENGTH_MULTIPLIER), new Float(endPos.y), COMPARISON_DELTA);
    }

    @Test
    public void testGetBeamEndWithAdderBeamLeft()
    {
        final float angle = 270;
        final Vector2 endPos = new Vector2();
        Mockito.when(mockLevel.getTile(Matchers.anyInt(), Matchers.anyInt())).thenReturn(mockValidTile);
        Mockito.when(mockLevel.getTile(-1, 0)).thenReturn(mockInvalidTile);

        Assert.assertEquals(new Float(TILE_SIZE), new Float(target.getBeamEnd(Flashlight.BEAM_ADDER, mockLevel, new Vector2(0, 0), angle, endPos, 0)),
                COMPARISON_DELTA);
        Assert.assertEquals(new Float(0), new Float(endPos.y), COMPARISON_DELTA);
        Assert.assertEquals(new Float(-1 * TILE_SIZE), new Float(endPos.x), COMPARISON_DELTA);
    }

    @Test
    public void testGetBeamEndWithAdderMaxBeamLefttLength()
    {
        final float angle = 270;
        final Vector2 endPos = new Vector2();
        Mockito.when(mockLevel.getTile(Matchers.anyInt(), Matchers.anyInt())).thenReturn(mockValidTile);

        Assert.assertEquals(new Float(TILE_SIZE * Flashlight.BEAM_LENGTH_MULTIPLIER),
                new Float(target.getBeamEnd(Flashlight.BEAM_ADDER, mockLevel, new Vector2(0, 0), angle, endPos, 0)), COMPARISON_DELTA);
        Assert.assertEquals(new Float(0), new Float(endPos.y), COMPARISON_DELTA);
        Assert.assertEquals(new Float(-1 * TILE_SIZE * Flashlight.BEAM_LENGTH_MULTIPLIER), new Float(endPos.x), COMPARISON_DELTA);
    }

    @Test
    public void testGetBeamEndWithSubberBeamUp()
    {
        final float angle = 0;
        final float distance = TILE_SIZE * Flashlight.BEAM_LENGTH_MULTIPLIER;

        final Vector2 endPos = new Vector2();
        Mockito.when(mockLevel.getTile(Matchers.anyInt(), Matchers.anyInt())).thenReturn(mockInvalidTile);
        Mockito.when(mockLevel.getTile(0, 0)).thenReturn(mockValidTile);

        Assert.assertEquals(new Float(TILE_SIZE - Flashlight.BEAM_INCREMENT),
                new Float(target.getBeamEnd(Flashlight.BEAM_SUBBER, mockLevel, new Vector2(0, 0), angle, endPos, distance)), COMPARISON_DELTA);
        Assert.assertEquals(new Float(0), new Float(endPos.x), COMPARISON_DELTA);
        Assert.assertEquals(new Float(TILE_SIZE - Flashlight.BEAM_INCREMENT), new Float(endPos.y), COMPARISON_DELTA);
    }

    @Test
    public void testGetBeamEndWithSubberBeamRight()
    {
        final float angle = 90;
        final float distance = TILE_SIZE * Flashlight.BEAM_LENGTH_MULTIPLIER;
        final Vector2 endPos = new Vector2();
        Mockito.when(mockLevel.getTile(Matchers.anyInt(), Matchers.anyInt())).thenReturn(mockInvalidTile);
        Mockito.when(mockLevel.getTile(0, 0)).thenReturn(mockValidTile);

        Assert.assertEquals(new Float(TILE_SIZE - Flashlight.BEAM_INCREMENT),
                new Float(target.getBeamEnd(Flashlight.BEAM_SUBBER, mockLevel, new Vector2(0, 0), angle, endPos, distance)), COMPARISON_DELTA);
        Assert.assertEquals(new Float(0), new Float(endPos.y), COMPARISON_DELTA);
        Assert.assertEquals(new Float(TILE_SIZE - Flashlight.BEAM_INCREMENT), new Float(endPos.x), COMPARISON_DELTA);
    }

    @Test
    public void testGetBeamEndWithSubberBeamDown()
    {
        final float angle = 180;
        final float distance = TILE_SIZE * Flashlight.BEAM_LENGTH_MULTIPLIER;
        final Vector2 endPos = new Vector2();
        Mockito.when(mockLevel.getTile(Matchers.anyInt(), Matchers.anyInt())).thenReturn(mockInvalidTile);
        Mockito.when(mockLevel.getTile(0, 0)).thenReturn(mockValidTile);

        Assert.assertEquals(new Float(TILE_SIZE - Flashlight.BEAM_INCREMENT),
                new Float(target.getBeamEnd(Flashlight.BEAM_SUBBER, mockLevel, new Vector2(0, 0), angle, endPos, distance)), COMPARISON_DELTA);
        Assert.assertEquals(new Float(0), new Float(endPos.x), COMPARISON_DELTA);
        Assert.assertEquals(new Float(-1 * (TILE_SIZE - Flashlight.BEAM_INCREMENT)), new Float(endPos.y), COMPARISON_DELTA);
    }

    @Test
    public void testGetBeamEndWithSubberBeamLeft()
    {
        final float angle = 270;
        final Vector2 endPos = new Vector2();
        final float distance = TILE_SIZE * Flashlight.BEAM_LENGTH_MULTIPLIER;
        Mockito.when(mockLevel.getTile(Matchers.anyInt(), Matchers.anyInt())).thenReturn(mockInvalidTile);
        Mockito.when(mockLevel.getTile(0, 0)).thenReturn(mockValidTile);

        Assert.assertEquals(new Float(TILE_SIZE - Flashlight.BEAM_INCREMENT),
                new Float(target.getBeamEnd(Flashlight.BEAM_SUBBER, mockLevel, new Vector2(0, 0), angle, endPos, distance)), COMPARISON_DELTA);
        Assert.assertEquals(new Float(0), new Float(endPos.y), COMPARISON_DELTA);
        Assert.assertEquals(new Float(-1 * (TILE_SIZE - Flashlight.BEAM_INCREMENT)), new Float(endPos.x), COMPARISON_DELTA);
    }
}