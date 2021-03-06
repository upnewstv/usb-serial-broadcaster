package tv.onsign.rc.view;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import tv.onsign.rc.R;
import tv.onsign.rc.activities.AttachActivity;
import tv.onsign.rc.persistence.DBHelper;
import tv.onsign.rc.util.RVAdapter;

/**
 * Created by lenoirzamboni on 8/27/15.
 */
public class ExistingControl extends LinearLayout {
    private AlertDialog mAlertDialog;
    private AlertDialog mVerifyButtonsDialog;
    private LayoutInflater mLayoutInflater;
    private Context mContext;
    private TextView mCancel;
    private TextView mOverwrite;
    private long mIDControl;
    private DBHelper mDBHelper;

    public ExistingControl(Context context, int idControl, AlertDialog verifyButtonsDialog) {
        super(context);

        mContext = context;
        mVerifyButtonsDialog = verifyButtonsDialog;
        mIDControl = idControl;
        mLayoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mDBHelper = new DBHelper(context);

        hideVerifyButtons();

        createDialog();

        mCancel = (TextView) mAlertDialog.findViewById(R.id.textView_cancel_overwrite);
        mOverwrite = (TextView) mAlertDialog.findViewById(R.id.textView_overwrite);

        cancelInsertion();
        overwriteControl();
    }

    public void createDialog() {

        RVAdapter.setUnregisterReceiver();

        AlertDialog.Builder mAlertDialogBuilder = new AlertDialog.Builder(mContext);

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        mAlertDialogBuilder.setView(mLayoutInflater.inflate(R.layout.existing_control, null));

        // create alert dialog
        mAlertDialog = mAlertDialogBuilder.create();

        // show it
        mAlertDialog.show();

        mAlertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                RVAdapter.setRegisterReceiver();
            }
        });
    }

    public void dismissPopup() {
        mAlertDialog.dismiss();
    }

    public void cancelInsertion() {
        mCancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                mVerifyButtonsDialog.dismiss();
                dismissPopup();
            }
        });
    }

    public void overwriteControl() {
        mOverwrite.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                mDBHelper.deleteControl((int) mIDControl);

                Intent deletedControl = new Intent(mContext, AttachActivity.class);
                deletedControl.putExtra("id_control_deleted", String.valueOf(mIDControl));
                mContext.startActivity(deletedControl);

                showVerifyButtons();

                dismissPopup();
            }
        });
    }

    public void showVerifyButtons() {
        this.mVerifyButtonsDialog.show();
    }

    public void hideVerifyButtons() {
        this.mVerifyButtonsDialog.hide();
    }


}
