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
public class AddedControl extends LinearLayout {
    private AlertDialog mAlertDialog;
    private LayoutInflater mLayoutInflater;
    private Context mContext;
    private TextView mOK;

    public AddedControl(Context context) {
        super(context);

        mLayoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.mContext = context;

        createDialog();

        mOK = (TextView) mAlertDialog.findViewById(R.id.textView_added_ok);

        showOk();
    }

    public void createDialog() {

        AlertDialog.Builder mAlertDialogBuilder = new AlertDialog.Builder(mContext);

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        mAlertDialogBuilder.setView(mLayoutInflater.inflate(R.layout.added_control, null));

        // create alert dialog
        mAlertDialog = mAlertDialogBuilder.create();

        // show it
        mAlertDialog.show();
    }

    public void dismissPopup() {
        mAlertDialog.dismiss();
    }

    public void showOk() {
        mOK.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                dismissPopup();
            }
        });
    }
}
