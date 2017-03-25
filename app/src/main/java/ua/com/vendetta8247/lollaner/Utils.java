package ua.com.vendetta8247.lollaner;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;

/**
 * Created by Y500 on 02.02.2017.
 */

public class Utils {

    public static int pxToDp(int px) {
        return (int) (px / Resources.getSystem().getDisplayMetrics().density);
    }

    public static int dpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, int pixels) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap
                .getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = pixels;

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }

    public static Bitmap addBorderToRoundedBitmap(Bitmap srcBitmap, int cornerRadius, int borderWidth, int borderColor){
        borderWidth = borderWidth*2;
        Bitmap dstBitmap = Bitmap.createBitmap(
                srcBitmap.getWidth() + borderWidth,
                srcBitmap.getHeight() + borderWidth,
                Bitmap.Config.ARGB_8888
        );
        Canvas canvas = new Canvas(dstBitmap);

        // Initialize a new Paint instance to draw border
        Paint paint = new Paint();
        paint.setColor(borderColor);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(borderWidth);
        paint.setAntiAlias(true);
        Rect rect = new Rect(
                borderWidth/2,
                borderWidth/2,
                dstBitmap.getWidth() - borderWidth/2,
                dstBitmap.getHeight() - borderWidth/2
        );
        RectF rectF = new RectF(rect);
        canvas.drawRoundRect(rectF,cornerRadius,cornerRadius,paint);

        // Draw source bitmap to canvas
        canvas.drawBitmap(srcBitmap, borderWidth / 2, borderWidth / 2, null);
        srcBitmap.recycle();
        return dstBitmap;
    }

}
