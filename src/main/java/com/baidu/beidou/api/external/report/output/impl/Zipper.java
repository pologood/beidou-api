package com.baidu.beidou.api.external.report.output.impl;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;
 
/**
 * 
 * ClassName: Zipper  <br>
 * Function: 将文件压缩成zip
 *
 * @author zhangxu
 * @version 2.0.0
 * @since cpweb357
 * @date Jan 6, 2012
 */
public class Zipper {
 
	/**
	 * 压缩文件
	 * @param zipFile 压缩文件路径
	 * @param srcFile 源文件路径
	 */
    public void zipFile(String zipFile, String srcFile) throws IOException {
        File f = new File(srcFile);
        FileInputStream fis = new FileInputStream(f);
        BufferedInputStream bis = new BufferedInputStream(fis);
        byte[] buf = new byte[1024];
        int len;
        FileOutputStream fos = new FileOutputStream(zipFile);
        BufferedOutputStream bos = new BufferedOutputStream(fos);
        ZipOutputStream zos = new ZipOutputStream(bos);//压缩包   
        ZipEntry ze = new ZipEntry(f.getName());//这是压缩包名里的文件名   
        zos.putNextEntry(ze);// 写入新的ZIP文件条目并将流定位到条目数据的开始处  

        while ((len = bis.read(buf)) != -1) {
            zos.write(buf, 0, len);
            zos.flush();
        }
        bis.close();
        zos.close();

    }
 
}
