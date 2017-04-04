package com.baidu.beidou.api.external.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.GZIPOutputStream;

import com.ice.tar.TarEntry;
import com.ice.tar.TarOutputStream;

/**
 * 
 * ClassName: Zipper <br>
 * Function: 将文件压缩成gzip
 * 
 * @author zhangxu
 * @version 2.0.0
 * @since cpweb357
 * @date Jan 6, 2012
 */
public class GZipper {

	/**
	 * 递归地压缩文件夹
	 * 
	 * @param targetGZIPFilePath
	 *            tar.gz压缩文件路径
	 * @param targetTmpFilePath
	 * 			  tar压缩文件路径
	 * @param folderPath
	 *            源文件夹路径
	 * @throws IOException
	 *             压缩文件的过程中可能会抛出IO异常
	 */
	public void gzipDir(String targetGZIPFilePath, String targetTmpFilePath, String folderPath) throws IOException{
		File srcPath = new File(folderPath);
		int length = srcPath.listFiles().length;
		byte[] buf = new byte[1024]; // 设定读入缓冲区尺寸
		File[] files = srcPath.listFiles();
		try {
			// 建立压缩文件输出流
			FileOutputStream fout = new FileOutputStream(targetTmpFilePath);
			// 建立tar压缩输出流
			TarOutputStream tout = new TarOutputStream(fout);
			for (int i = 0; i < length; i++) {
				String filename = srcPath.getPath() + File.separator
						+ files[i].getName();
				// 打开需压缩文件作为文件输入流
				FileInputStream fin = new FileInputStream(filename); // filename是文件全路径
				TarEntry tarEn = new TarEntry(files[i]); // 此处必须使用new
															// TarEntry(File
															// file);
				tarEn.setName(files[i].getName());
				// //此处需重置名称，默认是带全路径的，否则打包后会带全路径
				tout.putNextEntry(tarEn);
				int num;
				while ((num = fin.read(buf)) != -1) {
					tout.write(buf, 0, num);
				}
				tout.closeEntry();
				fin.close();
			}

			tout.close();
			fout.close();

			// 建立压缩文件输出流
			FileOutputStream gzFile = new FileOutputStream(targetGZIPFilePath);
			// 建立gzip压缩输出流
			GZIPOutputStream gzout = new GZIPOutputStream(gzFile);
			// 打开需压缩文件作为文件输入流
			FileInputStream tarin = new FileInputStream(targetTmpFilePath); // targzipFilePath是文件全路径
			int len;
			while ((len = tarin.read(buf)) != -1) {
				gzout.write(buf, 0, len);
			}
			gzout.close();
			gzFile.close();
			tarin.close();
		} catch (IOException e) {
			throw e;
		}
	}

	// 循环遍历目录结构中的文件并添加至tar的输出流
	public void addFiles(TarOutputStream tout, String folderPath) {
		File srcPath = new File(folderPath);
		int length = srcPath.listFiles().length;
		byte[] buf = new byte[1024]; // 设定读入缓冲区尺寸
		File[] files = srcPath.listFiles();
		try {
			for (int i = 0; i < length; i++) {
				if (files[i].isFile()) {
					//System.out.println("file:" + files[i].getName());
					String filename = srcPath.getPath() + File.separator
							+ files[i].getName();
					// 打开需压缩文件作为文件输入流
					FileInputStream fin = new FileInputStream(filename); // filename是文件全路径
					TarEntry tarEn = new TarEntry(files[i]); // 此处必须使用new
																// TarEntry(File
																// file);
					// tarEn.setName(files[i].getName());
					// //此处需重置名称，默认是带全路径的，否则打包后会带全路径
					tout.putNextEntry(tarEn);
					int num;
					while ((num = fin.read(buf)) != -1) {
						tout.write(buf, 0, num);
					}
					tout.closeEntry();
					fin.close();
				} else {
					System.out.println(files[i].getPath());
					addFiles(tout, files[i].getPath());
				}
			}
		} catch (FileNotFoundException e) {
			System.out.println(e);
		} catch (IOException e) {
			System.out.println(e);
		}

	}

	/**
	 * 递归地压缩文件夹
	 * 
	 * @param targzipFilePath
	 *            压缩文件路径
	 * @param suffix
	 * 			  压缩文后缀
	 * @param folderPath
	 *            源文件夹路径
	 * @throws IOException
	 *             压缩文件的过程中可能会抛出IO异常
	 */
	public void gzipRecursiveDir(String targzipFilePath, String suffix, String folderPath) {
		byte[] buf = new byte[102]; // 设定读入缓冲区尺寸
		try {
			// 建立压缩文件输出流
			FileOutputStream fout = new FileOutputStream(targzipFilePath);
			// 建立tar压缩输出流
			TarOutputStream tout = new TarOutputStream(fout);
			addFiles(tout, folderPath);
			tout.close();
			fout.close();

			// 建立压缩文件输出流
			FileOutputStream gzFile = new FileOutputStream(targzipFilePath + suffix);
			// 建立gzip压缩输出流
			GZIPOutputStream gzout = new GZIPOutputStream(gzFile);
			// 打开需压缩文件作为文件输入流
			FileInputStream tarin = new FileInputStream(targzipFilePath); // targzipFilePath是文件全路径
			int len;
			while ((len = tarin.read(buf)) != -1) {
				gzout.write(buf, 0, len);
			}
			gzout.close();
			gzFile.close();
			tarin.close();

		} catch (FileNotFoundException e) {
			System.out.println(e);
		} catch (IOException e) {
			System.out.println(e);
		}

		File tarfile = new File(targzipFilePath);
		tarfile.delete();
	}

	public static void main(String[] args) {

		// 方法一：对于目录中只含文件的文件夹打包并压缩
		// CompressedFiles_Gzip("E:\\list","E:\\list.tar");
		// 方法二：对目录中既含有文件又含有递归目录的文件夹打包
		new GZipper().gzipRecursiveDir("E:\\acctfile.tar", ".gz", "E:\\acctfile");
	}
}
