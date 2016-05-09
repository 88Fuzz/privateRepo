package com.murder.game.state;

import com.murder.game.drawing.WorldRenderer;
import com.murder.game.state.management.StateManager;

public abstract class State
{
    protected final StateManager stateManager;

    protected State(final StateManager stateManager)
    {
        this.stateManager = stateManager;
    }

    /**
     * Called when the game is paused (on Android)
     */
    public abstract void pause();

    /**
     * Called when the game is brought back to the front (on Android)
     */
    public abstract void resume();

    /**
     * Called when the window gets resized (on desktop, maybe when rotated on
     * android)
     * 
     * @param width
     * @param height
     */
    public abstract void resize(final int width, final int height);

    /**
     * Called right before render to update the state.
     * 
     * @param dt
     */
    public abstract void update(final float dt);

    /**
     * Called to render anything the state needs to display on the screen.
     * 
     * @param worldRenderer
     */
    public abstract void render(final WorldRenderer worldRenderer);

    /**
     * Called when the game is closed.
     */
    public abstract void dispose();

    /**
     * Called when a key is pressed. Return true if no other states should be
     * notified of the input.
     */
    public abstract boolean keyDown(final int keyCode);

    /**
     * Called when a key is released. Return true if no other states should be
     * notified of the input.
     */
    public abstract boolean keyUp(final int keyCode);

    /**
     * Called when a unicode character is entered. Return true if no other
     * states should be notified of the input.
     */
    public abstract boolean unicodeEntered(final char character);

    /**
     * Called when the screen is touched or clicked. Return true if no other
     * states should be notified of the input.
     * 
     * @param screenX
     * @param screenY
     * @param pointer
     * @param button
     * @return
     */
    public abstract boolean touchDown(final int screenX, final int screenY, final int pointer, final int button);

    /**
     * Called when the screen is no longer touched or clicked. Return true if no
     * other states should be notified of the input.
     * 
     * @param screenX
     * @param screenY
     * @param pointer
     * @param button
     * @return
     */
    public abstract boolean touchUp(final int screenX, final int screenY, final int pointer, final int button);

    /**
     * Called when the screen is touched or clicked and is moved around. Return
     * true if no other states should be notified of the input.
     * 
     * @param screenX
     * @param screenY
     * @param pointer
     * @return
     */
    public abstract boolean touchDragged(final int screenX, final int screenY, final int pointer);

    /**
     * Called when the mouse is moved without a click (I think). Return true if
     * no other states should be notified of the input.
     * 
     * @param screenX
     * @param screenY
     * @return
     */
    public abstract boolean mouseMoved(final int screenX, final int screenY);

    /**
     * Called when the mouse wheel is scrolled. Return true if no other states
     * should be notified of the input.
     * 
     * @param amount
     * @return
     */
    public abstract boolean mouseScrolled(final int amount);
}