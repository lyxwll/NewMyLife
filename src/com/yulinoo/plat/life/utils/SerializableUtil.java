package com.yulinoo.plat.life.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import config.ConfigCache;

public class SerializableUtil {
	// 将对象保存到sdcard
	public static void writeObject(Serializable serializable) {
		try {

			// if (employee != null) {
			String fileDir = ConfigCache.getObjectPath();
			File file = new File(fileDir);
			if (!file.isDirectory() || !file.exists()) {
				file.mkdirs();
			}

			try {
				File tmpFile = new File(fileDir + serializable.getClass().getName());
				tmpFile.delete();
			} catch (Exception e) {
			}

			try {
			ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(fileDir + serializable.getClass().getName()));
			out.writeObject(serializable);
			out.close();
			} catch (Exception e) {
			}
			// }
		} catch (Exception e) {
			System.out.println("---------------->" + e.getMessage());
			// TODO: handle exception
		}
	}

	// 读取sdcard上的对象
	public static Serializable readObject(String className) {
		try {
			String fileDir = ConfigCache.getObjectPath();
			File file = new File(fileDir);
			if (!file.isDirectory() || !file.exists()) {
				file.mkdirs();
			}

			ObjectInputStream in = new ObjectInputStream(new FileInputStream(fileDir + className));
			Serializable serializable = (Serializable) in.readObject();
			in.close();
			return serializable;
		} catch (Exception e) {
		}
		return null;
	}

	
}
