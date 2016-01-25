package com.squared.space.game.context;

import java.util.List;
import java.util.NavigableMap;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.squared.space.game.event.StateEvent;
import com.squared.space.game.drawing.Actor;
import com.squared.space.game.drawing.WorldRenderer;

public class SceneContext
{
    private TextureAtlas atlas;
    private WorldRenderer worldRenderer;
    private NavigableMap<Integer, StateEvent> activateEvents;
    private List<Actor> actors;

    public void init(final NavigableMap<Integer, StateEvent> activateEvents, final List<Actor> actors)
    {
        this.activateEvents = activateEvents;
        this.actors = actors;
    }

    public TextureAtlas getAtlas()
    {
        return atlas;
    }

    public void setAtlas(final TextureAtlas atlas)
    {
        this.atlas = atlas;
    }

    public WorldRenderer getWorldRenderer()
    {
        return worldRenderer;
    }

    public void setWorldRenderer(final WorldRenderer worldRenderer)
    {
        this.worldRenderer = worldRenderer;
    }

    public NavigableMap<Integer, StateEvent> getActivateEvents()
    {
        return activateEvents;
    }

    public List<Actor> getActors()
    {
        return actors;
    }
}
