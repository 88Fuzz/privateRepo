package com.murder.game.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;

public class FileUtils
{
    private static final int DEFAULT_BUFFER_SIZE = 1024 * 4;

    public static String readFile(final File file) throws IOException
    {
        final FileInputStream inputStream = new FileInputStream(file);

        try
        {
            return readInputStream(inputStream);
        }
        finally
        {
            inputStream.close();
        }
    }

    public static String readInputStream(final InputStream inStream) throws IOException
    {
        final StringWriter sw = new StringWriter();
        byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
        int n = 0;

        while(-1 != (n = inStream.read(buffer)))
        {
            final String stringBuf = new String(buffer);
            sw.write(stringBuf.toCharArray(), 0, n);
        }
        return sw.toString();
    }
}