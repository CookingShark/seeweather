package com.seeweather.app.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.sax.StartElementListener;

public class AutoUpdateReceiver extends BroadcastReceiver {
/**
 * ���������Զ����·���Ĺ㲥
 */
	@Override
	public void onReceive(Context context, Intent intent) {
		Intent i = new Intent(context , AutoUpdateReceiver.class);
		context.startService(i);
	}

}
