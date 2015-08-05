package com.yulinoo.plat.life.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.net.Uri;
import android.text.SpannableString;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;
import com.yulinoo.plat.life.MyApplication;
import com.yulinoo.plat.life.bean.Account;
import com.yulinoo.plat.life.bean.AreaInfo;
import com.yulinoo.plat.life.bean.Category;
import com.yulinoo.plat.life.bean.ForumInfo;
import com.yulinoo.plat.life.bean.ForumNote;
import com.yulinoo.plat.life.bean.Merchant;
import com.yulinoo.plat.life.bean.ProductInfo;
import com.yulinoo.plat.life.net.callback.UICallback;
import com.yulinoo.plat.life.net.reqbean.AreaConcernCancelReq;
import com.yulinoo.plat.life.net.reqbean.CategoryForumReq;
import com.yulinoo.plat.life.net.reqbean.CategoryReq;
import com.yulinoo.plat.life.net.reqbean.ConcernAreaListReq;
import com.yulinoo.plat.life.net.reqbean.ConcernMerchantReq;
import com.yulinoo.plat.life.net.reqbean.DelGoodsReq;
import com.yulinoo.plat.life.net.reqbean.GetMyConcernMerchantsReq;
import com.yulinoo.plat.life.net.reqbean.LoginReq;
import com.yulinoo.plat.life.net.reqbean.SendMessageReq;
import com.yulinoo.plat.life.net.reqbean.UsrDetailReq;
import com.yulinoo.plat.life.net.resbean.ConcernMerchantResponse;
import com.yulinoo.plat.life.net.resbean.ForumResponse;
import com.yulinoo.plat.life.net.resbean.LoginResponse;
import com.yulinoo.plat.life.net.resbean.MerchantResponse;
import com.yulinoo.plat.life.net.resbean.MyFocusAreaResponse;
import com.yulinoo.plat.life.net.resbean.NormalResponse;
import com.yulinoo.plat.life.net.resbean.UsrDetailResponse;
import com.yulinoo.plat.life.net.resbean.ZoneMainFunctionResponse;
import com.yulinoo.plat.life.net.service.RequestBean;
import com.yulinoo.plat.life.service.MeMessageService;
import com.yulinoo.plat.life.ui.widget.ConfirmCloseDialog;
import com.yulinoo.plat.life.ui.widget.ConfirmCloseDialog.FinishCallback;
import com.yulinoo.plat.life.ui.widget.MeRadioWidget;
import com.yulinoo.plat.life.ui.widget.bean.MeRadio;
import com.yulinoo.plat.life.utils.Constant.HTTP_DATA_FORMAT;
import com.yulinoo.plat.life.utils.Constant.HTTP_METHOD;
import com.yulinoo.plat.life.views.activity.ForumMerchantActivity;
import com.yulinoo.plat.life.views.activity.GoodsDetailActivity;
import com.yulinoo.plat.life.views.activity.LoginActivity;
import com.yulinoo.plat.life.views.activity.MeLifeMainActivity;
import com.yulinoo.plat.life.views.activity.MerchantCommentListActivity;
import com.yulinoo.plat.life.views.activity.MerchantMapActivity;
import com.yulinoo.plat.life.views.activity.MerchantPraiseListActivity;
import com.yulinoo.plat.life.views.activity.PrivateMessageActivity;
import com.yulinoo.plat.life.views.activity.UsrShopActivity;
import com.yulinoo.plat.life.views.activity.UsrZoneActivity;
import com.yulinoo.plat.life.views.adapter.CommonHolderView;
import com.yulinoo.plat.life.views.adapter.ZoneSelectAreaAdapter.OnAreaInfoConcernedListener;
import com.yulinoo.plat.melife.R;

import config.AppContext;

@SuppressLint({ "InlinedApi", "ResourceAsColor" })
public class MeUtil {
	public static void showToast(Context context,String message) {

		try {
			Toast toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
			toast.setGravity(Gravity.CENTER, 0, 0);
			toast.show();

		} catch (Error e) {
		}
	}

	/**
	 * 微信分享图片处理
	 * @param bmp
	 * @param needRecycle
	 * @return
	 */
	public static byte[] bmpToByteArray(final Bitmap bmp, final boolean needRecycle) {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		bmp.compress(CompressFormat.PNG, 100, output);
		if (needRecycle) {
			bmp.recycle();
		}

		byte[] result = output.toByteArray();
		try {
			output.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}


	/*
	 * 从网络上获取图片，如果图片在本地存在的话就直接拿，如果不存在再去服务器上下载图片
	 * 这里的path是图片的地址
	 */
	public static Uri getImageURI(String path, File cache) throws Exception {
		//String name = MD5.getMD5(path) + path.substring(path.lastIndexOf("."));
		String name = MessageDigest.getInstance("MD5").digest(path.getBytes("UTF-8"))+ path.substring(path.lastIndexOf("."));
		File file = new File(cache, name);
		// 如果图片存在本地缓存目录，则不去服务器下载 
		if (file.exists()) {
			return Uri.fromFile(file);//Uri.fromFile(path)这个方法能得到文件的URI
		} else {
			// 从网络上获取图片
			URL url = new URL(path);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(5000);
			conn.setRequestMethod("GET");
			conn.setDoInput(true);
			if (conn.getResponseCode() == 200) {
				InputStream is = conn.getInputStream();
				FileOutputStream fos = new FileOutputStream(file);
				byte[] buffer = new byte[1024];
				int len = 0;
				while ((len = is.read(buffer)) != -1) {
					fos.write(buffer, 0, len);
				}
				is.close();
				fos.close();
				// 返回一个URI对象
				return Uri.fromFile(file);
			}
		}
		return null;
	}

	public static void closeSoftPad(Activity activity)
	{
		View view = activity.getWindow().peekDecorView();
		if (view != null) {
			InputMethodManager inputmanger = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
			inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
		}
	}

	public static boolean isMobilePhone(String mobilePhone)
	{
		String regExp = "^[1]([3|5|7|8][0-9])[0-9]{8}$";  
		Pattern p = Pattern.compile(regExp);
		Matcher m = p.matcher(mobilePhone);
		return m.find();//boolean
	}
	public static StateListDrawable createCheckedListDrawable(Resources resources,int checked,int unchecked) {
		StateListDrawable drawable = new StateListDrawable();
		Bitmap selected_bt=BitmapFactory.decodeResource(resources,checked);
		Bitmap new_selected_bt=ImageThumbnail.PicZoom(selected_bt, 80, 80);
		Drawable selected =new BitmapDrawable(resources, new_selected_bt) ;
		drawable.addState(new int[]{android.R.attr.state_checked},selected);
		Bitmap no_selected_bt=BitmapFactory.decodeResource(resources,unchecked);
		Bitmap new_no_selected_bt=ImageThumbnail.PicZoom(no_selected_bt, 80, 80);
		Drawable no_selected =new BitmapDrawable(resources, new_no_selected_bt);
		drawable.addState(new int[]{-android.R.attr.state_checked},no_selected);//前面加个负号表示未选中状态
		return drawable;
	}

	public static  ColorStateList createColorStateList(int normal, int pressed, int focused, int unable) {
		int[] colors = new int[] { pressed, focused, normal, focused, unable, normal };
		int[][] states = new int[6][];
		states[0] = new int[] { android.R.attr.state_pressed, android.R.attr.state_enabled };
		states[1] = new int[] { android.R.attr.state_enabled, android.R.attr.state_focused };
		states[2] = new int[] { android.R.attr.state_enabled };
		states[3] = new int[] { android.R.attr.state_focused };
		states[4] = new int[] { android.R.attr.state_window_focused };
		states[5] = new int[] {};
		ColorStateList colorList = new ColorStateList(states, colors);
		return colorList;
	}
	public static  ColorStateList createColorSelectStateList(int selected, int normal) {
		int[] colors = new int[] { selected, normal };
		int[][] states = new int[2][];
		states[0] = new int[] { android.R.attr.state_selected};
		states[1] = new int[] { -android.R.attr.state_selected};
		ColorStateList colorList = new ColorStateList(states, colors);
		return colorList;
	}

	public static StateListDrawable createImageSelectStateListDrawable(Resources resources,int checked,int unchecked,int[] viewSize) {
		StateListDrawable drawable = new StateListDrawable();
		Bitmap selected_bt=BitmapFactory.decodeResource(resources,checked);
		Bitmap new_selected_bt=ImageThumbnail.PicZoom(selected_bt, viewSize[0], viewSize[1]);
		Drawable selected =new BitmapDrawable(resources, new_selected_bt) ;
		drawable.addState(new int[]{android.R.attr.state_selected},selected);
		Bitmap no_selected_bt=BitmapFactory.decodeResource(resources,unchecked);
		Bitmap new_no_selected_bt=ImageThumbnail.PicZoom(no_selected_bt, viewSize[0], viewSize[1]);
		Drawable no_selected =new BitmapDrawable(resources, new_no_selected_bt);
		drawable.addState(new int[]{-android.R.attr.state_selected},no_selected);//前面加个负号表示未选中状态
		return drawable;
	}
	public static StateListDrawable createImageSelectStateListDrawable(Resources resources,int checked,int unchecked) {
		StateListDrawable drawable = new StateListDrawable();
		Bitmap selected_bt=BitmapFactory.decodeResource(resources,checked);
		Drawable selected =new BitmapDrawable(resources, selected_bt);
		drawable.addState(new int[]{android.R.attr.state_selected},selected);
		Bitmap no_selected_bt=BitmapFactory.decodeResource(resources,unchecked);
		Drawable no_selected =new BitmapDrawable(resources, no_selected_bt);
		drawable.addState(new int[]{-android.R.attr.state_selected},no_selected);//前面加个负号表示未选中状态
		return drawable;
	}
	public static StateListDrawable createBgSelectStateListDrawable(Resources resources,int checked,int unchecked) {
		StateListDrawable drawable = new StateListDrawable();
		Drawable selected =resources.getDrawable(checked);
		drawable.addState(new int[]{android.R.attr.state_selected},selected);
		Drawable no_selected =resources.getDrawable(unchecked);
		drawable.addState(new int[]{-android.R.attr.state_selected},no_selected);//前面加个负号表示未选中状态
		return drawable;
	}
	//设置性别
	public static void setSexGroup(Context context,MeRadioWidget sex_group,int initSex)
	{
		List<MeRadio> lstRadios=new ArrayList<MeRadio>(3);
		MeRadio man=new MeRadio();
		man.setIndex(Constant.SEX.SEX_MAN);
		man.setDirect(MeRadio.DIRECT_TOP);
		man.setName(context.getString(R.string.iamman));
		if(initSex==Constant.SEX.SEX_MAN)
		{
			man.setSelected(true);
		}
		man.setSelectedPicture(R.drawable.man_selected);
		man.setUnSelectedPicture(R.drawable.man_normal);
		lstRadios.add(man);
		MeRadio woman=new MeRadio();
		woman.setIndex(Constant.SEX.SEX_WOMAN);
		woman.setDirect(MeRadio.DIRECT_TOP);
		woman.setName(context.getString(R.string.iamwoman));
		if(initSex==Constant.SEX.SEX_WOMAN)
		{
			woman.setSelected(true);
		}
		woman.setSelectedPicture(R.drawable.woman_selected);
		woman.setUnSelectedPicture(R.drawable.woman_normal);
		lstRadios.add(woman);
		//		MeRadio secret=new MeRadio();
		//		secret.setIndex(Constant.SEX.SEX_SECRECT);
		//		secret.setDirect(MeRadio.DIRECT_TOP);
		//		secret.setName(context.getString(R.string.iamsecret));
		//		if(initSex==Constant.SEX.SEX_SECRECT)
		//		{
		//			secret.setSelected(true);
		//		}
		//		secret.setSelectedPicture(R.drawable.secret_selected);
		//		secret.setUnSelectedPicture(R.drawable.secret_normal);
		//		lstRadios.add(secret);
		int[] viewSize=SizeUtil.usr_zone_sex_size(context);
		sex_group.load(lstRadios,viewSize);
	}

	//评论和赞
	public static void addCommentPraise(Context context,long goodsSid,long merchantSid,final String content,final OnPraiseListener onPraiseListener) {
		if(!AppContext.currentAccount().getIsUsrLogin())
		{
			showToast(context,context.getString(R.string.need_login_first));
			context.startActivity(new Intent(context, LoginActivity.class));
			return;
		}

		SendMessageReq req=new SendMessageReq();
		req.setAccSid(merchantSid);
		req.setMevalue(AppContext.currentAccount().getMevalue());
		if(NullUtil.isStrNotNull(content))
		{//说明是评论
			req.setType(Constant.MSGTYPE.TYPE_COMMENT);
			req.setDesc(content);
		}else
		{
			req.setType(Constant.MSGTYPE.TYPE_PRAISE);
		}

		req.setAlongAreaSid(AppContext.nearByArea().getSid());
		req.setGoodsSid(goodsSid);

		RequestBean<NormalResponse> requestBean = new RequestBean<NormalResponse>();
		requestBean.setHttpMethod(HTTP_METHOD.POST);
		requestBean.setRequestBody(req);
		requestBean.setRequsetFormat(HTTP_DATA_FORMAT.FORM);
		requestBean.setResponseBody(NormalResponse.class);
		requestBean.setURL(Constant.Requrl.getSendWrapMessage());
		ProgressUtil.showProgress(context, "处理中...");
		MeMessageService.instance().requestServer(requestBean, new UICallback<NormalResponse>() {

			@Override
			public void onSuccess(NormalResponse respose) {
				try {
					ProgressUtil.dissmissProgress();
					if(onPraiseListener!=null)
					{
						if(respose.isSuccess())
						{
							onPraiseListener.OnPraiseed(true,null);
						}else
						{
							onPraiseListener.OnPraiseed(false,respose.getMsg());
						}
					}
				} catch (Exception e) {
				}
			}

			@Override
			public void onError(String message) {
				ProgressUtil.dissmissProgress();
				if(onPraiseListener!=null)
				{
					onPraiseListener.OnPraiseed(false,message);
				}
			}

			@Override
			public void onOffline(String message) {
				ProgressUtil.dissmissProgress();
				if(onPraiseListener!=null)
				{
					onPraiseListener.OnPraiseed(false,message);
				}
			}
		});
	}
	public interface OnPraiseListener
	{
		public void OnPraiseed(boolean isOk,String message);
	}

	//登录
	public static void login(final String userName_str,final String password_str,final OnLoginListener onLoginListener)
	{
		if (NullUtil.isStrNull(userName_str)) {
			if(onLoginListener!=null)
			{
				onLoginListener.onLogin(false,"用户名不能为空");
			}
			return;
		}

		if (NullUtil.isStrNull(password_str)) {
			if(onLoginListener!=null)
			{
				onLoginListener.onLogin(false,"密码不能为空");
			}
			return;
		}

		LoginReq loginReq = new LoginReq();
		loginReq.setAccount(userName_str);
		loginReq.setPassword(password_str);
		loginReq.setPhoneType(AppContext.getPhoneType());
		String randCode=SharedPreferencesUtil.getString(MeMessageService.instance(),"randCode", "no");
		loginReq.setRandCode(randCode);
		RequestBean<LoginResponse> requestBean = new RequestBean<LoginResponse>();
		requestBean.setRequestBody(loginReq);
		requestBean.setResponseBody(LoginResponse.class);
		requestBean.setURL(Constant.Requrl.getUsrLogin());
		MeMessageService.instance().requestServer(requestBean, new UICallback<LoginResponse>() {
			@Override
			public void onSuccess(LoginResponse respose) {
				if(!respose.isSuccess()) {
					if(onLoginListener!=null)
					{
						onLoginListener.onLogin(false,respose.getMsg());
					}
					return;
				}

				if(respose.getLoginOther())
				{//表明在其他地方登录过，此时则需要更新本地相关缓存
					final String mevalue=respose.getMevalue();
					//重新加载一遍用户详情
					UsrDetailReq merchantDetailReq=new UsrDetailReq();
					merchantDetailReq.setSid(respose.getAccSid());
					RequestBean<UsrDetailResponse> requestBean = new RequestBean<UsrDetailResponse>();
					requestBean.setRequestBody(merchantDetailReq);
					requestBean.setResponseBody(UsrDetailResponse.class);
					requestBean.setURL(Constant.Requrl.getUsrDetail());
					MeMessageService.instance().requestServer(requestBean, new UICallback<UsrDetailResponse>() {
						@Override
						public void onSuccess(UsrDetailResponse respose) {
							if(respose.isSuccess())
							{
								AppContext.setCurrentAccountNull();
								AppContext.setCurrentAreaInfo(null);

								new Delete().from(Account.class).where("account=?",userName_str).execute();
								final Account saveAcc=respose.getAccout();
								saveAcc.setIsTemp(false);
								saveAcc.setLastAccTime(System.currentTimeMillis());
								saveAcc.setIsUsrLogin(true);
								saveAcc.setAccPwd(password_str);
								saveAcc.setMevalue(mevalue);
								saveAcc.save();

								//完全加载的情况下,将原来可能存在的用户所对于的小区全部删除,然后从服务器重新加载一下小区
								new Delete().from(AreaInfo.class).where("accSid=? ",saveAcc.getSid()).execute();
								//加载用户所关注的小区
								loadFocusArea(null,new OnLoadFocusAreaListener() {
									@Override
									public void onLoadFocusAreaDone(boolean isSuccess, String message) {
										if(AppContext.currentAreaInfo()!=null)
										{
											Account tempAcc=new Select().from(Account.class).where("isTemp=?",Constant.DB_BOOLEAN.BOOLEAN_TRUE).executeSingle();
											if(tempAcc==null)
											{//本地还没有临时账号,则将当前用户注册时的临时账号放于本地
												Long tempSid=saveAcc.getTempSid();
												if(tempSid!=null)
												{
													tempAcc=new Account();
													tempAcc.setAccName("me_"+tempSid);
													tempAcc.setAccount("me_"+tempSid);
													tempAcc.setAccType(Constant.ACCTYPE.ACCTYPE_NORMAL_USR);
													tempAcc.setIsTemp(true);
													tempAcc.setIsUsrLogin(false);
													tempAcc.setLastAccTime(0L);
													tempAcc.setSid(tempSid);
													tempAcc.save();
													AreaInfo are=AppContext.currentAreaInfo();//把当前用户关注的小区赋值给临时账号
													AreaInfo tmp=new AreaInfo();
													tmp.setAccSid(tempSid);
													tmp.setAlongCitySid(are.getAlongCitySid());
													tmp.setAlongDistrictSid(are.getAlongDistrictSid());
													tmp.setAreaName(are.getAreaName());
													tmp.setAreaType(are.getAreaType());
													tmp.setAttNum(are.getAttNum());
													tmp.setCityDomain(are.getCityDomain());
													tmp.setConcernAt(are.getConcernAt());
													tmp.setEnterNum(are.getEnterNum());
													tmp.setIcon(are.getIcon());
													tmp.setLatItude(are.getLatItude());
													tmp.setLongItude(are.getLongItude());
													tmp.setSid(are.getSid());
													tmp.save();
												}
											}

											final List<Integer> list=new ArrayList<Integer>();//list用来判断需要的数据是否加载完成
											list.add(1);//用于加载关注的商家列表结束事件标识
											List<AreaInfo> otherCityAreaInfo=new ArrayList<AreaInfo>();
											List<AreaInfo> listArea=AppContext.currentFocusArea();
											for(AreaInfo ai:listArea)
											{
												boolean needAdd=true;
												for(AreaInfo aai:otherCityAreaInfo)
												{
													if(aai.getAlongCitySid()==ai.getAlongCitySid())
													{
														needAdd=false;
														break;
													}
												}
												if(needAdd)
												{
													otherCityAreaInfo.add(ai);
												}
											}
											int listAreaLen=otherCityAreaInfo.size();
											for(int kk=0;kk<listAreaLen;kk++)
											{//添加结束事件标识
												list.add(2);//加载城市分类的要求
												list.add(3);//加载城市栏目的要求
											}
											for(AreaInfo ai:otherCityAreaInfo)
											{
												new Delete().from(Category.class).where("citySid=? ",ai.getAlongCitySid()).execute();
												MeUtil.loadCityCategory(ai.getAlongCitySid(),new OnLoadCityCategoryListener() {
													@Override
													public void onLoadCityCategoryDone() {
														synchronized (list) {
															list.remove(0);
															if(list.size()==0)
															{
																if(onLoginListener!=null)
																{
																	onLoginListener.onLogin(true,null);
																}
															}
														}
													}
												});
												new Delete().from(Category.class).where("citySid=? ",ai.getAlongCitySid()).execute();
												MeUtil.loadCityForums(ai.getAlongCitySid(),new OnCityForumLoadListener() {
													@Override
													public void cityForumLoadDone() {
														synchronized (list) {
															list.remove(0);
															if(list.size()==0)
															{
																if(onLoginListener!=null)
																{
																	onLoginListener.onLogin(true,null);
																}
															}
														}
													}
												});
											}
											//完全加载的情况下,先将所有的关注的对象全部删除
											new Delete().from(Merchant.class).where("accSid=?",saveAcc.getSid()).execute();
											//加载用户所关注的商家
											loadSubscribe(null, new OnLoadFocusMerchantListener() {
												@Override
												public void onLoadFocusMerchantDone(boolean isSuccess, String message) {
													synchronized (list) {
														list.remove(0);
														if(list.size()==0)
														{
															if(onLoginListener!=null)
															{
																onLoginListener.onLogin(true,null);
															}
														}
													}
												}
											});
										}else
										{
											if(onLoginListener!=null)
											{
												onLoginListener.onLogin(false,"未知错误");
											}
										}
									}
								});
							}else
							{
								if(onLoginListener!=null)
								{
									onLoginListener.onLogin(false,respose.getMsg());
								}
							}

						}

						@Override
						public void onError(String message) {
							if(onLoginListener!=null)
							{
								onLoginListener.onLogin(false,message);
							}
						}

						@Override
						public void onOffline(String message) {
							if(onLoginListener!=null)
							{
								onLoginListener.onLogin(false,message);
							}
						}
					});
				}else
				{//登录成功，且没有切换过手机
					List<Account> list=new Select().from(Account.class).where("sid=?",respose.getAccSid()).execute();
					for(Account acc:list)
					{
						acc.setIsUsrLogin(true);
						acc.setMevalue(respose.getMevalue());
						acc.setLastAccTime(System.currentTimeMillis());
						acc.setViewNumber(respose.getViewNumber());
						acc.setFansNumber(respose.getFansNumber());
						acc.setAccPwd(password_str);
						acc.save();
					}
					AppContext.setCurrentAccountNull();
					//更新分类和栏目，服务器端有可能进行修改
					{
						if(AppContext.currentFocusArea()!=null&&AppContext.currentFocusArea().size()>0)
						{
							List<AreaInfo> otherCityAreaInfo=new ArrayList<AreaInfo>();
							List<AreaInfo> listArea=AppContext.currentFocusArea();
							for(AreaInfo ai:listArea)
							{
								boolean needAdd=true;
								for(AreaInfo aai:otherCityAreaInfo)
								{
									if(aai.getAlongCitySid()==ai.getAlongCitySid())
									{
										needAdd=false;
										break;
									}
								}
								if(needAdd)
								{
									otherCityAreaInfo.add(ai);
								}
							}
							for(AreaInfo ai:otherCityAreaInfo)
							{
								MeUtil.loadCityCategory(ai.getAlongCitySid(),null);
								MeUtil.loadCityForums(ai.getAlongCitySid(),null);
							}
						}
					}
					if(onLoginListener!=null)
					{
						onLoginListener.onLogin(true,null);
					}
				}
			}

			@Override
			public void onError(String message) {
				if(onLoginListener!=null)
				{
					onLoginListener.onLogin(false,message);
				}
			}

			@Override
			public void onOffline(String message) {
				if(onLoginListener!=null)
				{
					onLoginListener.onLogin(false,message);
				}
			}
		});
	}

	public interface OnLoginListener
	{
		public void onLogin(boolean isSuccess,String message);
	}


	//加载用户关注的小区
	public static void loadFocusArea(Context context,final OnLoadFocusAreaListener loadFocusAreaListener)
	{
		final Account account=AppContext.currentAccount();
		if(account==null)
		{
			if(loadFocusAreaListener!=null)
			{
				loadFocusAreaListener.onLoadFocusAreaDone(false,"当前无用户可操作");
			}
			return;
		}

		ConcernAreaListReq concernAreaListReq = new ConcernAreaListReq();
		concernAreaListReq.setAccSid(account.getSid());
		concernAreaListReq.setUpdatedAt(AppContext.areaUpdatedAt());
		RequestBean<MyFocusAreaResponse> requestBean = new RequestBean<MyFocusAreaResponse>();
		requestBean.setRequestBody(concernAreaListReq);
		requestBean.setResponseBody(MyFocusAreaResponse.class);
		requestBean.setURL(Constant.Requrl.getConcernAreaList());
		if(MeMessageService.isReady())
		{
			MeMessageService.instance().requestServer(requestBean, new UICallback<MyFocusAreaResponse>() {
				@Override
				public void onSuccess(MyFocusAreaResponse respose) {
					try {
						if(respose.isSuccess())
						{
							List<AreaInfo> concerns = respose.getList();
							if (concerns != null&&concerns.size()>0)
							{
								for(AreaInfo ai:concerns)
								{
									new Delete().from(AreaInfo.class).where("accSid=? and sid=?",account.getSid(),ai.getSid()).execute();
									ai.setAccSid(account.getSid());
									if(ai.getConcernAt()==null)
									{
										ai.setConcernAt(System.currentTimeMillis());
									}
									ai.save();
								}
							}
							AppContext.setCurrentAreaInfo(null);

							if(loadFocusAreaListener!=null)
							{
								loadFocusAreaListener.onLoadFocusAreaDone(true,null);
							}
						}else
						{
							if(loadFocusAreaListener!=null)
							{
								loadFocusAreaListener.onLoadFocusAreaDone(false,respose.getMsg());
							}
						}
					} catch (Exception e) {
						if(loadFocusAreaListener!=null)
						{
							loadFocusAreaListener.onLoadFocusAreaDone(false,e.getMessage());
						}
					}

				}
				@Override
				public void onError(String message) {
					if(loadFocusAreaListener!=null){
						loadFocusAreaListener.onLoadFocusAreaDone(false,message);
					}
				}

				@Override
				public void onOffline(String message) {
					if(loadFocusAreaListener!=null){
						loadFocusAreaListener.onLoadFocusAreaDone(false,message);
					}
				}
			});
		}
	}
	public  interface OnLoadFocusAreaListener
	{
		public void onLoadFocusAreaDone(boolean isSuccess,String message);
	}
	//加载用户关注的商家
	public static void loadSubscribe(Context context,final OnLoadFocusMerchantListener onLoadFocusMerchantListener)
	{
		final Account account=AppContext.currentAccount();
		if(account==null)
		{
			if(onLoadFocusMerchantListener!=null)
			{
				onLoadFocusMerchantListener.onLoadFocusMerchantDone(false,"当前无用户可操作");
			}
			return;
		}
		GetMyConcernMerchantsReq getMyConcernMerchantsReq = new GetMyConcernMerchantsReq();
		getMyConcernMerchantsReq.setAccSid(account.getSid());
		getMyConcernMerchantsReq.setUpadtedAt(AppContext.merchantUpdatedAt());
		RequestBean<MerchantResponse> requestBean = new RequestBean<MerchantResponse>();
		requestBean.setRequestBody(getMyConcernMerchantsReq);
		requestBean.setResponseBody(MerchantResponse.class);
		requestBean.setURL(Constant.Requrl.getConcernMerchantList());
		MeMessageService.instance().requestServer(requestBean, new UICallback<MerchantResponse>() {
			@Override
			public void onSuccess(MerchantResponse respose) {
				try {
					if(respose.isSuccess())
					{
						List<Merchant> concerns = respose.getList();
						if (concerns != null&&concerns.size()>0) {
							new Delete().from(Merchant.class).where("accSid=?",account.getSid()).execute();
							for(Merchant merchant:concerns)
							{
								merchant.setAccSid(account.getSid());//设置商家的所属账号
								merchant.save();
							}
							List<Merchant> listConcerns=AppContext.currentFocusMerchant();
							System.out.println("kkkkkkkkkkkkk-"+listConcerns.size());
						}
						if(onLoadFocusMerchantListener!=null)
						{
							onLoadFocusMerchantListener.onLoadFocusMerchantDone(true,null);
						}
					}else
					{
						if(onLoadFocusMerchantListener!=null)
						{
							onLoadFocusMerchantListener.onLoadFocusMerchantDone(false,respose.getMsg());
						}
					}
				} catch (Exception e) {
					if(onLoadFocusMerchantListener!=null)
					{
						onLoadFocusMerchantListener.onLoadFocusMerchantDone(false,e.getMessage());
					}
				}
			}

			@Override
			public void onError(String message) {
				if(onLoadFocusMerchantListener!=null)
				{
					onLoadFocusMerchantListener.onLoadFocusMerchantDone(false,message);
				}
			}

			@Override
			public void onOffline(String message) {
				if(onLoadFocusMerchantListener!=null)
				{
					onLoadFocusMerchantListener.onLoadFocusMerchantDone(false,message);
				}
			}
		});
	}
	public interface OnLoadFocusMerchantListener
	{
		public void onLoadFocusMerchantDone(boolean isSuccess,String message);
	}


	//取消关注
	public static void cancelConcernArea(final Context mContext,final AreaInfo areaInfo,final OnAreaInfoConcernedListener concernListerner) {
		AreaConcernCancelReq areaConcernCancelReq = new AreaConcernCancelReq();
		areaConcernCancelReq.setAccSid(AppContext.currentAccount().getSid());
		areaConcernCancelReq.setAreaSid(areaInfo.getSid());
		RequestBean<String> requestBean = new RequestBean<String>();
		requestBean.setRequestBody(areaConcernCancelReq);
		requestBean.setResponseBody(String.class);
		requestBean.setURL(Constant.Requrl.getCancelConcernArea());
		ProgressUtil.showProgress(mContext,"正在取消关注");
		MeMessageService.instance().requestServer(requestBean, new UICallback<String>() {
			@Override
			public void onSuccess(String respose) {
				try {
					ProgressUtil.dissmissProgress();
					for(AreaInfo ai:AppContext.currentFocusArea())
					{
						if(ai.getSid()==areaInfo.getSid())
						{
							AppContext.focusArea(ai, false);
							break;
						}
					}
					if(concernListerner != null) {
						concernListerner.onAreaInfoConcerned(areaInfo, false);
					}
				} catch (Exception e) {
					if(concernListerner != null) {
						concernListerner.onAreaInfoConcerned(areaInfo, false);
					}
				}

			}

			@Override
			public void onError(String message) {
				ProgressUtil.dissmissProgress();
				MeUtil.showToast(mContext, message);
			}

			@Override
			public void onOffline(String message) {
				ProgressUtil.dissmissProgress();
				MeUtil.showToast(mContext, message);
			}
		});
	}
	//加载城市下的分类
	public static void loadCityCategory(final Long citySid,final OnLoadCityCategoryListener onLoadCityCategoryListener)
	{
		//final AreaInfo ai=AppContext.currentAreaInfo();
		CategoryReq categoryReq = new CategoryReq();
		categoryReq.setCitySid(citySid);
		categoryReq.setUpdatedAt(AppContext.categoryUpdatedAt(citySid));
		RequestBean<ZoneMainFunctionResponse> requestBean = new RequestBean<ZoneMainFunctionResponse>();
		requestBean.setRequestBody(categoryReq);
		requestBean.setResponseBody(ZoneMainFunctionResponse.class);
		requestBean.setURL(Constant.Requrl.getCategoryList());
		MeMessageService.instance().requestServer(requestBean, new UICallback<ZoneMainFunctionResponse>() {
			@Override
			public void onSuccess(ZoneMainFunctionResponse respose) {
				try {
					if (respose != null && respose.getList() != null) {
						List<Category> listCategorys=AppContext.currentCategorys(citySid);
						List<Category> listUpdats=respose.getList();
						for (Category category : listUpdats) {
							boolean included=false;
							for(Category cat:listCategorys)
							{
								if(category.getSid()==cat.getSid())
								{//原来包含了
									included=true;
									break;
								}
							}
							if(included)
							{//原来的数据中包含了更新的数据
								new Delete().from(Category.class).where("sid=? and citySid=?", category.getSid(),citySid).execute();
								if(Constant.DB_STATUS.STATUS_VALIDATE==category.getStatus())
								{
									category.setCitySid(citySid);
									category.save();
								}
							}else
							{//原来的数据未包含
								if(Constant.DB_STATUS.STATUS_VALIDATE==category.getStatus())
								{//状态又可以用
									category.setCitySid(citySid);
									category.save();
								}
							}
						}
					}
					if(onLoadCityCategoryListener!=null)
					{
						onLoadCityCategoryListener.onLoadCityCategoryDone();
					}
				} catch (Exception e) {
					if(onLoadCityCategoryListener!=null)
					{
						onLoadCityCategoryListener.onLoadCityCategoryDone();
					}
				}

			}

			@Override
			public void onError(String message) {
				if(onLoadCityCategoryListener!=null)
				{
					onLoadCityCategoryListener.onLoadCityCategoryDone();
				}
			}

			@Override
			public void onOffline(String message) {
				if(onLoadCityCategoryListener!=null)
				{
					onLoadCityCategoryListener.onLoadCityCategoryDone();
				}
			}
		});
	}

	public interface OnLoadCityCategoryListener
	{
		public void onLoadCityCategoryDone();
	}

	//加载城市下的栏目列表，栏目从属于分类
	public static void loadCityForums(final Long citySid,final OnCityForumLoadListener onCityForumLoadListener)
	{
		CategoryForumReq cfr=new CategoryForumReq();
		cfr.setCitySid(citySid);
		cfr.setUpdatedAt(AppContext.forumUpdatedAt(citySid));
		RequestBean<ForumResponse> requestBean = new RequestBean<ForumResponse>();
		requestBean.setRequestBody(cfr);
		requestBean.setResponseBody(ForumResponse.class);
		requestBean.setURL(Constant.Requrl.getForumList());
		MeMessageService.instance().requestServer(requestBean, new UICallback<ForumResponse>() {
			@Override
			public void onSuccess(ForumResponse respose) {
				try {
					if (respose != null && respose.getList() != null) {
						List<ForumInfo> listForums=AppContext.getCityForums(citySid);
						List<ForumInfo> listUpdats=respose.getList();
						for (ForumInfo forum : listUpdats) {
							boolean included=false;
							for(ForumInfo cat:listForums)
							{
								if(forum.getSid().longValue()==cat.getSid().longValue())
								{//原来包含了
									included=true;
									break;
								}
							}
							if(included)
							{//原来的数据中包含了更新的数据
								new Delete().from(ForumInfo.class).where("sid=? and citySid=?", forum.getSid(),citySid).execute();
								if(Constant.DB_STATUS.STATUS_VALIDATE==forum.getStatus())
								{
									forum.setCitySid(citySid);
									if(forum.getProduct()!=null)
									{
										ProductInfo product=forum.getProduct();
										forum.setProductSid(product.getSid());
										forum.setCommentCando(product.getCommentCando());
										forum.setCommentCanopen(product.getCommentCanopen());
										forum.setPermissionPersional(product.getPermissionPersional());
									}
									forum.save();
								}
							}else
							{
								if(Constant.DB_STATUS.STATUS_VALIDATE==forum.getStatus())
								{
									forum.setCitySid(citySid);
									if(forum.getProduct()!=null)
									{
										ProductInfo product=forum.getProduct();
										forum.setProductSid(product.getSid());
										forum.setCommentCando(product.getCommentCando());
										forum.setCommentCanopen(product.getCommentCanopen());
										forum.setPermissionPersional(product.getPermissionPersional());
									}
									forum.save();
								}
							}
						}
					}
				} catch (Exception e) {
				}
				if(onCityForumLoadListener!=null)
				{
					onCityForumLoadListener.cityForumLoadDone();
				}
			}

			@Override
			public void onError(String message) {
				if(onCityForumLoadListener!=null)
				{
					onCityForumLoadListener.cityForumLoadDone();
				}
			}

			@Override
			public void onOffline(String message) {
				if(onCityForumLoadListener!=null)
				{
					onCityForumLoadListener.cityForumLoadDone();
				}
			}
		});
	}

	public interface OnCityForumLoadListener
	{
		public void cityForumLoadDone();
	}



	//初始化微博内容
	public static void initWeiboContent(final Context mContext,final CommonHolderView holder,View convertView,final ForumNote item)
	{
		holder.uinfo_ll= convertView.findViewById(R.id.uinfo_ll);
		holder.merchantLogo = (ImageView) convertView.findViewById(R.id.merchantLogo);
		holder.categoryName = (TextView) convertView.findViewById(R.id.categoryName);
		holder.forumName = (TextView) convertView.findViewById(R.id.forumName);
		holder.public_time = (TextView) convertView.findViewById(R.id.public_time);
		holder.name = (TextView) convertView.findViewById(R.id.name);
		holder.merchantTag = (LinearLayout) convertView.findViewById(R.id.merchantTag);
		holder.merchantAddr = (TextView) convertView.findViewById(R.id.merchantAddr);
		holder.focusMerchant = (TextView) convertView.findViewById(R.id.focusMerchant);
		holder.okNumber = (TextView) convertView.findViewById(R.id.ok_number);
		holder.commentNumber = (TextView) convertView.findViewById(R.id.comment_number);
		holder.phone_number = convertView.findViewById(R.id.phone_number);
		holder.ok_number_rl=convertView.findViewById(R.id.ok_number_rl);
		holder.comment_number_rl=convertView.findViewById(R.id.comment_number_rl);
		holder.phone_number_rl=convertView.findViewById(R.id.phone_number_rl);
		holder.view_number = (TextView) convertView.findViewById(R.id.view_number);
		holder.lastCont = (TextView) convertView.findViewById(R.id.lastCont);
		holder.picture_fl = convertView.findViewById(R.id.picture_fl);
		holder.weibo_picture = (ImageView) convertView.findViewById(R.id.weibo_picture);
		holder.usr_sex_img = (ImageView) convertView.findViewById(R.id.usr_sex);
		holder.del_goods = (TextView) convertView.findViewById(R.id.del_goods);
		holder.merchant_star =  convertView.findViewById(R.id.merchant_star);
	}
	private static ConfirmCloseDialog confirmCloseDialog;
	//设置微博内容
	public static void setWeiboContent(final Context mContext,final CommonHolderView holder,View convertView,final ForumNote item,final boolean isInShop)
	{
		OnClickListener onClickListener=new OnClickListener() {
			@Override
			public void onClick(View v) {
				switch(v.getId())
				{
				case R.id.uinfo_ll:
				{
					if(item.getLongItude()!=null&&item.getLongItude().floatValue()>0)
					{//是商家
						Merchant merchant=new Merchant();
						merchant.setSid(item.getAccSid());
						merchant.setAlongAreaSid(item.getAlongAreaSid());
						merchant.setAreaName(item.getAccName());
						merchant.setType(Constant.SUBTYPE.SUBTYPE_MERCHANT);
						mContext.startActivity(new Intent(mContext, UsrShopActivity.class).putExtra(Constant.ActivityExtra.MERCHANT, merchant).putExtra("usr_shop_forumNote", item));
					}else
					{//是用户
						Account account=new Account();
						account.setSid(item.getAccSid());
						account.setAccName(item.getAccName());
						account.setSex(item.getSex());
						AreaInfo ai=new AreaInfo();
						ai.setSid(item.getAlongAreaSid());
						ai.setAreaName(item.getAreaName());
						account.setAreaInfo(ai);
						account.setHeadPicture(item.getHeadPicture());
						mContext.startActivity(new Intent(mContext, UsrZoneActivity.class).putExtra(Constant.ActivityExtra.ACCOUNT, account));
					}
					break;
				}
				case R.id.categoryName:
				{
					Category category=new Category();
					category.setCategoryName(item.getCategoryName());
					category.setSid(item.getCategorySid());
					mContext.startActivity(new Intent(mContext, ForumMerchantActivity.class).putExtra(Constant.ActivityExtra.CATEGORY, category));
					break;
				}
				case R.id.ok_number_rl://点赞
				{	if (item.getLongItude()!=null&&item.getLongItude().floatValue()>0) {
//					Merchant merchant=new Merchant();
//					merchant.setSid(item.getAccSid());
//					merchant.setAccSid(item.getAccSid());
//					merchant.setAlongAreaSid(item.getAlongAreaSid());
//					merchant.setAreaName(item.getAccName());
//					merchant.setType(Constant.SUBTYPE.SUBTYPE_MERCHANT);
					mContext.startActivity(new Intent(mContext, MerchantPraiseListActivity.class).putExtra(Constant.ActivityExtra.MERCHANT, item));
				}else {
					MeUtil.addCommentPraise(mContext, item.getGoodsSid(), item.getAccSid(),null, new OnPraiseListener() {
						@Override
						public void OnPraiseed(boolean isOk,String message) {
							if(isOk)
							{
								MeUtil.showToast(mContext, mContext.getString(R.string.praise_success));
								int oknum=item.getPraiseNumber()+1;
								if(oknum>9999)
								{
									holder.okNumber.setText(mContext.getString(R.string.much_more_view_number));
								}else
								{
									holder.okNumber.setText(""+oknum);
								}

								item.setPraiseNumber(oknum);
							}else
							{
								MeUtil.showToast(mContext, message);
							}
						}
					});
				}
				break;
				}
				case R.id.comment_number_rl:{//评论
					if (item.getLongItude()!=null&&item.getLongItude().floatValue()>0) {//是商家
//						Merchant merchant=new Merchant();
//						merchant.setSid(item.getAccSid());
//						merchant.setAccSid(item.getAccSid());
//						merchant.setAlongAreaSid(item.getAlongAreaSid());
//						merchant.setAreaName(item.getAccName());
//						merchant.setType(Constant.SUBTYPE.SUBTYPE_MERCHANT);
						mContext.startActivity(new Intent(mContext, MerchantCommentListActivity.class).putExtra(Constant.ActivityExtra.MERCHANT, item));
					}else {
						if (isInShop==true) {
							mContext.startActivity(new Intent(mContext, GoodsDetailActivity.class).putExtra(GoodsDetailActivity.MERCHANT, item));
						}else {
							if(item.getLongItude()!=null&&item.getLongItude().floatValue()>0)
							{//是商家
								Merchant merchant=new Merchant();
								merchant.setSid(item.getAccSid());
								merchant.setAlongAreaSid(item.getAlongAreaSid());
								merchant.setAreaName(item.getAccName());
								merchant.setType(Constant.SUBTYPE.SUBTYPE_MERCHANT);
								mContext.startActivity(new Intent(mContext, UsrShopActivity.class).putExtra(Constant.ActivityExtra.MERCHANT, merchant).putExtra("usr_shop_forumNote", item));
							}else
							{//是用户
								Account account=new Account();
								account.setSid(item.getAccSid());
								account.setAccName(item.getAccName());
								account.setSex(item.getSex());
								AreaInfo ai=new AreaInfo();
								ai.setSid(item.getAlongAreaSid());
								ai.setAreaName(item.getAreaName());
								account.setAreaInfo(ai);
								account.setHeadPicture(item.getHeadPicture());
								mContext.startActivity(new Intent(mContext, UsrZoneActivity.class).putExtra(Constant.ActivityExtra.ACCOUNT, account));
							}
						}
					}
					break;
				}
				case R.id.lastCont:
				case R.id.picture_fl:
				{	if (isInShop==true) {
					mContext.startActivity(new Intent(mContext, GoodsDetailActivity.class).putExtra(GoodsDetailActivity.MERCHANT, item));
				}else {
					if(item.getLongItude()!=null&&item.getLongItude().floatValue()>0)
					{//是商家
						Merchant merchant=new Merchant();
						merchant.setSid(item.getAccSid());
						merchant.setAlongAreaSid(item.getAlongAreaSid());
						merchant.setAreaName(item.getAccName());
						merchant.setType(Constant.SUBTYPE.SUBTYPE_MERCHANT);
						mContext.startActivity(new Intent(mContext, UsrShopActivity.class).putExtra(Constant.ActivityExtra.MERCHANT, merchant).putExtra("usr_shop_forumNote", item));
					}else
					{//是用户
						Account account=new Account();
						account.setSid(item.getAccSid());
						account.setAccName(item.getAccName());
						account.setSex(item.getSex());
						AreaInfo ai=new AreaInfo();
						ai.setSid(item.getAlongAreaSid());
						ai.setAreaName(item.getAreaName());
						account.setAreaInfo(ai);
						account.setHeadPicture(item.getHeadPicture());
						mContext.startActivity(new Intent(mContext, UsrZoneActivity.class).putExtra(Constant.ActivityExtra.ACCOUNT, account));
					}
				}
				break;
				}
				case R.id.phone_number_rl:
				{
					if(item.getLatItude()!=null&&item.getLatItude().doubleValue()>0)
					{//商家
						if(NullUtil.isStrNotNull(item.getTelphone()))
						{
							Intent phoneIntent = new Intent("android.intent.action.CALL", Uri.parse("tel:" + item.getTelphone())); 
							mContext.startActivity(phoneIntent);// 启动 
						}else
						{
							showToast(mContext,mContext.getString(R.string.have_no_merchant_telphone));
						}
					}else
					{//个人则私信
						Account currentAcc=AppContext.currentAccount();
						if(!currentAcc.getIsUsrLogin())
						{
							MeUtil.showToast(mContext,mContext.getString(R.string.need_login_first));
							//MeLifeMainActivity.instance().onMenuSelected(MeLifeMainActivity.instance().my_zone.getIndex());
							return;
						}
						if(currentAcc.getSid()==item.getAccSid())
						{//是自己本人
							showToast(mContext, mContext.getString(R.string.can_not_send_message_to_self));
							return;
						}
						Account account=new Account();
						account.setSid(item.getAccSid());
						account.setAccName(item.getAccName());
						account.setSex(item.getSex());
						AreaInfo ai=new AreaInfo();
						ai.setSid(item.getAlongAreaSid());
						ai.setAreaName(item.getAreaName());
						account.setAreaInfo(ai);
						account.setHeadPicture(item.getHeadPicture());
						mContext.startActivity(new Intent(mContext, PrivateMessageActivity.class).putExtra(Constant.ActivityExtra.ACCOUNT, account));
					}

					break;
				}
				case R.id.merchantAddr:
				{
					Merchant merchant=new Merchant();
					merchant.setSid(item.getAccSid());
					merchant.setAlongAreaSid(item.getAlongAreaSid());
					merchant.setAreaName(item.getAccName());
					if(item.getLongItude()!=null&&item.getLatItude()!=null)
					{
						merchant.setLongItude(item.getLongItude().doubleValue());
						merchant.setLatItude(item.getLatItude().doubleValue());
					}
					merchant.setMerchantName(item.getAccName());
					mContext.startActivity(new Intent(mContext, MerchantMapActivity.class).putExtra(Constant.ActivityExtra.MERCHANT, merchant));
					break;
				}
				case R.id.focusMerchant:
				{
					Merchant merchant=new Merchant();
					merchant.setSid(item.getAccSid());
					merchant.setAlongAreaSid(item.getAlongAreaSid());
					merchant.setAreaName(item.getAreaName());
					if(item.getLongItude()!=null&&item.getLongItude().floatValue()>0)
					{
						merchant.setType(Constant.SUBTYPE.SUBTYPE_MERCHANT);
					}else
					{
						merchant.setType(Constant.SUBTYPE.SUBTYPE_USR);
					}

					concernMerchant(mContext,merchant,new ConcernResultListener() {
						@Override
						public void concernResult(boolean isConcern, boolean result) {
							if(result)
							{
								if(isConcern)
								{
									holder.focusMerchant.setSelected(true);
									holder.focusMerchant.setText(R.string.have_concern);
								}else
								{
									holder.focusMerchant.setSelected(false);
									holder.focusMerchant.setText(R.string.add_concern);
								}
							}
						}
					});
					break;
				}
				case R.id.del_goods:
				{//删除商品
					confirmCloseDialog = new ConfirmCloseDialog(mContext, "\n是否删除该记录?\n", "删除记录", "删除", "取消", new FinishCallback() {
						@Override
						public void confirmThisOperation() {
							confirmCloseDialog.dismiss();
							confirmCloseDialog = null;
							final Account me=AppContext.currentAccount();
							if(me!=null)
							{
								//删除联系人
								DelGoodsReq req=new DelGoodsReq();
								req.setMevalue(me.getMevalue());
								req.setGoodsSid(item.getGoodsSid());
								RequestBean<NormalResponse> requestBean = new RequestBean<NormalResponse>();
								requestBean.setRequestBody(req);
								requestBean.setResponseBody(NormalResponse.class);
								requestBean.setURL(Constant.Requrl.getDelGoods());
								if(MeLifeMainActivity.isInstanciated())
								{
									MeMessageService.instance().requestServer(requestBean, new UICallback<NormalResponse>() {
										@Override
										public void onSuccess(NormalResponse respose) {
											try {
												if(respose.isSuccess())
												{
													Intent intent = new Intent();  
													intent.setAction(Constant.BroadType.GOODS_DELETED);  
													//intent.putExtra("msg", "接收静态注册广播成功！");
													mContext.sendBroadcast(intent);
												}else
												{
													MeUtil.showToast(mContext, respose.getMsg());
												}
											} catch (Exception e) {
											}
										}
										@Override
										public void onError(String message) {
											MeUtil.showToast(mContext, message);
										}

										@Override
										public void onOffline(String message) {
											MeUtil.showToast(mContext, message);
										}
									});
								}
							}
						}

						@Override
						public void cancle() {
							confirmCloseDialog.dismiss();
							confirmCloseDialog = null;
						}
					});
					confirmCloseDialog.show();
					break;
				}
				}
			}
		};


		if(holder.uinfo_ll!=null&&NullUtil.isStrNotNull(item.getAccName()))
		{//含有发贴者的信息
			holder.uinfo_ll.setTag(item);
			holder.uinfo_ll.setOnClickListener(onClickListener);
		}
		if(NullUtil.isStrNotNull(item.getCategoryName()))
		{//含有分类内容
			if(holder.categoryName!=null)
			{
				holder.categoryName.setVisibility(View.VISIBLE);
				holder.categoryName.setText(item.getCategoryName());
				//				holder.categoryName.setTag(item);
				//				holder.categoryName.setOnClickListener(onClickListener);
			}
			if(holder.forumName!=null)
			{
				holder.forumName.setVisibility(View.GONE);
			}
		}else
		{//无分类内容
			if(holder.categoryName!=null)
			{
				holder.categoryName.setVisibility(View.GONE);
			}
			if(NullUtil.isStrNotNull(item.getAlongForumName()))
			{
				if(holder.forumName!=null)
				{
					holder.forumName.setVisibility(View.VISIBLE);
					holder.forumName.setText(item.getAlongForumName());
				}
			}else
			{
				if(holder.forumName!=null)
				{
					holder.forumName.setVisibility(View.GONE);
				}
			}
		}
		if(NullUtil.isStrNotNull(item.getAreaName())&&holder!=null&&holder.merchantAddr!=null)
		{
			String come_from=mContext.getString(R.string.come_from);
			holder.merchantAddr.setText(come_from+item.getAreaName());
			holder.merchantAddr.setTag(item);
			if(item.getLatItude()!=null&&item.getLatItude().doubleValue()>0)
			{
				holder.merchantAddr.setOnClickListener(onClickListener);
			}
		}

		if(holder.focusMerchant!=null)
		{
			int subType=Constant.SUBTYPE.SUBTYPE_USR;
			if(item.getLatItude()!=null&&item.getLatItude().doubleValue()>0)
			{
				subType=Constant.SUBTYPE.SUBTYPE_MERCHANT;
			}
			if(AppContext.hasFocusMerchant(item.getAccSid(),subType))
			{//忆关注该商家
				holder.focusMerchant.setSelected(true);
				holder.focusMerchant.setText(R.string.have_concern);
			}else
			{
				holder.focusMerchant.setSelected(false);
				holder.focusMerchant.setText(R.string.add_concern);
			}
			holder.focusMerchant.setTag(item);
			holder.focusMerchant.setOnClickListener(onClickListener);
		}
		if(holder.ok_number_rl!=null)
		{
			holder.ok_number_rl.setTag(item);
			holder.ok_number_rl.setOnClickListener(onClickListener);
		}
		if(holder.comment_number_rl!=null)
		{
			holder.comment_number_rl.setTag(item);
			holder.comment_number_rl.setOnClickListener(onClickListener);
		}
		if(holder.phone_number_rl!=null)
		{
			holder.phone_number_rl.setTag(item);
			holder.phone_number_rl.setOnClickListener(onClickListener);
			if(item.getLatItude()!=null&&item.getLatItude().doubleValue()>0)
			{//说明是商家
				((ImageView)holder.phone_number).setImageResource(R.drawable.telphone);
			}else
			{//说明是用户,则私信
				((ImageView)holder.phone_number).setImageResource(R.drawable.msg);
			}
		}
		MyApplication myapp=(MyApplication)mContext.getApplicationContext();
		if(holder.merchantLogo!=null)
		{
			if(NullUtil.isStrNotNull(item.getHeadPicture()))
			{
				MeImageLoader.displayImage(item.getHeadPicture(), holder.merchantLogo, myapp.getHeadIconOption());
			}else
			{
				if(item.getLatItude()!=null&&item.getLatItude().doubleValue()>0)
				{//是商家
					holder.merchantLogo.setImageResource(R.drawable.merchant_logo);
				}else
				{//是个人
					if(Constant.SEX.SEX_WOMAN==item.getSex())
					{
						holder.merchantLogo.setImageResource(R.drawable.woman_selected);
					}else
					{
						holder.merchantLogo.setImageResource(R.drawable.man_selected);
					}
				}
			}
		}
		if(holder.name!=null)
		{
			holder.name.setText(item.getAccName());
		}

		if(NullUtil.isStrNotNull(item.getMerchantTagNameArray())&&holder.merchantTag!=null)
		{
			holder.merchantTag.removeAllViews();
			LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
			String[] tagArray=item.getMerchantTagNameArray().split(",");
			for(String str:tagArray)
			{
				TextView tv=(TextView)LayoutInflater.from(mContext).inflate(R.layout.tag_button, null);
				tv.setText(str);
				lp.setMargins(3, 0, 3, 0);  
				tv.setLayoutParams(lp);  
				holder.merchantTag.addView(tv);
			}
		}

		if(holder.public_time!=null&&item.getCreateDate()>100)
		{
			holder.public_time.setText(BaseTools.getChajuDate(item.getCreateDate()));
		}
		if(holder.lastCont!=null&&NullUtil.isStrNotNull(item.getGoodsContent()))
		{
			holder.lastCont.setText(null);
			SpannableString spannableString = FaceConversionUtil.getInstance().getExpressionString(mContext, item.getGoodsContent());
			holder.lastCont.append(spannableString);
			holder.lastCont.setTag(item);
			holder.lastCont.setOnClickListener(onClickListener);
		}
		if(holder.picture_fl!=null)
		{
			String goodsPhotoArray=item.getGoodsPhotoArray();
			if(NullUtil.isStrNotNull(goodsPhotoArray))
			{
				if(holder.weibo_picture!=null)
				{
					int[] picture_size=SizeUtil.weibo_picture_size(mContext);
					holder.weibo_picture.getLayoutParams().width=picture_size[0];
					holder.weibo_picture.getLayoutParams().height=picture_size[1];
					holder.picture_fl.setVisibility(View.VISIBLE);
					holder.picture_fl.setTag(item);
					holder.picture_fl.setOnClickListener(onClickListener);
					String[] pics=goodsPhotoArray.split(",");
					MeImageLoader.displayImage(pics[0], holder.weibo_picture, myapp.getWeiIconOption());
				}else
				{
					holder.picture_fl.setVisibility(View.GONE);
				}
			}else
			{
				holder.picture_fl.setVisibility(View.GONE);
			}
		}
		if(holder.okNumber!=null)
		{
			int tempNumber=item.getPraiseNumber();
			if(tempNumber>9999)
			{
				holder.okNumber.setText(mContext.getString(R.string.much_more_view_number));
			}else
			{
				holder.okNumber.setText(""+tempNumber);
			}
		}
		if(holder.commentNumber!=null)
		{
			int tempNumber=item.getCommentNumber();
			if(tempNumber>9999)
			{
				holder.commentNumber.setText(mContext.getString(R.string.much_more_view_number));
			}else
			{
				holder.commentNumber.setText(""+tempNumber);
			}
		}
		if(holder.view_number!=null)
		{
			int tempNumber=item.getViewNumber();
			if(tempNumber>9999)
			{
				holder.view_number.setText(mContext.getString(R.string.view_number)+mContext.getString(R.string.much_more_view_number));
			}else
			{
				holder.view_number.setText(mContext.getString(R.string.view_number)+tempNumber);
			}
		}
		if(item.getLongItude()!=null&&item.getLongItude().floatValue()>0)
		{//是商家，则隐藏性别
			if(holder.usr_sex_img!=null)
			{
				holder.usr_sex_img.setVisibility(View.GONE);
			}

		}else
		{//是用户则显示性别
			if(holder.usr_sex_img!=null)
			{
				holder.usr_sex_img.setVisibility(View.VISIBLE);
				int sex =item.getSex();
				if(sex==Constant.SEX.SEX_MAN)
				{
					holder.usr_sex_img.setImageResource(R.drawable.man_selected);
				}else if(sex==Constant.SEX.SEX_WOMAN)
				{
					holder.usr_sex_img.setImageResource(R.drawable.woman_selected);
				}
			}
		}
		if(holder.del_goods!=null)
		{
			Account currentAcc=AppContext.currentAccount();
			if(currentAcc!=null)
			{
				if(currentAcc.getSid().longValue()==item.getAccSid().longValue())
				{//是自己本人
					holder.del_goods.setVisibility(View.VISIBLE);
					holder.del_goods.setOnClickListener(onClickListener);
				}else
				{
					holder.del_goods.setVisibility(View.GONE);
				}
			}else
			{
				holder.del_goods.setVisibility(View.GONE);
			}
		}
		if(holder.merchant_star!=null)
		{
			try {
				if(item.getLatItude()!=null&&item.getLatItude().doubleValue()>0
						&&item.getMerchantType().intValue()==Constant.MERCHANT_TYPE.NORMAL_MERCHANT
						&&item.getVipLevel().intValue()>0)
				{//是商家
					holder.merchant_star.setVisibility(View.VISIBLE);
				}else
				{
					holder.merchant_star.setVisibility(View.GONE);
				}
			} catch (Exception e) {
				holder.merchant_star.setVisibility(View.GONE);
			}

		}
	}

	//关注/取消关注
	public static void concernMerchant(final Context mContext,final Merchant merchant,final ConcernResultListener cl) {
		Account account=AppContext.currentAccount();
		if(account.getSid()==merchant.getSid())
		{//不能关注自己
			return;
		}
		ConcernMerchantReq concernMerchantReq = new ConcernMerchantReq();
		concernMerchantReq.setAccSid(account.getSid());
		concernMerchantReq.setMerchantSid(merchant.getSid());
		concernMerchantReq.setSubType(merchant.getType());
		concernMerchantReq.setAreaSid(merchant.getAlongAreaSid());
		concernMerchantReq.setAreaName(merchant.getAreaName());
		RequestBean<ConcernMerchantResponse> requestBean = new RequestBean<ConcernMerchantResponse>();
		requestBean.setRequestBody(concernMerchantReq);
		requestBean.setResponseBody(ConcernMerchantResponse.class);
		if(AppContext.hasFocusMerchant(merchant.getSid(),merchant.getType()))
		{//已关注
			requestBean.setURL(Constant.Requrl.getCancelConcernMerchant());
		}else
		{
			requestBean.setURL(Constant.Requrl.getConcernMerchant());
		}
		MeMessageService.instance().requestServer(requestBean, new UICallback<ConcernMerchantResponse>() {

			@Override
			public void onSuccess(ConcernMerchantResponse respose) {
				try {
					if (respose.isSuccess()) {
						if(AppContext.hasFocusMerchant(merchant.getSid(),merchant.getType()))
						{
							AppContext.focusMerchant(merchant, false);
							//showToast(mContext.getString(R.string.cancel_concern_success));
							if(cl!=null)
							{
								cl.concernResult(false, true);
							}
						}else
						{//关注结果
							Merchant mer=respose.getMerchant();
							mer.setAlongAreaSid(merchant.getAlongAreaSid());
							mer.setAreaName(merchant.getAreaName());
							mer.setType(merchant.getType());
							mer.setSid(merchant.getSid());
							AppContext.focusMerchant(mer,true);
							//showToast(mContext.getString(R.string.concern_success));
							if(cl!=null)
							{
								cl.concernResult(true, true);
							}
						}
					} else {
						if(AppContext.hasFocusMerchant(merchant.getSid(),merchant.getType()))
						{
							showToast(mContext,mContext.getString(R.string.cancel_concern_failure)+":"+respose.getMsg());
							if(cl!=null)
							{
								cl.concernResult(false, false);
							}
						}else
						{
							showToast(mContext,mContext.getString(R.string.concern_failure)+":"+respose.getMsg());
							if(cl!=null)
							{
								cl.concernResult(true, false);
							}
						}
					}
				} catch (Exception e) {
					showToast(mContext,mContext.getString(R.string.concern_failure)+":"+e.getMessage());
				}
				//ProgressUtil.dissmissProgress();
			}

			@Override
			public void onError(String message) {
				showToast(mContext,message);
				ProgressUtil.dissmissProgress();
			}

			@Override
			public void onOffline(String message) {
				ProgressUtil.dissmissProgress();
				showToast(mContext,message);
			}
		});
	}

	public interface ConcernResultListener{
		public void concernResult(boolean isConcern,boolean result);
	}

	//	public static void setSwipeLayoutColor(SwipeRefreshLayout mSwipeLayout)
	//	{
	//		mSwipeLayout.setColor(android.R.color.holo_green_light,android.R.color.holo_green_dark,android.R.color.holo_blue_light,android.R.color.holo_blue_dark);
	//	}

}
