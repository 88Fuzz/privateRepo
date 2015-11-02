package com.libgdx.airplane.game.drawable.airplanes.Maneuvers;

import com.libgdx.airplane.game.drawable.airplanes.Airplane;

public class RotationManeuver implements ManeuverInterface
{
    private final float pitchChange;

    private float rotation;
    private float stopRotation;
    private boolean started;

    public RotationManeuver(final float pitchChange, final float stopRotation)
    {
        this.pitchChange = pitchChange;
        this.stopRotation = stopRotation;
    }

    @Override
    public void initManeuver(final Airplane parentAirplane)
    {
        rotation = 0;
        started = false;
    }

    @Override
    public void startManeuver(final Airplane parentAirplane)
    {
        if(!started)
        {
            started = true;
            parentAirplane.addPitchAcceleration(pitchChange);
        }
    }

    @Override
    public boolean isManeuverFinished(final Airplane parentAirplane)
    {
        if(!started)
        {
            return false;
        }

        if(rotation > stopRotation)
            return true;

        return false;
    }

    @Override
    public void endManeuver(final Airplane parentAirplane)
    {
        if(started)
        {
            parentAirplane.addPitchAcceleration(-1 * pitchChange);
        }
        started = false;
    }

    @Override
    public void triggeredManeuver(final Airplane parentAirplane)
    {
        rotation += Math.abs(parentAirplane.getPitchAcceleration());
    }
}