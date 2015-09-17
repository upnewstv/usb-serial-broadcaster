package net.signagewidgets.serial.view;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.signagewidgets.serial.R;
import net.signagewidgets.serial.activities.AttachActivity;
import net.signagewidgets.serial.persistence.DBHelper;
import net.signagewidgets.serial.util.Logging;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by lenoirzamboni on 8/27/15.
 */
public class VerifyButtons extends LinearLayout {

    private static Logging sLogging = new Logging(VerifyButtons.class);

    private AlertDialog mAlertDialog;
    private LayoutInflater mLayoutInflater;
    private Context mContext;
    private int mNumberButtons;
    private int mCountTimes;
    private int mCountClicks;
    private String mName;
    private TextView mRestart;
    private TextView mCancel;
    private TextView mClick01;
    private TextView mClick02;
    private TextView mClick03;
    private TextView mDescription;
    private ImageView mOK;
    private Integer mIDControl;
    private Integer mIDButton;
    private List<Integer> mListIdButtons;
    private BroadcastReceiver mReceiver;
    private DBHelper mDBHelper;
    private int mPreventDialog;


    public VerifyButtons(Context context, String name, int numberButtons) {
        super(context);

        mDBHelper = new DBHelper(context);

        IntentFilter filter = new IntentFilter("net.signagewidgets.serial.BUTTON");

        context.registerReceiver(mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                int idControl = intent.getExtras().getInt("id");
                int idButton = intent.getExtras().getInt("button");
                addButton(idControl, idButton);
            }
        }, filter);


        mLayoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mContext = context;
        mNumberButtons = numberButtons;
        mName = name;
        mListIdButtons = new ArrayList<>(numberButtons);

        createDialog();

        mRestart = (TextView) mAlertDialog.findViewById(R.id.textView_restart);
        mCancel = (TextView) mAlertDialog.findViewById(R.id.textView_cancel_verify);
        mClick01 = (TextView) mAlertDialog.findViewById(R.id.click_1);
        mClick02 = (TextView) mAlertDialog.findViewById(R.id.click_2);
        mClick03 = (TextView) mAlertDialog.findViewById(R.id.click_3);
        mDescription = (TextView) mAlertDialog.findViewById(R.id.textView_description_register_control);

        mOK = (ImageView) mAlertDialog.findViewById(R.id.imageView_verify_ok);

        cancel();
        restart();
    }

    public void createDialog() {

        AlertDialog.Builder mAlertDialogBuilder = new AlertDialog.Builder(mContext);

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        mAlertDialogBuilder.setView(mLayoutInflater.inflate(R.layout.verify_buttons, null));

        // create alert dialog
        mAlertDialog = mAlertDialogBuilder.create();

        // show it
        mAlertDialog.show();
    }

    public void dismissPopup() {
        if (mReceiver != null) {
            mContext.unregisterReceiver(mReceiver);
            mReceiver = null;
        }
        mAlertDialog.dismiss();
    }

    public void cancel() {
        mCancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                mCountTimes = 0;
                mNumberButtons = 0;
                dismissPopup();
            }
        });
    }

    public void restart() {
        mRestart.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                dismissPopup();
                new AddControl(mContext);
            }
        });
    }

    public void addButton(Integer idControl, Integer idButton) {

        if(this.mIDControl == null) {
            this.mIDControl = idControl;
        }

        if(mDBHelper.controlExists(idControl)) {
            if(mPreventDialog == 0){
                new ExistingControl(mContext, idControl, mAlertDialog);
                mPreventDialog++;
            }
        }else{
            if(this.mIDControl.equals(idControl) && this.mIDButton == null && !mListIdButtons.contains(idButton)) {
                this.mIDButton = idButton;
            }

            assert this.mIDButton != null;

            if(this.mIDControl.equals(idControl) && this.mIDButton != null && this.mIDButton.equals(idButton)) {

                sLogging.error("ID Control", idControl);
                sLogging.error("ID Button" , idButton);

                if(!mListIdButtons.contains(idButton) || mListIdButtons == null) {
                    mCountClicks++;

                    switch (mCountClicks) {
                        case 1:
                            mClick01.setBackgroundResource(R.drawable.circle_verified);
                            break;

                        case 2:
                            mClick02.setBackgroundResource(R.drawable.circle_verified);
                            break;

                        case 3:
                            mClick03.setBackgroundResource(R.drawable.circle_verified);

                            mListIdButtons.add(idButton);

                            mCountTimes++;
                            this.mIDButton = null;

                            final Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    mOK.setVisibility(VISIBLE);
                                }
                            }, 200);

                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    if (mCountTimes == mNumberButtons) {
                                        insertControlDB();
                                        Intent createdControl = new Intent(mContext, AttachActivity.class);
                                        createdControl.putExtra("control_created", "net.signagewidgets.serial.CONTROL_CREATED");
                                        mContext.startActivity(createdControl);
                                    }
                                    confirm();
                                }
                            }, 3000);

                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    if (mCountTimes == mNumberButtons) {
                                        dismissPopup();
                                        new AddedControl(mContext);
                                    }
                                }
                            }, 2000);
                            break;
                    }
                }
            }
        }
    }

    public void confirm() {
        setTextClicks(mCountTimes + 1);
        mOK.setVisibility(GONE);
        mCountClicks = 0;
    }

    public void setTextClicks(int num) {

        switch (num) {
            case 1:
                mClick01.setText(R.string.name_button_a);
                mClick02.setText(R.string.name_button_a);
                mClick03.setText(R.string.name_button_a);
                mClick01.setBackgroundResource(R.drawable.circle_unverified);
                mClick02.setBackgroundResource(R.drawable.circle_unverified);
                mClick03.setBackgroundResource(R.drawable.circle_unverified);
                mDescription.setText(R.string.description_register_button_first);
                break;
            case 2:
                mClick01.setText(R.string.name_button_b);
                mClick02.setText(R.string.name_button_b);
                mClick03.setText(R.string.name_button_b);
                mClick01.setBackgroundResource(R.drawable.circle_unverified);
                mClick02.setBackgroundResource(R.drawable.circle_unverified);
                mClick03.setBackgroundResource(R.drawable.circle_unverified);
                mDescription.setText(R.string.description_register_button_second);
                break;
            case 3:
                mClick01.setText(R.string.name_button_c);
                mClick02.setText(R.string.name_button_c);
                mClick03.setText(R.string.name_button_c);
                mClick01.setBackgroundResource(R.drawable.circle_unverified);
                mClick02.setBackgroundResource(R.drawable.circle_unverified);
                mClick03.setBackgroundResource(R.drawable.circle_unverified);
                mDescription.setText(R.string.description_register_button_third);
                break;
            case 4:
                mClick01.setText(R.string.name_button_d);
                mClick02.setText(R.string.name_button_d);
                mClick03.setText(R.string.name_button_d);
                mClick01.setBackgroundResource(R.drawable.circle_unverified);
                mClick02.setBackgroundResource(R.drawable.circle_unverified);
                mClick03.setBackgroundResource(R.drawable.circle_unverified);
                mDescription.setText(R.string.description_register_button_fourth);
                break;
        }
    }

    public void insertControlDB() {
       mDBHelper.insertControl(this.mName, mContext.getString(R.string.date_added) + " " + getDate(), this.mIDControl, this.mListIdButtons);
    }

    /**
     * Get the current date
     * @return Return a string that is the current date
     */
    public String getDate() {
        Locale current = getResources().getConfiguration().locale;
        DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT, current);
        return df.format(new Date());
    }
}
