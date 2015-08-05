package com.yulinoo.plat.life.views.activity;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.yulinoo.plat.life.bean.Merchant;
import com.yulinoo.plat.life.net.callback.UICallback;
import com.yulinoo.plat.life.net.reqbean.ProductListAddReq;
import com.yulinoo.plat.life.net.resbean.NormalResponse;
import com.yulinoo.plat.life.net.service.RequestBean;
import com.yulinoo.plat.life.ui.widget.BackWidget;
import com.yulinoo.plat.life.ui.widget.MeFaceViewWiget;
import com.yulinoo.plat.life.ui.widget.MeFaceViewWiget.OnFaceClickListener;
import com.yulinoo.plat.life.ui.widget.NeighbourTalkRightWidget;
import com.yulinoo.plat.life.ui.widget.SThumbnail;
import com.yulinoo.plat.life.ui.widget.SThumbnail.IThumbnail;
import com.yulinoo.plat.life.utils.Constant;
import com.yulinoo.plat.life.utils.MeUtil;
import com.yulinoo.plat.life.utils.ProgressUtil;
import com.yulinoo.plat.melife.R;

import config.AppContext;
//发布商品，包含普通用户在论坛里面发帖子
public class PublishGoodsActivity extends EditPhotoActivity {
	public static final String PUBLIC_TAB="mTab";
	private SThumbnail sThumbnail;
	private ProductListAddReq productListAddReq;
	private EditText des;
	private ImageView ivIcon = null;
	private MeFaceViewWiget meface;
	private View photoPosition;
	private TextView publish;
	protected Map<String, String> filePathMap = new HashMap<String, String>();
	private int max_text_number=500;
	private TextView sy_text_number;
	private int publishType=Constant.PUBLISH_GOODS_TYPE.PERSONAL_INDEX_PUBLISH;//默认是个人发布
	@Override
	protected void initView(Bundle savedInstanceState) {
		setContentView(R.layout.publish_goods);
		max_text_number=Integer.parseInt(getString(R.string.max_text_number));
		sy_text_number=(TextView)findViewById(R.id.sy_text_number);
		sy_text_number.setText("( "+max_text_number+"/"+max_text_number+" )");
		meface = (MeFaceViewWiget) findViewById(R.id.meface);
		meface.setOnFaceClickListener(new OnFaceClickListener() {
			@Override
			public void onFaceClick(SpannableString spannableString) {
				des.append(spannableString);
			}
		});
		ivIcon = (ImageView) findViewById(R.id.ivIcon);
		photoPosition=findViewById(R.id.photoPosition);
		publish=(TextView)findViewById(R.id.publish);
		setListener(); 
	}

	@Override
	protected void initComponent() {
		sThumbnail = (SThumbnail) findViewById(R.id.sThumbnail);
		productListAddReq = (ProductListAddReq) getIntent().getSerializableExtra(PUBLIC_TAB);
		publishType=getIntent().getIntExtra(Constant.ActivityExtra.PUBLISH_GOODS_TYPE, Constant.PUBLISH_GOODS_TYPE.PERSONAL_INDEX_PUBLISH);
		des = (EditText) findViewById(R.id.des);
		des.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				String txt=s.toString();
				//				System.out.println("-----------"+s.length());
				//				for(int kk=0;kk<s.length();kk++)
				//				{
				//					System.out.println(s.charAt(kk));
				//				}
				//				if(txt.matches("[a-zA-Z0-9_\u4e00-\u9fa5]*")){
				//		            //不是非法字符
				//					System.out.println("aaaaaaaaaaaaa");
				//		        }

				int newSize=txt.length();
				if(newSize>max_text_number)
				{
					des.setText(des.getText().toString().substring(0,max_text_number));
					showToast("输入字数过多,将会被截断");
					newSize=120;
				}
				sy_text_number.setText("( "+(max_text_number-newSize)+"/"+max_text_number+" )");

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});
		//		String tmp="ksks[生气]";
		//		SpannableString spannableString = FaceConversionUtil.getInstance().getExpressionString(this, tmp);
		//		des.append(spannableString);
		findViewById(R.id.select_img).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				takePhoto();
			}
		});

		publish.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				publish();
			}
		});
		sThumbnail.setiThumbnail(new IThumbnail() {
			@Override
			public void onPreview(String compressUrl) {
				Intent intent = new Intent(mContext, PreviewPhotoActivity.class);
				intent.putExtra(Constant.IntentKey.IMAGE_FILE_PATH, compressUrl);
				startActivity(intent);
			}

			@Override
			public void onDelete(String compressUrl, String rawUrl) {
				try {
					File compressFile = new File(compressUrl);
					compressFile.delete();
					filePathMap.remove(compressUrl);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onAdd(String compressUrl, String rawUrl) {
				
			}
		});

	}

	public void takePhoto() {
		//List<Thumbnail> thumbnails = new Select().from(Thumbnail.class).execute();
		if (sThumbnail.getChildCount() >= 3) {
			showToast("照片最多上传三张");
			return;
		}
		editPhoto(photoPosition, 1080, 648,null);
	}

	private void publish() {
		String txt=getViewString(des);
		int newSize=txt.length();
		if(newSize>max_text_number)
		{
			des.setText(txt.substring(0,max_text_number));
			showToast("输入字数过多,将会被截断");
		}
		productListAddReq.setDesc(getViewString(des));
		Set<Entry<String, String>> set = filePathMap.entrySet();
		if (set.size() > 0) {
			StringBuffer indoorPath = new StringBuffer();

			for (Entry<String, String> entry : set) {
				if(indoorPath.length() == 0) {
					indoorPath.append(entry.getValue());
				} else {
					indoorPath.append(",").append(entry.getValue());
				}
			}
			productListAddReq.setIndoorPath(indoorPath.toString());
		}

		RequestBean<NormalResponse> requestBean = new RequestBean<NormalResponse>();
		requestBean.setRequestBody(productListAddReq);
		requestBean.setResponseBody(NormalResponse.class);
		requestBean.setURL(Constant.Requrl.getPublishGoods());
		ProgressUtil.showProgress(mContext, getString(R.string.publish_ing));
		requestServer(requestBean, new UICallback<NormalResponse>() {

			@Override
			public void onSuccess(NormalResponse respose) {
				ProgressUtil.dissmissProgress();
				try {
					if(respose.isSuccess())
					{
						//showToast(getString(R.string.publish_success));
						if(publishType==Constant.PUBLISH_GOODS_TYPE.PERSONAL_INDEX_PUBLISH)
						{//是首页进入的个人发布
							mContext.startActivity(new Intent(mContext, NeighbourTalkActivity.class)
							.putExtra(Constant.ActivityExtra.NEIGHBOUR_TALK_OPEN_TYPE, Constant.NEIGHBOURE_TALK_OPEN_TYPE.OPEN_POST));
						}else if(publishType==Constant.PUBLISH_GOODS_TYPE.PERSONAL_NEIGHBOUR_PUBLISH
								||publishType==Constant.PUBLISH_GOODS_TYPE.MERCHANT_ZONE_PUBLISH)
						{//是从邻语进入的以及商家空间发布
							Intent intent = new Intent();  
							intent.setAction(Constant.BroadType.GOODS_ADDED);  
							mContext.sendBroadcast(intent);
							
						}else if(publishType==Constant.PUBLISH_GOODS_TYPE.MERCHANT_INDEX_PUBLISH)
						{
							Merchant merchant=new Merchant();
							merchant.setSid(AppContext.currentAccount().getSid());
							mContext.startActivity(new Intent(mContext,UsrShopActivity.class).putExtra(Constant.ActivityExtra.MERCHANT, merchant));
						}
						finish();
					}else
					{
						showToast(respose.getMsg());
					}
				} catch (Exception e) {
					showToast(e.getMessage());
				}

			}

			@Override
			public void onError(String message) {
				ProgressUtil.dissmissProgress();
				showToast(message);
			}

			@Override
			public void onOffline(String message) {
				ProgressUtil.dissmissProgress();
				showToast(message);
			}
		});

	}

	@Override
	protected void initHead(BackWidget back_btn, TextView rightImg,View RightImaView , TextView rightText, TextView title, View titleLayout) {
		title.setText("发布信息");
		rightText.setVisibility(View.INVISIBLE);
		rightImg.setVisibility(View.INVISIBLE);
	}

	private void setListener() {
		ivIcon.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				if (meface.getVisibility()==View.VISIBLE) {
					meface.setVisibility(View.GONE);
				} else {
					meface.setVisibility(View.VISIBLE);
					MeUtil.closeSoftPad(PublishGoodsActivity.this);
				}
			}
		});
	}

	@Override
	public void photoUploadDone(Bitmap newPhoto, String afterCompressUrl,
			String netUrlPath) {
		sThumbnail.addImage(afterCompressUrl, null);
		filePathMap.put(afterCompressUrl, netUrlPath);		
	}
}
