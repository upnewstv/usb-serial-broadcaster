package net.signagewidgets.serial.activities;

import android.app.Activity;
import android.app.Notification;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import net.signagewidgets.serial.R;
import net.signagewidgets.serial.services.SerialService;

public class AttachActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SerialService.start(this);
		finish();
	}
}
