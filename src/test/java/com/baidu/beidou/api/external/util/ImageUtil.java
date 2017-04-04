package com.baidu.beidou.api.external.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import sun.misc.BASE64Encoder;

@SuppressWarnings("restriction")
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
			System.out.println(e.getMessage());
		}
		return data;
	}
	
	public static void main(String[] args) {
		System.out.println(GetImageStrInBase64("e:\\250_250.jpg"));
	}

}
