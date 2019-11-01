package com.aepronunciation.ipa;

import android.content.Context;
import android.graphics.Typeface;
import androidx.appcompat.widget.AppCompatTextView;
import android.util.AttributeSet;

public class IpaTextView extends AppCompatTextView {

    // Constructors
    public IpaTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }
    public IpaTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }
    public IpaTextView(Context context) {
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