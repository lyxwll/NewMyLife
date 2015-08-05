package com.yulinoo.plat.life.views.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.yulinoo.plat.life.ui.widget.BackWidget;
import com.yulinoo.plat.life.ui.widget.preview.BitmapPreview;
import com.yulinoo.plat.life.utils.Constant;
import com.yulinoo.plat.melife.R;

public class PreviewPhotoActivity extends BaseActivity {

	@Override
	protected void initView(Bundle savedInstanceState) {

	}

	@Override
	protected void initComponent() {

	}

	@Override
	protected void initHead(BackWidget back_btn, TextView rightImg,View RightImaView , TextView rightText, TextView title, View titleLayout) {

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.photo_view);
		Intent intent = this.getIntent();
		final BitmapPreview bp = (BitmapPreview) findViewById(R.id.pictureview);
		bp.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		String url = intent.getStringExtra(Constant.IntentKey.IMAGE_FIEL_URL);

		if (url != null) {
			//MeImageLoader.displayImage(uri, imageView, options);
//			ProgressUtil.showProgress(mContext, "正在加载");
//			DownloadPictureService downloadPictureService = new DownloadPictureService(new IPictureDownloadCallback() {
//				@Override
//				public void onSuccess(final String fileName, final Integer id) {
//
//					PreviewPhotoActivity.this.runOnUiThread(new Runnable() {
//
//						@Override
//						public void run() {
//							ProgressUtil.dissmissProgress();
//							bp.loadBitmap(fileName);
//						}
//					});
//				}
//
//				@Override
//				public void onError(Integer userid) {
//
//					PreviewPhotoActivity.this.runOnUiThread(new Runnable() {
//
//						@Override
//						public void run() {
//							ProgressUtil.dissmissProgress();
//							Toast.makeText(PreviewPhotoActivity.this, "加载图片异常！", Toast.LENGTH_SHORT).show();
//							finish();
//						}
//					});
//				}
//			});
//			downloadPictureService.downloadPicByDetailPage(url);
		} else {
			String path = intent.getStringExtra(Constant.IntentKey.IMAGE_FILE_PATH);
			bp.loadBitmap(path);
		}
	}

}
