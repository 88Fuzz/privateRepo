package com.murder.game.serialize;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.murder.game.drawing.Text;
import com.murder.game.state.StateManager.StateId;
import com.murder.game.state.modifier.TextStateModifier;

public class TextLevelSerialize
{
    private static final String DRAWABLE_TEXTS = "drawableTexts";
    private static final String TEXT_STATE_MODIFIERS = "textStateModifiers";
    private static final String NEXT_STATE = "nextState";
    private static final String NEXT_STATE_NAME = "nextStateName";

    private List<Text> drawableTexts;
    private List<TextStateModifier> textStateModifiers;
    private StateId stateId;
    private String nextStateName;

    @JsonCreator
    public TextLevelSerialize(@JsonProperty(DRAWABLE_TEXTS) final List<Text> drawableTexts,
            @JsonProperty(TEXT_STATE_MODIFIERS) final List<TextStateModifier> textStateModifier, @JsonProperty(NEXT_STATE) final StateId stateId,
            @JsonProperty(NEXT_STATE_NAME) final String nextStateName)
    {
        this.drawableTexts = drawableTexts;
        this.textStateModifiers = textStateModifier;
        this.stateId = stateId;
        this.nextStateName = nextStateName;
    }

    public List<Text> getDrawableTexts()
    {
        return drawableTexts;
    }

    public List<TextStateModifier> getTextStateModifiers()
    {
        return textStateModifiers;
    }

    public StateId getStateId()
    {
        return stateId;
    }

    public String getNextStateName()
    {
        return nextStateName;
    }
}