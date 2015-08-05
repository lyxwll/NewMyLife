package com.yulinoo.plat.life.utils;

import config.AppContext;

/**
 * @author jefry
 * 
 */
public class Constant {

	public static enum HTTP_METHOD {
		GET, POST, DELETE, PUT
	}

	public static enum HTTP_DATA_FORMAT {
		FORM, JSON
	}

	public static interface CachePath {
		// @SuppressLint("SdCardPath")
		// public static final String DOWNLOAD_IMG_CACHE =
		// "/sdcard/com.me500.life/download_img/";
	}

	public static interface IntentKey {
		public static final String IMAGE_FIEL_URL = "fileUrl";
		public static final String IMAGE_FILE_PATH = "filePath";
		public static final String IMAGE_FILE_URLS = "fileUrls";
	}

	public static interface AREA {
		/** 查询的位置范围 */
		public static final int QUERY_STEP_AREAINFO = 1;// 步行距离
		public static final int QUERY_OWN_AREAINFO = 2;// 自己所在小区
		public static final int QUERY_NEIB_AREAINFO = 3;// 近邻

		/** 频道位置 */
		public static final int CHANNEL_INDEX = 1;// 小区首页
		public static final int CHANNEL_MYNEIB = 2;// 近邻
	}

	public static interface DB_BOOLEAN {
		public static final int BOOLEAN_TRUE = 1;
		public static final int BOOLEAN_FALSE = 0;
	}

	public static interface DB_STATUS {
		public static final int STATUS_VALIDATE = 1;// 有效
		public static final int STATUS_INVALIDATE = 0;// 无效
	}

	// 打开邻语时需要显示的类别
	public static interface NEIGHBOURE_TALK_OPEN_TYPE {
		public static final int OPEN_CHAT = 1;// 打开聊天室
		public static final int OPEN_POST = 2;// 打开贴吧
	}

	// 消息类别
	public static interface MSGTYPE {
		public static final int TYPE_PRAISE = 1;// 点赞
		public static final int TYPE_COMMENT = 2;// 评论
		public static final int TYPE_CONTACT = 3;// 联系人
	}

	// 商家类型
	public static interface MERCHANT_TYPE {
		public static final int YULINOO_MERCHANT = 1;// 与邻自已经营的商家
		public static final int NORMAL_MERCHANT = 2;// 普通商家
	}

	// 账号类型,是个人还是商家
	public static interface ACCTYPE {
		public static final int ACCTYPE_NORMAL_USR = 0;// 普通用户
		public static final int ACCTYPE_NORMAL_MERCHANT = 1;// 商家
	}

	// 查询评论类别方向
	public static interface COMMDIRECTION {
		public static final int DIRECTION_ALL = 1;// 全部评论
		public static final int DIRECTION_MY_COMMENT = 2;// 我的评论
		public static final int DIRECTION_COMMENT_MINE = 3;// 评论我的
	}

	// 查询点赞类别方向
	public static interface PRAISEDIRECTION {
		public static final int DIRECTION_ALL = 1;// 全部评论
		public static final int DIRECTION_MY_PRAISE = 2;// 我的评论
		public static final int DIRECTION_PRAISE_ME = 3;// 评论我的
	}

	// 查询点赞类别方向
	public static interface ADVERTISE_POSITION {
		public static final int POSITION_INDEX = 1;// 首页广告
		public static final int POSITION_CATEGORY = 2;// 分类广告
	}

	// 订阅类型
	public static interface SUBTYPE {
		public static final int SUBTYPE_MERCHANT = 1;// 关注的商家类型
		public static final int SUBTYPE_USR = 2;// 关注的是普通用户类型
		public static final int SUBTYPE_SERVICE = 3;// 关注的是服务号
	}

	// 产品属性
	public static interface GOODS {
		public static final int COMMENT_CAN_DO = 0;// 产品允许评论
		public static final int COMMENT_CAN_NOT_DO = 1;// 产品不允许评论

		public static final int COMMENT_CAN_OPEN = 0;// 评论允许公开
		public static final int COMMENT_CAN_NOT_OPEN = 1;// 评论不允许公开

		public static final int COMMENT_PERSONAL_CAN_DO = 1;// 允许个人发布
		public static final int COMMENT_PERSONAL_CAN_NOT_DO = 2;// 不允许个人发布

	}

	public static interface CATEGORY {
		public static final int MERCHANT_CAN_CHOICE = 2;// 商家可以选择
		public static final int MERCHANT_CANOT_CHOICE = 1;// 商家不可选
		public static final int SHOW_AS_NEIGHBOUR_TALK = 1;// 显示为邻语
		public static final int NOT_SHOW_AS_NEIGHBOUR_TALK = 0;// 不需要显示为邻语
	}

	public static interface SEX {
		public static final int SEX_WOMAN = 2;// 女
		public static final int SEX_MAN = 1;// 男
		public static final int SEX_SECRECT = 3;// 保持神秘
	}

	public static interface ActivityExtra {
		public static final String MERCHANT = "merchant";// 传递的是商家信息
		public static final String CATEGORY = "category";// 传递的是分类信息
		public static final String ACCOUNT = "account";// 传递的是用户信息
		public static final String LINKEDTITLE = "linkedtitle";// 传递的是关于联系人的标题,比如我的粉丝,我的邻友,商铺收藏,服务号,猜你喜欢等
		public static final String LINKED_LOAD_TYPE = "linked_load_type";// 传递的是关于联系人的加载类型,不同的加载类型,加载的数据不一样
		public static final String GOODSSID = "goods_sid";// 传递的是商品主键
		public static final String SUGGESTION_TYPE = "suggestion_type";// 传递的是建议类型
		public static final String CITY_DO = "suggestion_type";// 传递的是建议类型
		public static final String NEW_VERSION = "new_version";// 版本名称
		public static final String NEIGHBOUR_TALK_OPEN_TYPE = "opentype";// 邻语的打开类型
		public static final String PUBLISH_GOODS_TYPE = "publishType";// 消息的发布类型
	}

	// 联系人的加载类型
	public static interface ConcernLoadType {
		public static final int LOAD_FANS = 1;// 粉丝
		public static final int LOAD_MYFRIEND = 2;// 我的邻友
		public static final int LOAD_FAVORITE = 3;// 店铺收藏
		public static final int LOAD_SERVICE = 4;// 店铺收藏
		public static final int LOAD_GUESS_LOVE = 5;// 猜你喜欢
		public static final int LOAD_USER_GROUP = 6;// 加载用户组信息
	}

	// 消息阅读状态
	public static interface MessageReadStatus {
		public static final int READ_STATUS_UNREADED = 1;// 未读
		public static final int READ_STATUS_READED = 2;// 已读
	}

	// 发布消息类型
	public static interface PUBLISH_GOODS_TYPE {
		public static final int MERCHANT_INDEX_PUBLISH = 1;// 商家首页的发布商品
		public static final int MERCHANT_ZONE_PUBLISH = 2;// 商家个人空间点击后的发布商品
		public static final int PERSONAL_INDEX_PUBLISH = 3;// 首页的个人发布
		public static final int PERSONAL_NEIGHBOUR_PUBLISH = 4;// 邻语情况下的个人发布
	}

	// 广播
	public static interface BroadType {
		public static final String AREA_CHANGED = "com.yulinoo.plat.melife.areachangedbroadcast";// 小区被改变了
		public static final String GOODS_DELETED = "com.yulinoo.plat.melife.goods.deleted";// 商品被删除了
		public static final String GOODS_ADDED = "com.yulinoo.plat.melife.goods.added";// 商品新增了
		public static final String AREA_OPEN_SHOP = "com.yulinoo.plat.melife.openshop";// 小区开店成功
		public static final String ADD_COMMENT_OK = "com.yulinoo.plat.melife.addcomment";// 评论成功成功
		public static final String ADD_PRAISE_OK = "com.yulinoo.plat.melife.addpraise";// 点赞成功
		public static final String SUBSCRIBE_READED = "com.yulinoo.plat.melife.subscribeReaded";// 关注的用户已经被阅读
		public static final String NEW_VERSION = "com.yulinoo.plat.melife.newversion";// 关注的用户已经被阅读
	}

	// 建议类型
	public static interface SuggestionType {
		public static final int ERROR = 1;// 纠错投诉
		public static final int SUGGESTION = 2;// 意见建议
		public static final int RECOMMEND_MERCHANT = 3;// 推荐商家
		public static final int LOSE_AREA = 4;// 推荐小区

	}

	public static final String DOT = ".";

	public static final class Requrl {
		// 获取城市
		public static final String getCityList() {
			return URL.ADMIN_SERVER_DOMAIN + "/app/v1/city/list.do";
		}

		// 根据城市获取区县
		public static final String getDistrictList() {
			return URL.ADMIN_SERVER_DOMAIN + "/app/v1/district/list.do";
		}

		// 根据区县获取小区
		public static final String getAreaInfoList() {
			return URL.ADMIN_SERVER_DOMAIN + "/app/v1/areainfo/list.do";
		}

		// 获取临时sid
		public static final String getTempSid() {
			return URL.ADMIN_SERVER_DOMAIN + "/app/v1/account/tempAcc.do";
		}

		// 获取账号关注的小区列表
		public static final String getConcernAreaList() {
			return URL.ADMIN_SERVER_DOMAIN
					+ "/app/v1/areainfo/getConcernAreaInfo.do";
		}

		// 获取账号关注的商家列表
		public static final String getConcernMerchantList() {
			return URL.HTTP + getCityDomain() + DOT + URL.BASE_DOMAIN
					+ "/app/v1/account/getSubscribeByAcc.do";
		}

		// 关注小区
		public static final String getConcernArea() {
			return URL.ADMIN_SERVER_DOMAIN
					+ "/app/v1/areainfo/concernAreaInfo.do";
		}

		// 取消小区关注
		public static final String getCancelConcernArea() {
			return URL.ADMIN_SERVER_DOMAIN
					+ "/app/v1/areainfo/cancelConcernAreaInfo.do";
		}

		// 获取小区详情
		public static final String getAreaDetail() {
			return URL.ADMIN_SERVER_DOMAIN + "/app/v1/areainfo/detail.do";
		}

		// 获取城市分类
		public static final String getCategoryList() {
			return URL.ADMIN_SERVER_DOMAIN + "/app/v1/category/cityCategory.do";
		}

		// 获取首页商家列表
		public static final String getIndexMerchantList() {
			return URL.HTTP + getCityDomain() + DOT + URL.BASE_DOMAIN
					+ "/app/v1/merchant/getMerchantByChanel.do";
		}

		// 获取分类下的栏目列表
		public static final String getForumList() {
			return URL.ADMIN_SERVER_DOMAIN + "/app/v1/forum/list.do";
		}

		// 获取某一栏目下的商家(包括个人)
		public static final String getForumMerchantList() {
			return URL.HTTP + getCityDomain() + DOT + URL.BASE_DOMAIN
					+ "/app/v1/merchant/getMerchantByForum.do";
		}

		// 获取近邻
		public static final String getNearByAreaInfoList() {
			return URL.HTTP + getCityDomain() + DOT + URL.BASE_DOMAIN
					+ "/app/v1/areainfo/near.do";
		}

		// 获取商家产品列表
		public static final String getMerchantProductsList() {
			return URL.HTTP + getCityDomain() + DOT + URL.BASE_DOMAIN
					+ "/app/v1/goods/getProduct.do";
		}

		// 获取商家商品列表
		public static final String getMerchantGoodsList() {
			return URL.HTTP + getCityDomain() + DOT + URL.BASE_DOMAIN
					+ "/app/v1/goods/getGoodsByPrdSid.do";
		}

		// 获取商家详情
		public static final String getMerchantDetail() {
			return URL.HTTP + getCityDomain() + DOT + URL.BASE_DOMAIN
					+ "/app/v1/merchant/getMerchantByAcc.do";
		}

		// 获取某商品点评列表
		public static final String getGoodsCommentList() {
			return URL.HTTP + getCityDomain() + DOT + URL.BASE_DOMAIN
					+ "/app/v1/goodsComment/getGoodsComment.do";
		}

		// 获取某商品点赞列表
		public static final String getGoodsPraiseList() {
			return URL.HTTP + getCityDomain() + DOT + URL.BASE_DOMAIN
					+ "/app/v1/goodsComment/getGoodsPraise.do";
		}

		// 获取关注商家
		public static final String getConcernMerchant() {
			return URL.HTTP + getCityDomain() + DOT + URL.BASE_DOMAIN
					+ "/app/v1/account/accAttention.do";
		}

		// 获取取消商家关注
		public static final String getCancelConcernMerchant() {
			return URL.HTTP + getCityDomain() + DOT + URL.BASE_DOMAIN
					+ "/app/v1/account/delSubscribe.do";
		}

		// 获取猜你喜欢
		public static final String getGuessYourLove() {
			return URL.HTTP + getCityDomain() + DOT + URL.BASE_DOMAIN
					+ "/app/v1/merchant/youwantMer.do";
		}

		// 获取用户登录
		public static final String getUsrLogin() {
			return URL.ADMIN_SERVER_DOMAIN + "/app/v2/account/login.do";
		}

		// 获取登录背景图片，用于用户登录页面的背景显示
		// public static final String getLoginBgPicture(){return
		// URL.HTTP+getCityDomain()+DOT+ URL.BASE_DOMAIN+
		// "/app/v1/areainfo/regBackPic.do";}
		// 获取用户注册
		public static final String getUsrRegister() {
			return URL.ADMIN_SERVER_DOMAIN
					+ "/app/v1/account/registerAccount.do";
		}

		// 获取修改用户头像
		public static final String getModifyUsrAvarta() {
			return URL.HTTP + getCityDomain() + DOT + URL.BASE_DOMAIN
					+ "/app/v1/merchant/modHeadPicture.do";
		}

		// 获取修改商家头像
		public static final String getModifyMerchantAvarta() {
			return URL.HTTP + getCityDomain() + DOT + URL.BASE_DOMAIN
					+ "/app/v1/merchant/modLogPic.do";
		}

		// 获取分类下的标签列表
		public static final String getCategoryTagList() {
			return URL.HTTP + getCityDomain() + DOT + URL.BASE_DOMAIN
					+ "/app/v1/merchant/taglist.do";
		}

		// 获取关于500米
		public static final String getAboutME() {
			return URL.ADMIN_SERVER_DOMAIN + "/app/protocol/about.do";
		}

		// 获取用户协议
		public static final String getUsrProtocal() {
			return URL.ADMIN_SERVER_DOMAIN + "/app/protocol/protocol.do";
		}

		// 用户建议
		public static final String getUsrSuggestion() {
			return URL.HTTP + getCityDomain() + DOT + URL.BASE_DOMAIN
					+ "/app/suggestion/suggestion.do";
		}

		// 获取小区开店
		public static final String getOpenShop() {
			return URL.HTTP + getCityDomain() + DOT + URL.BASE_DOMAIN
					+ "/app/v1/merchant/openSeller.do";
		}

		// 获取用户详情
		public static final String getUsrDetail() {
			return URL.ADMIN_SERVER_DOMAIN + "/app/v1/account/accDetails.do";
		}

		// 获取发表消息
		public static final String getPublishGoods() {
			return URL.HTTP + getCityDomain() + DOT + URL.BASE_DOMAIN
					+ "/app/v1/goods/personalPush.do";
		}

		// 获取修改店铺
		public static final String getModifyShop() {
			return URL.HTTP + getCityDomain() + DOT + URL.BASE_DOMAIN
					+ "/app/v1/merchant/modMerchant.do";
		}

		// 获取修改用户
		public static final String getModifyAccount() {
			return URL.HTTP + getCityDomain() + DOT + URL.BASE_DOMAIN
					+ "/app/v1/account/modAccout.do";
		}

		// 获取文件上传
		public static final String getFileUpload() {
			return URL.HTTP + getCityDomain() + DOT + URL.BASE_DOMAIN
					+ "/app/v1/goods/fileUpload.do";
		}

		// 获取消息盒子信息
		public static final String getMessageBox() {
			return URL.HTTP + getCityDomain() + DOT + URL.BASE_DOMAIN
					+ "/app/v1/messagebox/getMessagebox.do";
		}

		// 获取聊天室消息
		public static final String getChatRoomMessage() {
			return URL.HTTP + getCityDomain() + DOT + URL.BASE_DOMAIN
					+ "/app/v1/areainfo/getAreaChatRoomMsg.do";
		}

		// 获取聊天室发送消息
		public static final String getSendChatRoomMessage() {
			return URL.HTTP + getCityDomain() + DOT + URL.BASE_DOMAIN
					+ "/app/v2/areainfo/sendAreaChatRoomMsg.do";
		}

		// 获取发私信/点赞/评论
		public static final String getSendWrapMessage() {
			return URL.HTTP + getCityDomain() + DOT + URL.BASE_DOMAIN
					+ "/app/v1/messagebox/saveMessage.do";
		}

		// 获取与我有关的评论和赞
		public static final String getMyWrapMessage() {
			return URL.HTTP + getCityDomain() + DOT + URL.BASE_DOMAIN
					+ "/app/v1/messagebox/getMessageList.do";
		}

		// 获取我的私信
		public static final String getMyPrivateMessage() {
			return URL.HTTP + getCityDomain() + DOT + URL.BASE_DOMAIN
					+ "/app/v1/letter/getLetterList.do";
		}

		// 获取个人发布的微博列表
		public static final String getWeiBoList() {
			return URL.HTTP + getCityDomain() + DOT + URL.BASE_DOMAIN
					+ "/app/v1/account/accGoodsList.do";
		}

		// 获取随机昵称
		public static final String getRandName() {
			return URL.HTTP + getCityDomain() + DOT + URL.BASE_DOMAIN
					+ "/app/v1/account/getRandNickName.do";
		}

		// 获取商品详情
		public static final String getGoodsDetail() {
			return URL.HTTP + getCityDomain() + DOT + URL.BASE_DOMAIN
					+ "/app/v1/goods/getGoodsBySid.do";
		}

		// 获取广告详情
		public static final String getAdv() {
			return URL.HTTP + getCityDomain() + DOT + URL.BASE_DOMAIN
					+ "/app/v1/merchantAdvert/getMerchantAdvertApp.do";
		}

		// 获取粉丝
		public static final String getFans() {
			return URL.HTTP + getCityDomain() + DOT + URL.BASE_DOMAIN
					+ "/app/v1/account/accFansList.do";
		}

		// 要求服务器发送验证码
		public static final String getSendValShortMessage() {
			return URL.ADMIN_SERVER_DOMAIN + "/app/v1/account/getMobileCode.do";
		}

		// 修改密码
		public static final String getModifyPassword() {
			return URL.ADMIN_SERVER_DOMAIN
					+ "/app/v1/account/modifyPassword.do";
		}

		// 重置密码,即忘记密码
		public static final String getResetPassword() {
			return URL.ADMIN_SERVER_DOMAIN + "/app/v1/account/resetPassword.do";
		}

		// 获取我的关注中的用户组
		public static final String getConcernUsrGroup() {
			return URL.ADMIN_SERVER_DOMAIN + "/app/v1/group/getGroupList.do";
		}

		// 获取我的关注中的用户组中的用户
		public static final String getConcernUsrGroupUsrs() {
			return URL.HTTP + getCityDomain() + DOT + URL.BASE_DOMAIN
					+ "/app/v1/userGroup/getUserGroup.do";
		}

		// 获取删除私信联系人
		public static final String getDelLinkedUsr() {
			return URL.HTTP + getCityDomain() + DOT + URL.BASE_DOMAIN
					+ "/app/v1/letter/delLetter.do";
		}

		// 获取删除商品
		public static final String getDelGoods() {
			return URL.HTTP + getCityDomain() + DOT + URL.BASE_DOMAIN
					+ "/app/v1/goods/updateGoods.do";
		}

		// 获取热门商家
		public static final String getHotMerchant() {
			return URL.HTTP + getCityDomain() + DOT + URL.BASE_DOMAIN
					+ "/app/v1/merchant/hotMerchant.do";
		}

		// 获取热门搜索
		public static final String getHotSearch() {
			return URL.ADMIN_SERVER_DOMAIN + "/app/v1/city/hotSearch.do";
		}

		// 获取模糊搜索
		public static final String getFuzzySearch() {
			return URL.HTTP + getCityDomain() + DOT + URL.BASE_DOMAIN
					+ "/app/v1/merchant/fuzzyQuery.do";
		}

		// 获取版本信息
		public static final String getVersion() {
			return URL.ADMIN_SERVER_DOMAIN + "/app/version/ver.json";
		}

		// 获取商品HTML内容
		public static final String getGoodsHtmlDesc() {
			return URL.HTTP + getCityDomain() + DOT + URL.BASE_DOMAIN
					+ "/app/v1/goods/goodsDesc.do";
		}

		// 高德地图获取近邻小区
		public static final String getAreaLax() {
			return URL.ADMIN_SERVER_DOMAIN + "/app/v1/areainfo/getAreaLax.do";
		}

		// 根据定位经纬度返回城市信息
		public static final String getCityBylt() {
			return URL.ADMIN_SERVER_DOMAIN + "/app/v1/city/getcitybylt.do";
		}

		// 根据城市Id获取天气
		public static final String getweather() {
			return URL.ADMIN_SERVER_DOMAIN + "/app/weather/getweather.do";
		}

		// 根据小区ID获得小区详情
		public static final String getAreaDetailById() {
			return URL.HTTP + getCityDomain() + DOT + URL.BASE_DOMAIN
					+ "/app/v2/areainfo/detail.do";
		};

		// 获得广告
		public static final String getAdvertList() {
			return URL.HTTP + getCityDomain() + DOT + URL.BASE_DOMAIN
					+ "/app/v2/merchantAdvert/getAdvertList.do";
		};

		// 获取与我有关的评论和赞
		public static final String getMerchantReview() {
			return URL.HTTP + getCityDomain() + DOT + URL.BASE_DOMAIN
					+ "/app/v1/merchant/getMerchantReview.do";
		}

		// 获取商家最早发布的商品
		public static final String getEarlyGood() {
			return URL.HTTP + getCityDomain() + DOT + URL.BASE_DOMAIN
					+ "/app/v1/merchant/getEarGoods.do";
		}
	}

	private static String getCityDomain() {
		if (AppContext.currentAreaInfo() != null) {
			return AppContext.currentAreaInfo().getCityDomain();
		} else {
			return "";
		}
	}

	public static interface URL {
		public static final String HTTP = "http://";
		public static final String IMG = "img.";
		public static final String BASE_DOMAIN = "yulinoo.com";
		public static final String ADMIN_SERVER_DOMAIN = HTTP + "admin."
				+ BASE_DOMAIN;
	}

}
