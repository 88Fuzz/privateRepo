package com.squared.space.game.state.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.squared.space.game.drawing.text.Text;
import com.squared.space.game.drawing.text.TextOption;

public class TextStateConfig
{
    public static final String TEST_TEXT = "TEST_TEXT";
    public static final String INTRO_TEXT = "INTRO_TEXT";
    public static final Map<String, Text> TEXT_STATE_CONFIG = new HashMap<String, Text>();

    // Yes this should be some kind of config file somewhere and not hard coded.
    // But that's for future improvements.
    static
    {
        Text text = new Text();
        text.setTexts("Boop my beep.", "Doctor");

        Text text1 = new Text();
        text1.setTexts(
                "Pink bubblegum. Pink bubblegum. Pink bubblegum. Pink bubblegum.\nPink bubblegum. Pink bubblegum. Pink bubblegum.",
                "Mom");
        text.setNextText(text1);

        Text text2 = new Text();
        text2.setTexts("My blue cups.", "Doctor");
        text1.setNextText(text2);

        Text text3 = new TextOption();
        text3.setTexts("Christmas tree surprise...", "");
        text2.setNextText(text3);

        final Text text4 = new Text();
        text4.setTexts("Ending text", "Tits");

        final List<Text> textOptions = new ArrayList<Text>();
        textOptions.add(text);
        textOptions.add(text1);
        textOptions.add(text2);
        textOptions.add(text4);

        ((TextOption) text3).setTextOptions(textOptions);;

        TEXT_STATE_CONFIG.put(TEST_TEXT, text);

        // INTRO_TEXT
        final String doctorName = "Doctor";
        final String playerName = "Kate";
        text = new Text();
        text.setTexts("Look at me!", doctorName);
        TEXT_STATE_CONFIG.put(INTRO_TEXT, text);
        
        text2 = new Text();
        text2.setTexts("Look at me and breathe with me!", doctorName);
        text.setNextText(text2);
        
        text3 = new Text();
        text3.setTexts("Yeah, okay. I can do that.", playerName);
        text2.setNextText(text3);

        text1 = new Text();
        text1.setTexts("Deep breath in...", doctorName);
        text3.setNextText(text1);

        text = new Text();
        text.setTexts("And out...", doctorName);
        text1.setNextText(text);

        text1 = new Text();
        text1.setTexts("Breathe in...", doctorName);
        text.setNextText(text1);

        text = new Text();
        text.setTexts("Now out...", doctorName);
        text1.setNextText(text);

        text1 = new Text();
        text1.setTexts("Alright, " + playerName + ".", doctorName);
        text.setNextText(text1);

        text2 = new Text();
        text2.setTexts("On the count of three I want you to push.", doctorName);
        text1.setNextText(text2);

        text = new Text();
        text.setTexts("1...", doctorName);
        text2.setNextText(text);

        text1 = new Text();
        text1.setTexts("Okay.", playerName);
        text.setNextText(text1);

        text = new Text();
        text.setTexts("Okay. I can do this.", playerName);
        text1.setNextText(text);

        text1 = new Text();
        text1.setTexts("2...", doctorName);
        text.setNextText(text1);

        text = new Text();
        text.setTexts("Just push.", playerName);
        text1.setNextText(text);

        text2 = new Text();
        text2.setTexts("It'll be worth it, all I do is push.", playerName);
        text1.setNextText(text2);

        text1 = new Text();
        text1.setTexts("3...", doctorName);
        text2.setNextText(text1);

        text = new Text();
        text.setTexts("Just.", playerName);
        text1.setNextText(text);
        
        text1 = new Text();
        text1.setTexts("Push.", playerName);
        text.setNextText(text1);
    }

    public static Text getTexts(final String state)
    {
        return TEXT_STATE_CONFIG.get(state);
    }
}