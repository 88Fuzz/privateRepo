package com.murder.game.drawing.flashlight;

import com.badlogic.gdx.math.Vector2;

/**
 * A beam of the flashlight. Each beam contains the angle offset from straight
 * up (0 degrees). Endposition, the x,y coordinate of the end of the beam.
 * Distance, the length the beam has traveled from the center of the player.
 */
public class Beam
{
    private float angle;
    private Vector2 endPos;
    private float distance;
    private Beam prevBeam;
    private Beam nextBeam;

    public Beam()
    {
        angle = -1;
        endPos = new Vector2();
        distance = 0;
        setPrevBeam(null);
        setNextBeam(null);
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

    public Beam getPrevBeam()
    {
        return prevBeam;
    }

    public void setPrevBeam(final Beam prevBeam)
    {
        this.prevBeam = prevBeam;
    }

    public Beam getNextBeam()
    {
        return nextBeam;
    }

    public void setNextBeam(final Beam nextBeam)
    {
        this.nextBeam = nextBeam;
    }
}