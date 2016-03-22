package com.murder.game.utils;

public class RotationUtils
{
    public enum RotationDirection
    {
        NONE,
        COUNTER_CLOCKWISE,
        CLOCKWISE;
    }

    private static final int MAX_ANGLE = 360;

    /**
     * Adjusts an angle to never be less than 0 and never > 360
     * 
     * @param angle
     * @return
     */
    public static float adjustAngleAbout360(final float angle)
    {
        if(angle < 0)
            return MAX_ANGLE + angle;

        if(angle > MAX_ANGLE)
            return MAX_ANGLE + angle;

        return angle;
    }

    /**
     * Given two angles, return if the destination is clockwise or
     * counter-clockwise rotation away from the source.
     * 
     * @param srcAngle
     * @param destAngle
     * @return
     */
    public static RotationDirection getRotationDirection(final float srcAngle, final float dstAngle)
    {
        RotationDirection rotationDirection = RotationDirection.NONE;
        float minValue = Float.MAX_VALUE;

        float oldValue = minValue;
        minValue = Math.min(minValue, ignoreNegative(srcAngle - dstAngle));
        if(oldValue != minValue)
            rotationDirection = RotationDirection.COUNTER_CLOCKWISE;

        oldValue = minValue;
        minValue = Math.min(minValue, ignoreNegative(dstAngle - srcAngle));
        if(oldValue != minValue)
            rotationDirection = RotationDirection.CLOCKWISE;

        // The last two cases explicitly check for cases that involve angles
        // that cross over the 360 mark.
        oldValue = minValue;
        minValue = Math.min(minValue, ignoreNegative(srcAngle + MAX_ANGLE - dstAngle));
        if(oldValue != minValue)
            rotationDirection = RotationDirection.COUNTER_CLOCKWISE;

        oldValue = minValue;
        minValue = Math.min(minValue, ignoreNegative(dstAngle + MAX_ANGLE - srcAngle));
        if(oldValue != minValue)
            rotationDirection = RotationDirection.CLOCKWISE;

        return rotationDirection;
    }

    private static float ignoreNegative(final float value)
    {
        return (value < 0) ? Float.MAX_VALUE : value;
    }

    /**
     * Returns the distance of rotation between two angles. The angle must be
     * between 0 and 360 degrees.
     * 
     * @param srcAngle
     * @param dstAngle
     * @return
     */
    public static float getRotationDistance(final float srcAngle, final float dstAngle)
    {
        float minValue = Float.MAX_VALUE;
        minValue = Math.min(minValue, Math.abs(srcAngle - dstAngle));
        minValue = Math.min(minValue, Math.abs(dstAngle - srcAngle));

        // The last two cases explicitly check for cases that involve angles
        // that cross over the 360 mark.
        minValue = Math.min(minValue, Math.abs(srcAngle + MAX_ANGLE - dstAngle));
        minValue = Math.min(minValue, Math.abs(dstAngle + MAX_ANGLE - srcAngle));

        return minValue;
    }
}