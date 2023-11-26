package com.haunt.game;

import java.util.Random;

public class Util {

    public static final Random random = new Random();

    public static String formatTime(float time) {
        int millis = (int) (time % 1 * 100);
        int second = (int) (time % 60 - time % 1);
        int minute = ((int) time) / 60;
        if (minute != 0)
            return Integer.toString(minute) + "m " + Integer.toString(second) + "." + Integer.toString(millis) + "s";
        return Integer.toString(second) + "." + Integer.toString(millis) + "s";
    }
}
