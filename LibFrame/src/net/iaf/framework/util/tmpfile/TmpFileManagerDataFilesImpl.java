package net.iaf.framework.util.tmpfile;

import android.content.Context;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 文件存储在'./data/data/com.xxx/files'目录下
 *
 * @author Bob
 */
public class TmpFileManagerDataFilesImpl implements FileManager {

    private static FileManager mTempFileManager;
    private Context context;

    private TmpFileManagerDataFilesImpl() {
    }

    /**
     * 实例化
     *
     * @param context 上下文引用
     * @return TempFileManager
     */
    public static FileManager getInstance(Context context) {
        if (mTempFileManager == null) {
            TmpFileManagerDataFilesImpl tempFileManager = new TmpFileManagerDataFilesImpl();
            tempFileManager.setContext(context);
            mTempFileManager = tempFileManager;
        }
        return mTempFileManager;
    }

    private void setContext(Context context) {
        this.context = context;
    }

    @Override
    public byte[] readFile(String fileKey) {
        FileInputStream inStream = null;
        byte[] fileByte = null;
        try {
            inStream = this.context.openFileInput(fileKey);
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
        try {
            outStream = this.context.openFileOutput(fileKey,
                    Context.MODE_PRIVATE);
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
        try {
            bufInputStream = new BufferedInputStream(inputStream);
            outputStream = this.context.openFileOutput(fileKey, Context.MODE_PRIVATE);

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
        this.context.deleteFile(fileKey);
        // File file = null;
        // file = new File(this.context.getFilesDir(), fileKey);
        // if(file.exists()){
        // file.delete();
        // }
    }

}
