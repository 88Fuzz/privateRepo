package com.squared.space.game.state;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.squared.space.game.constants.TextureConstants;
import com.squared.space.game.drawing.WorldRenderer;
import com.squared.space.game.drawing.text.Text;
import com.squared.space.game.state.StateManager.PendingAction;
import com.squared.space.game.state.StateManager.StateAction;

public class TextState implements State
{
    private static final int NEXT_TEXT_BUTTON = Input.Keys.SPACE;
    private static final int SELECTION_UP_BUTTON = Input.Keys.W;
    private static final int SELECTION_DOWN_BUTTON = Input.Keys.S;
    private final Vector2 position;
    private final Vector2 namePosition;
    private final BitmapFont font;
    private final StateManager stateManager;
    private final TextureAtlas atlas;
    private PendingAction nextState;
    private Text activeText;
    private Sprite sprite;

    public TextState(final StateManager stateManager, final BitmapFont font, final Vector2 position,
            final TextureAtlas atlas)
    {
        this.atlas = atlas;
        this.position = position;
        this.font = font;
        this.stateManager = stateManager;
        this.nextState = null;

        namePosition = new Vector2(0, Gdx.graphics.getHeight() / 30);
        final int boxHeight = Gdx.graphics.getHeight() / 2;
        sprite = new Sprite(atlas.findRegion(TextureConstants.SINGLE_PIXEL));
        sprite.setBounds(namePosition.x, namePosition.y, Gdx.graphics.getWidth(), boxHeight);
        namePosition.y += boxHeight;
        namePosition.x += 5;
        sprite.setColor(1, 1, 1, 0.9f);
    }

    public void init(final Text text)
    {
        activeText = text;
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
        if(activeText != null)
            activeText.update(dt);
    }

    @Override
    public void render(final WorldRenderer worldRenderer)
    {
        if(activeText != null)
        {
            worldRenderer.render(sprite);
            worldRenderer.render(activeText);
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
            if(!activeText.isFinishedText())
            {
                activeText.finishText();
                return true;
            }

            activeText = activeText.getNextText();
            if(activeText == null)
            {
                stateManager.addAction(StateAction.POP);
                if(nextState != null)
                    stateManager.addAction(nextState);
                return true;
            }

            initActiveText();
        }else if(keyCode == SELECTION_UP_BUTTON)
            activeText.selectionUp();
        else if(keyCode == SELECTION_DOWN_BUTTON)
            activeText.selectionDown();
        return true;
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
        activeText.init(atlas, font, position, namePosition);
    }
}