package com.squared.space.game.state.config;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.squared.space.event.StateEvent;
import com.squared.space.game.constants.TextureConstants;
import com.squared.space.game.context.SceneContext;
import com.squared.space.game.drawing.Actor;
import com.squared.space.game.drawing.WorldRenderer;
import com.squared.space.game.state.StateManager.StateId;

public class SceneStateConfig
{
    public static final String TEST_SCENE = "TEST_SCENE";
    public static final String INTRO_SCENE = "INTRO_SCENE";
    private static final Rectangle CLEAR_COLOR = new Rectangle(31/255f, 25/255f, 102/255f, 1);

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
        final TextureAtlas atlas = assMan.get(TextureConstants.TILE_TEXTURES);
        final Actor player = new Actor();
        final Sprite playerSprite = new Sprite(atlas.findRegion(TextureConstants.SINGLE_PIXEL));
        final int size = Gdx.graphics.getHeight() / 10;
        playerSprite.setBounds(0, 0, size, size);
        playerSprite.setColor(0, 0, 1, 1);
        player.init(new Vector2(0, 0), playerSprite);

        final Actor emptyPlayer = new Actor();
        final Sprite emptySprite = new Sprite(atlas.findRegion(TextureConstants.SINGLE_PIXEL));
        emptySprite.setColor(0, 0, 0, 0);
        emptyPlayer.init(new Vector2(0, 0), emptySprite);

        // TEST_SCENE
        StateEvent stateEvent = new StateEvent(StateId.TEXT_STATE, TextStateConfig.TEST_TEXT, 600, 700);
        TreeMap<Integer, StateEvent> activateEvents = new TreeMap<Integer, StateEvent>();
        activateEvents.put(600, stateEvent);

        SceneContext scene = new SceneContext();
        Actor singleActor = new Actor();
        Sprite singleSprite = new Sprite(atlas.findRegion(TextureConstants.SINGLE_PIXEL));
        singleActor.init(new Vector2(600, 0), singleSprite);
        singleSprite.setBounds(600, 0, 100, 400);
        singleSprite.setColor(1, 1, 1, 1);

        scene.init(activateEvents, Collections.singletonList(singleActor), player, CLEAR_COLOR);
        sceneContextConfig.put(TEST_SCENE, scene);

        // INTRO_SCENE
        stateEvent = new StateEvent(StateId.TEXT_STATE, TextStateConfig.INTRO_TEXT, Integer.MIN_VALUE,
                Integer.MAX_VALUE);
        activateEvents = new TreeMap<Integer, StateEvent>();
        activateEvents.put(Integer.MIN_VALUE, stateEvent);

        scene = new SceneContext();
        singleActor = new Actor();
        singleSprite = new Sprite(atlas.findRegion(TextureConstants.AMBULANCE));
        singleActor.init(new Vector2(0, 0), singleSprite);

        scene.init(activateEvents, Collections.singletonList(singleActor), emptyPlayer, CLEAR_COLOR);
        sceneContextConfig.put(INTRO_SCENE, scene);
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