package cn.andy.study.imageloader.imageloader.view;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;

/**
 * 圆形图片
 *
 * @author zdxing 2015年2月4日
 */
public class CircledDrawable extends Drawable {
    protected final Paint paint;
    private float radius;
    private Bitmap bitmap;

    public CircledDrawable(Bitmap bitmap) {
        BitmapShader bitmapShader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        radius = Math.min(bitmap.getWidth() / 2, bitmap.getHeight() / 2);

        this.bitmap = bitmap;

        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        paint.setShader(bitmapShader);
    }

    @Override
    public int getIntrinsicHeight() {
        return (int) (radius * 2);
    }

    @Override
    public int getIntrinsicWidth() {
        return (int) (radius * 2);
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        canvas.save();
        canvas.translate(radius - bitmap.getWidth() / 2, radius - bitmap.getHeight() / 2);
        canvas.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2, radius, paint);
        canvas.restore();
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }

    @Override
    public void setAlpha(int alpha) {
        paint.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(ColorFilter cf) {
        paint.setColorFilter(cf);
    }
}
