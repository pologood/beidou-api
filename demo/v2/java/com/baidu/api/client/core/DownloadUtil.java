/**
 * DownloadUtil.java
 *
 * Copyright @year@ @company@, Inc.
 *
 * @company@ licenses this file to you under the Apache License, version 2.0
 * (the "License"); you may not use this file except in compliance with the
 * License.  You may obtain a copy of the License at:
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package com.baidu.api.client.core;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import org.apache.commons.lang.ArrayUtils;

/**
 * Download file from remote server.
 * 
 * @author @author@ (@author-email@)
 * 
 * @version @version@, $Date: 2011-7-22$
 *
 */
public class DownloadUtil {

	private static final int BUFFER_SIZE = 1024 * 16;
	private static final int MATERIAL_SIZE = 1024 * 30;
	private static final int CONNECT_TIMEOUT = 6000;
	private static final int READ_TIMEOUT = 6000;
	private static final int MAX_SIZE = 1024 * 1024 * 50;
	private static final int MIN_SIZE = 5;

	/**
	 * 
	 * Download the file pointed by the url and return the content as byte array.
	 * 
	 * @param strUrl
	 *            The Url to be downloaded.
	 * @return The file content as byte array.
	 */
	public static byte[] downloadFile(String strUrl) {
		return downloadFile(strUrl, CONNECT_TIMEOUT, READ_TIMEOUT, MAX_SIZE);
	}

	/**
	 * Download the file pointed by the url and return the content as byte array.
	 * 
	 * @param strUrl
	 *            The Url to be downloaded.
	 * @param connectTimeout
	 *            Connect timeout in milliseconds.
	 * @param readTimeout
	 *            Read timeout in milliseconds.
	 * @param maxFileSize
	 *            Max file size in BYTE.
	 * @return The file content as byte array.
	 */
	public static byte[] downloadFile(String strUrl, int connectTimeout, int readTimeout, int maxFileSize) {
		InputStream in = null;
		try {
			URL url = new URL(strUrl); // 得到文件的URL地址
			URLConnection ucon = url.openConnection();
			ucon.setConnectTimeout(connectTimeout);
			ucon.setReadTimeout(readTimeout);
			ucon.connect();
			if (ucon.getContentLength() > maxFileSize) {
				String msg = "File " + strUrl + " size [" + ucon.getContentLength() + "] too large, download stoped.";
				throw new ClientInternalException(msg);
			}
			if (ucon instanceof HttpURLConnection) {
			    HttpURLConnection httpCon = (HttpURLConnection)ucon;
			    if (httpCon.getResponseCode() > 399) {
			        String msg = "Failed to download file " + strUrl + " server response " + httpCon.getResponseMessage();
			        throw new ClientInternalException(msg);
			    }
			}
			in = ucon.getInputStream(); // 得到文件输入流
			byte[] byteBuf = new byte[BUFFER_SIZE];
			byte[] ret = null;
			int count, total = 0;
			// 首先使用直接缓冲法，避免开更多的对象
			while ((count = in.read(byteBuf, total, BUFFER_SIZE - total)) > 0) {
			    total += count;
			    if (total + 124 >= BUFFER_SIZE)
			        break;
			}
			if (total < BUFFER_SIZE - 124) {
				ret = ArrayUtils.subarray(byteBuf, 0, total);
			} else {
				ByteArrayOutputStream bos = new ByteArrayOutputStream(MATERIAL_SIZE);
				count = total;
				total = 0;
				do {
					bos.write(byteBuf, 0, count);
					total += count;
					if (total > maxFileSize) {
						String msg = "File " + strUrl + " size exceed [" + maxFileSize + "] download stoped.";
						throw new ClientInternalException(msg);
					}
				} while ((count = in.read(byteBuf)) > 0);
				ret = bos.toByteArray();
			}
			if (ret.length < MIN_SIZE) {
			    String msg = "File " + strUrl + " size [" + maxFileSize + "] too small.";
			    throw new ClientInternalException(msg);
			}
			return ret;
		} catch (IOException e) {
			String msg = "Failed to download file " + strUrl + " msg=" + e.getMessage();
			throw new ClientInternalException(msg);
		} finally {
			try {
				if (in != null)
					in.close();
			} catch (IOException e) {
				// 关闭过程中的异常我们不关心
			    System.out.println("Exception while close url - " + e.getMessage());
			}
		}
	}
	
	private DownloadUtil() {
		// Utility类，禁止实例化
	}
}
