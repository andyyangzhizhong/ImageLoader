package cn.andy.study.imageloader.imageloader.view;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;

/**
 * 圆角 图片
 *
 * @author zdxing 2015年1月30日
 */
public class RoundedDrawable extends Drawable {
    protected final float cornerRadiusX;
    protected final float cornerRadiusY;
    protected final int margin;

    protected final RectF mRect = new RectF(), mBitmapRect;
    protected final BitmapShader bitmapShader;
    protected final Paint paint;
    private Bitmap bitmap;

    private float rx;
    private float ry;

    public RoundedDrawable(Bitmap bitmap, int cornerRadiusX, int cornerRadiusY, int margin) {
        this.cornerRadiusX = cornerRadiusX;
        this.cornerRadiusY = cornerRadiusY;
        this.margin = margin;
        this.bitmap = bitmap;

        bitmapShader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        mBitmapRect = new RectF(margin, margin, bitmap.getWidth() - margin, bitmap.getHeight() - margin);

        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setShader(bitmapShader);
    }

    @Override
    public int getIntrinsicHeight() {
        return bitmap.getHeight();
    }

    @Override
    public int getIntrinsicWidth() {
        return bitmap.getWidth();
    }

    @Override
    protected void onBoundsChange(Rect bounds) {
        super.onBoundsChange(bounds);
        mRect.set(margin, margin, bounds.width() - margin, bounds.height() - margin);

        Matrix shaderMatrix = new Matrix();
        shaderMatrix.setRectToRect(mBitmapRect, mRect, Matrix.ScaleToFit.FILL);
        bitmapShader.setLocalMatrix(shaderMatrix);

        rx = (float) (bounds.width() - margin * 2) / bitmap.getWidth() * cornerRadiusX;
        ry = (float) (bounds.height() - margin * 2) / bitmap.getHeight() * cornerRadiusY;
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        canvas.drawRoundRect(mRect, rx, ry, paint);
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
