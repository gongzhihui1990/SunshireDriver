package net.iaf.framework.util.tmpfile;

import android.content.Context;
import android.util.Base64;

import com.google.gson.Gson;

import net.iaf.framework.app.BaseApplication;
import net.iaf.framework.util.EmptyUtil;
import net.iaf.framework.util.EncryptUtil;
import net.iaf.framework.util.FileUtils;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;

/**
 * 文件存储在'.smartPos/'目录下
 *
 * @author Heisenberg
 */
public class PersistenceFileManagerDataFilesImpl implements FileManager {

    private static boolean secureFlag;
    private static PersistenceFileManagerDataFilesImpl mTempFileManager;

    private static String key = "19900910";
    private static Gson gson = new Gson();
    private String packageName;

    private PersistenceFileManagerDataFilesImpl() {
    }

    /**
     * @param secure : true readFile/writeFile use EncryptUtil
     * @return
     */
    public static PersistenceFileManagerDataFilesImpl getInstance(boolean secure) {
        secureFlag = secure;
        if (mTempFileManager == null) {
            PersistenceFileManagerDataFilesImpl fileManager = new PersistenceFileManagerDataFilesImpl();
            mTempFileManager = fileManager;
        }
        return mTempFileManager;
    }

    /**
     * 实例化
     *
     * @param context 上下文引用
     * @return TempFileManager
     */
    public static PersistenceFileManagerDataFilesImpl getInstance() {
        return getInstance(false);
    }

    @Override
    public byte[] readFile(String fileKey) {
        FileInputStream inStream = null;
        File file = getFile(fileKey);
        if (file == null || file.length() == 0) {
            return null;
        }
        if (secureFlag) {
            byte[] fileByte = null;
            ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
            try {
                inStream = new FileInputStream(file);
                EncryptUtil.decodeStream(inStream, swapStream, key);
                fileByte = swapStream.toByteArray();
                return fileByte;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (inStream != null) {
                    try {
                        inStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return null;
        } else {
            byte[] fileByte = null;
            try {
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
    }

    @Override
    public boolean writeFile(byte[] fileByte, String fileKey) {
        if (secureFlag) {
            FileOutputStream outStream = null;
            try {
                outStream = new FileOutputStream(getFile(fileKey));
                EncryptUtil.encodeStream(new ByteArrayInputStream(fileByte),
                        outStream, key);
                return true;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (outStream != null) {
                    try {
                        outStream.close();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            }
            return false;
        } else {
            FileOutputStream outStream = null;
            try {
                outStream = new FileOutputStream(getFile(fileKey));
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
    }

    public boolean writeObject(Serializable object, Class<?> fileKey) {
        try {
            byte[] fileByte = Base64.encode(gson.toJson(object).getBytes("UTF-8"), Base64.DEFAULT);
            return writeFile(fileByte, fileKey.getSimpleName());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Serializable readObject(Class<?> fileKey) {
        byte[] byteRead = readFile(fileKey.getSimpleName());
        try {
            if (byteRead == null) {
                return null;
            }
            String objStr = new String(Base64.decode(byteRead, Base64.DEFAULT), "UTF-8");
            Serializable object = (Serializable) gson.fromJson(objStr, fileKey);
            return object;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean writeFile(InputStream inputStream, String fileKey) {
        BufferedInputStream bufInputStream = null;
        FileOutputStream outputStream = null;
        try {
            bufInputStream = new BufferedInputStream(inputStream);
            outputStream = new FileOutputStream(getFile(fileKey));
            byte[] buffer = new byte[1024];
            int readIndex;
            while (-1 != (readIndex = bufInputStream.read(buffer, 0,
                    buffer.length))) {
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

    /**
     * 获取内置SD卡路径
     *
     * @return
     */
    public String getRootPath(Context context) {
        if (context == null) {
            context = BaseApplication.getContext();
        }
        if (EmptyUtil.isEmpty(packageName)) {
            packageName = context.getPackageName();
        }
//		boolean sdExist = android.os.Environment.MEDIA_MOUNTED.equals(android.os.Environment.getExternalStorageState());
        File dataFile = context.getFilesDir();
//		if (sdExist) {
//			//存在sd卡，则使用sd卡目录（好处是卸载应用仍然能保存数据）
//			dataFile = new File(Environment.getExternalStorageDirectory()
//					.getPath() + "/"+packageName);
//		}else {
//			//不存在sd卡，则使用应用数据目录
//			  dataFile = context.getFilesDir();
//		}
        if (dataFile != null && !dataFile.exists()) {
            dataFile.mkdir();
        }
        return dataFile.getPath();
    }


    /**
     * 获取隐藏文件夹内的文件
     *
     * @param fileKey
     * @return
     */
    public File getFile(String fileKey) {
        String filenaame = getRootPath(null) + "/" + fileKey;
        File file = new File(filenaame);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }

    /* (non-Javadoc)
     * @see net.iaf.framework.util.tmpfile.FileManager#deleteFile(java.lang.String)
     * if fileKey is null delete RootFile
     */
    @Override
    public void deleteFile(String fileKey) {
        File filey = null;
        if (fileKey == null) {
            //filey = new File(getRootPath());
        } else {
            filey = new File(getRootPath(null) + "/" + fileKey);
        }
        if (filey != null && filey.exists()) {
            FileUtils.deleteDir(filey);
        }
    }


}
