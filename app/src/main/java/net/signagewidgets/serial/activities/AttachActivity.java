package net.signagewidgets.serial.activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import net.signagewidgets.serial.R;
import net.signagewidgets.serial.services.SerialService;
import net.signagewidgets.serial.util.RVAdapter;

import java.text.SimpleDateFormat;
import java.util.Date;

import net.signagewidgets.serial.model.RemoteControl;

public class AttachActivity extends AppCompatActivity {

	private FloatingActionButton actionButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SerialService.start(this);
		//finish(); it will finish the activity...

		setContentView(R.layout.recycler_view);

		RecyclerView recyclerView = (RecyclerView)findViewById(R.id.rv);

		RemoteControl[] remoteControlData = {
				new RemoteControl("Control A", new SimpleDateFormat("dd-MM-yyyy").format(new Date()), R.drawable.remote_example),
				new RemoteControl("Control B", new SimpleDateFormat("dd-MM-yyyy").format(new Date()), R.drawable.remote_example),
				new RemoteControl("Control C", new SimpleDateFormat("dd-MM-yyyy").format(new Date()), R.drawable.remote_example),
				new RemoteControl("Control D", new SimpleDateFormat("dd-MM-yyyy").format(new Date()), R.drawable.remote_example),
				new RemoteControl("Control E", new SimpleDateFormat("dd-MM-yyyy").format(new Date()), R.drawable.remote_example),
				new RemoteControl("Control F", new SimpleDateFormat("dd-MM-yyyy").format(new Date()), R.drawable.remote_example),
				new RemoteControl("Control G", new SimpleDateFormat("dd-MM-yyyy").format(new Date()), R.drawable.remote_example),
				new RemoteControl("Control H", new SimpleDateFormat("dd-MM-yyyy").format(new Date()), R.drawable.remote_example)};


		recyclerView.setLayoutManager(new LinearLayoutManager(this));
		RVAdapter rvAdapter = new RVAdapter(remoteControlData);

		recyclerView.setAdapter(rvAdapter);

	}
}
