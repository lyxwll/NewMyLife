package com.yulinoo.plat.life.utils;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

/**
 * 文件名: com.goodwin.finance.util.ViewUtil
 * 作者:  熊杰 Wilson
 * 日期: 14-9-30
 * 时间: 14:28
 * 开发工具: IntelliJ IDEA 12.1.1
 * 开发语言: Java,Android
 * 开发框架:
 * 版本: v0.1
 * <strong>软件中所有与布局相关的工具类</strong>
 * <p></p>
 */
public class ViewUtil {
  public static void setListViewHeightBasedOnChildren(ListView listView) {
    ListAdapter listAdapter = listView.getAdapter();
    if (listAdapter == null) {
      return;
    }

    int totalHeight = 0;
    for (int i = 0; i < listAdapter.getCount(); i++) {
      View listItem = listAdapter.getView(i, null, listView);
      listItem.measure(0, 0);
      totalHeight += listItem.getMeasuredHeight();
    }

    ViewGroup.LayoutParams params = listView.getLayoutParams();
    params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
    listView.setLayoutParams(params);
  }

  /**
   * 获取Listview的高度，然后设置ViewPager的高度
   * @param listView
   * @return
   */
  public static int setListViewHeightBasedOnChildren1(ListView listView) {
    //获取ListView对应的Adapter
    ListAdapter listAdapter = listView.getAdapter();
    if (listAdapter == null) {
      // pre-condition
      return 0;
    }

    int totalHeight = 0;
    for (int i = 0, len = listAdapter.getCount(); i < len; i++) { //listAdapter.getCount()返回数据项的数目
      View listItem = listAdapter.getView(i, null, listView);
      listItem.measure(0, 0); //计算子项View 的宽高
      totalHeight += listItem.getMeasuredHeight(); //统计所有子项的总高度
    }

    ViewGroup.LayoutParams params = listView.getLayoutParams();
    params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
    //listView.getDividerHeight()获取子项间分隔符占用的高度
    //params.height最后得到整个ListView完整显示需要的高度
    listView.setLayoutParams(params);
    return params.height;
  }
}