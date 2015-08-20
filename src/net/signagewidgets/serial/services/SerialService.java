package net.signagewidgets.serial.services;

import android.app.Notification;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.IBinder;

import net.signagewidgets.serial.services.SerialDeviceManager.SerialDeviceListener;
import net.signagewidgets.serial.util.Logging;

public class SerialService extends Service implements SerialDeviceListener {
	private static Logging sLogging = new Logging(SerialService.class);	

	private static final String START = "net.signagewidgets.serial.services.SerialService.START";

	private SerialDeviceManager mDeviceManager;
	private Notification mNotification;

	public static void start(Context context) {
		Intent intent = new Intent(context, SerialService.class);
		intent.setAction(START);
		context.startService(intent);
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onCreate() {
		sLogging.info("onCreate");

		Notification.Builder builder = new Notification.Builder(SerialService.this)
	        .setContentTitle(getApplicationInfo().loadLabel(getPackageManager()))
	        .setSmallIcon(net.signagewidgets.serial.R.drawable.icon)
	        .setLargeIcon(BitmapFactory.decodeResource(getResources(),
	        		net.signagewidgets.serial.R.drawable.icon))
	        .setAutoCancel(false);

        if (android.os.Build.VERSION.SDK_INT < 16) {
            mNotification = builder.getNotification();
        }  else {
            mNotification = builder.build();
        }

        mNotification.flags |= Notification.FLAG_ONGOING_EVENT;
        mNotification.flags |= Notification.FLAG_NO_CLEAR;
        startForeground(5378, mNotification);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		String action = intent != null ? intent.getAction() : null;

		if (START.equals(action)) {
			sLogging.info("Start command received");
			if (mDeviceManager == null) mDeviceManager = new SerialDeviceManager(this, this);
		}

		return START_STICKY;
	}

	@Override
	public void onDestroy() {
		sLogging.info("Destroy");
		if (mDeviceManager != null) {
			mDeviceManager.destroy();
			mDeviceManager = null;
		}
		super.onDestroy();
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onLineReceived(String line) {
		try {
			long number = Long.valueOf(line);
    		long id = number >> 4;
    		long button = number & 0xF;
    		sLogging.info("ID:", id, "- Button:", button);
    		Intent intent = new Intent("net.signagewidgets.serial.BUTTON");
    		intent.putExtra("id", id);
    		intent.putExtra("button", button);
    		sendBroadcast(intent);
		} catch (Exception e) {
			sLogging.error("Error parsing serial data");
			sLogging.captureException(e);
		}
	}
}
