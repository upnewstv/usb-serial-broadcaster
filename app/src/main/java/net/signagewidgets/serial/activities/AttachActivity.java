package net.signagewidgets.serial.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
    private RemoteControl[] mRemoteControls;
    public static boolean publicIsConnected;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SerialService.start(this);

		setContentView(R.layout.recycler_view);

		mRecyclerView = (RecyclerView) findViewById(R.id.rv);

        mDBHelper = new DBHelper(this);

        mRemoteControls = getControls();

		LinearLayoutManager mLayoutManager = new LinearLayoutManager(CONTEXT);

		mRecyclerView.setLayoutManager(mLayoutManager);

		mRvAdapter = new RVAdapter(this, mRemoteControls, mLayoutManager);

		mRecyclerView.setAdapter(mRvAdapter);

        setDoodle();
		addListener();
        testReceiverIDButton();
        testReceiverName();
	}

    /**
	 * Get all remote controls that are registered in the DB
	 * @return Returns an array of remote controls
	 */
	public RemoteControl[] getControls() {
		return mDBHelper.getAllControls();
	}

	public void listenerFAB(View view) {
        if(publicIsConnected == true){
            AddControl addControl = new AddControl(this);
            setDoodle();
        }else {
            Toast toast = Toast.makeText(this, this.getString(R.string.message_connect), Toast.LENGTH_SHORT);
            TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
            if( v != null) v.setGravity(Gravity.CENTER);

            toast.setGravity(Gravity.CENTER_HORIZONTAL, 0, 0);
            toast.show();
        }
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		if(intent != null){
            setDoodle();
            mRvAdapter.dataChanged();
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

    public void setDoodle(){

        ImageView mImageViewDoodle = (ImageView) findViewById(R.id.imageView_doodle);

        if(getControls().length == 0){
            mImageViewDoodle.setVisibility(View.VISIBLE);
        }else{
            mImageViewDoodle.setVisibility(View.INVISIBLE);
        }
    }

    public void testReceiverIDButton(){

        IntentFilter filter = new IntentFilter("net.signagewidgets.serial.ID_CONTROL_ID_BUTTON");

        BroadcastReceiver mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                sLogging.error(intent.getExtras().getLong("id_to_onsign"), "ID_CONTROL");
                sLogging.error(intent.getExtras().getLong("button_to_onsign"), "ID_BUTTON");
            }
        };
        this.registerReceiver(mReceiver, filter);
    }

    public void testReceiverName(){

        IntentFilter filter = new IntentFilter("net.signagewidgets.serial.NAME_ID_BUTTON");

        BroadcastReceiver mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                sLogging.error(intent.getExtras().getString("name_to_onsign"), "NAME");
                sLogging.error(intent.getExtras().getLong("button_to_onsign"), "ID_BUTTON");
            }
        };
        this.registerReceiver(mReceiver, filter);
    }


}
