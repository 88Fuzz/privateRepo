package com.murder.game.level.generator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.murder.game.constants.drawing.FontType;
import com.murder.game.drawing.drawables.PercentageText;
import com.murder.game.drawing.drawables.Text;
import com.murder.game.effects.text.LinearTextColorChanger;
import com.murder.game.effects.text.TextEffect;
import com.murder.game.serialize.TextLevelSerialize;
import com.murder.game.state.config.GameStateConfig;
import com.murder.game.state.config.StateConfig;
import com.murder.game.state.management.PendingAction;
import com.murder.game.state.management.StateAction;
import com.murder.game.state.management.StateId;
import com.murder.game.state.modifier.TextStateAdder;
import com.murder.game.state.modifier.TextStateModifier;

public class TextLevelGenerator
{
    private static final String DIRECTORY = "texts/";
    private static final String FILE_EXTENSION = ".json";
    private static final ObjectMapper SERIALIZER = new ObjectMapper();

    public static TextLevelSerialize getTextState(final String textStateId)
    {
         final TextLevelSerialize textState = loadLevelFromFile(textStateId);
         if(textState != null)
         return textState;
        
         throw new RuntimeException("Unknown textStateId " + textStateId);

//        generateTextLevel3("Text03");
//        generateTextLevel2("Text02");
//        return generateTextLevel1("Text01");
    }

    private static TextLevelSerialize generateTextLevel3(final String textStateId)
    {
        final List<Color> colors = new ArrayList<Color>();
        colors.add(Color.BLACK);
        colors.add(Color.WHITE);

        final LinkedList<TextEffect> slowFadeIn = new LinkedList<TextEffect>();
        slowFadeIn.add(new LinearTextColorChanger(colors, false, .55f));

        final LinkedList<TextEffect> fastFadeIn = new LinkedList<TextEffect>();
        fastFadeIn.add(new LinearTextColorChanger(colors, false, .35f));

        final List<Text> texts = new LinkedList<Text>();

        final List<TextStateModifier> textStateModifiers = new LinkedList<TextStateModifier>();
        final List<Text> addedTexts = new LinkedList<Text>();
        addedTexts.add(new PercentageText(-.92f, .86f, FontType.HAND_48, "Wait.", 0, slowFadeIn));
        addedTexts.add(new PercentageText(.0f, .0f, FontType.HAND_48, "...", 0, slowFadeIn));
        addedTexts.add(new PercentageText(-.3f, -.75f, FontType.HAND_48, "What is this!?", 0, fastFadeIn));
        addedTexts.add(new PercentageText(.30f, .35f, FontType.HAND_48, "A flashlight!?", 0, fastFadeIn));
        addedTexts.add(new PercentageText(.50f, .85f, FontType.HAND_48, "It is!", 0, fastFadeIn));
        addedTexts.add(new PercentageText(-.10f, -.35f, FontType.HAND_48, "Hope it has batteries.", 0, fastFadeIn));
        textStateModifiers.add(new TextStateAdder(addedTexts));

        final List<PendingAction> actions = new LinkedList<PendingAction>();
        actions.add(new PendingAction().withAction(StateAction.POP));
        actions.add(new PendingAction().withAction(StateAction.PUSH).withStateId(StateId.GAME_STATE).withStateConfig(new GameStateConfig("Level01", Color.BLACK)));
        return writeTextState(new TextLevelSerialize(texts, textStateModifiers, actions), textStateId);
    }

    private static TextLevelSerialize generateTextLevel2(final String textStateId)
    {
        final List<Color> colors = new ArrayList<Color>();
        colors.add(Color.BLACK);
        colors.add(Color.WHITE);
        final LinkedList<TextEffect> textEffects = new LinkedList<TextEffect>();
        textEffects.add(new LinearTextColorChanger(colors, false, .55f));

        final List<Text> texts = new LinkedList<Text>();

        final List<TextStateModifier> textStateModifiers = new LinkedList<TextStateModifier>();
        final List<Text> addedTexts = new LinkedList<Text>();
        addedTexts.add(new PercentageText(-.2f, -.34f, FontType.HAND_48, "Do I dare move?", 0, textEffects));
        addedTexts.add(new PercentageText(-.32f, .58f, FontType.HAND_48, "I can't see past my nose.", 0, textEffects));
        addedTexts.add(new PercentageText(.0f, -.85f, FontType.HAND_48, "If only I had a flashlight.", 0, textEffects));
        textStateModifiers.add(new TextStateAdder(addedTexts));

        final List<PendingAction> actions = new LinkedList<PendingAction>();
        actions.add(new PendingAction().withAction(StateAction.POP));
        actions.add(new PendingAction().withAction(StateAction.PUSH).withStateId(StateId.TEXT_STATE).withStateConfig(new StateConfig("Text03")));
        return writeTextState(new TextLevelSerialize(texts, textStateModifiers, actions), textStateId);
    }

    private static TextLevelSerialize generateTextLevel1(final String textStateId)
    {
        final List<Color> colors = new ArrayList<Color>();
        colors.add(Color.BLACK);
        colors.add(Color.WHITE);

        final LinkedList<TextEffect> longFadeIn = new LinkedList<TextEffect>();
        longFadeIn.add(new LinearTextColorChanger(colors, false, 1));

        final LinkedList<TextEffect> mediumFadeIn = new LinkedList<TextEffect>();
        mediumFadeIn.add(new LinearTextColorChanger(colors, false, .75f));

        final LinkedList<TextEffect> shortFadeIn = new LinkedList<TextEffect>();
        shortFadeIn.add(new LinearTextColorChanger(colors, false, .55f));

        final List<Text> texts = new LinkedList<Text>();

        final List<TextStateModifier> textStateModifiers = new LinkedList<TextStateModifier>();
        final List<Text> addedTexts = new LinkedList<Text>();
        addedTexts.add(new PercentageText(.2f, .04f, FontType.HAND_48, "Where am I?", 0, longFadeIn));
        addedTexts.add(new PercentageText(-.52f, .78f, FontType.HAND_48, "How did I get here?", 0, mediumFadeIn));
        addedTexts.add(new PercentageText(-.80f, -.35f, FontType.HAND_48, "Goddamn it's dark.", 0, shortFadeIn));
        textStateModifiers.add(new TextStateAdder(addedTexts));

        final List<PendingAction> actions = new LinkedList<PendingAction>();
        actions.add(new PendingAction().withAction(StateAction.POP));
        actions.add(new PendingAction().withAction(StateAction.PUSH).withStateId(StateId.TEXT_STATE).withStateConfig(new StateConfig("Text02")));
        return writeTextState(new TextLevelSerialize(texts, textStateModifiers, actions), textStateId);
    }

    private static TextLevelSerialize writeTextState(final TextLevelSerialize textState, final String textStateId)
    {
        try
        {
            SERIALIZER.writeValue(Gdx.files.internal(DIRECTORY + textStateId + FILE_EXTENSION).file(), textState);
        }
        catch(final IOException e)
        {
            e.printStackTrace();
        }
        return textState;
    }

    private static TextLevelSerialize loadLevelFromFile(final String levelId)
    {
        try
        {
            return SERIALIZER.readValue(Gdx.files.internal(DIRECTORY + levelId + FILE_EXTENSION).readString(), TextLevelSerialize.class);
        }
        catch(final Exception e)
        {
            throw new RuntimeException(e);
        }
    }
}