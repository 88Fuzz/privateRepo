package com.murder.game.utils;

public class StringUtils
{
    public static boolean equals(final String string1, final String string2)
    {
        if(string1 == string2)
        {
            return true;
        }
        if(string1 == null || string2 == null)
        {
            return false;
        }

        return string1.equals(string2);
    }
}