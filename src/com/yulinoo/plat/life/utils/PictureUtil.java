package com.yulinoo.plat.life.utils;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.widget.ImageView;
import android.widget.Toast;

import com.yulinoo.plat.life.net.callback.IPictureDownloadCallback;
import com.yulinoo.plat.life.net.service.DownloadPictureService;

public class PictureUtil {
	public static Bitmap getRoundedCornerBitmap(Bitmap bitmap) { 
		Bitmap output = null; 
		if(bitmap.getWidth() <  bitmap.getHeight() ) {
			output = Bitmap.createBitmap(bitmap.getWidth(),bitmap.getWidth(), Config.ARGB_8888); 
		} else {
			output = Bitmap.createBitmap(bitmap.getHeight(), bitmap.getHeight(), Config.ARGB_8888); 
		}
		
		Canvas canvas = new Canvas(output); 
       
		final int color = 0xff424242; 
		final Paint paint = new Paint(); 
		 Rect rect = null; 
		if(bitmap.getWidth() > bitmap.getHeight() ) {
			rect = new Rect(0, 0,bitmap.getHeight(), bitmap.getHeight()); 
		} else {
			rect = new Rect(0, 0,bitmap.getWidth(), bitmap.getWidth()); 
		}
		
		final RectF rectF = new RectF(rect); 
		 float roundPx = 0;
		if( bitmap.getWidth() < bitmap.getHeight()) {
		  roundPx = bitmap.getWidth() / 2; 
		} else {
			roundPx =  bitmap.getHeight() / 2; 
		}
		paint.setAntiAlias(true); 
		canvas.drawARGB(0, 0, 0, 0); 
		paint.setColor(color); 
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint); 
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN)); 
		canvas.drawBitmap(bitmap, rect, rect, paint); 
		return output; 
	} 
//	public static void download(final Activity context,final String url, final ImageView bp,final boolean isCircle) {
//		if (url != null) {
//			DownloadPictureService downloadPictureService = new DownloadPictureService(new IPictureDownloadCallback() {
//				@Override
//				public void onSuccess(final String fileName, final Integer id) {
//					context.runOnUiThread(new Runnable() {
//						@Override
//						public void run() {
//							if(isCircle)
//							{
//								Bitmap bitmap = getRoundedCornerBitmap(BitmapFactory.decodeFile(fileName));
//								bp.setImageBitmap(bitmap);
//							}else
//							{
//								Bitmap bitmap = BitmapFactory.decodeFile(fileName);
//								bp.setImageBitmap(bitmap);
//							}
//						}
//					});
//				}
//
//				@Override
//				public void onError(Integer userid) {
//					context.runOnUiThread(new Runnable() {
//						@Override
//						public void run() {
//							Toast.makeText(context, "加载图片异常！", Toast.LENGTH_SHORT).show();
//						}
//					});
//				}
//			});
//			downloadPictureService.downloadPicByDetailPage(url);
//		}
//	}
}
