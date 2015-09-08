package net.signagewidgets.serial.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import net.signagewidgets.serial.R;
import net.signagewidgets.serial.model.RemoteControl;
import net.signagewidgets.serial.model.RecyclerItemClickListener;
import net.signagewidgets.serial.persistence.DBHelper;
import net.signagewidgets.serial.services.SerialService;
import net.signagewidgets.serial.util.Logging;
import net.signagewidgets.serial.util.RVAdapter;
import net.signagewidgets.serial.view.AddControl;
import net.signagewidgets.serial.view.InfoControl;


public class AttachActivity extends AppCompatActivity {

	private static Logging sLogging = new Logging(AttachActivity.class);

	private final Context CONTEXT = this;

	private RecyclerView mRecyclerView;
	private RVAdapter mRvAdapter;
	private DBHelper mDBHelper;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SerialService.start(this);

		setContentView(R.layout.recycler_view);

		mDBHelper = new DBHelper(this);

		mRecyclerView = (RecyclerView) findViewById(R.id.rv);

		RemoteControl[] mRemoteControls = getControls();

		LinearLayoutManager mLayoutManager = new LinearLayoutManager(CONTEXT);

		mRecyclerView.setLayoutManager(mLayoutManager);

		mRvAdapter = new RVAdapter(this, mRemoteControls, mLayoutManager);

		mRecyclerView.setAdapter(mRvAdapter);

		addListener();
	}

	/**
	 * Get all remote controls that are registered in the DB
	 * @return Returns an array of remote controls
	 */
	public RemoteControl[] getControls() {
		return mDBHelper.getAllControls();
	}

	public void listenerFAB(View view) {
		AddControl addControl = new AddControl(this);
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		if(intent != null){
			mRvAdapter.dataChanged();
			sLogging.error("RECEIVED INTENT");
		}
	}

	public void addListener() {
		mRecyclerView.addOnItemTouchListener(
				new RecyclerItemClickListener(CONTEXT, new RecyclerItemClickListener.OnItemClickListener() {
					@Override
					public void onItemClick(View view, final int position) {
                        setAnimation(view);

                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                new InfoControl(AttachActivity.this, getControls()[position]);
                            }
                        }, 200);
					}
				})
		);
	}

	private void setAnimation(View viewToAnimate) {
			Animation animation = AnimationUtils.loadAnimation(this, android.R.anim.fade_in);
			viewToAnimate.startAnimation(animation);
	}
}
