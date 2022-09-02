package com.aepronunciation.ipa;

import java.util.concurrent.TimeUnit;

class TimeUtil {

    // converts time in nanoseconds to hours, minutes, and seconds
    static CharSequence getTimeString(long time) {

        // calculate hours, minutes, and seconds
        int seconds = (int) TimeUnit.NANOSECONDS.toSeconds(time);
        int hours = seconds / 3600;
        seconds = seconds % 3600;
        int minutes = seconds / 60;
        seconds = seconds % 60;
        // FIXME rounding error, use milliseconds to round up or down

        // prefix single digits with zero for minutes and seconds
        String minutesString = String.valueOf(minutes);
        if (minutes < 10) {
            minutesString = "0" + minutesString;
        }
        String secondsString = String.valueOf(seconds);
        if (seconds < 10) {
            secondsString = "0" + secondsString;
        }

        return hours + ":" + minutesString + ":" + secondsString;
    }
}
