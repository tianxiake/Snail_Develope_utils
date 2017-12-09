package com.snail.java;

import java.io.File;
import java.io.FileOutputStream;

/**
 * @author yongjie on 2017/12/9.
 */

public class FileIOUtils {

    /**
     * 向指定文件路径写数据
     *
     * @param bytes   数据内容
     * @param desFile 目录文件路径
     * @param append  文件存在是否追加写入
     * @return true写入成功，否则返回false
     */
    public static boolean writeBytesToFile(byte[] bytes, File desFile, boolean append) {
        FileOutputStream outputStream = null;
        try {
            if (desFile != null) {
                desFile.getParentFile().mkdir();
                outputStream = new FileOutputStream(desFile, append);
                outputStream.write(bytes);
                outputStream.flush();
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            CloseUtils.closeIO(outputStream);
        }
        return false;
    }
}
