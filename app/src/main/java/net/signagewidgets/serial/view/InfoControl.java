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
import net.signagewidgets.serial.util.Logging;

/**
 * Created by lenoirzamboni on 8/27/15.
 */
public class InfoControl extends LinearLayout {

    private static Logging sLogging = new Logging(InfoControl.class);

    private AlertDialog.Builder alertDialogBuilder;
    private AlertDialog alertDialog;
    private LayoutInflater li;
    private Context context;

    private TextView cancel;
    private TextView save;
    private TextView delete;

    private String name;
    private EditText nameControl;
    private RemoteControl remoteControl;

    private DBHelper dbHelper;

    public InfoControl(Context context, RemoteControl remoteControl) {
        super(context);

        li = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context = context;
        this.remoteControl = remoteControl;

        dbHelper = new DBHelper(this.context);

        createDialog();

        delete = (TextView) alertDialog.findViewById(R.id.textView_delete_info);
        cancel = (TextView) alertDialog.findViewById(R.id.textView_cancel_info);
        save = (TextView) alertDialog.findViewById(R.id.textView_save_info);

        nameControl = (EditText) alertDialog.findViewById(R.id.EditText_name_control_info);
        nameControl.setText(remoteControl.getName());

        delete();
        updateName();
        cancel();

        alertDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
    }

    public void createDialog(){

        alertDialogBuilder = new AlertDialog.Builder(new ContextThemeWrapper(context, R.style.dialogTheme));

        // create alert dialog
        alertDialog = alertDialogBuilder.create();

        alertDialog.getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        alertDialogBuilder.setView(li.inflate(R.layout.add_control, null));


        // show it
        alertDialog.show();

        alertDialog.setContentView(R.layout.info_control);
    }

    public void dismissPopup(){
        alertDialog.dismiss();
    }

    public void delete(){
        delete.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                dbHelper.deleteControl((int) remoteControl.getIdControl());
                sendCommandUpdate();
                dismissPopup();
            }
        });
    }

    public void updateName(){
        save.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                setNameControl();
                dbHelper.updateNameControl((int) remoteControl.getIdControl(), name);
                sendCommandUpdate();
                dismissPopup();

            }
        });
    }

    public void cancel(){
        cancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                dismissPopup();
            }
        });
    }

    public void setNameControl(){
        name = nameControl.getText().toString();
    }

    public void sendCommandUpdate(){
        Intent createdControl = new Intent(context, AttachActivity.class);
        createdControl.putExtra("control_update", "net.signagewidgets.serial.CONTROL_UPDATE");
        context.startActivity(createdControl);
    }
}
