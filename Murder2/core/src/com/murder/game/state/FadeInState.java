package com.murder.game.state;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.murder.game.constants.drawing.TextureType;
import com.murder.game.drawing.WorldRenderer;
import com.murder.game.drawing.manager.TextureManager;
import com.murder.game.state.management.PendingAction;
import com.murder.game.state.management.StateAction;
import com.murder.game.state.management.StateManager;

public class FadeInState extends State
{
    // Take .41 seconds at 30 frames per second
    private static final float FADE_IN_AMOUNT = 1.0f / 30.0f / .41f;
    private Sprite sprite;
    private Color color;

    public FadeInState(final StateManager stateManager)
    {
        super(stateManager);
    }

    public void init(final TextureManager textureManager, final Rectangle size)
    {
        color = new Color(0, 0, 0, 1);
        sprite = new Sprite(textureManager.getTexture(TextureType.SINGLE_PIXEL_TEXTURE), (int) size.width, (int) size.height);
        sprite.setColor(color);
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
        color.a -= FADE_IN_AMOUNT;
        sprite.setColor(color);
        if(color.a <= 0)
        {
            color.a = 0;
            stateManager.addAction(new PendingAction().withAction(StateAction.POP));
        }
    }

    @Override
    public void render(final WorldRenderer worldRenderer)
    {
        worldRenderer.render(sprite);
    }

    @Override
    public void dispose()
    {
        // TODO Auto-generated method stub
    }

    @Override
    public boolean keyDown(final int keyCode)
    {
        // Do nothing but consume the input
        return true;
    }

    @Override
    public boolean keyUp(final int keyCode)
    {
        // Do nothing but consume the input
        return true;
    }

    @Override
    public boolean unicodeEntered(final char character)
    {
        // Do nothing but consume the input
        return true;
    }

    @Override
    public boolean touchDown(final int screenX, final int screenY, final int pointer, final int button)
    {
        // Do nothing but consume the input
        return true;
    }

    @Override
    public boolean touchUp(final int screenX, final int screenY, final int pointer, final int button)
    {
        // Do nothing but consume the input
        return true;
    }

    @Override
    public boolean touchDragged(final int screenX, final int screenY, final int pointer)
    {
        // Do nothing but consume the input
        return true;
    }

    @Override
    public boolean mouseMoved(final int screenX, final int screenY)
    {
        // Do nothing but consume the input
        return true;
    }

    @Override
    public boolean mouseScrolled(final int amount)
    {
        // Do nothing but consume the input
        return true;
    }
}