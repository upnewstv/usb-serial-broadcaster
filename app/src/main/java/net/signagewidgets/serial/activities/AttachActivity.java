package net.signagewidgets.serial.activities;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import net.signagewidgets.serial.R;
import net.signagewidgets.serial.util.Logging;
import net.signagewidgets.serial.util.RVAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.signagewidgets.serial.model.RemoteControl;
import net.signagewidgets.serial.view.AddControl;

public class AttachActivity extends Activity {
	private static Logging sLogging = new Logging(AttachActivity.class);

	private AlertDialog.Builder alertDialogBuilder;
	private AlertDialog alertDialog;
	LayoutInflater inflater;
	final Context context = this;
	private RadioGroup radioGroupButtons;
	private RadioButton radioButton_1;
	private RadioButton radioButton_2;
	private RadioButton radioButton_3;
	private RadioButton radioButton_4;
	private int qtdButtons;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//SerialService.start(this);

		setContentView(R.layout.recycler_view);

		RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rv);

		RemoteControl[] remoteControlData =  getControls();

		LinearLayoutManager layoutManager = new LinearLayoutManager(context);

		recyclerView.setLayoutManager(layoutManager);

		RVAdapter rvAdapter = new RVAdapter(remoteControlData);
		recyclerView.setAdapter(rvAdapter);

		//initializeComponents();
	}

	public void initializeComponents(){

		LayoutInflater inflater = getLayoutInflater();
		View viewAddControl = inflater.inflate(R.layout.add_control, null);

		radioGroupButtons = (RadioGroup) viewAddControl.findViewById(R.id.radioGroup);

		radioButton_1 = (RadioButton) viewAddControl.findViewById(R.id.button_1);
		sLogging.error("IDDDDD" + radioButton_1.getId());
		radioButton_2 = (RadioButton) viewAddControl.findViewById(R.id.button_2);
		radioButton_3 = (RadioButton) viewAddControl.findViewById(R.id.button_3);
		radioButton_4 = (RadioButton) viewAddControl.findViewById(R.id.button_4);

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

		List<Integer> listIdButtons4 = new ArrayList<>();
		listIdButtons4.add(1);
		listIdButtons4.add(2);
		listIdButtons4.add(3);
		listIdButtons4.add(4);

		List<Integer> listIdButtons2 = new ArrayList<>();
		listIdButtons2.add(1);
		listIdButtons2.add(2);

		RemoteControl[] controlsFromDB = {
				new RemoteControl("Control A", getDate(), 0, listIdButtons4),
				new RemoteControl("Control B", getDate(), 1, listIdButtons4),
				new RemoteControl("Control C", getDate(), 2, listIdButtons4),
				new RemoteControl("Control D", getDate(), 3, listIdButtons4),
				new RemoteControl("Control E", getDate(), 4, listIdButtons4),
				new RemoteControl("Control F", getDate(), 5, listIdButtons2),
				new RemoteControl("Control G", getDate(), 7, listIdButtons2),
				new RemoteControl("Control H", getDate(), 8, listIdButtons2),
				new RemoteControl("Control I", getDate(), 9, listIdButtons2),
		};
		return controlsFromDB;
	}

	public void listenerFAB(View view){
		AddControl addControl = new AddControl(this);
	}
}
