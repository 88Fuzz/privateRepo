package com.murder.game.effects.text;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.murder.game.drawing.drawables.Text;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * Any time a text needs to change, for example color, size, font, etc., should
 * implement this interface.
 *
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({ @Type(value = LinearTextColorChanger.class, name = "LinearTextColorChanger") })
public interface TextEffect
{
    /**
     * Initializes the TextEffect. If the Text object should be any specific
     * state for the TextEffect, it should be modified here. The init method
     * will always be called before update.
     * 
     * @param text
     */
    void init(final Text text);

    /**
     * Called with every tick of the game update.
     * 
     * @param text
     * @param dt
     */
    void update(final Text text, final float dt);
}