package com.murder.game.state.modifier;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.murder.game.drawing.manager.FontManager;
import com.murder.game.state.TextState;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({ @Type(value = TextStateAdder.class, name = "TextStateAdder") })
public interface TextStateModifier
{
    /**
     * Called before update, touchDown, or touchUp will ever be called.
     * 
     * @param textState
     * @param fontManager
     */
    void init(final TextState textState, final FontManager fontManager);

    /**
     * Called on every tick of the game.
     * 
     * @param dt
     */
    void update(final TextState textState, final float dt);

    /**
     * Called once the screen is touched. Return true if the touch is consumed.
     * 
     * @param screenX
     * @param screenY
     * @param pointer
     * @param button
     * @return
     */
    boolean touchDown(final TextState textState, final int screenX, final int screenY, final int pointer, final int button);

    /**
     * Called once the screen is un-touched. Return true if the touch is
     * consumed.
     * 
     * @param screenX
     * @param screenY
     * @param pointer
     * @param button
     * @return
     */
    boolean touchUp(final TextState textState, final int screenX, final int screenY, final int pointer, final int button);

    /**
     * Return true if the TextState should advance to the next state of the
     * game.
     * 
     * @param textState
     * @return
     */
    boolean isFinished(final TextState textState);
}