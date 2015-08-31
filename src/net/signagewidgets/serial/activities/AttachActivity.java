package net.signagewidgets.serial.activities;

import android.app.Activity;
import android.os.Bundle;

import net.signagewidgets.serial.services.SerialService;

public class AttachActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SerialService.start(this);
		finish();
	}
}
