package net.signagewidgets.serial.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import net.signagewidgets.serial.R;
import net.signagewidgets.serial.persistence.DBHelper;
import net.signagewidgets.serial.services.SerialService;
import net.signagewidgets.serial.util.Logging;
import net.signagewidgets.serial.util.RVAdapter;

import net.signagewidgets.serial.model.RemoteControl;
import net.signagewidgets.serial.view.AddControl;

import java.util.List;

public class AttachActivity extends Activity {
	private static Logging sLogging = new Logging(AttachActivity.class);
	final Context context = this;
	RecyclerView recyclerView;
	LinearLayoutManager layoutManager;
	RemoteControl[] remoteControls;
	RVAdapter rvAdapter;
	DBHelper db;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SerialService.start(this);

		setContentView(R.layout.recycler_view);

		db = new DBHelper(this);

		recyclerView = (RecyclerView) findViewById(R.id.rv);

		remoteControls = getControls();

		layoutManager = new LinearLayoutManager(context);

		recyclerView.setLayoutManager(layoutManager);

		rvAdapter = new RVAdapter(remoteControls);

		recyclerView.setAdapter(rvAdapter);

		showControls();
	}

	/**
	 * Get all remote controls that are registered in the DB
	 * @return Returns an array of remote controls
	 */
	public RemoteControl[] getControls(){
		return db.getAllControls();
	}

	public void listenerFAB(View view){
		AddControl addControl = new AddControl(this);
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		if(intent != null){
			this.recreate();
		}
	}

	public void showControls(){
		for(int i = 0; i < remoteControls.length; i++){
			sLogging.error( "\n", "NAME", remoteControls[i].getName());
			sLogging.error("DATE", remoteControls[i].getDate());
			sLogging.error("ID_CONTROL", remoteControls[i].getIdControl());

			List<Long> list = remoteControls[i].getIdButtons();

			for(int j = 0; j < list.size(); j++){
				sLogging.error("ID_BUTTON", j, list.get(j));
			}
		}
	}
}
