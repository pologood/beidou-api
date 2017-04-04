package com.baidu.beidou.api.external.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * 
 * ClassName: Zipper <br>
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
	 * 
	 * @param zipFile
	 *            压缩文件路径
	 * @param srcFile
	 *            源文件路径
	 * @throws IOException
	 *             压缩文件的过程中可能会抛出IO异常
	 */
	public void zipFile(String zipFile, String srcFile) throws IOException {
		File f = new File(srcFile);
		FileInputStream fis = new FileInputStream(f);
		BufferedInputStream bis = new BufferedInputStream(fis);
		byte[] buf = new byte[1024];
		int len;
		FileOutputStream fos = new FileOutputStream(zipFile);
		BufferedOutputStream bos = new BufferedOutputStream(fos);
		ZipOutputStream zos = new ZipOutputStream(bos);// 压缩包
		ZipEntry ze = new ZipEntry(f.getName());// 这是压缩包名里的文件名
		zos.putNextEntry(ze);// 写入新的ZIP文件条目并将流定位到条目数据的开始处

		while ((len = bis.read(buf)) != -1) {
			zos.write(buf, 0, len);
			zos.flush();
		}
		bis.close();
		zos.close();
	}

	/*
	 * inputFileName 输入一个文件夹 zipFileName 输出一个压缩文件夹
	 */
	public void zipDir(String zipFile, String srcFile) throws Exception {
		zip(zipFile, new File(srcFile));
	}

	private void zip(String zipFileName, File inputFile) throws Exception {
		ZipOutputStream out = new ZipOutputStream(new FileOutputStream(
				zipFileName));
		zip(out, inputFile, "");
		//System.out.println("zip done");
		out.close();
	}

	private void zip(ZipOutputStream out, File f, String base) throws Exception {
		if (f.isDirectory()) {
			File[] fl = f.listFiles();
			out.putNextEntry(new ZipEntry(base + "/"));
			base = base.length() == 0 ? "" : base + "/";
			for (int i = 0; i < fl.length; i++) {
				zip(out, fl[i], base + fl[i].getName());
			}
		} else {
			out.putNextEntry(new ZipEntry(base));
			FileInputStream in = new FileInputStream(f);
			int b;
			//System.out.println(base);
			while ((b = in.read()) != -1) {
				out.write(b);
			}
			in.close();
		}
	}

}
