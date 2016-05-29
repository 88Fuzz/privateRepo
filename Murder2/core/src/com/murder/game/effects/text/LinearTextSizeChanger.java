package com.murder.game.effects.text;

import java.util.List;

import com.badlogic.gdx.math.Vector2;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.murder.game.MurderMainMain;
import com.murder.game.drawing.drawables.Text;
import com.murder.game.serialize.MyVector2;

/**
 * Changes the size of a text object.
 */
// TODO, any linear change can probably be templited out and abstracted into a
// linear text effect
public class LinearTextSizeChanger implements TextEffect
{
    private static final String SCALES = "scales";
    private static final String CYCLE_SCALES = "cyclesScales";
    private static final String TIME_TO_CHANGE = "timeToChange";

    private int currentScalePos;
    private int nextScalePos;
    private Vector2 scaleChangeRate;
    private Vector2 currentScaleValue;
    private List<MyVector2> scaleChanges;
    private boolean cycleScales;
    private float timeToChange;
    private float currentTime;

    /**
     * @param scaleChanges
     *            List of scales to cycle through
     * @param cycleScales
     *            Once the list of scales runs out, should it loop back around
     * @param timeToChange
     *            The amount of time in seconds for each scale change
     */
    @JsonCreator
    public LinearTextSizeChanger(@JsonProperty(SCALES) final List<MyVector2> scaleChanges, @JsonProperty(CYCLE_SCALES) final boolean cycleScales,
            @JsonProperty(TIME_TO_CHANGE) final float timeToChange)
    {
        this.scaleChanges = scaleChanges;
        this.cycleScales = cycleScales;
        this.timeToChange = timeToChange;
        scaleChangeRate = new Vector2();
        currentScaleValue = new Vector2();
    }

    @Override
    public void init(final Text text)
    {
        currentScalePos = 0;
        nextScalePos = 0;
        currentTime = 0;
        setNextScalePos();
        setScaleChange();
        setCurrentScaleValue();
    }

    @Override
    public void update(final Text text, final float dt)
    {
        if(isScaleChangingFinished())
            return;

        text.setFontScale(currentScaleValue.x, currentScaleValue.y);
        currentScaleValue.x += scaleChangeRate.x;
        currentScaleValue.y += scaleChangeRate.y;

        currentTime += dt;
        if(currentTime >= timeToChange)
        {
            text.setFontScale(scaleChanges.get(nextScalePos).x, scaleChanges.get(nextScalePos).y);
            currentScalePos = nextScalePos;
            setNextScalePos();

            if(isScaleChangingFinished())
                return;

            currentTime = 0;
            setScaleChange();
            setCurrentScaleValue();
        }
    }

    private void setCurrentScaleValue()
    {
        currentScaleValue.x = scaleChanges.get(currentScalePos).x;
        currentScaleValue.y = scaleChanges.get(currentScalePos).y;
    }

    private void setScaleChange()
    {
        final Vector2 scale = scaleChanges.get(currentScalePos);
        final Vector2 nextScale = scaleChanges.get(nextScalePos);

        scaleChangeRate.x = ((nextScale.x - scale.x) * MurderMainMain.TIMEPERFRAME) / timeToChange;
        scaleChangeRate.y = ((nextScale.y - scale.y) * MurderMainMain.TIMEPERFRAME) / timeToChange;
    }

    private void setNextScalePos()
    {
        nextScalePos++;
        if(nextScalePos >= scaleChanges.size() && cycleScales)
            nextScalePos = 0;
    }

    private boolean isScaleChangingFinished()
    {
        return nextScalePos >= scaleChanges.size();
    }
}