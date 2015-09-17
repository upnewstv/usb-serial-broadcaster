package net.signagewidgets.serial.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import net.signagewidgets.serial.R;
import net.signagewidgets.serial.model.RemoteControl;
import net.signagewidgets.serial.persistence.DBHelper;

/**
 * Created by lenoirzamboni on 8/20/15.
 */

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.ViewHolder> {
    private static Logging sLogging = new Logging(RVAdapter.class);
    private RemoteControl [] mRemoteControls;
    private LinearLayoutManager mLayoutManager;
    private TextView mTextViewItem;
    private DBHelper mDBHelper;
    private Context mContext;
    private boolean mStartToast = true;

    public RVAdapter(Context context, RemoteControl[] remoteControls, LinearLayoutManager layoutManager) {
        mRemoteControls = remoteControls;
        mLayoutManager = layoutManager;
        mDBHelper = new DBHelper(context);
        mContext = context;

        IntentFilter filter = new IntentFilter("net.signagewidgets.serial.BUTTON");

        mContext.registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                int id = intent.getExtras().getInt("id");
                int button = intent.getExtras().getInt("button");
            }
        }, filter);
    }

    /**
     * Inner class to hold a reference to each item of RecyclerView
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView controlName;
        public TextView dateAdd;
        public ImageView iconControl;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            controlName = (TextView) itemLayoutView.findViewById(R.id.no_controls);
            dateAdd = (TextView) itemLayoutView.findViewById(R.id.date_add);
            iconControl = (ImageView) itemLayoutView.findViewById(R.id.icon_control);
        }
    }

    /**
     * Called when RecyclerView needs a new RecyclerView.ViewHolder of the given type to represent an item
     * (Create new views (invoked by the layout manager))
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public RVAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        // For each item:

        // Inflate item_layout
        // Create a ViewHolder
        // Bind the view “ViewHolder” with data “mRemoteControls”

        View itemLayoutView;

        switch (viewType) {

            case 1:
                // create a new view with room for 1 button
                itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_control_1_button, null);
                break;

            case 2:
                // create a new view with room for 2 buttons
                itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_control_2_buttons, null);
                break;

            case 3:
                // create a new view with room for 3 buttons
                itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_control_3_buttons, null);
                break;

            default:
                // create a new view with room for 4 buttons
                itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_control_4_buttons, null);
                break;
        }

        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        itemLayoutView.setLayoutParams(lp);
        ViewHolder holder = new ViewHolder(itemLayoutView);

        return holder;
    }

    /**
     * Return the view type of the item at position for the purposes of view recycling
     *
     * @param position Position that contains the remote control on RemoteControl array
     * @return 2 if the remote control has 2 buttons, 4 if contains 4 buttons
     */
    @Override
    public int getItemViewType(int position) {

        int type = 0;

        if(mRemoteControls.length == 0) {
            type = 0;
        }else {
            type = mRemoteControls[position].getIdButtons().size();
        }
        return type;
    }

    /**
     * Method that replace the contents of a view (invoked by the layout manager)
     *
     * @param viewHolder
     * @param position
     */
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {

        // - get data from your mRemoteControls at this position
        // - replace the contents of the view with that mRemoteControls

        viewHolder.controlName.setText(mRemoteControls[position].getName());
        viewHolder.dateAdd.setText(mRemoteControls[position].getDate());

        switch (mRemoteControls[position].getIdButtons().size()) {
            case 1:
                viewHolder.iconControl.setImageResource(R.drawable.rc_icon_1);
                break;

            case 2:
                viewHolder.iconControl.setImageResource(R.drawable.rc_icon_2);
                break;

            case 3:
                viewHolder.iconControl.setImageResource(R.drawable.rc_icon_3);
                break;

            case 4:
                viewHolder.iconControl.setImageResource(R.drawable.rc_icon_4);
                break;

        }
    }

    // Return the size of your mRemoteControls (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mRemoteControls.length;
    }

    void findControl(int idControl, int idButton) {

        for(int i = 0; i < getItemCount(); i++){

            if(mRemoteControls[i].getIDControl() == idControl) {

                if(mRemoteControls[i].getIdButtons().contains(idButton)) {

                    switch (mRemoteControls[i].getIdButtons().indexOf(idButton)) {
                        case 0:
                            changeBG(0, i, mRemoteControls[i]);
                            break;

                        case 1:
                            changeBG(1, i, mRemoteControls[i]);
                            break;

                        case 2:
                            changeBG(2, i, mRemoteControls[i]);
                            break;

                        case 3:
                            changeBG(3, i, mRemoteControls[i]);
                            break;
                    }
                }
            }
        }
    }

    public void changeBG(int button, int listPosition, final RemoteControl remoteControl) {

        int firstPosition = mLayoutManager.findFirstVisibleItemPosition();
        int wantedChild = listPosition - firstPosition;

        String mLetter = "";

        View item = this.mLayoutManager.getChildAt(wantedChild);

        switch (button) {
            case 0:
                mTextViewItem = (TextView) item.findViewById(R.id.button_1);
                mTextViewItem.setBackgroundResource(R.drawable.buttons_layout_verified);
                mLetter = mContext.getString(R.string.name_button_a);
                break;

            case 1:
                mTextViewItem = (TextView) item.findViewById(R.id.button_2);
                mTextViewItem.setBackgroundResource(R.drawable.buttons_layout_verified);
                mLetter = mContext.getString(R.string.name_button_b);
                break;

            case 2:
                mTextViewItem = (TextView) item.findViewById(R.id.button_3);
                mTextViewItem.setBackgroundResource(R.drawable.buttons_layout_verified);
                mLetter = mContext.getString(R.string.name_button_c);
                break;

            case 3:
                mTextViewItem = (TextView) item.findViewById(R.id.button_4);
                mTextViewItem.setBackgroundResource(R.drawable.buttons_layout_verified);
                mLetter = mContext.getString(R.string.name_button_d);
                break;
        }

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mTextViewItem != null) {
                    mTextViewItem.setBackgroundResource(R.drawable.buttons_layout_unverified);
                }
            }
        }, 300);

        final String finalMLetter = mLetter;

        Toast toast = Toast.makeText(mContext,
                mContext.getString(R.string.pressed_message_part_1) + " " + finalMLetter + " " +
                mContext.getString(R.string.pressed_message_part_2) + " " + remoteControl.getName() + " " +
                mContext.getString(R.string.pressed_message_part_3), Toast.LENGTH_SHORT);

        TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
        if (v != null) v.setGravity(Gravity.CENTER);

        if(mStartToast){
            toast.show();
            mStartToast = false;
            handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mStartToast = true;
                }
            }, 2000);
        }
    }

    public void dataChanged() {
        this.mRemoteControls = getControls();
        this.notifyDataSetChanged();
        sLogging.error("COMMAND RECEIVED");
    }

    public RemoteControl[] getControls() {
        return mDBHelper.getAllControls();
    }

    public void sendBroadcasts(long idControl, long idButton, String name){

        Intent intentIDs = new Intent("net.signagewidgets.serial.ID_CONTROL_ID_BUTTON");
        intentIDs.putExtra("id_to_onsign", idControl);
        intentIDs.putExtra("button_to_onsign", idButton);
        mContext.sendBroadcast(intentIDs);

        Intent intentName = new Intent("net.signagewidgets.serial.NAME_ID_BUTTON");
        intentName.putExtra("name_to_onsign", name);
        intentName.putExtra("button_to_onsign", idButton);
        mContext.sendBroadcast(intentName);
    }
}
