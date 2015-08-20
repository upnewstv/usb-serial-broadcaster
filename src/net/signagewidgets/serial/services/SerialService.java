package net.signagewidgets.serial.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import net.signagewidgets.serial.services.SerialDeviceManager.SerialDeviceListener;
import net.signagewidgets.serial.util.Logging;

public class SerialService extends Service implements SerialDeviceListener {
	private static Logging sLogging = new Logging(SerialService.class);	

	private static final String START = "net.signagewidgets.serial.services.SerialService.START";

	private SerialDeviceManager mDeviceManager;

	public static void start(Context context) {
		Intent intent = new Intent(context, SerialService.class);
		intent.setAction(START);
		context.startService(intent);
	}

	@Override
	public void onCreate() {
		sLogging.info("onCreate");
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
