/**
 * @Description: TODO
 * @author: {Zhou Haitao.  }
 * @version: 1.0
 * @see
 */

package net.iaf.framework.util;

import java.io.File;

/**
 * 文件处理的工具类
 *
 * @author heisenberg.gong
 */
public class FileUtils {

    /**
     * Deletes all files and subdirectories under "dir".
     *
     * @param dir Directory to be deleted
     * @return boolean Returns "true" if all deletions were successful.
     * <p>
     * If a deletion fails, the method stops attempting to
     * <p>
     * delete and returns "false".
     */
    public static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            if (children.length == 0) {
                return dir.delete();
            }
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        // The directory is now empty so now it can be smoked
        return dir.delete();

    }

    /**
     * 获取文件或文件夹的大小
     *
     * @param file 文件或文件夹
     * @return 单位byte
     */
    public static long getFileSize(File file) {
        long size = 0;
        File flist[] = file.listFiles();
        if (flist != null) {
            for (int i = 0; i < flist.length; i++) {
                if (flist[i].isDirectory()) {
                    size = size + getFileSize(flist[i]);
                } else {
                    size = size + flist[i].length();
                }
            }
        }
        return size;
    }

}
