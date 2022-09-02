package com.aepronunciation.ipa;


import android.content.Context;

import java.util.Locale;

class AppLocale {
    static Locale getLocale(Context context) {
        // this allows things to only be formatted for translated languages
        // All others will use US format.
        String currentTranslationLocale = context.getString(R.string.locale);
        switch (currentTranslationLocale) {
            case "zh-rCN":
                return Locale.SIMPLIFIED_CHINESE;
            case "zh-rTW":
            case "zh":
                return Locale.TRADITIONAL_CHINESE;
            case "ru":
                return new Locale("ru","RU");
            case "mn":
                return new Locale("mn", "MN");
            case "ja":
                return new Locale("ja", "JP");
            case "vi":
                return new Locale("vi", "VN");
            case "ko":
                //return new Locale("ko", "KR");
                return Locale.KOREA;
            default:
                return Locale.US;
        }
    }
}
