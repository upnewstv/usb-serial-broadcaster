package net.signagewidgets.serial.view;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.signagewidgets.serial.R;
import net.signagewidgets.serial.persistence.DBHelper;

/**
 * Created by lenoirzamboni on 8/27/15.
 */
public class ExistingControl extends LinearLayout {
    private AlertDialog.Builder alertDialogBuilder;
    private AlertDialog alertDialog;
    private AlertDialog alertDialogToCancel;
    private LayoutInflater li;
    private Context context;
    private TextView cancel;
    private TextView overwrite;
    private long idControl;
    private DBHelper dbHelper;


    public ExistingControl(Context context, Long idControl, AlertDialog alertDialogToCancel) {
        super(context);

        li = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context = context;
        this.alertDialogToCancel = alertDialogToCancel;
        this.idControl = idControl;
        dbHelper = new DBHelper(context);

        hideVerifyButtons();

        createDialog();

        cancel = (TextView) alertDialog.findViewById(R.id.textView_cancel_overwrite);
        overwrite = (TextView) alertDialog.findViewById(R.id.textView_overwrite);

        cancelInsertion();
        overwriteControl();
    }

    public void createDialog(){

        alertDialogBuilder = new AlertDialog.Builder(context);

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        alertDialogBuilder.setView(li.inflate(R.layout.existing_control, null));

        // create alert dialog
        alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }

    public void dismissPopup(){
        alertDialog.dismiss();
    }

    public void cancelInsertion(){
        cancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialogToCancel.dismiss();
                dismissPopup();
            }
        });
    }

    public void overwriteControl(){
        overwrite.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                dbHelper.deleteControl((int) idControl);
                showVerifyButtons();
                dismissPopup();
            }
        });
    }

    public void showVerifyButtons(){
        this.alertDialogToCancel.show();
    }

    public void hideVerifyButtons(){
        this.alertDialogToCancel.hide();
    }
}
