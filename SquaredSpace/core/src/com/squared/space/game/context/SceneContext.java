package com.squared.space.game.context;

import java.util.List;
import java.util.NavigableMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.squared.space.event.StateEvent;
import com.squared.space.game.constants.TextureConstants;
import com.squared.space.game.drawing.Actor;
import com.squared.space.game.drawing.WorldRenderer;

public class SceneContext
{
    private TextureAtlas atlas;
    private Actor player;
    private WorldRenderer worldRenderer;
    private NavigableMap<Integer, StateEvent> activateEvents;
    private List<Actor> actors;
    private Rectangle clearColor;

    public void init(final NavigableMap<Integer, StateEvent> activateEvents, final List<Actor> actors,
            final Actor player, final Rectangle clearColor)
    {
        this.clearColor = clearColor;
        this.player = player;
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

    public Actor getPlayer()
    {
        return player;
    }

    public List<Actor> getActors()
    {
        return actors;
    }

    public Rectangle getClearColor()
    {
        return clearColor;
    }
}