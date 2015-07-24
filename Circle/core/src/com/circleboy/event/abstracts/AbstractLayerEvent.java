package com.circleboy.event.abstracts;

public abstract class AbstractLayerEvent
{
    abstract protected boolean shouldProcess(int wrapNumber);

    abstract protected void process();

    abstract protected boolean shouldDelete();

    public boolean checkEvent(int wrapNumber)
    {
        if(shouldProcess(wrapNumber))
        {
            process();
            return shouldDelete();
        }

        return false;
    }
}
