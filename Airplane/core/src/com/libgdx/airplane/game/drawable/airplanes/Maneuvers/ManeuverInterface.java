package com.libgdx.airplane.game.drawable.airplanes.Maneuvers;

import com.libgdx.airplane.game.drawable.airplanes.Airplane;

/**
 * 
 * Any class that needs to do a maneuver, loop, u-turn, etc. Needs to implement
 * the this interface.
 * 
 */
public interface ManeuverInterface
{
    /**
     * Initialize the maneuver.
     */
    public void initManeuver(final Airplane parentAirplane);

    /**
     * Start the maneuver after it has been initialized. This method is not
     * garentied to be called only once.
     */
    public void startManeuver(final Airplane parentAirplane);

    /**
     * Called every time the maneuver has been triggered during the update *
     * phase.
     * 
     * @param parentAirplane
     */
    public void triggeredManeuver(final Airplane parentAirplane);

    /**
     * Checks if the maneuver is finished processing.
     * 
     * @return
     */
    public boolean isManeuverFinished(final Airplane parentAirplane);

    /**
     * Called if the maneuver should stop. This can be if the maneuver needs to
     * be cancelled or if isManeuverFinished returns true. Maneuver may not have
     * been started either before calling.
     */
    public void endManeuver(final Airplane parentAirplane);
}