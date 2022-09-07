package com.gunuedu.room_escape;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class TextOutLineView extends TextView {

    private static Paint getWhiteBorderPaint(){
        Paint p = new Paint(Color.WHITE);
        return p;
    }

    private static final Paint BLACK_BORDER_PAINT = getWhiteBorderPaint();

    static {
        BLACK_BORDER_PAINT.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
    }

    @Override
    public void setText(CharSequence text, BufferType type) {

        super.setText(String.format(text.toString()), type);
    }

    private static final int BORDER_WIDTH = 1;

    private Typeface typeface;

    public TextOutLineView(Context context) {
        super(context);
    }

    public TextOutLineView(Context context, AttributeSet attrs) {
        super(context, attrs);

        setDrawingCacheEnabled(false);

        setTypeface(attrs);
    }

    private void setTypeface(AttributeSet attrs) {
        final String typefaceFileName = attrs.getAttributeValue(null, "typeface");
        if (typefaceFileName != null) {
            typeface = Typeface.createFromAsset(getContext().getAssets(), typefaceFileName);
        }

        setTypeface(typeface);
    }

    public TextOutLineView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        setTypeface(attrs);
    }

    @Override
    public void draw(Canvas aCanvas) {
        for (int i = 0; i < 5; i++) {
            super.draw(aCanvas);
        }

    }

    private void drawBackground(Canvas aCanvas, int aDX, int aDY) {
        aCanvas.translate(aDX, aDY);
        super.draw(aCanvas);
    }
}