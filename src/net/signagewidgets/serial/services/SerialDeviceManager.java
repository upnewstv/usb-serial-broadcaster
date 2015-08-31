package net.signagewidgets.serial.services;

import android.content.Context;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbManager;

import com.hoho.android.usbserial.driver.UsbSerialDriver;
import com.hoho.android.usbserial.driver.UsbSerialPort;
import com.hoho.android.usbserial.driver.UsbSerialProber;
import com.hoho.android.usbserial.util.SerialInputOutputManager;

import net.signagewidgets.serial.util.CaptureExceptionsScheduledExecutor;
import net.signagewidgets.serial.util.Logging;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class SerialDeviceManager implements SerialInputOutputManager.Listener {
	private static Logging sLogging = new Logging(SerialDeviceManager.class);	
	private static final int RECONNETION_DELAY = 2;

	public static interface SerialDeviceListener {
		public void onLineReceived(String line);
	}

	private final UsbManager mUsbManager;
	private final CaptureExceptionsScheduledExecutor mExecutor;
	private final CaptureExceptionsScheduledExecutor mIOExecutor;
	private final StringBuilder mStringBuilder;
	private final SerialDeviceListener mListener;

	private UsbSerialPort mSerialPort;
	private SerialInputOutputManager mSerialIoManager;
	private UsbDeviceConnection mConnection;

	public SerialDeviceManager(Context context, SerialDeviceListener listener) {
		mUsbManager = (UsbManager) context.getSystemService(Context.USB_SERVICE);
		mExecutor = new CaptureExceptionsScheduledExecutor(1, SerialService.class.getName());
		mIOExecutor = new CaptureExceptionsScheduledExecutor(1, "SerialInputOutputManagerExecutor");
		mStringBuilder = new StringBuilder(48);
		mListener = listener;
		scanConnect();
	}

	public int getDeviceID() {
		return (mSerialPort != null) ? mSerialPort.getDriver().getDevice().getDeviceId() : 0;
	}

	public void destroy() {
		close();
		mExecutor.execute(new Runnable() {
			@Override
			public void run() {
				sLogging.info("Shutting down executors");
				mExecutor.shutdownNow();
				mIOExecutor.shutdownNow();
			}
		});
	}

	private void scanConnect() {
		mExecutor.execute(new Runnable() {
			@Override
			public void run() {
				try{
					sLogging.info("Scanning and connecting to available devices");

					List<UsbSerialDriver> drivers = UsbSerialProber.getDefaultProber().findAllDrivers(mUsbManager);

					if (drivers.isEmpty()) {
						mExecutor.schedule(this, RECONNETION_DELAY, TimeUnit.SECONDS);
						return;
					}
	
					UsbSerialDriver driver = drivers.get(0);
					UsbSerialPort serialPort = driver.getPorts().get(0);
	
					mConnection = mUsbManager.openDevice(driver.getDevice());
	
					if (mConnection != null) {
						try {
							mSerialPort = serialPort;
							mSerialPort.open(mConnection);
							mSerialPort.setParameters(9600, 8, UsbSerialPort.STOPBITS_1, UsbSerialPort.PARITY_NONE);
	
							mSerialIoManager = new SerialInputOutputManager(mSerialPort, SerialDeviceManager.this);
							mIOExecutor.submit(mSerialIoManager);
	
							sLogging.info("Connected to the device");
						} catch (Exception e) {
							sLogging.error("Error connecting to the serial service");
							close();
							throw e;
						}
					} else {
						mExecutor.schedule(this, RECONNETION_DELAY, TimeUnit.SECONDS);
					}
				} catch (Exception e) {
					sLogging.info("Error on method scanConnect");
					sLogging.captureStack();
					mExecutor.schedule(this, RECONNETION_DELAY, TimeUnit.SECONDS);
				}
			}
		});
	}

	private void close() {
		mExecutor.execute(new Runnable() {
			@Override
			public void run() {
				try {
					if (mConnection != null) {
						sLogging.info("Closing connection");
						mConnection.close();
						mConnection = null;
					}
				} catch (Exception e) {
					sLogging.error("Error closing connection");
					sLogging.captureStack();
				}

				try {
					if (mSerialPort != null) {
						sLogging.info("Stopping SerialPort");
						mSerialPort.close();
						mSerialPort = null;
					}
				} catch (Exception e) {
					sLogging.error("Error closing serial port");
					sLogging.captureStack();
				}

				try {
					if (mSerialIoManager != null) {
						sLogging.info("Stopping SerialIOManager");
						mSerialIoManager.stop();
						mSerialIoManager = null;
					}
				} catch (Exception e) {
					sLogging.error("Error stopping SeriaIOManager");
					sLogging.captureStack();
				}
			}
		});
	}

	@Override
	public void onNewData(final byte[] data) {
		mExecutor.execute(new Runnable() {
			@Override
			public void run() {
				try {
					for (int i = 0; i < data.length; i++) {
						mStringBuilder.append((char) data[i]);
					}
	
					String content = null;
	
			    	for (int i = 0; i < mStringBuilder.length(); i++) {
			    		if (mStringBuilder.charAt(i) == '\n') {
			    			content = mStringBuilder.substring(0, i-1);
			    			mStringBuilder.delete(0, i+1);
			    		}
			    	}
	
			    	if (content != null) mListener.onLineReceived(content);
				} catch (Exception e) {
					sLogging.error("Error parsing received data from the device: " + data);
					sLogging.captureStack();
				}
			}
		});
	}

	@Override
	public void onRunError(Exception exception) {
		close();
		scanConnect();
	}
}
