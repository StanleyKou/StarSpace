package com.kou.starspace;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.Random;

public class SampleCircle extends View {

    int framesPerSecond = 60;
    long startTime = 0L;
    int width = 0;
    int height = 0;
    int centerWidth = 0;
    int centerHeight = 0;

    Random random = new Random();

    final int dotCount = 300;
    Dot[] dots = new Dot[dotCount + 1];

    class Dot {
        float x = -1;
        float y = -1;
        float xDirection = 1;
        float yDirection = 1;
        float ratio = 0;

        final float initialDotSize = 5f;
        final float initialXSpeed = 5f;

        float dotSize = 5f;
        float xSpeed = 5f;

        private Paint paint;

        public Dot() {
            paint = new Paint();
            paint.setAntiAlias(true);
            paint.setColor(Color.RED);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(dotSize);
        }

        public void drawNext(Canvas canvas) {
            if (x == -1 && y == -1) {
                x = width * random.nextFloat();
                if (x >= centerWidth) {
                    xDirection = 1;
                } else {
                    xDirection = -1;
                }

                y = height * random.nextFloat();
                if (y >= centerHeight) {
                    yDirection = 1;
                } else {
                    yDirection = -1;
                }
                ratio = (y - centerHeight) / (x - centerWidth);

                dotSize = initialDotSize + random.nextFloat() * initialDotSize * 2;
                paint.setStrokeWidth(dotSize);

                xSpeed = initialXSpeed + random.nextFloat() * initialXSpeed * 2;
            }

            float dx = xSpeed * xDirection;
            float dy = dx * ratio;

            x = x + dx;
            y = y + dy;

            if (
                    (xDirection == 1 && x > width || xDirection == -1 && x < -1)
                            || (yDirection == 1 && y > height || yDirection == -1 && y < -1)) {
                x = -1;
                y = -1;
                paint.setARGB(255, random.nextInt(256), random.nextInt(256), random.nextInt(256));
            }

            // Log.d("TAG", "width: " + width + " x: " + x + " height: " + height + " y: " + y);
            canvas.drawCircle(x, y, 1, paint);
        }
    }

    public SampleCircle(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs);
    }

    public SampleCircle(Context context, @Nullable AttributeSet attrs) {
        this(context);
    }

    public SampleCircle(Context context) {
        super(context);
        setFocusable(true);

        this.startTime = System.currentTimeMillis();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new Dot();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(Color.TRANSPARENT);

        width = getWidth();
        height = getHeight();
        centerWidth = (int) (width / 2f);
        centerHeight = (int) (height / 2f);

        // long elapsedTime = System.currentTimeMillis() - startTime;

        for (int i = 0; i < dots.length; i++) {
            dots[i].drawNext(canvas);
        }

        this.postInvalidateDelayed(1000 / framesPerSecond);
    }
}
