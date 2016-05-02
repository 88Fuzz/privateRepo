package com.murder.game.level.generator;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.murder.game.constants.drawing.FontType;
import com.murder.game.drawing.PercentageText;
import com.murder.game.drawing.Text;
import com.murder.game.serialize.MyVector2;
import com.murder.game.serialize.TextStateSerialize;
import com.murder.game.state.modifier.TextStateAdder;
import com.murder.game.state.modifier.TextStateModifier;

public class TextStateGenerator
{
    private static final String DIRECTORY = "texts/";
    private static final String FILE_EXTENSION = ".json";
    private static final ObjectMapper SERIALIZER = new ObjectMapper();

    public static TextStateSerialize getTextState(final String textStateId)
    {
        return generateTextState1(textStateId);
    }

    private static TextStateSerialize generateTextState1(final String textStateId)
    {
        final List<Text> texts = new LinkedList<Text>();

        final List<TextStateModifier> textStateModifiers = new LinkedList<TextStateModifier>();
        final List<Text> addedTexts = new LinkedList<Text>();
//        addedTexts.add(new PercentageText(0, 0, FontType.HAND_48, "Where am I?", 0));
        addedTexts.add(new Text(new MyVector2(1280/2, 0), FontType.HAND_48, "Where am I?", 0));
        addedTexts.add(new PercentageText(-.52f, .78f, FontType.HAND_48, "How did I get here?", 0));
        addedTexts.add(new PercentageText(-.80f, -.35f, FontType.HAND_48, "Goddamn it's dark.", 0));
        textStateModifiers.add(new TextStateAdder(addedTexts));

        return writeTextState(new TextStateSerialize(texts, textStateModifiers), textStateId);
    }

    private static TextStateSerialize writeTextState(final TextStateSerialize textState, final String textStateId)
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
}
