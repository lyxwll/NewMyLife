package config;



public class ConfigCache {



	public static String cache_path;
	public static String take_photo_path;

	public static String object_serializable_path;




	public static String getObjectPath() {
		if (object_serializable_path == null) {
			object_serializable_path = "/sdcard/com.500me.life/obj/";
		}
		return object_serializable_path;
	}

	public static String getCachePath() {
		if (cache_path == null) {
			cache_path = "/sdcard/com.500me.life/download_img/";
		}
		return cache_path;
	}

	public static String getTakePhotoPath() {
		if (take_photo_path == null) {
			take_photo_path = "/sdcard/com.500me.life/photo/";
		}
		return take_photo_path;
	}

	
}
