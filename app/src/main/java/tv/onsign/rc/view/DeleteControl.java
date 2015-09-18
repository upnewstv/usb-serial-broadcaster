package tv.onsign.rc.view;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import tv.onsign.rc.R;

/**
 * Created by lenoirzamboni on 8/27/15.
 */
public class DeleteControl extends LinearLayout {

    protected AlertDialog.Builder mAlertDialogBuilder;
    protected AlertDialog mAlertDialog;
    protected LayoutInflater mLayoutInflater;
    protected Context mContext;
    protected TextView mCancel;
    protected TextView mDelete;

    public DeleteControl(Context context) {
        super(context);

        mLayoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.mContext = context;

        createDialog();

        mCancel = (TextView) mAlertDialog.findViewById(R.id.textView_cancel_delete);
        mDelete = (TextView) mAlertDialog.findViewById(R.id.textView_delete);

        showOk();
    }

    public void createDialog() {

        mAlertDialogBuilder = new AlertDialog.Builder(mContext);

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        mAlertDialogBuilder.setView(mLayoutInflater.inflate(R.layout.delete_control, null));

        // create alert dialog
        mAlertDialog = mAlertDialogBuilder.create();

        // show it
        mAlertDialog.show();
    }

    public void dismissPopup() {
        mAlertDialog.dismiss();
    }

    public void showOk() {
        mCancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                dismissPopup();
            }
        });
    }

    public void deleteControl() {
        mDelete.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                dismissPopup();
            }
        });
    }
}
