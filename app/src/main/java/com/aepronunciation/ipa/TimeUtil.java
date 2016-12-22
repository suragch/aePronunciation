package com.aepronunciation.ipa;

import java.util.concurrent.TimeUnit;

public class TimeUtil {

    // converts time in nanoseconds to hours, minutes, and seconds
    public static CharSequence getTimeString(long time) {

        // calculate hours, minutes, and seconds
        int seconds = (int) TimeUnit.NANOSECONDS.toSeconds(time);
        int hours = seconds / 3600;
        seconds = seconds % 3600;
        int minutes = seconds / 60;
        seconds = seconds % 60;
        // TODO use milliseconds to round up or down

        // prefix single digits with zero for minutes and seconds
        String minutesString = String.valueOf(minutes);
        if (minutes < 10) {
            minutesString = "0" + minutesString;
        }
        String secondsString = String.valueOf(seconds);
        if (seconds < 10) {
            secondsString = "0" + secondsString;
        }

        return  String.valueOf(hours) + ":" + minutesString + ":" + secondsString;

        // Not using this because not formatted for localization
//        StringBuilder timeString = new StringBuilder();
//        if (hours > 1) {
//            timeString.append(hours + " hours");
//        } else if (hours == 1) {
//            timeString.append("1 hour");
//        }
//        if (timeString.length() > 0) {
//            if (minutes > 1) {
//                timeString.append(", " + minutes + " minutes");
//            } else if (minutes == 1) {
//                timeString.append(", 1 minute");
//            }
//        } else {
//            if (minutes > 1) {
//                timeString.append(minutes + " minutes");
//            } else if (minutes == 1) {
//                timeString.append("1 minute");
//            }
//        }
//        if (timeString.length() > 0) {
//            if (seconds > 1) {
//                timeString.append(", " + seconds + " seconds");
//            } else if (seconds == 1) {
//                timeString.append(", 1 second");
//            }
//        } else {
//            if (seconds > 1) {
//                timeString.append(seconds + " seconds");
//            } else if (minutes == 1) {
//                timeString.append("1 second");
//            }
//        }
//
//        return timeString.toString();
    }
}
