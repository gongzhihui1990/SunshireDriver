package net.iaf.framework.util;

import android.content.Context;

import net.iaf.framework.util.tmpfile.FileManager;
import net.iaf.framework.util.tmpfile.TmpFileManagerDataFilesImpl;

/**
 * 对象仓库，文件方式持久化对象
 *
 * @author Bob
 */
public class ObjectBank {
    private static ObjectBank objectBank;

    private FileManager fileManager;

    private ObjectBank(Context context) {
        this.fileManager = TmpFileManagerDataFilesImpl.getInstance(context);
    }

    public static ObjectBank getInstance(Context context) {
        if (objectBank == null) {
            objectBank = new ObjectBank(context);
        }
        return objectBank;
    }

    public boolean saveObject(Object obj, String objKey) {
        byte[] fileByte;
        try {
            fileByte = ObjectConverter.ObjectToByte(obj);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return this.fileManager.writeFile(fileByte, objKey);
    }

    public Object getObject(String objKey) {
        Object obj = null;
        byte[] fileByte = this.fileManager.readFile(objKey);
        try {
            obj = ObjectConverter.ByteToObject(fileByte);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return obj;
    }
}
