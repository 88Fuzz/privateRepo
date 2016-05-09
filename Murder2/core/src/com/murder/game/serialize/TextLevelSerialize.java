package com.murder.game.serialize;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.murder.game.drawing.Text;
import com.murder.game.state.management.PendingAction;
import com.murder.game.state.modifier.TextStateModifier;

public class TextLevelSerialize
{
    private static final String DRAWABLE_TEXTS = "drawableTexts";
    private static final String TEXT_STATE_MODIFIERS = "textStateModifiers";
    private static final String STATE_ACTIONS = "stateActions";

    private List<Text> drawableTexts;
    private List<TextStateModifier> textStateModifiers;
    private List<PendingAction> stateActions;

    @JsonCreator
    public TextLevelSerialize(@JsonProperty(DRAWABLE_TEXTS) final List<Text> drawableTexts,
            @JsonProperty(TEXT_STATE_MODIFIERS) final List<TextStateModifier> textStateModifier,
            @JsonProperty(STATE_ACTIONS) List<PendingAction> stateActions)
    {
        this.drawableTexts = drawableTexts;
        this.textStateModifiers = textStateModifier;
        this.stateActions = stateActions;
    }

    public List<Text> getDrawableTexts()
    {
        return drawableTexts;
    }

    public List<TextStateModifier> getTextStateModifiers()
    {
        return textStateModifiers;
    }

    public List<PendingAction> getStateActions()
    {
        return stateActions;
    }
}