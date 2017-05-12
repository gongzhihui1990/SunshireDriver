/**
 * @Description: TODO
 * @author: Administrator
 * @version: 1.0
 * @see
 */

package net.iaf.framework.util;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * 图片处理工具类
 *
 * @author Bob
 */
public class ImageUtil {

    public static void saveBitmap2File(Bitmap mBitmap, String filePath) throws IOException {
        File f = new File(filePath);
        f.createNewFile();
        FileOutputStream fOut = null;
        fOut = new FileOutputStream(f);
        mBitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
        fOut.flush();
        fOut.close();
    }

    // 将图片圆角显示的函数,返回Bitmap
    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap) {

        // Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
        // bitmap.getHeight(), Config.ARGB_8888);
        // Canvas canvas = new Canvas(output);
        //
        // // DisplayMetrics dm =
        // MyApplication.getAppContext().getResources().getDisplayMetrics();
        //
        // final int color = 0xff424242;
        // final Paint paint = new Paint();
        // // 根据原来图片大小画一个矩形
        // final Rect rect = new Rect(0, 0, bitmap.getWidth(),
        // bitmap.getHeight());
        // final RectF rectF = new RectF(rect);
        // // 圆角弧度参数,数值越大圆角越大,甚至可以画圆形
        // final float roundPx = 4 * ((bitmap.getWidth())/ 50);
        //
        // paint.setAntiAlias(true);
        // canvas.drawARGB(0, 0, 0, 0);
        // paint.setColor(color);
        // // 画出一个圆角的矩形
        // canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        // // 取两层绘制交集,显示上层
        // paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        // // 显示图片
        // canvas.drawBitmap(bitmap, rect, rect, paint);
        //
        // int width = output.getWidth();
        // int height = output.getHeight();
        // int nLength = width * height * 4;
        //
        // int[] pixels = new int[nLength];
        // output.getPixels(pixels, 0, width, 0, 0, width, height);
        // bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        // output.recycle();
        // output = null;
        // 返回Bitmap对象
        return bitmap;
    }

    /**
     * 从资源获取默认图片
     *
     * @param res
     * @param resid
     * @return
     */
    public static Bitmap getDefaultBitmap(Resources res, int resid) {
        return BitmapFactory.decodeResource(res, resid);
    }

    /**
     * 从资源获取圆角默认图片
     *
     * @param res
     * @param resid
     * @return
     */
    public static Bitmap getDefaultBitmapRoundedCorner(Resources res, int resid) {
        Bitmap bitmap = BitmapFactory.decodeResource(res, resid);

        // Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
        // bitmap.getHeight(), Config.ARGB_8888);
        // Canvas canvas = new Canvas(output);
        //
        // // DisplayMetrics dm =
        // MyApplication.getAppContext().getResources().getDisplayMetrics();
        //
        // final int color = 0xff424242;
        // final Paint paint = new Paint();
        // // 根据原来图片大小画一个矩形
        // final Rect rect = new Rect(0, 0, bitmap.getWidth(),
        // bitmap.getHeight());
        // final RectF rectF = new RectF(rect);
        // // 圆角弧度参数,数值越大圆角越大,甚至可以画圆形
        // final float roundPx = 4 * ((bitmap.getWidth())/ 50);
        //
        // paint.setAntiAlias(true);
        // canvas.drawARGB(0, 0, 0, 0);
        // paint.setColor(color);
        // // 画出一个圆角的矩形
        // canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        // // 取两层绘制交集,显示上层
        // paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        // // 显示图片
        // canvas.drawBitmap(bitmap, rect, rect, paint);
        //
        // bitmap.recycle();
        // bitmap = null;

        return bitmap;
    }

    /**
     * drawable到Bitmap的转换方法
     *
     * @param drawable
     * @return
     */
    public static Bitmap drawableToBitmap(Drawable drawable) {
        Bitmap bitmap = Bitmap
                .createBitmap(
                        drawable.getIntrinsicWidth(),
                        drawable.getIntrinsicHeight(),
                        drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                                : Bitmap.Config.RGB_565);

        Canvas canvas = new Canvas(bitmap);
        // canvas.setBitmap(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    public static Bitmap byteBuffer2Bitmap(ByteBuffer data, int maxSize) {
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        Bitmap bm = BitmapFactory.decodeByteArray(data.array(), 0,
                data.limit(), o);
        if (bm != null) {
            bm.recycle();
            bm = null;
        }
        int sampleSize = 1;
        if (o.outHeight > maxSize || o.outWidth > maxSize) {
            sampleSize = (int) Math.pow(
                    2,
                    (int) Math.round(Math.log(maxSize
                            / (double) Math.max(o.outHeight, o.outWidth))
                            / Math.log(0.5)));
        }
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inSampleSize = sampleSize;
        return BitmapFactory.decodeByteArray(data.array(), 0, data.limit(),
                opts);
    }

}
