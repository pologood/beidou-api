package com.baidu.api.client.core;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import sun.misc.BASE64Encoder;

/**
 * Get image file base64 or byte code
 * 
 * @author @author@ (@author-email@)
 * 
 * @version @version@, $Date: 2011-7-22$
 *
 */
public class ImageUtil {

	public static String GetImageStrInBase64(String imgFile) {
		InputStream in = null;
		byte[] data = null;
		try {
			in = new FileInputStream(imgFile);
			data = new byte[in.available()];
			in.read(data);
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		BASE64Encoder encoder = new BASE64Encoder();
		return encoder.encode(data);
	}
	
	public static byte[] GetImageByte(String imgFile) {
		InputStream in = null;
		byte[] data = null;
		try {
			in = new FileInputStream(imgFile);
			data = new byte[in.available()];
			in.read(data);
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return data;
	}

}
