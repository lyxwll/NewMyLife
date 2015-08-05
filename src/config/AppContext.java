package config;

import java.util.List;

import android.content.Context;

import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.model.LatLng;
import com.yulinoo.plat.life.bean.Account;
import com.yulinoo.plat.life.bean.AreaInfo;
import com.yulinoo.plat.life.bean.Category;
import com.yulinoo.plat.life.bean.ForumInfo;
import com.yulinoo.plat.life.bean.Merchant;
import com.yulinoo.plat.life.utils.Constant;
import com.yulinoo.plat.life.utils.SharedPreferencesUtil;

public class AppContext {
	private static final String phoneType = "android";

	public static final String getPhoneType() {
		return phoneType;
	}

	public static final int bg_max_width = 1080;
	public static final int bg_max_height = 810;

	private static final String DOT = ".";
	private static final String X = "x";

	public static String getBgPictureSize(Context context, String sourcePic) {
		int screenWidth = getScreenWidth(context);
		int tmp = (int) (screenWidth * bg_max_height / bg_max_width);
		return sourcePic + DOT + screenWidth + X + tmp
				+ sourcePic.substring(sourcePic.lastIndexOf("."));
	}

	private static int screenWidth = 0;
	private static int screenHeight = 0;

	// 屏幕高度
	public static int getScreenHeight(Context context) {
		if (screenHeight == 0) {
			screenHeight = Integer.parseInt(SharedPreferencesUtil.getString(
					context, "screenHeight", "1280"));
		}
		return screenHeight;
	}

	public static void setScreenHeight(Context context, int height) {
		SharedPreferencesUtil.save(context, "screenHeight", "" + height);
	}

	// 屏幕宽度
	public static int getScreenWidth(Context context) {
		if (screenWidth == 0) {
			screenWidth = Integer.parseInt(SharedPreferencesUtil.getString(
					context, "screenWidth", "760"));
		}
		return screenWidth;
	}

	public static void setScreenWidth(Context context, int width) {
		SharedPreferencesUtil.save(context, "screenWidth", "" + width);
	}

	private static final String CURRENT_ACCOUNT_SID = "CURRENT_ACCOUNT_SID";
	private static Account currentAccount = null;// 当前账号信息

	// 获得当前账号
	public static Account currentAccount() {
		if (currentAccount == null) {
			List<Account> loginedAccs = new Select().from(Account.class)
					.where("isUsrLogin=?", Constant.DB_BOOLEAN.BOOLEAN_TRUE)
					.execute();
			if (loginedAccs != null && loginedAccs.size() == 1) {// 正常情况下，只允许有一个账号被登录
				currentAccount = loginedAccs.get(0);
			} else {// 没有已经登录的账号，则此时只能选取其中的临时账号了
				List<Account> tempAccs = new Select().from(Account.class)
						.where("isTemp=?", Constant.DB_BOOLEAN.BOOLEAN_TRUE)
						.execute();
				if (tempAccs != null && tempAccs.size() > 0) {
					currentAccount = tempAccs.get(0);
				}
			}
		}
		return currentAccount;
	}

	// 将当前账号置为空,这样就会去取新的账号为当前账号
	public static void setCurrentAccountNull() {
		currentAccount = null;
	}

	// 返回当前小区与用户关联的最近的关联小区
	public static AreaInfo nearByArea() {
		AreaInfo curArea = currentAreaInfo();
		AreaInfo tmp = null;
		double distance = 100000000;
		List<AreaInfo> focusArea = currentFocusArea();
		for (AreaInfo ai : focusArea) {
			double tc = getDistance(ai.getLatItude(), curArea.getLatItude(),
					ai.getLongItude(), curArea.getLongItude());
			if (tc < distance) {
				distance = tc;
				tmp = ai;
			}
		}
		if (tmp == null) {
			tmp = curArea;
		}
		return tmp;
	}

	public static int getDistance(double lat1, double lat2, double lon1,
			double lon2) {

		return (int) Math.floor(AMapUtils.calculateLineDistance(new LatLng(
				lat1, lon1), new LatLng(lat2, lon2)));
	}

	private static AreaInfo currentArea;// 当前选中的小区，用户不一定是关注了的，但可能是直接查看当前选中的小区而已

	public synchronized static AreaInfo currentAreaInfo() {
		if (currentArea == null) {// 查找一个类型为NOWCHOICE的小区，即当前用于显示的小区，当前显示的小区与用户关注的小区有可能相同，有可能不同
			Account curAccount = currentAccount();
			if (curAccount != null) {
				List<AreaInfo> lstAreaInfos = new Select().from(AreaInfo.class)
						.where("accSid=?", curAccount.getSid())
						.orderBy("concernAt desc").execute();
				if (lstAreaInfos != null && lstAreaInfos.size() > 0) {
					currentArea = lstAreaInfos.get(0);
				}
			}
		}
		return currentArea;
	}

	// 关注/取消关注某个小区
	public static void focusArea(AreaInfo ai, boolean focused) {
		if (focused) {// 为关注某个小区
			ai.setAccSid(currentAccount().getSid());
			ai.setConcernAt(System.currentTimeMillis());
			ai.save();
			setCurrentAreaInfo(ai);
		} else {
			// new
			// Delete().from(AreaInfo.class).where("sid=?",ai.getSid()).execute();
			new Delete()
					.from(AreaInfo.class)
					.where("sid=? and accSid=?", ai.getSid(),
							currentAccount().getSid()).execute();
		}
		listFocusArea = null;
	}

	// 设置当前显示小区，可能性较大的就是切换小区
	public static void setCurrentAreaInfo(AreaInfo ai) {
		if (currentArea != null && ai != null) {
			if (currentArea.getSid() == ai.getSid()) {
				return;
			}
		}
		currentArea = ai;
		if (currentArea == null) {
			listFocusArea = null;
		}
	}

	private static List<AreaInfo> listFocusArea;// 关注的小区列表

	public static List<AreaInfo> currentFocusArea() {
		if (currentAccount() != null) {
			if (listFocusArea == null) {
				listFocusArea = new Select().from(AreaInfo.class)
						.where("accSid=?", currentAccount().getSid()).execute();
			}
		}
		return listFocusArea;
	}

	public static Long areaUpdatedAt() {
		long updateAt = 0L;
		List<AreaInfo> listForums = currentFocusArea();
		if (listForums != null) {
			for (AreaInfo forum : listForums) {
				if (forum.getUpdatedAt() != null) {
					if (forum.getUpdatedAt().longValue() > updateAt) {
						updateAt = forum.getUpdatedAt().longValue();
					}
				}
			}
		}

		return updateAt;
	}

	// private static List<Merchant> listFocusMerchant=null;//关注的商家列表
	public static List<Merchant> currentFocusMerchant() {

		return new Select().from(Merchant.class)
				.where("accSid=?", currentAccount().getSid()).execute();
	}

	public static Long merchantUpdatedAt() {
		long updateAt = 0L;
		List<Merchant> listForums = currentFocusMerchant();
		for (Merchant forum : listForums) {
			if (forum.getUpdatedAt() != null) {
				if (forum.getUpdatedAt().longValue() > updateAt) {
					updateAt = forum.getUpdatedAt().longValue();
				}
			}
		}
		return updateAt;
	}

	// 是否已关注商家
	public static boolean hasFocusMerchant(long merchantSid, int subType) {
		boolean result = false;
		List<Merchant> lst = currentFocusMerchant();
		if (lst != null) {
			for (Merchant merchant : lst) {
				if (subType == Constant.SUBTYPE.SUBTYPE_USR) {// 个人的情况
					if (merchant.getSid() == merchantSid
							&& merchant.getType() == Constant.SUBTYPE.SUBTYPE_USR) {
						result = true;
						break;
					}
				} else {// 商家的情况
					if (merchant.getSid() == merchantSid
							&& merchant.getType() != Constant.SUBTYPE.SUBTYPE_USR) {
						result = true;
						break;
					}
				}
			}
		}
		return result;
	}

	// 关注
	public static void focusMerchant(Merchant merchant, boolean focused) {
		List<Merchant> lst = currentFocusMerchant();
		if (focused) {// 关注
			if (merchant.getGoods() != null) {
				merchant.setLastCont(merchant.getGoods().getGoodsDesc());
			}
			lst.add(merchant);
			merchant.setAccSid(currentAccount().getSid());
			merchant.save();
		} else {// 取消关注
			for (Merchant mer : lst) {
				if (mer.getSid() == merchant.getSid()
						&& merchant.getType() == mer.getType()) {
					lst.remove(mer);
					break;
				}
			}
			// new
			// Delete().from(Merchant.class).where("sid=? and type=?",merchant.getSid()).execute();
			new Delete()
					.from(Merchant.class)
					.where("accSid=? and sid=? and type=?",
							currentAccount().getSid(), merchant.getSid(),
							merchant.getType()).execute();
		}
	}

	public static List<Category> currentCategorys(Long citySid) {
		return new Select()
				.from(Category.class)
				.where("citySid=? and status=?", citySid,
						Constant.DB_STATUS.STATUS_VALIDATE)
				.orderBy("orderBy asc").execute();
	}

	public static Long categoryUpdatedAt(Long citySid) {
		long updateAt = 0L;
		List<Category> list = currentCategorys(citySid);
		for (Category category : list) {
			if (category.getUpdatedAt() != null) {
				if (category.getUpdatedAt().longValue() > updateAt) {
					updateAt = category.getUpdatedAt().longValue();
				}
			}
		}
		return updateAt;
	}

	// 根据分类ID获取栏目列表
	public static List<ForumInfo> getForumByCategoryId(long categorySid) {
		return new Select().from(ForumInfo.class)
				.where("alongCategorySid=?", categorySid).execute();
	}

	public static void saveForumInfo(ForumInfo ffi) {
		ffi.save();
	}

	public static List<ForumInfo> getCityForums(Long citySid) {
		List<ForumInfo> listForums = new Select().from(ForumInfo.class)
				.where("citySid=?", citySid).execute();
		return listForums;
	}

	public static Long forumUpdatedAt(Long citySid) {
		long updateAt = 0L;
		List<ForumInfo> listForums = getCityForums(citySid);
		for (ForumInfo forum : listForums) {
			if (forum.getUpdatedAt() != null) {
				if (forum.getUpdatedAt().longValue() > updateAt) {
					updateAt = forum.getUpdatedAt().longValue();
				}
			}
		}
		return updateAt;
	}

	// 获得语邻的分类
	public static Category getNeighbourTalk() {
		AreaInfo ai = currentAreaInfo();
		List<Category> lstCa = currentCategorys(ai.getAlongCitySid());
		for (Category ca : lstCa) {
			if (ca.getHomeDisplay() == Constant.CATEGORY.SHOW_AS_NEIGHBOUR_TALK) {
				return ca;
			}
		}
		return null;
	}
}
