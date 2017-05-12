package net.iaf.framework.util.tmpfile;

import java.io.InputStream;

/**
 * 临时文件管理接口
 *
 * @author zgg
 */
public interface FileManager {
    /**
     * 读取文件
     *
     * @param fileKey 文件key
     * @return byte[]
     */
    public abstract byte[] readFile(String fileKey);

    /**
     * 写文件
     *
     * @param fileByte 文件的字节数组
     * @param fileKey  文件key
     * @return
     */
    public abstract boolean writeFile(byte[] fileByte, String fileKey);

    public abstract boolean writeFile(InputStream inputStream, String fileKey);

    /**
     * 删除指定的文件
     *
     * @param fileKey 文件key
     */
    public abstract void deleteFile(String fileKey);
}
