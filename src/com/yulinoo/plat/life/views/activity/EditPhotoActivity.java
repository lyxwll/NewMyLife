package com.yulinoo.plat.life.views.activity;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.ImageColumns;
import android.view.View;

import com.nostra13.universalimageloader.utils.StorageUtils;
import com.yulinoo.plat.life.net.callback.UICallback;
import com.yulinoo.plat.life.net.callback.UIUploadCallback;
import com.yulinoo.plat.life.net.reqbean.AvartaModifyReq;
import com.yulinoo.plat.life.net.service.RequestBean;
import com.yulinoo.plat.life.net.service.UploadBean;
import com.yulinoo.plat.life.ui.widget.ActionSheet;
import com.yulinoo.plat.life.ui.widget.ConfirmCloseDialog;
import com.yulinoo.plat.life.utils.Constant;
import com.yulinoo.plat.life.utils.ImageThumbnail;
import com.yulinoo.plat.life.utils.NullUtil;
import com.yulinoo.plat.life.utils.ProgressUtil;
import com.yulinoo.plat.life.utils.Constant.HTTP_DATA_FORMAT;
import com.yulinoo.plat.life.utils.Constant.HTTP_METHOD;
import com.yulinoo.plat.melife.R;

import config.AppContext;

//带有拍照和本地相册选取的基类
public abstract class EditPhotoActivity extends BaseActivity {
	private String filePath = null;// Environment.getExternalStorageDirectory()+
									// "/temp.jpg";
	final CharSequence[] items = { "手机相册", "相机拍摄" };
	public static final String IMAGE_UNSPECIFIED = "image/*";
	public static final int NONE = 0;
	public static final int PHOTOHRAPH = 1;// 拍照
	public static final int PHOTOZOOM = 2; // 缩放
	public static final int PHOTORESOULT = 3;// 结果

	private int photowidth = 0;
	private int photoheight = 0;
	private String saveUrl;// 图像的保存路径

	// 编辑头像，参数为弹出的选择对话框的显示位置
	public void editPhoto(View positionView, int width, int height, String sp) {
		photowidth = width;
		photoheight = height;
		saveUrl = sp;
		List<ActionSheet.ActionSheetParameter> cctionSheetParameters = new ArrayList<ActionSheet.ActionSheetParameter>();
		ActionSheet actionSheet = new ActionSheet(this,
				new ActionSheet.ReturnResult() {
					@Override
					public void onResult(String key, String value) {
						filePath = StorageUtils.getCacheDirectory(mContext)
								.getAbsolutePath();//
						if (!filePath.endsWith(File.separator)) {
							filePath = filePath + File.separator + "thumb"
									+ File.separator;
						}
						File folders = new File(filePath);
						folders.mkdirs();
						filePath = filePath + "temp.jpg";
						if (key.equals("1")) {// 媒体库
							Intent intent = new Intent(Intent.ACTION_PICK, null);
							intent.setDataAndType(
									MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
									IMAGE_UNSPECIFIED);
							startActivityForResult(intent, PHOTOZOOM);
						} else {// 拍照
							Intent intent2 = new Intent(
									MediaStore.ACTION_IMAGE_CAPTURE);
							intent2.putExtra(MediaStore.EXTRA_OUTPUT,
									Uri.fromFile(new File(filePath)));
							startActivityForResult(intent2, PHOTOHRAPH);
						}
					}
				});
		ActionSheet.ActionSheetParameter actionSheetParameter = actionSheet.new ActionSheetParameter();
		actionSheetParameter
				.setDrawableId(R.drawable.btn_style_alert_dialog_button_normal);
		actionSheetParameter.setKey("1");
		actionSheetParameter.setValue("手机相册");
		cctionSheetParameters.add(actionSheetParameter);

		ActionSheet.ActionSheetParameter actionSheetParameter2 = actionSheet.new ActionSheetParameter();
		actionSheetParameter2
				.setDrawableId(R.drawable.btn_style_alert_dialog_button_normal);
		actionSheetParameter2.setKey("2");
		actionSheetParameter2.setValue("相机拍摄");
		cctionSheetParameters.add(actionSheetParameter2);

		actionSheet.putData(cctionSheetParameters, positionView);

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == NONE)
			return;
		// 拍照
		if (requestCode == PHOTOHRAPH) {
			if (photowidth > 10) {// 说明要裁剪
				File picture = new File(filePath);
				startPhotoZoom(Uri.fromFile(picture));
			} else {// 不需要裁剪
				setPicToView(data);
			}
		}

		if (data == null)
			return;

		// 读取相册缩放图片
		if (requestCode == PHOTOZOOM) {
			if (photowidth > 10) {
				startPhotoZoom(data.getData());
			} else {
				Uri uri = data.getData();
				filePath = getRealFilePath(this, uri);
				ContentResolver cr = this.getContentResolver();
				try {
					newPhoto = BitmapFactory.decodeStream(cr
							.openInputStream(uri));
					setPic();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
			}
		}
		// 处理结果
		if (requestCode == PHOTORESOULT) {
			setPicToView(data);
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	public void startPhotoZoom(Uri uri) {
		Intent intentCarema = new Intent("com.android.camera.action.CROP");
		intentCarema.setDataAndType(uri, "image/*");
		intentCarema.putExtra("crop", true);
		intentCarema.putExtra("scale", false);
		intentCarema.putExtra("scaleUpIfNeeded", true);// 去黑边
		// intentCarema.putExtra("scale", false);
		// intentCarema.putExtra("noFaceDetection", true);//不需要人脸识别功能
		// intentCarema.putExtra("circleCrop", "");//设定此方法选定区域会是圆形区域
		// aspectX aspectY是宽高比例
		intentCarema.putExtra("aspectX", photowidth);
		intentCarema.putExtra("aspectY", photoheight);
		// outputX outputY 是裁剪图片的宽高
		intentCarema.putExtra("outputX", photowidth);
		intentCarema.putExtra("outputY", photoheight);
		// intentCarema.putExtra("return-data", true);
		intentCarema.putExtra("outputFormat",
				Bitmap.CompressFormat.JPEG.toString());
		intentCarema.putExtra("noFaceDetection", true);
		// 加上下面的这两句之后，系统就会把图片给我们拉伸了，哇哈哈，愁死我例差点
		intentCarema.putExtra("scale", true);
		intentCarema.putExtra("scaleUpIfNeeded", true);
		File temp_picture = new File(filePath);
		intentCarema.putExtra(MediaStore.EXTRA_OUTPUT,
				Uri.fromFile(temp_picture));
		intentCarema.putExtra("return-data", false);// 设置为不返回数据
		startActivityForResult(intentCarema, PHOTORESOULT);
	}

	private Bitmap newPhoto;
	private ConfirmCloseDialog confirmCloseDialog;

	/**
	 * 保存裁剪之后的图片数据
	 * 
	 * @param picdata
	 */
	private void setPicToView(Intent picdata) {
		if (picdata != null) {
			Bundle extras = picdata.getExtras();
			if (extras != null) {
				newPhoto = extras.getParcelable("data");
				if (newPhoto == null) {
					newPhoto = BitmapFactory.decodeFile(filePath);
				}
			} else {
				newPhoto = BitmapFactory.decodeFile(filePath);
			}
		} else {
			newPhoto = BitmapFactory.decodeFile(filePath);
		}
		try {
			File myCaptureFile = new File(filePath);
			BufferedOutputStream bos = new BufferedOutputStream(
					new FileOutputStream(myCaptureFile));
			newPhoto.compress(Bitmap.CompressFormat.JPEG, 80, bos);
			bos.flush();
			bos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		setPic();
	}

	private void setPic() {
		upload();
		// confirmCloseDialog = new ConfirmCloseDialog(
		// this, "\n照片准备完成，你是否需要设置？\n", "设置图像", "确定", "取消",
		// new FinishCallback() {
		//
		// @Override
		// public void confirmThisOperation() {
		// confirmCloseDialog.dismiss();
		// upload();
		// }
		//
		// @Override
		// public void cancle() {
		// confirmCloseDialog.dismiss();
		// }
		// });
		// confirmCloseDialog.show();
	}

	// 上传图像
	private void upload() {
		final String afterCompressUrl = ImageThumbnail.compressPictrue(this,
				filePath);
		ProgressUtil.showProgress(this, "正在更新图像");
		UploadBean uploadBean = new UploadBean();
		uploadBean.setFilePath(afterCompressUrl);
		uploadBean.setURL(Constant.Requrl.getFileUpload());
		uploadToServer(uploadBean, new UIUploadCallback() {
			@Override
			public void onSuccess(final String urlPath) {
				if (NullUtil.isStrNotNull(saveUrl)) {
					AvartaModifyReq avartaModify = new AvartaModifyReq();
					avartaModify.setPictureUrl(urlPath);
					avartaModify.setMevalue(AppContext.currentAccount()
							.getMevalue());

					RequestBean<String> requestBean = new RequestBean<String>();
					requestBean.setHttpMethod(HTTP_METHOD.POST);
					requestBean.setRequestBody(avartaModify);
					requestBean.setRequsetFormat(HTTP_DATA_FORMAT.FORM);
					requestBean.setResponseBody(String.class);
					requestBean.setURL(saveUrl);
					requestServer(requestBean, new UICallback<String>() {
						@Override
						public void onSuccess(String respose) {
							try {
								ProgressUtil.dissmissProgress();
								photoUploadDone(newPhoto, afterCompressUrl,
										urlPath);
							} catch (Exception e) {
							}
						}

						@Override
						public void onError(String message) {
							ProgressUtil.dissmissProgress();
							showToast(message);
						}

						@Override
						public void onOffline(String message) {
							showToast(message);
						}
					});
				} else {
					ProgressUtil.dissmissProgress();
					photoUploadDone(newPhoto, afterCompressUrl, urlPath);
				}
			}

			@Override
			public void onProgress(String progress) {
				ProgressUtil.setProgress(progress);
			}

			@Override
			public void onOffLine() {
				ProgressUtil.dissmissProgress();
			}

			@Override
			public void onError(String message) {
				ProgressUtil.dissmissProgress();
				showToast("修改失败");
			}
		});
	}

	// 图像上传成功，有可能是用户照的，有可能是用户选的本地图片，都无所谓
	// 参数一，图片，参数二，本地进行了压缩后存储的路径，参数三，上传成功之后返回的服务器URL地址
	public abstract void photoUploadDone(Bitmap newPhoto,
			String afterCompressUrl, String netUrlPath);

	public static String getRealFilePath(final Context context, final Uri uri) {
		if (null == uri)
			return null;
		final String scheme = uri.getScheme();
		String data = null;
		if (scheme == null)
			data = uri.getPath();
		else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
			data = uri.getPath();
		} else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
			Cursor cursor = context.getContentResolver().query(uri,
					new String[] { ImageColumns.DATA }, null, null, null);
			if (null != cursor) {
				if (cursor.moveToFirst()) {
					int index = cursor.getColumnIndex(ImageColumns.DATA);
					if (index > -1) {
						data = cursor.getString(index);
					}
				}
				cursor.close();
			}
		}
		return data;
	}

}
