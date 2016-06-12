package com.murder.game.state.config;

import com.badlogic.gdx.graphics.Color;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class GameStateConfig extends StateConfig
{
    protected static final String COLOR = "Color";
    private final Color color;

    @JsonCreator
    public GameStateConfig(@JsonProperty(STRING_CONFIG) final String stringConfig, @JsonProperty(COLOR) final Color color)
    {
        super(stringConfig);
        this.color = color;
    }

    public Color getColor()
    {
        return color;
    }
}
