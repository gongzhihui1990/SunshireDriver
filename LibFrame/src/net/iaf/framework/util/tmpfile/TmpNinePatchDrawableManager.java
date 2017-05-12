package net.iaf.framework.util.tmpfile;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.NinePatchDrawable;
import android.util.Log;

import net.iaf.framework.util.NinePatchChunk;

import java.io.InputStream;

/**
 * 临时的图片管理容器
 *
 * @author zgg
 */
public class TmpNinePatchDrawableManager {

    private String TAG = "TempImageManagerAdapter";
    private FileManager tempFileManager;
    private Context context;

    public TmpNinePatchDrawableManager(Context context,
                                       FileManager tempFileManager) {
        this.tempFileManager = tempFileManager;
        this.context = context;
    }

    public TmpNinePatchDrawableManager(Context context) {
        // 默认存在应用的catch区
        this.tempFileManager = TmpFileManagerDataFilesImpl
                .getInstance(context);
        this.context = context;
    }

    /**
     * 根据文件key读取图片的bitmap
     *
     * @param fileKey
     * @return Bitmap
     */
    public NinePatchDrawable readImage(String fileKey) {
        if (tempFileManager == null) {
            Log.e(TAG, "The property 'tempFileManager' have not be setted.");
            return null;
        }
        return bytes2NinePatchDrawable(tempFileManager.readFile(fileKey));
    }

    public boolean containImage(String fileKey) {
        return tempFileManager.readFile(fileKey) != null;
    }

    /**
     * @param @param  inputStream
     * @param @param  fileKey
     * @param @return
     * @return boolean
     * @throws
     * @Title: writeImage
     * @Description: TODO
     */
    public boolean writeImage(InputStream inputStream, String fileKey) {
        if (tempFileManager == null) {
            Log.e(TAG, "The property 'tempFileManager' have not be setted.");
            return false;
        }
        return tempFileManager.writeFile(inputStream, fileKey);
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
    public NinePatchDrawable bytes2NinePatchDrawable(byte[] bytes) {
        if (bytes == null || bytes.length == 0) {
            return null;
        }
        NinePatchDrawable ninePatchDrawable = null;
        try {
            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            byte[] chunk = bitmap.getNinePatchChunk();
            final NinePatchChunk npc = NinePatchChunk.deserialize(chunk);
            ninePatchDrawable = new NinePatchDrawable(context.getResources(), bitmap, chunk, npc.mPaddings, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ninePatchDrawable;
    }

    public NinePatchDrawable inputStream2NinePatchDrawable(InputStream inputStream) {
        if (inputStream == null) {
            return null;
        }
        NinePatchDrawable ninePatchDrawable = null;
        try {
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            byte[] chunk = bitmap.getNinePatchChunk();
            final NinePatchChunk npc = NinePatchChunk.deserialize(chunk);
            ninePatchDrawable = new NinePatchDrawable(context.getResources(), bitmap, chunk, npc.mPaddings, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ninePatchDrawable;
    }
}
