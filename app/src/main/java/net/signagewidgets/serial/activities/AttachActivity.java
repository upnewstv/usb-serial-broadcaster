package net.signagewidgets.serial.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import net.signagewidgets.serial.R;
import net.signagewidgets.serial.services.SerialService;
import net.signagewidgets.serial.util.RVAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

		RemoteControl[] remoteControlData =  getControls();

		recyclerView.setLayoutManager(new LinearLayoutManager(this));

		RVAdapter rvAdapter = new RVAdapter(remoteControlData);

		recyclerView.setAdapter(rvAdapter);
	}

	/**
	 * Get the current date
	 * @return Return a string that is the current date
	 */
	public String getDate(){
		return new SimpleDateFormat("dd-MM-yyyy").format(new Date());
	}

	//this method will be in the DBActions class...
	/**
	 * Add a control in DB
	 */
	public void addControl(String name, String date, int idControl, List<Integer> idButtons){
		new RemoteControl(name, date, idControl, idButtons);
	}

	//this method will be in the DBActions class...
	/**
	 * Get all remote controls that are registered in the DB
	 * @return Returns an array of remote controls
	 */
	public RemoteControl[] getControls(){
		RemoteControl[] controlsFromDB = null;
		return controlsFromDB;
	}
}
