package com.murder.game.level.generator;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.murder.game.constants.drawing.FontType;
import com.murder.game.drawing.PercentageText;
import com.murder.game.drawing.Text;
import com.murder.game.serialize.TextLevelSerialize;
import com.murder.game.state.StateManager.StateId;
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
//         generateTextLevel2("Text02");
//         return generateTextLevel1("Text01");
    }

    private static TextLevelSerialize generateTextLevel3(final String textStateId)
    {
        final List<Text> texts = new LinkedList<Text>();

        final List<TextStateModifier> textStateModifiers = new LinkedList<TextStateModifier>();
        final List<Text> addedTexts = new LinkedList<Text>();
        addedTexts.add(new PercentageText(-.92f, .86f, FontType.HAND_48, "Wait.", 0));
        addedTexts.add(new PercentageText(.0f, .0f, FontType.HAND_48, "...", 0));
        addedTexts.add(new PercentageText(-.3f, -.75f, FontType.HAND_48, "What is this!?", 0));
        addedTexts.add(new PercentageText(.30f, .35f, FontType.HAND_48, "A flashlight!?", 0));
        addedTexts.add(new PercentageText(.50f, .85f, FontType.HAND_48, "It is!", 0));
        addedTexts.add(new PercentageText(-.10f, -.35f, FontType.HAND_48, "Hope it has batteries.", 0));
        textStateModifiers.add(new TextStateAdder(addedTexts));

        return writeTextState(new TextLevelSerialize(texts, textStateModifiers, StateId.GAME_STATE, "Level01"), textStateId);
    }

    private static TextLevelSerialize generateTextLevel2(final String textStateId)
    {
        final List<Text> texts = new LinkedList<Text>();

        final List<TextStateModifier> textStateModifiers = new LinkedList<TextStateModifier>();
        final List<Text> addedTexts = new LinkedList<Text>();
        addedTexts.add(new PercentageText(-.2f, -.34f, FontType.HAND_48, "Do I dare move?", 0));
        addedTexts.add(new PercentageText(-.32f, .58f, FontType.HAND_48, "I can't see past my nose.", 0));
        addedTexts.add(new PercentageText(.0f, -.85f, FontType.HAND_48, "If only I had a flashlight.", 0));
        textStateModifiers.add(new TextStateAdder(addedTexts));

        return writeTextState(new TextLevelSerialize(texts, textStateModifiers, StateId.TEXT_STATE, "Text03"), textStateId);
    }

    private static TextLevelSerialize generateTextLevel1(final String textStateId)
    {
        final List<Text> texts = new LinkedList<Text>();

        final List<TextStateModifier> textStateModifiers = new LinkedList<TextStateModifier>();
        final List<Text> addedTexts = new LinkedList<Text>();
        addedTexts.add(new PercentageText(.2f, .04f, FontType.HAND_48, "Where am I?", 0));
        addedTexts.add(new PercentageText(-.52f, .78f, FontType.HAND_48, "How did I get here?", 0));
        addedTexts.add(new PercentageText(-.80f, -.35f, FontType.HAND_48, "Goddamn it's dark.", 0));
        textStateModifiers.add(new TextStateAdder(addedTexts));

        return writeTextState(new TextLevelSerialize(texts, textStateModifiers, StateId.TEXT_STATE, "Text02"), textStateId);
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