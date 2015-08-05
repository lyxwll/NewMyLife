package com.yulinoo.plat.life.views.activity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.yulinoo.plat.life.bean.AreaInfo;
import com.yulinoo.plat.life.net.callback.UICallback;
import com.yulinoo.plat.life.net.reqbean.WeatherReq;
import com.yulinoo.plat.life.net.resbean.WeatherRespone;
import com.yulinoo.plat.life.net.service.RequestBean;
import com.yulinoo.plat.life.service.MeMessageService;
import com.yulinoo.plat.life.utils.Constant;
import com.yulinoo.plat.life.utils.Constant.HTTP_DATA_FORMAT;
import com.yulinoo.plat.life.utils.Constant.HTTP_METHOD;
import com.yulinoo.plat.life.utils.MeUtil;
import com.yulinoo.plat.melife.R;

import config.AppContext;

public class WeatherMoreFunction implements OnClickListener{

	private Context context;
	private LayoutInflater inflater;
	private View position;

	//private View popView;
	private TextView todayWeather;
	private TextView tomorrowWeather;
	private TextView afterWeather;
	private Animation push_top_out=null;
	private Animation push_top_in=null;
	public WeatherMoreFunction(Context context, LayoutInflater inflater,View position) {
		this.context = context;
		this.inflater = inflater;
		this.position = position;
		//		popView = inflater.inflate(R.layout.weather_more_function_layout,null);
		//		todayWeather=(TextView) popView.findViewById(R.id.today_weather_tv);
		//		tomorrowWeather=(TextView) popView.findViewById(R.id.tomorrow_weather_tv);
		//		afterWeather=(TextView) popView.findViewById(R.id.after_tomorrow_weather_tv);

		push_top_out=AnimationUtils.loadAnimation(context, R.anim.push_top_out);
		push_top_in=AnimationUtils.loadAnimation(context, R.anim.push_top_in);
	}

	private PopupWindow weatherPopupWindow;

	//	public void showWeatherPopupWindow() {
	//		weatherPopupWindow = new PopupWindow(popView,ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT, true);
	//		// 设置点击窗口外边窗口消失
	//		weatherPopupWindow.setOutsideTouchable(false);
	//		// 设置此参数获得焦点，否则无法点击
	//		weatherPopupWindow.setFocusable(true);
	//		popView.setFocusable(true);// comment by danielinbiti,设置view能够接听事件，标注1
	//		popView.setFocusableInTouchMode(true); // comment by danielinbiti,设置view能够接听事件 标注2
	//		popView.setOnKeyListener(new OnKeyListener() {
	//			@Override
	//			public boolean onKey(View arg0, int arg1, KeyEvent arg2) {
	//				if (arg1 == KeyEvent.KEYCODE_BACK) {
	//					if (weatherPopupWindow != null) {
	//						weatherPopupWindow.dismiss();
	//					}
	//				}
	//				return false;
	//			}
	//		});
	//		// 点击其他地方消失
	//		popView.setOnTouchListener(new OnTouchListener() {
	//			@Override
	//			public boolean onTouch(View v, MotionEvent event) {
	//				if (weatherPopupWindow != null && weatherPopupWindow.isShowing()) {
	//					weatherPopupWindow.dismiss();
	//					weatherPopupWindow = null;
	//				}
	//				return false;
	//			}
	//		});
	//		weatherPopupWindow.showAsDropDown(position, 0, 0);
	//	}
	private TextView today_weather_c;
	private TextView today_weather_f;
	private TextView tomorrow_c;
	private TextView tomorrow_f;
	private TextView after_c;
	private TextView after_f;
	private View weather_more;
	//private View more_weather;
	private ImageView more_weather_pic;
	public void getWeather(final View weahter_ll)
	{
		weahter_ll.setVisibility(View.GONE);
		today_weather_c=(TextView)weahter_ll.findViewById(R.id.today_weather_c);
		today_weather_f=(TextView)weahter_ll.findViewById(R.id.today_weather_f);
		tomorrow_c=(TextView)weahter_ll.findViewById(R.id.tomorrow_c);
		tomorrow_f=(TextView)weahter_ll.findViewById(R.id.tomorrow_f);
		after_c=(TextView)weahter_ll.findViewById(R.id.after_c);
		after_f=(TextView)weahter_ll.findViewById(R.id.after_f);
		weather_more=weahter_ll.findViewById(R.id.weather_more);
		weather_more.setVisibility(View.GONE);
		//more_weather=weahter_ll.findViewById(R.id.more_weather);
		weahter_ll.setOnClickListener(this);
		more_weather_pic=(ImageView)weahter_ll.findViewById(R.id.more_weather_pic);

		AreaInfo areaInfo=AppContext.currentAreaInfo();
		if (areaInfo!=null) {
			WeatherReq req=new WeatherReq();
			req.setCityId(areaInfo.getAlongCitySid());
			req.setDistrictId(areaInfo.getAlongDistrictSid());
			RequestBean<WeatherRespone> requestBean=new RequestBean<WeatherRespone>();
			requestBean.setRequestBody(req);
			requestBean.setHttpMethod(HTTP_METHOD.POST);
			requestBean.setRequsetFormat(HTTP_DATA_FORMAT.FORM);
			requestBean.setResponseBody(WeatherRespone.class);
			requestBean.setURL(Constant.Requrl.getweather());
			MeMessageService.instance().requestServer(requestBean, new UICallback<WeatherRespone>() {

				@Override
				public void onSuccess(WeatherRespone respose) {
					try {
						if (respose.isSuccess()) {

							weahter_ll.setVisibility(View.VISIBLE);
							String weather=respose.getContent();
							String[] weathers=weather.split(";");

							String tdweather=weathers[0];
							String[] tdws=null;
							if(tdweather.contains(","))
							{
								tdws=tdweather.split(",");
								today_weather_c.setText(tdws[0]);
								today_weather_f.setText(tdws[1]+","+tdws[2]);
							}

							tdweather=weathers[1];
							tdws=null;
							if(tdweather.contains(","))
							{
								tdws=tdweather.split(",");
								tomorrow_c.setText(tdws[0]);
								tomorrow_f.setText(tdws[1]+","+tdws[2]);
							}
							tdweather=weathers[2];
							tdws=null;
							if(tdweather.contains(","))
							{
								tdws=tdweather.split(",");
								after_c.setText(tdws[0]);
								after_f.setText(tdws[1]+","+tdws[2]);
							}

						}else {
							MeUtil.showToast(context, "获取天气失败,请稍后再试");
						}
					} catch (Exception e) {
						MeUtil.showToast(context, "获取天气失败,请稍后再试");
					}
				}

				@Override
				public void onError(String message) {
					MeUtil.showToast(context, "获取天气失败,请稍后再试");
				}

				@Override
				public void onOffline(String message) {
					MeUtil.showToast(context, "获取天气失败,请稍后再试");
				}
			});
		}
	}

	//	public void getWeatherInfro(){
	//		AreaInfo areaInfo=AppContext.currentAreaInfo();
	//		if (areaInfo!=null) {
	//			showWeatherPopupWindow();
	////			WeatherReq req=new WeatherReq();
	////			req.setCityId(areaInfo.getAlongCitySid());
	////			req.setDistrictId(areaInfo.getAlongDistrictSid());
	////			RequestBean<WeatherRespone> requestBean=new RequestBean<WeatherRespone>();
	////			requestBean.setRequestBody(req);
	////			requestBean.setHttpMethod(HTTP_METHOD.POST);
	////			requestBean.setRequsetFormat(HTTP_DATA_FORMAT.FORM);
	////			requestBean.setResponseBody(WeatherRespone.class);
	////			requestBean.setURL(Constant.Requrl.getweather());
	////			MeMessageService.instance().requestServer(requestBean, new UICallback<WeatherRespone>() {
	////
	////				@Override
	////				public void onSuccess(WeatherRespone respose) {
	////					if (respose.isSuccess()) {
	////						String weather=respose.getContent();
	////						String[] weathers=weather.split(";");
	////						todayWeather.setText("今天  "+weathers[0]);
	////						tomorrowWeather.setText("明天  "+weathers[1]);
	////						afterWeather.setText("后天  "+weathers[2]);
	////						showWeatherPopupWindow();
	////					}else {
	////						MeUtil.showToast(context, "获取天气失败,请稍后再试");
	////					}
	////				}
	////
	////				@Override
	////				public void onError(String message) {
	////					MeUtil.showToast(context, "获取天气失败,请稍后再试");
	////				}
	////
	////				@Override
	////				public void onOffline(String message) {
	////					MeUtil.showToast(context, "获取天气失败,请稍后再试");
	////				}
	////			});
	//		}
	//	}

	@Override
	public void onClick(View v) {
		switch(v.getId())
		{
		case R.id.weahter_ll:
		{
			if(weather_more.getVisibility()==View.GONE)
			{
				more_weather_pic.setImageResource(R.drawable.drop_up);
				//weather_more.setAnimation(push_top_out);
				weather_more.setVisibility(View.VISIBLE);
			}else
			{
				more_weather_pic.setImageResource(R.drawable.drop_down);
				//weather_more.setAnimation(push_top_in);
				weather_more.setVisibility(View.GONE);
			}
			break;
		}
		default:
			break;
		}
	}

}
