package com.yulinoo.plat.life.views.activity;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import android.content.Intent;
import android.graphics.Bitmap;
import android.view.View;

import com.activeandroid.query.Delete;
import com.yulinoo.plat.life.bean.Thumbnail;
import com.yulinoo.plat.life.ui.widget.SThumbnail;
import com.yulinoo.plat.life.ui.widget.SThumbnail.IThumbnail;
import com.yulinoo.plat.life.utils.Constant;
import com.yulinoo.plat.life.utils.ImageThumbnail;

public abstract class ImageActivtity extends EditPhotoActivity {

	//private String tmpFilePath;

	private final int IMAGE_CAPTURE = 1001;

	private SThumbnail sThumbnail;

	private IThumbnail thumbnail;
	private HashSet<String> images = new HashSet<String>();
	protected Map<String, String> filePathMap = new HashMap<String, String>();
	public void setThumbnail(IThumbnail thumbnail) {
		this.thumbnail = thumbnail;
	}

	public void takePhoto(SThumbnail sThumbnail,View photoPosition) {
		//List<Thumbnail> thumbnails = new Select().from(Thumbnail.class).execute();
		if (sThumbnail.getChildCount() >= 3) {
			showToast("照片最多上传三张");
			return;
		}
		if (this.sThumbnail == null) {
			this.sThumbnail = sThumbnail;
			this.sThumbnail.setiThumbnail(new IThumbnail() {
				@Override
				public void onPreview(String compressUrl) {
					Intent intent = new Intent(ImageActivtity.this, PreviewPhotoActivity.class);
					intent.putExtra("intent_key_file_path", compressUrl);
					startActivity(intent);
				}

				@Override
				public void onDelete(String compressUrl, String rawUrl) {
					new Delete().from(Thumbnail.class).where("compressPath=?", compressUrl).execute();
					try {
						File compressFile = new File(compressUrl);
						compressFile.delete();
						images.remove(compressUrl);
					} catch (Exception e) {
					}

					try {
						File rawFile = new File(rawUrl);
						rawFile.delete();
					} catch (Exception e) {
					}

					if(thumbnail != null) {
						thumbnail.onDelete(compressUrl, rawUrl);
					}
				}

				@Override
				public void onAdd(String compressUrl, String rawUrl) {
					Thumbnail thumbnail = new Thumbnail();
					thumbnail.setCompressPath(compressUrl);
					thumbnail.setRawPath(rawUrl);
					thumbnail.save();
					if(ImageActivtity.this.thumbnail != null) {
						(ImageActivtity.this).thumbnail.onAdd(compressUrl, rawUrl);
					}
				}
			});
		}
		
		
		editPhoto(photoPosition, -1, -1,null);
		
		
		
		
		
//		//		File IconPath = new File(ConfigCache.getTakePhotoPath());
//		//		if (!IconPath.exists()) {
//		//			IconPath.mkdirs();
//		//		}
//		//tmpFilePath = new StringBuffer(ConfigCache.getTakePhotoPath()).append(System.currentTimeMillis()).append("_picture.jpg").toString();
//		File icon_file = StorageUtils.getOwnCacheDirectory(getApplicationContext(), "500me/Cache"); 
//		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//		intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(icon_file));
//		startActivityForResult(intent, IMAGE_CAPTURE);
//		
//		
//		
//		if (this.sThumbnail == null) {
//			this.sThumbnail = sThumbnail;
//			this.sThumbnail.setiThumbnail(new IThumbnail() {
//
//				@Override
//				public void onPreview(String compressUrl) {
//					Intent intent = new Intent(ImageActivtity.this, PreviewPhotoActivity.class);
//					intent.putExtra("intent_key_file_path", compressUrl);
//					startActivity(intent);
//				}
//
//				@Override
//				public void onDelete(String compressUrl, String rawUrl) {
//					new Delete().from(Thumbnail.class).where("compressPath=?", compressUrl).execute();
//					try {
//						File compressFile = new File(compressUrl);
//						compressFile.delete();
//						images.remove(compressUrl);
//					} catch (Exception e) {
//					}
//
//					try {
//						File rawFile = new File(rawUrl);
//						rawFile.delete();
//					} catch (Exception e) {
//					}
//
//					if(thumbnail != null) {
//						thumbnail.onDelete(compressUrl, rawUrl);
//					}
//				}
//
//				@Override
//				public void onAdd(String compressUrl, String rawUrl) {
//					Thumbnail thumbnail = new Thumbnail();
//					thumbnail.setCompressPath(compressUrl);
//					thumbnail.setRawPath(rawUrl);
//					thumbnail.save();
//					if(ImageActivtity.this.thumbnail != null) {
//						(ImageActivtity.this).thumbnail.onAdd(compressUrl, rawUrl);
//					}
//				}
//			});
//		}
	}


//	private void initsThumbnail()
//	{
//		this.sThumbnail = (SThumbnail) findViewById(R.id.sThumbnail);
//		if(sThumbnail==null)
//		{
//			return;
//		}
//		this.sThumbnail.setiThumbnail(new IThumbnail() {
//
//			@Override
//			public void onPreview(String compressUrl) {
//				Intent intent = new Intent(ImageActivtity.this, PreviewPhotoActivity.class);
//				intent.putExtra("intent_key_file_path", compressUrl);
//				startActivity(intent);
//			}
//
//			@Override
//			public void onDelete(String compressUrl, String rawUrl) {
//				new Delete().from(Thumbnail.class).where("compressPath=?", compressUrl).execute();
//				try {
//					File compressFile = new File(compressUrl);
//					compressFile.delete();
//					images.remove(compressUrl);
//				} catch (Exception e) {
//				}
//
//				try {
//					File rawFile = new File(rawUrl);
//					rawFile.delete();
//				} catch (Exception e) {
//				}
//
//				if(thumbnail != null) {
//					thumbnail.onDelete(compressUrl, rawUrl);
//				}
//			}
//
//			@Override
//			public void onAdd(String compressUrl, String rawUrl) {
//				Thumbnail thumbnail = new Thumbnail();
//				thumbnail.setCompressPath(compressUrl);
//				thumbnail.setRawPath(rawUrl);
//				thumbnail.save();
//				if(ImageActivtity.this.thumbnail != null) {
//					(ImageActivtity.this).thumbnail.onAdd(compressUrl, rawUrl);
//				}
//			}
//		});
//	}
//
//	private HashSet<String> images = new HashSet<String>();
//
//
//	@Override
//	protected void onRestoreInstanceState(Bundle savedInstanceState) {
//		super.onRestoreInstanceState(savedInstanceState);
//		tmpFilePath = savedInstanceState.getString("path");
//		images.addAll(savedInstanceState.getStringArrayList("images"));
//		if(sThumbnail==null)
//		{
//			initsThumbnail();
//		}
//		if(sThumbnail!=null)
//		{
//			for(String s:images)
//				sThumbnail.addImage(s, null);
//		}
//
//		Set<String> datas = savedInstanceState.keySet();
//		if(datas != null) {
//			for(String data : datas) {
//				try {
//					((EditText)findViewById(Integer.parseInt(data))).setText(savedInstanceState.getString(data));
//				} catch (Exception e) {
//
//				}
//			}
//		}
//
//
//		//		Log.e("csx", "restorer"+images+(sThumbnail!=null));
//	}
//
//	private HashMap<String, String> datas = new HashMap<String, String>();
//
//
//	protected void saveToBundle(int wigetId,String value){
//		datas.put("" + wigetId, value);
//	}
//
//	@Override
//	protected void onSaveInstanceState(Bundle outState) {
//		super.onSaveInstanceState(outState);
//		ArrayList<String> list = new ArrayList<String>();
//		list.addAll(images);
//		outState.putStringArrayList("images", list);
//
//		outState.putString("path",tmpFilePath );
//
//		Set<Entry<String, String>> entries = datas.entrySet();
//		for(Entry<String, String> entry : entries ){
//			outState.putString(entry.getKey(), entry.getValue());
//		}
//
//
//
//		//		Log.e("csx", "save"+images);
//	}
//
//	@Override
//	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//		super.onActivityResult(requestCode, resultCode, data);
//		if (IMAGE_CAPTURE == requestCode) {
//			try {
//
//				if (tmpFilePath != null && resultCode == RESULT_OK) {
//					// 压缩处理
//					String afterCompressUrl = ImageThumbnail.compressPictrue(tmpFilePath);
//					sThumbnail.addImage(afterCompressUrl, tmpFilePath);
//					images.add(afterCompressUrl);
//					//					Log.e("csx", "result"+images);
//
//				}
//			} catch (Exception e) {
//				//				Log.e("csx", "error"+e);
//			}
//		}
//	}
	
	@Override
	public void photoUploadDone(Bitmap newPhoto,String filePath,String netUrlPath) {
		sThumbnail.addImage(filePath, null);
		filePathMap.put(filePath, netUrlPath);
	}
}
