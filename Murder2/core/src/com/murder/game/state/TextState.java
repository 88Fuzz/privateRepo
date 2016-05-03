package com.murder.game.state;

import java.util.List;

import com.murder.game.drawing.Text;
import com.murder.game.drawing.WorldRenderer;
import com.murder.game.drawing.manager.FontManager;
import com.murder.game.level.generator.TextLevelGenerator;
import com.murder.game.serialize.TextLevelSerialize;
import com.murder.game.state.StateManager.PendingAction;
import com.murder.game.state.StateManager.StateAction;
import com.murder.game.state.modifier.TextStateModifier;

public class TextState implements State
{
    private final StateManager stateManager;
    private TextLevelSerialize textState;
    private List<Text> drawableTexts;
    private List<TextStateModifier> textStateModifiers;

    public TextState(final StateManager stateManager)
    {
        this.stateManager = stateManager;
    }

    public void init(final FontManager fontManager, final String levelKey)
    {
        textState = TextLevelGenerator.getTextState(levelKey);
        drawableTexts = textState.getDrawableTexts();
        textStateModifiers = textState.getTextStateModifiers();
        for(final Text text: drawableTexts)
        {
            text.init(fontManager);
        }

        for(final TextStateModifier modifier: textStateModifiers)
        {
            modifier.init(this, fontManager);
        }
    }

    // TODO remove once font tests are complete.
    public void init(final FontManager fontManager, final List<Text> drawableTexts, final List<TextStateModifier> textStateModifier)
    {
        this.drawableTexts = drawableTexts;
        this.textStateModifiers = textStateModifier;
        for(final Text text: drawableTexts)
        {
            text.init(fontManager);
        }
    }

    @Override
    public void pause()
    {
        // TODO Auto-generated method stub
    }

    @Override
    public void resume()
    {
        // TODO Auto-generated method stub
    }

    @Override
    public void resize(final int width, final int height)
    {
        // TODO Auto-generated method stub
    }

    @Override
    public void update(final float dt)
    {
        for(final Text text: drawableTexts)
        {
            text.update(dt);
        }

        boolean finished = true;
        for(final TextStateModifier modifier: textStateModifiers)
        {
            modifier.update(this, dt);
            if(!modifier.isFinished(this))
                finished = false;
        }

        if(finished)
        {
            stateManager.addAction(new PendingAction().withAction(StateAction.POP));
            stateManager.addAction(new PendingAction().withAction(StateAction.PUSH).withStatId(textState.getStateId())
                    .withStateConfig(textState.getNextStateName()));
        }
    }

    @Override
    public void render(final WorldRenderer worldRenderer)
    {
        for(final Text text: drawableTexts)
        {
            worldRenderer.render(text);
        }
    }

    @Override
    public void dispose()
    {
        // TODO Auto-generated method stub
    }

    @Override
    public boolean keyDown(final int keyCode)
    {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean keyUp(final int keyCode)
    {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean unicodeEntered(final char character)
    {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean touchDown(final int screenX, final int screenY, final int pointer, final int button)
    {
        for(final TextStateModifier modifier: textStateModifiers)
        {
            if(modifier.touchDown(this, screenX, screenY, pointer, button))
                return true;
        }

        return false;
    }

    @Override
    public boolean touchUp(final int screenX, final int screenY, final int pointer, final int button)
    {
        for(final TextStateModifier modifier: textStateModifiers)
        {
            if(modifier.touchUp(this, screenX, screenY, pointer, button))
                return true;
        }

        return false;
    }

    @Override
    public boolean touchDragged(final int screenX, final int screenY, final int pointer)
    {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean mouseMoved(final int screenX, final int screenY)
    {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean mouseScrolled(final int amount)
    {
        // TODO Auto-generated method stub
        return false;
    }

    public void addText(final Text text)
    {
        drawableTexts.add(text);
    }
}