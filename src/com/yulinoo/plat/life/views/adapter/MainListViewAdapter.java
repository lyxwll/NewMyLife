package com.yulinoo.plat.life.views.adapter;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.yulinoo.plat.life.MyApplication;
import com.yulinoo.plat.life.bean.Account;
import com.yulinoo.plat.life.bean.Advertise;
import com.yulinoo.plat.life.bean.AreaInfo;
import com.yulinoo.plat.life.bean.ConcernNumber;
import com.yulinoo.plat.life.bean.ForumNote;
import com.yulinoo.plat.life.bean.MessageBox;
import com.yulinoo.plat.life.net.callback.UICallback;
import com.yulinoo.plat.life.net.reqbean.BeanDetailReq;
import com.yulinoo.plat.life.net.resbean.GoodsDetailResponse;
import com.yulinoo.plat.life.net.service.RequestBean;
import com.yulinoo.plat.life.service.MeMessageService;
import com.yulinoo.plat.life.service.MeMessageService.OnMessageBoxArrivedListener;
import com.yulinoo.plat.life.utils.Constant;
import com.yulinoo.plat.life.utils.MeImageLoader;
import com.yulinoo.plat.life.utils.MeUtil;
import com.yulinoo.plat.life.utils.SizeUtil;
import com.yulinoo.plat.life.utils.MeUtil.OnLoadFocusMerchantListener;
import com.yulinoo.plat.life.views.activity.AroundCategoryActivity;
import com.yulinoo.plat.life.views.activity.ConcernActivity;
import com.yulinoo.plat.life.views.activity.GoodsDetailActivity;
import com.yulinoo.plat.life.views.activity.IndexActivity;
import com.yulinoo.plat.life.views.activity.LoginActivity;
import com.yulinoo.plat.life.views.activity.MeLifeMainActivity;
import com.yulinoo.plat.life.views.activity.MyAreaActivity;
import com.yulinoo.plat.life.views.activity.NeighbourTalkActivity;
import com.yulinoo.plat.life.views.activity.NewZoneSelectActivity;
import com.yulinoo.plat.life.views.activity.UsrZoneActivity;
import com.yulinoo.plat.melife.R;

import config.AppContext;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;

public class MainListViewAdapter extends YuLinooLoadAdapter<Advertise> implements OnClickListener ,OnMessageBoxArrivedListener{

	private LayoutInflater inflater;
	private MyApplication myapp;
	private ListView mListView;
	private MeLifeMainActivity activity;
	private ImageView mainAdvertise;

	private ImageView neibourRh;// 邻里之间的红点
	private ImageView concernRh;// 我的关注的红点
	private ImageView usr_zoneRh;// 个人中心的红点
	private boolean rh_isShowing = false;

	public MainListViewAdapter(MeLifeMainActivity activity,ListView mListView) {
		this.mListView=mListView;
		this.activity=activity;
		this.inflater = LayoutInflater.from(activity.getApplicationContext());
		this.myapp = (MyApplication) activity.getApplicationContext();
		mListView.setAdapter(this);
		if (MeMessageService.isReady()) {
			MeMessageService.instance().addOnMessageBoxArrivedListener(this);
		}
	}

	public static interface IViewType {
		int HEADER = 0;// 头部
		int FOOTER = 1;// 下面的广告部分
	}

	@Override
	public int getItemViewType(int position) {
		Advertise item = getItem(position);
		if (item != null) {
			return IViewType.FOOTER;
		} else {
			return IViewType.HEADER;
		}
	}

	@Override
	public int getViewTypeCount() {
		return 2;
	}

	@Override
	public View getView(int position, View convertview, ViewGroup parent) {
		Holder holder;
		final Advertise advertise=getItem(position);
		if (convertview==null) {
			holder=new Holder();
			if (position==0) {//头部
				convertview=inflater.inflate(R.layout.main_listview_header, null);
				holder.function=convertview.findViewById(R.id.main_function);
				holder.around=convertview.findViewById(R.id.around_rl);
				holder.neibour=convertview.findViewById(R.id.neibour_rl);
				holder.neibour_rh=neibourRh=(ImageView) convertview.findViewById(R.id.neibour_rh);
				holder.index=convertview.findViewById(R.id.index_rl);
				holder.concern=convertview.findViewById(R.id.concern_rl);
				holder.concern_rh=concernRh=(ImageView) convertview.findViewById(R.id.concern_rh);
				holder.usr_zone=convertview.findViewById(R.id.usr_zone_rl);
				holder.usr_zone_rh=usr_zoneRh=(ImageView) convertview.findViewById(R.id.usr_zone_rh);
				holder.myArea=convertview.findViewById(R.id.my_area_ll);

				holder.around.setOnClickListener(this);
				holder.concern.setOnClickListener(this);
				holder.index.setOnClickListener(this);
				holder.neibour.setOnClickListener(this);
				holder.usr_zone.setOnClickListener(this);
				holder.myArea.setOnClickListener(this);
			}else {
				convertview=inflater.inflate(R.layout.main_advertise_item, parent,false);
				mainAdvertise=(ImageView) convertview.findViewById(R.id.main_advertise_iv);
				int[] sizes=SizeUtil.getAdvertiseSizeNoMargin(activity);
				mainAdvertise.getLayoutParams().width=sizes[0];
				mainAdvertise.getLayoutParams().height=sizes[1];
				String advertiseUrl=advertise.getUrl();
				if (advertiseUrl!=null) {
					MeImageLoader.displayImage(advertiseUrl, mainAdvertise, myapp.getWeiIconOption());
				}
				mainAdvertise.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						BeanDetailReq req=new BeanDetailReq();
						req.setSid(advertise.getAlongGoodsSid());
						RequestBean<GoodsDetailResponse> requestBean = new RequestBean<GoodsDetailResponse>();
						requestBean.setRequestBody(req);
						requestBean.setResponseBody(GoodsDetailResponse.class);
						requestBean.setURL(Constant.Requrl.getGoodsDetail());
						MeMessageService.instance().requestServer(requestBean, new UICallback<GoodsDetailResponse>() {
							@Override
							public void onSuccess(GoodsDetailResponse respose) {
								try {
									if(respose.isSuccess())
									{
										ForumNote forumNote=respose.getForumNote();
										if(forumNote!=null)
										{
											activity.startActivity(new Intent(activity, GoodsDetailActivity.class)
											.putExtra(GoodsDetailActivity.MERCHANT, forumNote));
										}
									}else
									{
										MeUtil.showToast(activity,respose.getMsg());
									}
								} catch (Exception e) {
								}
							}

							@Override
							public void onError(String message) {
								MeUtil.showToast(activity,message);
							}

							@Override
							public void onOffline(String message) {
								MeUtil.showToast(activity,message);
							}
						});
					}
				});
			}
			convertview.setTag(holder);
		}else {
			holder=(Holder) convertview.getTag();
		}
		return convertview;
	}

	class Holder {
		public View function;
		public View around;// 周边生活
		public View neibour;// 邻里之间
		public ImageView neibour_rh;//邻里之间的红点
		public View index;// 生活百科
		public View concern;// 我的关注
		public ImageView concern_rh;//我的关注的红点
		public View usr_zone;// 个人中心
		public ImageView usr_zone_rh;//个人中心的红点
		public View myArea;
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.usr_zone_rl:{//个人中心
			Account account = AppContext.currentAccount();
			if (account != null && account.getIsUsrLogin()) {// 用户已登录
				if (AppContext.currentFocusArea() != null && AppContext.currentFocusArea().size() > 0) {//用户关注了小区
					AreaInfo ai = AppContext.currentFocusArea().get(0);
					account.setAreaInfo(ai);
					activity.startActivity(new Intent(activity,UsrZoneActivity.class).putExtra(
							Constant.ActivityExtra.ACCOUNT, account).putExtra(
									"rh_isShowing", rh_isShowing));
				} else {
					activity.startActivity(new Intent(activity, LoginActivity.class));
				}
			} else {
				activity.startActivity(new Intent(activity, LoginActivity.class));
			}
			//activity.overridePendingTransition(R.anim.slide_left_in,R.anim.slide_right_out);
			break;
		}
		case R.id.around_rl:{//周边生活
			if (AppContext.currentAreaInfo()==null) {
				MeUtil.showToast(activity, activity.getResources().getString(R.string.needconcerarea));
				activity.startActivity(new Intent(activity, NewZoneSelectActivity.class));
			}else {
				activity.startActivity(new Intent(activity, AroundCategoryActivity.class));
				//activity.overridePendingTransition(R.anim.slide_left_in,R.anim.slide_right_out);
			}
			break;
		}
		case R.id.neibour_rl:{//邻里之间
			if (neibourRh!=null && neibourRh.getVisibility()==View.VISIBLE) {
				neibourRh.setVisibility(View.GONE);
			}
			if (AppContext.currentAreaInfo()==null) {
				MeUtil.showToast(activity, activity.getResources().getString(R.string.needconcerarea));
				activity.startActivity(new Intent(activity, NewZoneSelectActivity.class));
			}else {
				activity.startActivity(new Intent(activity, NeighbourTalkActivity.class));
			}
			break;
		}
		case R.id.my_area_ll:{//我的小区
			if (AppContext.currentAreaInfo()!=null) {
				activity.startActivity(new Intent(activity, MyAreaActivity.class));
			}else {
				MeUtil.showToast(activity, activity.getResources().getString(R.string.needconcerarea));
				activity.startActivity(new Intent(activity, NewZoneSelectActivity.class));
			}
			break;
		}
		case R.id.index_rl:{//生活百科
			if (AppContext.currentAreaInfo()!=null) {//如果有小区信息
				activity.startActivity(new Intent(activity, IndexActivity.class));
				//activity.overridePendingTransition(R.anim.slide_left_in,R.anim.slide_right_out);
			}else {
				MeUtil.showToast(activity, activity.getResources().getString(R.string.needconcerarea));
				activity.startActivity(new Intent(activity, NewZoneSelectActivity.class));
			}
			break;
		}
		case R.id.concern_rl:{//我的关注
			Account account=AppContext.currentAccount();
			if (account!=null && account.getIsUsrLogin()) {
				activity.startActivity(new Intent(activity, ConcernActivity.class));
				//activity.overridePendingTransition(R.anim.slide_left_in,R.anim.slide_right_out);
			}else {
				MeUtil.showToast(activity, "请先登录");
				activity.startActivity(new Intent(activity, LoginActivity.class));
			}
			break;
		}
		}
	}

	@Override
	public void onMessageBoxArrived(MessageBox box) {
		int chatBoxNumber = box.getChatBoxNumber();
		if (chatBoxNumber > 0) {// 有聊天室的记录
			neibourRh.setVisibility(View.VISIBLE);
		} else {
			neibourRh.setVisibility(View.GONE);
		}

		int letterNumber = box.getLetterNumber();
		if (letterNumber > 0) {//关注有记录
			concernRh.setVisibility(View.VISIBLE);
		}else
		{

		}

		int commentNumber = box.getCommentNumber();
		int praiseNumber = box.getPraiseNumber();
		if (commentNumber == 0 && praiseNumber == 0) {//个人中心
			if (usr_zoneRh.getVisibility() == View.VISIBLE) {
				usr_zoneRh.setVisibility(View.GONE);
				rh_isShowing = false;
			}
		} else {
			if (usr_zoneRh.getVisibility() == View.GONE) {
				usr_zoneRh.setVisibility(View.VISIBLE);
				rh_isShowing = true;
			}
		}
		if (box.getAttUpdate() != null) {// 说明有关注的商家/好友有状态变更
			concernRh.setVisibility(View.VISIBLE);
			//ConcernActivity.instance().setConcernAttUpdate(true);
			//ConcernActivity.instance().setMessageBox(box);
			setSubscribeNumber(box.getAttUpdate());
		}else
		{
			if(letterNumber==0)
			{
				concernRh.setVisibility(View.GONE);
			}
		}
	}

	//订阅的账号有数量更新
	public void setSubscribeNumber(final Map<Long,Integer> map)
	{
		final Account account=AppContext.currentAccount();
		if(account!=null)
		{
			//更新关注的好友信息
			MeUtil.loadSubscribe(activity, new OnLoadFocusMerchantListener() {
				@Override
				public void onLoadFocusMerchantDone(boolean isSuccess, String message) {
					Iterator<Entry<Long, Integer>> it=map.entrySet().iterator();
					while(it.hasNext())
					{
						Entry<Long, Integer> entry=it.next();
						ConcernNumber.setConcernNumberReaded(account.getSid(), entry.getKey());
						ConcernNumber cn=new ConcernNumber();
						cn.setAlongSid(account.getSid());
						cn.setConcernAccSid(entry.getKey());
						cn.setNumber(entry.getValue());
						cn.save();
					}
				}
			});
		}
	}
}
