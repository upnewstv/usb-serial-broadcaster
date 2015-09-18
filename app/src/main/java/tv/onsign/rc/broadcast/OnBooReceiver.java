package tv.onsign.rc.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import tv.onsign.rc.services.SerialService;

public class OnBooReceiver extends BroadcastReceiver {
    public OnBooReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        SerialService.start(context);
    }
}
