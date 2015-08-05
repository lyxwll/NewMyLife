package com.yulinoo.plat.life.views.activity;

import java.util.List;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

import com.yulinoo.plat.life.bean.AreaInfo;
import com.yulinoo.plat.life.bean.LocationPoint;
import com.yulinoo.plat.life.bean.Merchant;
import com.yulinoo.plat.life.bean.Tag;
import com.yulinoo.plat.life.net.callback.UICallback;
import com.yulinoo.plat.life.net.reqbean.AreaInfoDetailReq;
import com.yulinoo.plat.life.net.reqbean.ShopModifyReq;
import com.yulinoo.plat.life.net.resbean.AreaInfoDetailResponse;
import com.yulinoo.plat.life.net.resbean.NormalResponse;
import com.yulinoo.plat.life.net.service.RequestBean;
import com.yulinoo.plat.life.service.MeMessageService;
import com.yulinoo.plat.life.ui.widget.BackWidget;
import com.yulinoo.plat.life.utils.Constant;
import com.yulinoo.plat.life.utils.MeUtil;
import com.yulinoo.plat.life.utils.NullUtil;
import com.yulinoo.plat.life.utils.ProgressUtil;
import com.yulinoo.plat.life.utils.Constant.HTTP_DATA_FORMAT;
import com.yulinoo.plat.melife.R;

import config.AppContext;

public class MerchantInfroActivity extends BaseActivity{

	private EditText index_merchant_name_et;
	private TextView index_shop_area;
	private TextView index_shop_position;
	private TextView index_shop_category;
	private EditText index_shop_addr_et;
	private EditText index_shop_telphone_et;
	private EditText index_shop_sign_name_et;
	private EditText index_shop_desc_et;
	private TextView index_edit_my_shop_info;
	
	private View index_shop_area_rl;
	private View index_shop_position_rl;
	private View index_shop_category_rl;
	
	private boolean isme=false;//是否是自己，如果是自己的话，则可以对自己的信息进行修改
	private Merchant merchant;
	
	private AreaInfo areaInfo;//商家选择了小区
	private LocationPoint locationPoint;//商家选择了地图位置
	private List<Tag> productInfos;//商家选择了经营范围
	
	private static MerchantInfroActivity instance=null;
	
	public static final MerchantInfroActivity instance(){
		if (instance!=null) {
			return instance;
		}else {
			throw new RuntimeException("MerchantInfroActivity not instantied yet");
		}
	}
	
	@Override
	protected void initView(Bundle savedInstanceState) {
		setContentView(R.layout.merchant_infor_layout);
		instance=this;
		merchant=(Merchant) getIntent().getSerializableExtra(Constant.ActivityExtra.MERCHANT);
		if (merchant.getSid()==AppContext.currentAccount().getSid()) {
			isme=true;
		}
	}

	@Override
	protected void initComponent() {
		showIndex();
	}

	@Override
	protected void initHead(BackWidget back_btn, TextView rightImg,
			View RightImaView, TextView rightText, TextView title,
			View titleLayout) {
		title.setText("商家信息");
	}
	
	private void showIndex()
	{
		index_merchant_name_et=(EditText)findViewById(R.id.index_merchant_name_et) ;
		index_shop_area=(TextView)findViewById(R.id.index_shop_area) ;
		index_shop_position=(TextView)findViewById(R.id.index_shop_position) ;
		index_shop_category=(TextView)findViewById(R.id.index_shop_category) ;
		index_shop_addr_et=(EditText)findViewById(R.id.index_shop_addr_et) ;
		index_shop_telphone_et=(EditText)findViewById(R.id.index_shop_telphone_et) ;
		index_shop_sign_name_et=(EditText)findViewById(R.id.index_shop_sign_name_et) ;
		index_shop_desc_et=(EditText)findViewById(R.id.index_shop_desc_et) ;
		index_edit_my_shop_info=(TextView)findViewById(R.id.index_edit_my_shop_info);

		index_shop_area_rl=findViewById(R.id.index_shop_area_rl);
		index_shop_position_rl=findViewById(R.id.index_shop_position_rl);
		index_shop_category_rl=findViewById(R.id.index_shop_category_rl);

		index_shop_area_rl.setEnabled(false);
		index_shop_position_rl.setEnabled(false);
		index_shop_category_rl.setEnabled(false);

		if(isme)
		{
			index_edit_my_shop_info.setVisibility(View.VISIBLE);
			//			StateListDrawable drawable =MeUtil.createImageSelectStateListDrawable(activity.getResources(),R.drawable.edit_selected,R.drawable.edit_normal,SizeUtil.normal_text_picture_size(activity));
			//			index_edit_my_shop_info.setCompoundDrawablesWithIntrinsicBounds(drawable,null,null,null);
			ColorStateList colors=MeUtil.createColorSelectStateList(getResources().getColor(R.color.font_red), MerchantInfroActivity.this.getResources().getColor(R.color.font_blue));
			index_edit_my_shop_info.setTextColor(colors);
			index_edit_my_shop_info.setText(R.string.modify_info);
			index_edit_my_shop_info.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					editMyShopInfo();
				}
			});
			index_shop_area_rl.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					startActivityForResult(new Intent(MerchantInfroActivity.this, ZoneSelectActivity.class).putExtra(ZoneSelectActivity.isOpenShop, true), ZoneSelectActivity.AREA_SELECTED);
				}
			});
			index_shop_position_rl.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					long areaSid=merchant.getAlongAreaSid();
					AreaInfoDetailReq areaDetailReq=new AreaInfoDetailReq();
					areaDetailReq.setAreaSid(areaSid);
					RequestBean<AreaInfoDetailResponse> requestBean = new RequestBean<AreaInfoDetailResponse>();
					requestBean.setRequestBody(areaDetailReq);
					requestBean.setResponseBody(AreaInfoDetailResponse.class);
					requestBean.setURL(Constant.Requrl.getAreaDetail());
					ProgressUtil.showProgress(MerchantInfroActivity.this, "处理中...");
					MerchantInfroActivity.this.requestServer(requestBean, new UICallback<AreaInfoDetailResponse>() {
						@Override
						public void onSuccess(AreaInfoDetailResponse respose) {
							try {
								ProgressUtil.dissmissProgress();
								if(respose.isSuccess())
								{
									AreaInfo ai=respose.getArea();
									startActivityForResult(new Intent(MerchantInfroActivity.this, MerchantChoiceMapActivity.class)
									.putExtra(MerchantChoiceMapActivity.area, ai)
									.putExtra(MerchantChoiceMapActivity.MERCHANT, merchant)
									, MerchantChoiceMapActivity.AMAP_SELECTED);
								}else
								{
									MerchantInfroActivity.this.showToast(respose.getMsg());
								}
							} catch (Exception e) {
							}
						}

						@Override
						public void onError(String message) {
							ProgressUtil.dissmissProgress();
							MerchantInfroActivity.this.showToast(message);
						}

						@Override
						public void onOffline(String message) {
							ProgressUtil.dissmissProgress();
							MerchantInfroActivity.this.showToast(message);
						}
					});
				}
			});

			index_shop_category_rl.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					MerchantInfroActivity.this.startActivityForResult(new Intent(MerchantInfroActivity.this, CategorySelectActivity.class).putExtra(Constant.ActivityExtra.MERCHANT, merchant), CategorySelectActivity.selectTagOk);
				}
			});
		}else
		{
			index_edit_my_shop_info.setVisibility(View.GONE);
			findViewById(R.id.index_shop_area_right_iv).setVisibility(View.GONE);
			findViewById(R.id.index_shop_position_iv).setVisibility(View.GONE);
			findViewById(R.id.index_shop_category_iv).setVisibility(View.GONE);
			findViewById(R.id.index_shop_position_ll).setVisibility(View.GONE);
		}

		index_merchant_name_et.setText(merchant.getMerchantName());
		index_shop_area.setText(merchant.getAreaName());
		if(merchant.getLatItude()>0&&merchant.getLongItude()>0)
		{
			index_shop_position.setText(MerchantInfroActivity.this.getString(R.string.have_choice_position));
		}else
		{
			index_shop_position.setText(MerchantInfroActivity.this.getString(R.string.not_choice_position));
		}

		index_shop_category.setText(merchant.getMerchantTagNameArray());
		index_shop_addr_et.setText(merchant.getMerchantAddr());
		index_shop_telphone_et.setText(merchant.getMerchantTelphone());
		index_shop_sign_name_et.setText(merchant.getSignatureName());
		index_shop_desc_et.setText(merchant.getMerchantDesc());
	}
	
	private void editMyShopInfo()
	{
		if(!index_edit_my_shop_info.isSelected())
		{
			index_edit_my_shop_info.setSelected(true);
			index_edit_my_shop_info.setText(R.string.save_info);
			areaInfo=null;
			locationPoint=null;
			productInfos=null;
			index_merchant_name_et.setEnabled(true);
			index_shop_addr_et.setEnabled(true);
			index_shop_telphone_et.setEnabled(true);
			index_shop_sign_name_et.setEnabled(true);
			index_shop_desc_et.setEnabled(true);

			index_shop_area_rl.setEnabled(true);
			index_shop_position_rl.setEnabled(true);
			index_shop_category_rl.setEnabled(true);
		}else if(index_edit_my_shop_info.isSelected())
		{
			index_edit_my_shop_info.setSelected(false);
			index_edit_my_shop_info.setText(R.string.modify_info);
			index_merchant_name_et.setEnabled(false);
			index_shop_addr_et.setEnabled(false);
			index_shop_telphone_et.setEnabled(false);
			index_shop_sign_name_et.setEnabled(false);
			index_shop_desc_et.setEnabled(false);

			index_shop_area_rl.setEnabled(false);
			index_shop_position_rl.setEnabled(false);
			index_shop_category_rl.setEnabled(false);

			String sn=index_merchant_name_et.getText().toString();
			if(NullUtil.isStrNull(sn))
			{
				MeUtil.showToast(mContext,mContext.getString(R.string.shop_name)+"不允许为空");
				return;
			}
			if(sn.length()>100)
			{
				MeUtil.showToast(mContext,mContext.getString(R.string.shop_name)+"不能多于100个字");
				return;
			}

			String tel=index_shop_telphone_et.getText().toString();
			if(NullUtil.isStrNull(tel))
			{
				MeUtil.showToast(mContext,mContext.getString(R.string.shop_telphone)+"不允许为空");
				return;
			}

			if(tel.length()>20)
			{
				MeUtil.showToast(mContext,mContext.getString(R.string.shop_telphone)+"不能多于20个字");
				return;
			}

			//保存商户信息
			final ShopModifyReq shopModifyReq = new ShopModifyReq();
			shopModifyReq.setMerchantDesc(index_shop_desc_et.getText().toString());
			shopModifyReq.setMerchantAddr(index_shop_addr_et.getText().toString());
			shopModifyReq.setMerchantName(sn);
			shopModifyReq.setMerchantTelphone(tel);
			shopModifyReq.setMevalue(AppContext.currentAccount().getMevalue());
			shopModifyReq.setSignatureName(index_shop_sign_name_et.getText().toString());
			if(areaInfo!=null)
			{
				shopModifyReq.setAlongAreaSid(areaInfo.getSid());
			}
			if(locationPoint!=null)
			{
				shopModifyReq.setLatItude(locationPoint.getLatitude());
				shopModifyReq.setLongItude(locationPoint.getLongitude());
			}
			if (productInfos != null) {
				StringBuffer scope = new StringBuffer();
				for (Tag productInfo : productInfos) {
					if (scope.length() == 0) {
						scope.append(productInfo.getAlongCategorySid() + "-" + productInfo.getSid());
					} else {
						scope.append(",").append(productInfo.getAlongCategorySid() + "-" + productInfo.getSid());
					}
				}
				shopModifyReq.setScope(scope.toString());
			}

			RequestBean<NormalResponse> requestBean = new RequestBean<NormalResponse>();
			requestBean.setRequestBody(shopModifyReq);
			requestBean.setRequsetFormat(HTTP_DATA_FORMAT.FORM);
			requestBean.setResponseBody(NormalResponse.class);
			requestBean.setURL(Constant.Requrl.getModifyShop());
			ProgressUtil.showProgress(mContext, "正在保存...");
			MeMessageService.instance().requestServer(requestBean, new UICallback<NormalResponse>() {
				@Override
				public void onSuccess(NormalResponse respose) {
					try {
						ProgressUtil.dissmissProgress();
						if(respose.isSuccess())
						{
							MeUtil.showToast(mContext,"修改信息成功");
							merchant.setMerchantName(index_merchant_name_et.getText().toString());
							if(areaInfo!=null)
							{
								merchant.setAreaName(areaInfo.getAreaName());
								merchant.setAlongAreaSid(areaInfo.getSid());
							}
							if(locationPoint!=null)
							{
								merchant.setLatItude(locationPoint.getLatitude());
								merchant.setLongItude(locationPoint.getLongitude());
							}
							if(productInfos!=null)
							{
								StringBuffer bufTagSids=new StringBuffer();
								StringBuffer bufTagName=new StringBuffer();
								for (Tag p : productInfos) {
									if (bufTagSids.length() == 0) {
										bufTagSids.append(p.getSid());
										bufTagName.append(p.getTagName());
									} else {
										bufTagSids.append(",").append(p.getSid());
										bufTagName.append(",").append(p.getTagName());
									}
								}
								merchant.setMerchantTagSidArray(bufTagSids.toString());
								merchant.setMerchantTagNameArray(bufTagName.toString());
							}
							merchant.setMerchantTelphone(index_shop_telphone_et.getText().toString());
							merchant.setSignatureName(index_shop_sign_name_et.getText().toString());
							merchant.setMerchantDesc(index_shop_desc_et.getText().toString());

							index_merchant_name_et.setText(merchant.getMerchantName());
							index_shop_addr_et.setText(merchant.getAreaName());
							index_shop_sign_name_et.setText(merchant.getSignatureName());
						}else
						{
							MeUtil.showToast(mContext,respose.getMsg());
						}
					} catch (Exception e) {
					}
				}

				@Override
				public void onError(String message) {
					ProgressUtil.dissmissProgress();
					MeUtil.showToast(mContext,message);
				}

				@Override
				public void onOffline(String message) {
					ProgressUtil.dissmissProgress();
					MeUtil.showToast(mContext,message);
				}
			});
		}
	}
	
	public void categorySelected(List<Tag> listTags)
	{
		productInfos = listTags;
		StringBuffer stringBuffer = new StringBuffer();
		if (productInfos != null) {
			for (Tag p : productInfos) {
				if (stringBuffer.length() == 0) {
					stringBuffer.append(p.getTagName());
				} else {
					stringBuffer.append(",").append(p.getTagName());
				}
			}
		}
		index_shop_category.setText(stringBuffer.toString());
	}
	public void areaSelected(AreaInfo ai)
	{
		areaInfo=ai;
		index_shop_area.setText(areaInfo.getAreaName());
	}
	public void loactionSelected(LocationPoint lp)
	{
		index_shop_position.setText(mContext.getString(R.string.have_choice_position));
		locationPoint=lp;
	}

}
