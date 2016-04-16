package com.kimfy.notenoughblocks.common.util;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;

public class Log
{
    public static void info(String message, Object... params)
    {
        log(Level.INFO, message, params);
    }

    public static void error(String message, Object... params)
    {
        log(Level.ERROR, message, params);
    }

    public static void log(Level logLevel, String message, Object... params)
    {
        LogManager.getLogger(Constants.MOD_ID).log(logLevel, "[" + Constants.MOD_NAME + "] " + message, params);
    }
}
