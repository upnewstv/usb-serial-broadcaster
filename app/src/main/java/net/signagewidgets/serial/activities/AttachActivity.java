package net.signagewidgets.serial.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
    private LinearLayoutManager mLayoutManager;
    private RecyclerView mRecyclerView;
	private RVAdapter mRvAdapter;
	private DBHelper mDBHelper;
    private RemoteControl[] mRemoteControls;
    public static boolean publicIsConnected;
    private boolean mStartToast = true;

    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		SerialService.start(this);

		setContentView(R.layout.recycler_view);
		mRecyclerView = (RecyclerView) findViewById(R.id.rv);
        mDBHelper = new DBHelper(this);
        mRemoteControls = getControls();
		mLayoutManager = new LinearLayoutManager(CONTEXT);
		mRecyclerView.setLayoutManager(mLayoutManager);

        setDoodle();
		addListener();
	}

    public void onStart() {
        super.onStart();
        mRvAdapter = new RVAdapter(this, mRemoteControls, mLayoutManager);
        mRecyclerView.setAdapter(mRvAdapter);
    }

    public void onStop() {
        mRvAdapter.destroy();
        super.onStop();
    }

    public void onDestroy() {
        super.onDestroy();
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

            if(mStartToast){
                toast.show();
                mStartToast = false;
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mStartToast = true;
                    }
                }, 2000);
            }
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
        LinearLayout mLinearLayoutDoodle = (LinearLayout) findViewById(R.id.linear_layout_doodle);
        TextView mMessageDoodle = (TextView) findViewById(R.id.textView_message_doodle);

        double screenSize = getScreenSize();

        if(screenSize < 6){
            mMessageDoodle.setTextSize(20);
        }

        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){

            if(screenSize < 6){
                mMessageDoodle.setText(this.getString(R.string.message_doodle_small_screen));
            }

            mMessageDoodle.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL);
            mLinearLayoutDoodle.setOrientation(LinearLayout.VERTICAL);
            mImageViewDoodle.setLayoutParams(new LinearLayout.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT, Gravity.CENTER | Gravity.TOP));
            mImageViewDoodle.setPadding(0, 0, 40, 0);
        } else {
            mMessageDoodle.setText(this.getString(R.string.message_doodle_landscape));
            mMessageDoodle.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
            mMessageDoodle.setPadding(0, 0, 40, 0);
            mLinearLayoutDoodle.setOrientation(LinearLayout.HORIZONTAL);
            mImageViewDoodle.setLayoutParams(new LinearLayout.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT, Gravity.LEFT));
            mImageViewDoodle.setPadding(40, 0, 0, 0);
        }

        if(getControls().length == 0){
            mImageViewDoodle.setVisibility(View.VISIBLE);
            mMessageDoodle.setVisibility(View.VISIBLE);
        }else{
            mImageViewDoodle.setVisibility(View.INVISIBLE);
            mMessageDoodle.setVisibility(View.INVISIBLE);
        }
    }

    public double getScreenSize(){
        DisplayMetrics dm = getResources().getDisplayMetrics();
        double density = dm.density * 160;
        double x = Math.pow(dm.widthPixels / density, 2);
        double y = Math.pow(dm.heightPixels / density, 2);
        return Math.sqrt(x + y);
    }

}
