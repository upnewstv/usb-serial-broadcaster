package net.signagewidgets.serial.view;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.signagewidgets.serial.R;

/**
 * Created by lenoirzamboni on 8/27/15.
 */
public class AddedControl extends LinearLayout {
    private AlertDialog.Builder alertDialogBuilder;
    private AlertDialog alertDialog;
    private LayoutInflater li;
    private Context context;
    private TextView ok;

    public AddedControl(Context context) {
        super(context);

        li = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context = context;

        createDialog();

        ok = (TextView) alertDialog.findViewById(R.id.textView_added_ok);

        showOk();
    }

    public void createDialog(){

        alertDialogBuilder = new AlertDialog.Builder(context);

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        alertDialogBuilder.setView(li.inflate(R.layout.added_control, null));

        // create alert dialog
        alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }

    public void dismissPopup(){
        alertDialog.dismiss();
    }

    public void showOk(){
        ok.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                dismissPopup();
            }
        });
    }
}
