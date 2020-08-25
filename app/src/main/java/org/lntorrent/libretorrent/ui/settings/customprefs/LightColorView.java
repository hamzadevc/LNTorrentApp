

package org.lntorrent.libretorrent.ui.settings.customprefs;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import org.lntorrent.libretorrent.R;

/*
 * Display the selected LED color in Preference.
 */

public class LightColorView extends View
{
    private final Paint paint;
    private int w, h;

    public LightColorView(Context context, AttributeSet attrs)
    {
        super(context, attrs);

        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.LightColorView,
                0, 0
        );

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        try {
            paint.setColor(a.getColor(R.styleable.LightColorView_lightColor, Color.WHITE));

        } finally {
            a.recycle();
        }
        paint.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh)
    {
        this.w = w;
        this.h = h;

        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);

        float centerX = (float)w / 2;
        float centerY = (float)h / 2;

        canvas.drawCircle(centerX, centerY, getWidth() / 2, paint);
    }

    public void setColor(int color)
    {
        paint.setColor(color);

        invalidate();
    }
}
