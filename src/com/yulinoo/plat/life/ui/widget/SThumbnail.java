package com.yulinoo.plat.life.ui.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.activeandroid.query.Delete;
import com.yulinoo.plat.life.bean.Thumbnail;
import com.yulinoo.plat.melife.R;

public class SThumbnail extends LinearLayout {

	private Context context;
	
	private IThumbnail iThumbnail; 
	
	public void setiThumbnail(IThumbnail iThumbnail) {
		this.iThumbnail = iThumbnail;
	}

	public SThumbnail(Context context) {
		super(context);
		this.context = context;
		new Delete().from(Thumbnail.class).execute();
	}

	public SThumbnail(Context context, AttributeSet attributeSet) {
		super(context, attributeSet);
		this.context = context;
		new Delete().from(Thumbnail.class).execute();
	}

	/**
	 * 
	 * @param compressFilePath 压缩后图片的URL
	 * @param rawFilePath 压缩前图片的URL
	 */
	public void addImage(final String compressFilePath,final String rawFilePath) {
		addImage(compressFilePath, rawFilePath, true);
	}

	@SuppressWarnings("deprecation")
	public void addImage(final String compressFilePath,final String rawFilePath,boolean deleteable) {
		BitmapFactory.Options opts = new BitmapFactory.Options();
		opts.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(compressFilePath, opts);
		opts.inSampleSize = computeSampleSize(opts, -1, 150 * 150);
		opts.inJustDecodeBounds = false;
		try {
			Bitmap bmp = BitmapFactory.decodeFile(compressFilePath, opts);
			final View convertView = LayoutInflater.from(context).inflate(R.layout.xdz_img, null);
			View parentView = convertView.findViewById(R.id.parent_view);
			BitmapDrawable bd = new BitmapDrawable(bmp);
			parentView.setBackgroundDrawable(bd);
			
			
			View deleteView = convertView.findViewById(R.id.icon_img);
			this.addView(convertView);
			deleteView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					SThumbnail.this.removeView(convertView);
					
					if(iThumbnail != null) {
						iThumbnail.onDelete(compressFilePath, rawFilePath);
					}
				}
			});

			convertView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if(iThumbnail != null) {
						iThumbnail.onPreview(compressFilePath);
					}
				}
			});
			if(!deleteable)
			{
				deleteView.setVisibility(GONE);
			}
			if(iThumbnail != null) {
				if(iThumbnail != null) {
					iThumbnail.onAdd(compressFilePath, rawFilePath);
				}
			}
		} catch (OutOfMemoryError err) {

		}

	}



	public static int computeSampleSize(BitmapFactory.Options options,
			int minSideLength, int maxNumOfPixels) {
		int initialSize = computeInitialSampleSize(options, minSideLength,
				maxNumOfPixels);

		int roundedSize;
		if (initialSize <= 8) {
			roundedSize = 1;
			while (roundedSize < initialSize) {
				roundedSize <<= 1;
			}
		} else {
			roundedSize = (initialSize + 7) / 8 * 8;
		}

		return roundedSize;
	}

	private static int computeInitialSampleSize(BitmapFactory.Options options,
			int minSideLength, int maxNumOfPixels) {
		double w = options.outWidth;
		double h = options.outHeight;

		int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math
				.sqrt(w * h / maxNumOfPixels));
		int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(
				Math.floor(w / minSideLength), Math.floor(h / minSideLength));

		if (upperBound < lowerBound) {
			return lowerBound;
		}

		if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
			return 1;
		} else if (minSideLength == -1) {
			return lowerBound;
		} else {
			return upperBound;
		}
	}
	
    public interface IThumbnail {
    	public void onDelete(String compressUrl,String rawUrl);
    	public void onAdd(String compressUrl,String rawUrl);
    	public void onPreview(String compressUrl);
    } 
    
    public void removeAllView() {
		try {
			SThumbnail.this.removeAllViews();
		} catch (Exception e) {
		}
	}

}
