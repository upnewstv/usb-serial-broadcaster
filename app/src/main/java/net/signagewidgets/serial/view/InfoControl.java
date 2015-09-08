package net.signagewidgets.serial.view;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.signagewidgets.serial.R;
import net.signagewidgets.serial.activities.AttachActivity;
import net.signagewidgets.serial.model.RemoteControl;
import net.signagewidgets.serial.persistence.DBHelper;

/**
 * Created by lenoirzamboni on 8/27/15.
 */
public class InfoControl extends LinearLayout {

    private AlertDialog mAlertDialog;
    private LayoutInflater mLayoutInflater;
    private Context mContext;

    private TextView mCancel;
    private TextView mSave;
    private TextView mDelete;

    private String mName;
    private EditText mNameControl;
    private RemoteControl mRemoteControl;

    private DBHelper dbHelper;

    public InfoControl(Context context, RemoteControl remoteControl) {
        super(context);

        mLayoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.mContext = context;
        this.mRemoteControl = remoteControl;

        dbHelper = new DBHelper(this.mContext);

        createDialog();

        mDelete = (TextView) mAlertDialog.findViewById(R.id.textView_delete_info);
        mCancel = (TextView) mAlertDialog.findViewById(R.id.textView_cancel_info);
        mSave = (TextView) mAlertDialog.findViewById(R.id.textView_save_info);

        mNameControl = (EditText) mAlertDialog.findViewById(R.id.EditText_name_control_info);
        mNameControl.setText(remoteControl.getName());

        delete();
        updateName();
        cancel();

        mAlertDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
    }

    public void createDialog() {

        AlertDialog.Builder mAlertDialogBuilder = new AlertDialog.Builder(new ContextThemeWrapper(mContext, R.style.dialogTheme));

        // create alert dialog
        mAlertDialog = mAlertDialogBuilder.create();

        mAlertDialog.getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        mAlertDialogBuilder.setView(mLayoutInflater.inflate(R.layout.add_control, null));


        // show it
        mAlertDialog.show();

        mAlertDialog.setContentView(R.layout.info_control);
    }

    public void dismissPopup() {
        mAlertDialog.dismiss();
    }

    public void delete() {
        mDelete.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                int idControl = (int) mRemoteControl.getIDControl();
                dbHelper.deleteControl(idControl);

                Intent deletedControl = new Intent(mContext, AttachActivity.class);
                deletedControl.putExtra("id_control_deleted", String.valueOf(idControl));
                mContext.startActivity(deletedControl);

                dismissPopup();
            }
        });
    }

    public void updateName() {
        mSave.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                int idControl = (int) mRemoteControl.getIDControl();

                setNameControl();
                dbHelper.updateNameControl(idControl, mName);

                Intent updateControl = new Intent(mContext, AttachActivity.class);
                updateControl.putExtra("id_control_updated", String.valueOf(idControl));
                mContext.startActivity(updateControl);

                dismissPopup();

            }
        });
    }

    public void cancel() {
        mCancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                dismissPopup();
            }
        });
    }

    public void setNameControl() {
        mName = mNameControl.getText().toString();
        this.mRemoteControl.setName(mName);

    }
}
