package com.libgdx.airplane.game.drawable.airplanes.Maneuvers;

import com.libgdx.airplane.game.drawable.airplanes.Airplane;

public class UTurnManeuver extends RotationManeuver
{

    public UTurnManeuver(float pitchChange)
    {
        super(pitchChange, 180f);
    }

    @Override
    public void endManeuver(final Airplane parentAirplane)
    {
        super.endManeuver(parentAirplane);
        parentAirplane.flipSprite(false, true);
    }
}
