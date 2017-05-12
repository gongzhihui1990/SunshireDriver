package net.iaf.framework.util.tmpfile;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * 临时的图片管理容器
 *
 * @author zgg
 */
public class TmpImageManagerAdapter {

    private String TAG = "TempImageManagerAdapter";
    private FileManager tempFileManager;

    public TmpImageManagerAdapter(Context context,
                                  FileManager tempFileManager) {
        this.tempFileManager = tempFileManager;
    }

    public TmpImageManagerAdapter(Context context) {
        // 默认存在应用的catch区
        this.tempFileManager = TmpFileManagerDataCacheImpl
                .getInstance(context);
    }

    /**
     * 根据文件key读取图片的bitmap
     *
     * @param fileKey
     * @return Bitmap
     */
    public Bitmap readImage(String fileKey) {
        if (tempFileManager == null) {
            Log.e(TAG, "The property 'tempFileManager' have not be setted.");
            return null;
        }
        return bytes2bitmap(tempFileManager.readFile(fileKey));
    }

    /**
     * 写入图片
     *
     * @param bitmap  位图
     * @param fileKey 文件key
     * @return 写入成功返回true，失败返回false
     */
    public boolean writeImage(Bitmap bitmap, String fileKey) {
        if (tempFileManager == null) {
            Log.e(TAG, "The property 'tempFileManager' have not be setted.");
            return false;
        }
        return tempFileManager.writeFile(bitmap2bytes(bitmap), fileKey);
    }

    /**
     * 根据filekey删除image
     *
     * @param fileKey 文件key
     */
    public void deleteImage(String fileKey) {
        if (tempFileManager == null) {
            Log.e(TAG, "The property 'tempFileManager' have not be setted.");
            return;
        }
        tempFileManager.deleteFile(fileKey);
    }

    /**
     * 解析bytes成为bitmap
     *
     * @param bytes byte数组
     * @return Bitmap
     */
    private Bitmap bytes2bitmap(byte[] bytes) {
        if (bytes == null || bytes.length == 0) {
            return null;
        }
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

    /**
     * 解析位图成为bity数组
     *
     * @param bitmap 位图
     * @return byte[]
     */
    private byte[] bitmap2bytes(Bitmap bitmap) {
        byte[] bitmapByte;
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        bitmapByte = stream.toByteArray();
        try {
            stream.flush();
            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmapByte;
    }

}
