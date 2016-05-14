package com.checkpoint.andela.gmaps;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * Created by suadahaji.
 */
public class EditTextLined extends EditText {


    private Rect mRect;
    private Paint mPaint;

    /**
     * This constructor is needed for the LayoutInflater
     * */

    public EditTextLined(Context context, AttributeSet attrs) {
        super(context, attrs);
        mRect = new Rect();
        mPaint = new Paint();

        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mPaint.setColor(getResources().getColor(R.color.colorPrimary));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int height = getHeight();
        int lineHeight = getLineHeight();
        int count = height / lineHeight;
        if (getLineCount() > count)
            count = getLineCount(); // for long text with scrolling

        Rect rect = mRect;
        Paint paint = mPaint;
        int baseline = getLineBounds(0, rect); // first line

        for (int line = 0; line < count; line++) {

            canvas.drawLine(rect.left, baseline + 1, rect.right, baseline + 1, paint);
            baseline += getLineHeight();  // next line
        }
        super.onDraw(canvas);
    }
}
