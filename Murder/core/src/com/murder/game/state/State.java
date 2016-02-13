package com.murder.game.state;

import com.murder.game.drawing.WorldRenderer;

public interface State
{
    /**
     * Called when the game is paused (on Android)
     */
    public void pause();

    /**
     * Called when the game is brought back to the front (on Android)
     */
    public void resume();

    /**
     * Called when the window gets resized (on desktop, maybe when rotated on
     * android)
     * 
     * @param width
     * @param height
     */
    public void resize(final int width, final int height);

    /**
     * Called right before render to update the state.
     * 
     * @param dt
     */
    public void update(final float dt);

    /**
     * Called to render anything the state needs to display on the screen.
     * 
     * @param worldRenderer
     */
    public void render(final WorldRenderer worldRenderer);

    /**
     * Called when the game is closed.
     */
    public void dispose();

    /**
     * Called when a key is pressed.
     */
    public boolean keyDown(final int keyCode);

    /**
     * Called when a key is released.
     */
    public boolean keyUp(final int keyCode);

    /**
     * Called when a unicode character is entered.
     */
    public boolean unicodeEntered(final char character);

    /**
     * Called when the screen is touched or clicked.
     * 
     * @param screenX
     * @param screenY
     * @param pointer
     * @param button
     * @return
     */
    public boolean touchDown(final int screenX, final int screenY, final int pointer, final int button);

    /**
     * Called when the screen is no longer touched or clicked.
     * 
     * @param screenX
     * @param screenY
     * @param pointer
     * @param button
     * @return
     */
    public boolean touchUp(final int screenX, final int screenY, final int pointer, final int button);

    /**
     * Called when the screen is touched or clicked and is moved around.
     * 
     * @param screenX
     * @param screenY
     * @param pointer
     * @return
     */
    public boolean touchDragged(final int screenX, final int screenY, final int pointer);

    /**
     * Called when the mouse is moved without a click (I think).
     * 
     * @param screenX
     * @param screenY
     * @return
     */
    public boolean mouseMoved(final int screenX, final int screenY);

    /**
     * Called when the mouse wheel is scrolled.
     * 
     * @param amount
     * @return
     */
    public boolean mouseScrolled(final int amount);
}