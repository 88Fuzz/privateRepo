package com.squared.space.game.state.config;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.squared.space.game.drawing.Text;

public class TextStateConfig
{
    public static final String TEST_TEXT = "TEST_TEXT";

    public static final Map<String, List<Text>> TEXT_STATE_CONFIG = new HashMap<String, List<Text>>();

    static
    {
        final List<Text> testTexts = new LinkedList<Text>();
        final Text text = new Text();
        text.setText("Boop my beep.");

        final Text text1 = new Text();
        text1.setText(
                "Pink bubblegum. Pink bubblegum. Pink bubblegum. Pink bubblegum. \nPink bubblegum. Pink bubblegum. Pink bubblegum.");

        final Text text2 = new Text();
        text2.setText("My blue cups.");

        testTexts.add(text);
        testTexts.add(text1);
        testTexts.add(text2);
        TEXT_STATE_CONFIG.put(TEST_TEXT, testTexts);
    }

    public static List<Text> getTexts(final String state)
    {
        final List<Text> texts = TEXT_STATE_CONFIG.get(state);
        if(texts == null)
            return new LinkedList<Text>();

        return texts;
    }
}