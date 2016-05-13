package com.checkpoint.andela.gmaps;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by suadahaji.
 */
public class CustomTextViewFont extends TextView {

    public CustomTextViewFont(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setTypeface(Typeface.createFromAsset(context.getAssets(), "digital.ttf"));
        this.setTextColor(getResources().getColor(R.color.colorPrimary));
    }
}
