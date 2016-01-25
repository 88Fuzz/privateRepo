package com.squared.space.game.event;

import com.squared.space.game.state.StateManager;
import com.squared.space.game.state.StateManager.StateAction;
import com.squared.space.game.state.StateManager.StateId;

public class StateEvent
{
    private StateId stateId;
    private String stateConfig;
    private float xMin;
    private float xMax;

    public StateEvent(final StateId stateId, final String stateConfig, final float xMin, final float xMax)
    {
        this.stateId = stateId;
        this.stateConfig = stateConfig;
        this.xMin = xMin;
        this.xMax = xMax;
    }

    public void process(final StateManager stateManager)
    {
        stateManager.addAction(StateAction.PUSH, stateId, stateConfig);
    }

    public boolean shouldDelete()
    {
        return true;
    }

    public boolean shouldProcess(final float xPos)
    {
        if(xPos >= xMin && xPos <= xMax)
            return true;
        return false;
    }
}
