package com.murder.game.state.config;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

/**
 * Data class used to construct a new game state.
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({ @Type(value = StateConfig.class, name = "StateConfig"), @Type(value = GameStateConfig.class, name = "GameStateConfig") })
public class StateConfig
{
    protected static final String STRING_CONFIG = "StringConfig";

    private final String stringConfig;

    @JsonCreator
    public StateConfig(@JsonProperty(STRING_CONFIG) final String stringConfig)
    {
        this.stringConfig = stringConfig;
    }

    public String getStringConfig()
    {
        return stringConfig;
    }
}