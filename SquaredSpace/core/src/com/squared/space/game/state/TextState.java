package com.squared.space.game.state;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.squared.space.game.constants.TextureConstants;
import com.squared.space.game.drawing.Text;
import com.squared.space.game.drawing.WorldRenderer;
import com.squared.space.game.state.StateManager.PendingAction;
import com.squared.space.game.state.StateManager.StateAction;

public class TextState implements State
{
    private static final int NEXT_TEXT_BUTTON = Input.Keys.SPACE;
    private final StateManager stateManager;
    private final Label label;
    private PendingAction nextState;
    private int activeText;
    private List<Text> texts;
    private Sprite sprite;

    public TextState(final StateManager stateManager, final Label label, final TextureAtlas atlas)
    {
        this.texts = new ArrayList<Text>();
        this.stateManager = stateManager;
        this.label = label;
        this.nextState = null;

        sprite = new Sprite(atlas.findRegion(TextureConstants.SINGLE_PIXEL));
        sprite.setBounds(0, Gdx.graphics.getHeight() / 30, Gdx.graphics.getWidth(), Gdx.graphics.getHeight() / 2);
        sprite.setColor(1, 1, 1, 0.9f);
    }

    public void init(final List<Text> texts)
    {
        activeText = 0;
        this.texts.addAll(texts);
        initActiveText();
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
    public void update(float dt)
    {
        if(activeText < texts.size())
            texts.get(activeText).update(dt);
    }

    @Override
    public void render(final WorldRenderer worldRenderer)
    {
        if(activeText < texts.size())
        {
            worldRenderer.render(sprite);
            worldRenderer.render(texts.get(activeText));
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
        if(keyCode == NEXT_TEXT_BUTTON)
        {
            final Text currentText = texts.get(activeText);
            if(!currentText.isFinishedText())
            {
                currentText.finishText();

                return true;
            }else if(++activeText >= texts.size())
            {
                stateManager.addAction(StateAction.POP);
                if(nextState != null)
                    stateManager.addAction(nextState);

                return true;
            }

            initActiveText();
            return true;
        }
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

    private void initActiveText()
    {
        texts.get(activeText).setLabel(label);
    }
}