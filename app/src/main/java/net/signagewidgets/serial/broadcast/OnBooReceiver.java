package net.signagewidgets.serial.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import net.signagewidgets.serial.services.SerialService;

public class OnBooReceiver extends BroadcastReceiver {
    public OnBooReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        SerialService.start(context);
    }
}
