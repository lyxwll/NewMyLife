package com.yulinoo.plat.life.utils;

import android.content.Context;

import com.yulinoo.plat.life.ui.widget.InternetProgressDialog;

public class ProgressUtil {
	private static InternetProgressDialog internetProgressDialog;

	public static void showProgress(Context context, String message) {
		try {
			internetProgressDialog = new InternetProgressDialog(context,
					message);
			internetProgressDialog.show();
		} catch (Exception e) {
		}
	}

	public static void setProgress(String progress) {
		try {
			if (internetProgressDialog != null) {
				internetProgressDialog.setProgress(progress);
			}
		} catch (Exception e) {
		}
	}

	public static void dissmissProgress() {
		try {
			if (internetProgressDialog != null
					&& internetProgressDialog.isShowing()) {
				internetProgressDialog.dismiss();
				internetProgressDialog = null;
			}
		} catch (Exception e) {
		}
	}
}
