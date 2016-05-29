package com.murder.game.effects.text;

import java.util.List;

import com.badlogic.gdx.graphics.Color;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.murder.game.MurderMainMain;
import com.murder.game.drawing.drawables.Text;

/**
 * Change the color of a text.
 */
public class LinearTextColorChanger implements TextEffect
{
    private static final String COLORS = "colors";
    private static final String CYCLE_COLORS = "cycleColors";
    private static final String TIME_TO_CHANGE = "timeToChange";

    private float colorChangeR;
    private float colorChangeG;
    private float colorChangeB;
    private float colorChangeA;

    private int currentColorPos;
    private int nextColorPos;
    private Color color;
    private List<Color> colors;
    private final boolean cycleColors;
    private float timeToChange;
    private float currentTime;

    /**
     * 
     * @param colors
     *            List of colors to change
     * @param cycleColors
     *            Once the list of colors runs out, should it loop back around
     * @param timeToChange
     *            The amount of time in seconds for each color change
     */
    @JsonCreator
    public LinearTextColorChanger(@JsonProperty(COLORS) final List<Color> colors, @JsonProperty(CYCLE_COLORS) final boolean cycleColors,
            @JsonProperty(TIME_TO_CHANGE) final float timeToChange)
    {
        this.colors = colors;
        this.cycleColors = cycleColors;
        this.timeToChange = timeToChange;
        this.color = new Color();
    }

    @Override
    public void init(final Text text)
    {
        currentColorPos = 0;
        currentTime = 0;
        final Color tmpColor = colors.get(currentColorPos);
        copyColor(tmpColor, color);
        text.setColor(tmpColor);
        setNextColorPos();
        setColorChange();
    }

    @Override
    public void update(final Text text, final float dt)
    {
        if(isColorChangingFinished())
            return;

        color.r += colorChangeR;
        color.g += colorChangeG;
        color.b += colorChangeB;
        color.a += colorChangeA;

        text.setColor(color);
        currentTime += dt;
        if(currentTime >= timeToChange)
        {
            text.setColor(colors.get(nextColorPos));
            currentColorPos = nextColorPos;
            setNextColorPos();

            if(isColorChangingFinished())
                return;

            currentTime = 0;
            setColorChange();
            copyColor(colors.get(currentColorPos), color);
        }
    }

    private void setColorChange()
    {
        final Color color = colors.get(currentColorPos);
        final Color nextColor = colors.get(nextColorPos);

        colorChangeR = ((nextColor.r - color.r) * MurderMainMain.TIMEPERFRAME) / timeToChange;
        colorChangeG = ((nextColor.g - color.g) * MurderMainMain.TIMEPERFRAME) / timeToChange;
        colorChangeB = ((nextColor.b - color.b) * MurderMainMain.TIMEPERFRAME) / timeToChange;
        colorChangeA = ((nextColor.a - color.a) * MurderMainMain.TIMEPERFRAME) / timeToChange;
    }

    private void copyColor(final Color srcColor, final Color dstColor)
    {
        dstColor.r = srcColor.r;
        dstColor.g = srcColor.g;
        dstColor.b = srcColor.b;
        dstColor.a = srcColor.a;
    }

    private void setNextColorPos()
    {
        nextColorPos++;
        if(nextColorPos >= colors.size() && cycleColors)
            nextColorPos = 0;
    }

    private boolean isColorChangingFinished()
    {
        return nextColorPos >= colors.size();
    }

    public boolean getCycleColors()
    {
        return cycleColors;
    }

    public List<Color> getColors()
    {
        return colors;
    }

    public float getTimeToChange()
    {
        return timeToChange;
    }
}