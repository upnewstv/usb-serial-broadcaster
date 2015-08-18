package net.signagewidgets.serial.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;

import net.signagewidgets.serial.services.SerialService;

public class DettachReceiver extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {
		UsbDevice usbDevice = (UsbDevice) intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);

		if (usbDevice != null) SerialService.stop(context, usbDevice.getDeviceId());
	}
}
