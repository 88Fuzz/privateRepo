package com.murder.game.state.modifier;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.murder.game.drawing.Text;
import com.murder.game.drawing.manager.FontManager;
import com.murder.game.state.TextState;

public class TextStateAdder implements TextStateModifier
{
    private static final String STATE_ADDER_TEXTS = "stateAdderTexts";
    private List<Text> texts;
    private boolean finished;

    @JsonCreator
    public TextStateAdder(@JsonProperty(STATE_ADDER_TEXTS) final List<Text> texts)
    {
        this.texts = texts;
    }

    @Override
    public void init(final TextState textState, final FontManager fontManager)
    {
        finished = false;
        for(final Text text: texts)
        {
            text.init(fontManager);
        }
    }

    @Override
    public void update(final TextState textState, final float dt)
    {
        // Do nothing
    }

    @Override
    public boolean touchDown(final TextState textState, final int screenX, final int screenY, final int pointer, final int button)
    {
        // Do nothing
        return false;
    }

    @Override
    public boolean touchUp(final TextState textState, final int screenX, final int screenY, final int pointer, final int button)
    {
        if(texts.isEmpty())
        {
            finished = true;
            return false;
        }

        textState.addText(texts.get(0));
        texts.remove(0);

        return true;
    }

    @Override
    public boolean isFinished(final TextState textState)
    {
        return finished;
    }

    public List<Text> getTexts()
    {
        return texts;
    }
}