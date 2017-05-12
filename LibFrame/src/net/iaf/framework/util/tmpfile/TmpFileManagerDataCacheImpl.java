package net.iaf.framework.util.tmpfile;

import android.content.Context;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 文件存储在'./data/data/com.xxx/cache'目录下 在系统资源紧张的时候会被系统自动释放，适合读写间隙比较短的场景
 *
 * @author Bob
 */
public class TmpFileManagerDataCacheImpl implements FileManager {

    private static FileManager mTempFileManager;
    private Context context;

    private TmpFileManagerDataCacheImpl() {
    }

    /**
     * 实例化
     *
     * @param context 上下文引用
     * @return TempFileManager
     */
    public static FileManager getInstance(Context context) {
        if (mTempFileManager == null) {
            TmpFileManagerDataCacheImpl tempFileManager = new TmpFileManagerDataCacheImpl();
            tempFileManager.setContext(context);
            mTempFileManager = tempFileManager;
        }
        return mTempFileManager;
    }

    /**
     * 设置上下午引用
     *
     * @param context
     */
    private void setContext(Context context) {
        this.context = context;
    }

    @Override
    public byte[] readFile(String fileKey) {
        FileInputStream inStream = null;
        byte[] fileByte = null;
        File file = null;
        try {
            file = new File(this.context.getCacheDir(), fileKey);
            inStream = new FileInputStream(file);
            fileByte = new byte[inStream.available()];
            inStream.read(fileByte);
            return fileByte;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            if (inStream != null) {
                try {
                    inStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public boolean writeFile(byte[] fileByte, String fileKey) {
        FileOutputStream outStream = null;
        File file = null;
        try {
            file = new File(this.context.getCacheDir(), fileKey);
            outStream = new FileOutputStream(file);
            outStream.write(fileByte);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            if (outStream != null) {
                try {
                    outStream.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    @Override
    public boolean writeFile(InputStream inputStream, String fileKey) {
        BufferedInputStream bufInputStream = null;
        FileOutputStream outputStream = null;
        File file = null;
        try {
            bufInputStream = new BufferedInputStream(inputStream);
            file = new File(this.context.getCacheDir(), fileKey);
            outputStream = new FileOutputStream(file);

            byte[] buffer = new byte[1024];
            int readIndex;
            while (-1 != (readIndex = bufInputStream.read(buffer, 0, buffer.length))) {
                outputStream.write(buffer, 0, readIndex);
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void deleteFile(String fileKey) {
        File file = null;
        file = new File(this.context.getCacheDir(), fileKey);
        if (file.exists()) {
            file.delete();
        }
    }
}
