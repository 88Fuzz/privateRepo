package com.murder.game.utils;

import org.junit.Assert;
import org.junit.Test;

import com.murder.game.utils.RotationUtils.RotationDirection;

public class RotationUtilsTest
{
    @Test
    public void getAngleDistanceCounterClockwiseOver360()
    {
        final float srcAngle = 10;
        final float dstAngle = 340;

        Assert.assertEquals(new Float(30), new Float(RotationUtils.getRotationDistance(srcAngle, dstAngle)));
    }

    @Test
    public void getAngleDistanceClockwiseOver360()
    {
        final float srcAngle = 340;
        final float dstAngle = 10;

        Assert.assertEquals(new Float(30), new Float(RotationUtils.getRotationDistance(srcAngle, dstAngle)));
    }

    @Test
    public void getAngleDistanceClockwise()
    {
        final float srcAngle = 30;
        final float dstAngle = 180;

        Assert.assertEquals(new Float(dstAngle - srcAngle), new Float(RotationUtils.getRotationDistance(srcAngle, dstAngle)));
    }

    @Test
    public void getAngleDistanceCounterClockwise()
    {
        final float srcAngle = 180;
        final float dstAngle = 30;

        Assert.assertEquals(new Float(srcAngle - dstAngle), new Float(RotationUtils.getRotationDistance(srcAngle, dstAngle)));
    }

    @Test
    public void getRotationDirectionClockwise()
    {
        final float srcAngle = 10;
        final float dstAngle = 70;

        Assert.assertEquals(RotationDirection.CLOCKWISE, RotationUtils.getRotationDirection(srcAngle, dstAngle));
    }

    @Test
    public void getRotationDirectionCounterClockwise()
    {
        final float srcAngle = 70;
        final float dstAngle = 10;

        Assert.assertEquals(RotationDirection.COUNTER_CLOCKWISE, RotationUtils.getRotationDirection(srcAngle, dstAngle));
    }

    @Test
    public void getRotationDirectionClockwiseOver360()
    {
        final float srcAngle = 310;
        final float dstAngle = 20;

        Assert.assertEquals(RotationDirection.CLOCKWISE, RotationUtils.getRotationDirection(srcAngle, dstAngle));
    }

    @Test
    public void getRotationDirectionCounterClockwiseOver360()
    {
        final float srcAngle = 30;
        final float dstAngle = 310;

        Assert.assertEquals(RotationDirection.COUNTER_CLOCKWISE, RotationUtils.getRotationDirection(srcAngle, dstAngle));
    }
}