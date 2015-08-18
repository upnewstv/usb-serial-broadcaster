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
	private static final String STOP = "net.signagewidgets.serial.services.SerialService.STOP";
	private static final String ID = "DEVICE_ID";

	private SerialDeviceManager mDeviceManager;

	public static void start(Context context) {
		Intent intent = new Intent(context, SerialService.class);
		intent.setAction(START);
		context.startService(intent);
	}

	public static void stop(Context context, int deviceId) {
		Intent intent = new Intent(context, SerialService.class);
		intent.setAction(STOP);
		intent.putExtra(ID, deviceId);
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
		} else if (STOP.equals(action)) {
			sLogging.info("Stop command received");
			if (mDeviceManager != null
					&& intent.getIntExtra(ID, -1) == mDeviceManager.getDeviceID()) {
				mDeviceManager.destroy();
				mDeviceManager = null;
				android.os.Process.killProcess(android.os.Process.myPid());
			}
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
    		long id = Integer.valueOf(line.substring(0, line.length()-1));
    		long button = Integer.valueOf(line.substring(line.length()-1));
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
