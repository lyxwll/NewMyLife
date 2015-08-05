package com.yulinoo.plat.life.utils;

import java.util.List;

import com.yulinoo.plat.life.bean.Account;
import com.yulinoo.plat.life.bean.ConcernNumber;
import com.yulinoo.plat.life.bean.Merchant;
import com.yulinoo.plat.life.bean.MessageCenterInfo;

import config.AppContext;

public class ConcernUtil {

	/**
	 * 判断我的关注的红点是否显示的方法
	 */
	public static  boolean isMenuShowRedHot()
	{
		Account account=AppContext.currentAccount();
		if(account!=null)
		{//显示组
			//当前用户关注的好友列表
			List<Merchant> listSubs=AppContext.currentFocusMerchant();
			//当成数据库存储的好友的更新状态
			List<ConcernNumber> lisCs=ConcernNumber.getUsrConcernNumber(account.getSid());
			if(lisCs!=null)
			{
				for(Merchant merchant:listSubs)
				{
					long accSid=merchant.getSid();//关注的商家/用户的主键
					for(ConcernNumber cn:lisCs)
					{
						long cacc=cn.getConcernAccSid().longValue();
						int concernNumber=cn.getNumber();
						if(concernNumber>0&&accSid==cacc)
						{//只要有一个没有读的话,就需要菜单显示红点
							return true;
						}
					}
				}
			}
		}
		List<MessageCenterInfo> lstMcis=MessageCenterInfo.getMessageCenterInfo();
		if(lstMcis!=null&&lstMcis.size()>0)
		{//检测最近联系人
			for(MessageCenterInfo mci:lstMcis)
			{
				if(mci.getReadStatus()==Constant.MessageReadStatus.READ_STATUS_UNREADED)
				{
					return true;
				}
			}
		}
		return false;
	}

}
