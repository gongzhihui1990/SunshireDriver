package net.iaf.framework.util;

import net.iaf.framework.util.tmpfile.FileManager;
import net.iaf.framework.util.tmpfile.PersistenceFileManagerDataFilesImpl;

/**
 * 对象仓库，文件方式持久化对象
 *
 * @author Bob
 */
public class ObjectPersistenceBank {
    private static ObjectPersistenceBank objectBank;

    private FileManager fileManager;

    private ObjectPersistenceBank(boolean safeMode) {
        this.fileManager = PersistenceFileManagerDataFilesImpl.getInstance(safeMode);
    }

    public static ObjectPersistenceBank getInstance(boolean safeMode) {
        if (objectBank == null) {
            objectBank = new ObjectPersistenceBank(safeMode);
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

    public void deleteObject(String objKey) {
        this.fileManager.deleteFile(objKey);
    }

    public Object getObject(String objKey) {
        Object obj = null;
        byte[] fileByte = this.fileManager.readFile(objKey);
        if (fileByte == null) {
            return null;
        }
        try {
            obj = ObjectConverter.ByteToObject(fileByte);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return obj;
    }
}
