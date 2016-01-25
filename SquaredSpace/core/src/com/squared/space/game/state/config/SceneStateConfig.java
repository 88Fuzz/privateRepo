package com.squared.space.game.state.config;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.squared.space.game.event.StateEvent;
import com.squared.space.game.constants.TextureConstants;
import com.squared.space.game.context.SceneContext;
import com.squared.space.game.drawing.Actor;
import com.squared.space.game.drawing.WorldRenderer;
import com.squared.space.game.state.StateManager.StateId;

public class SceneStateConfig
{
    public static final String TEST_SCENE = "TEST_SCENE";

    private final Map<String, SceneContext> sceneContextConfig;
    private final WorldRenderer worldRenderer;
    private final AssetManager assMan;

    public SceneStateConfig(final WorldRenderer worldRenderer, final AssetManager assMan)
    {
        this.worldRenderer = worldRenderer;
        this.sceneContextConfig = new HashMap<String, SceneContext>();
        this.assMan = assMan;

        initMap();
    }

    private void initMap()
    {
        // TEST_SCENE
        final SceneContext testScene = new SceneContext();
        final StateEvent stateEvent = new StateEvent(StateId.TEXT_STATE, TextStateConfig.TEST_TEXT, 600, 700);
        final TreeMap<Integer, StateEvent> activateEvents = new TreeMap<Integer, StateEvent>();
        activateEvents.put(600, stateEvent);

        final Actor singleActor = new Actor();
        final Sprite singleSprite = new Sprite(
                ((TextureAtlas) assMan.get(TextureConstants.TILE_TEXTURES)).findRegion(TextureConstants.SINGLE_PIXEL));
        singleActor.init(new Vector2(600, 0), singleSprite);
        singleSprite.setBounds(600, 0, 100, 400);
        singleSprite.setColor(1, 1, 1, 1);
        testScene.init(activateEvents, Collections.singletonList(singleActor));

        sceneContextConfig.put(TEST_SCENE, testScene);
    }

    public SceneContext getContext(final String sceneState)
    {
        final SceneContext sceneContext = sceneContextConfig.get(sceneState);
        if(sceneContext != null)
        {
            sceneContext.setWorldRenderer(worldRenderer);
            sceneContext.setAtlas((TextureAtlas) assMan.get(TextureConstants.TILE_TEXTURES));
        }

        return sceneContext;
    }
}
