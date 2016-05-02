package com.murder.game.serialize;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.murder.game.drawing.Text;
import com.murder.game.state.modifier.TextStateModifier;

public class TextStateSerialize
{
    private static final String DRAWABLE_TEXTS = "drawableTexts";
    private static final String TEXT_STATE_MODIFIERS = "textStateModifiers";

    private List<Text> drawableTexts;
    private List<TextStateModifier> textStateModifiers;

    @JsonCreator
    public TextStateSerialize(@JsonProperty(DRAWABLE_TEXTS) final List<Text> drawableTexts,
            @JsonProperty(TEXT_STATE_MODIFIERS) final List<TextStateModifier> textStateModifier)
    {
        this.drawableTexts = drawableTexts;
        this.textStateModifiers = textStateModifier;
    }

    public List<Text> getDrawableTexts()
    {
        return drawableTexts;
    }

    public List<TextStateModifier> getTextStateModifiers()
    {
        return textStateModifiers;
    }
}