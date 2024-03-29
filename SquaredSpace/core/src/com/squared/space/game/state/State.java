package com.squared.space.game.state;

import com.squared.space.game.drawing.WorldRenderer;

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
    public boolean keyDown(final int keycode);
    
    /**
     * Called when a key is released.
     */
    public boolean keyUp(final int keyCode);
    
    /**
     * Called when a unicode character is entered.
     */
    public boolean unicodeEntered(final char character);
}