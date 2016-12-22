package com.aepronunciation.ipa;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.EditText;


public class IpaEditText extends EditText {

    // Constructors
    public IpaEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }
    public IpaEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }
    public IpaEditText(Context context) {
        super(context);
        init();
    }

    // This class requires FreeSans.ttf to be in the assets folder
    private void init() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(),
                "FreeSans.ttf");
        setTypeface(tf);
    }

}