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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by lenoirzamboni on 8/27/15.
 */
public class VerifyButtons extends LinearLayout {

    Logging logging = new Logging(VerifyButtons.class);

    private AlertDialog.Builder alertDialogBuilder;
    private AlertDialog alertDialog;
    private LayoutInflater li;
    private Context context;
    private int numberButtons;
    private int countTimes;
    private String name;
    private TextView restart;
    private TextView cancel;
    private TextView click01;
    private TextView click02;
    private TextView click03;
    private ImageView ok;
    private Long idControl;
    private Long idButton;
    private List<Long> listIdButtons;
    private TextView description;
    private int countClicks;
    private BroadcastReceiver receiver;

    private DBHelper dbHelper;

    public VerifyButtons(Context context, String name, int numberButtons) {
        super(context);

        dbHelper = new DBHelper(context);

        IntentFilter filter = new IntentFilter("net.signagewidgets.serial.BUTTON");

        context.registerReceiver(receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Long idControl = intent.getExtras().getLong("id");
                Long idButton = intent.getExtras().getLong("button");

                addButton(idControl, idButton);
            }
        }, filter);


        li = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context = context;
        this.numberButtons = numberButtons;
        this.name = name;
        listIdButtons = new ArrayList<>(numberButtons);

        createDialog();

        restart = (TextView) alertDialog.findViewById(R.id.textView_restart);
        cancel = (TextView) alertDialog.findViewById(R.id.textView_cancel_verify);

        click01 = (TextView) alertDialog.findViewById(R.id.click_1);
        click02 = (TextView) alertDialog.findViewById(R.id.click_2);
        click03 = (TextView) alertDialog.findViewById(R.id.click_3);

        ok = (ImageView) alertDialog.findViewById(R.id.imageView_verify_ok);

        description = (TextView) alertDialog.findViewById(R.id.textView_description_register_control);

        cancel();
        restart();
    }

    public void createDialog(){

        alertDialogBuilder = new AlertDialog.Builder(context);

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        alertDialogBuilder.setView(li.inflate(R.layout.verify_buttons, null));

        // create alert dialog
        alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }

    public void dismissPopup(){
        alertDialog.dismiss();

        if (receiver != null) {
            context.unregisterReceiver(receiver);
            receiver = null;
        }
    }

    public void cancel(){
        cancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                clean();
                numberButtons = 0;
                dismissPopup();
            }
        });
    }

    public void restart(){
        restart.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                dismissPopup();
                new AddControl(context);
            }
        });
    }

    public void addButton(Long idControl, Long idButton) {

        if(this.idControl == null){
            this.idControl = idControl;
        }

        if(dbHelper.controlExists((int)(long) idControl)){
           new ExistingControl(context, idControl, alertDialog);
        }

        if(this.idControl.equals(idControl) && this.idButton == null && !listIdButtons.contains(idButton)){
            this.idButton = idButton;
        }

        assert this.idButton != null;

        if(this.idControl.equals(idControl) && this.idButton != null && this.idButton.equals(idButton)){

            logging.error("ID Control" , idControl);
            logging.error("ID Button" , idButton);

            if(!listIdButtons.contains(idButton) || listIdButtons == null){
                countClicks++;

                switch (countClicks) {
                    case 1:
                        click01.setBackgroundResource(R.drawable.circle_verified);
                        break;

                    case 2:
                        click02.setBackgroundResource(R.drawable.circle_verified);
                        break;

                    case 3:
                        click03.setBackgroundResource(R.drawable.circle_verified);

                        listIdButtons.add(idButton);

                        countTimes++;
                        this.idButton = null;

                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (countTimes == numberButtons) {
                                    ok.setVisibility(VISIBLE);
                                }
                            }
                        }, 1000);

                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                //add control -> DB
                                if (countTimes == numberButtons) {
                                    insertControlDB();
                                    Intent createdControl = new Intent(context, AttachActivity.class);
                                    createdControl.putExtra("control_created", "net.signagewidgets.serial.CONTROL_CREATED");
                                    context.startActivity(createdControl);
                                    dismissPopup();
                                }
                                confirm();
                            }
                        }, 2000);
                        break;
                }
            }
        }
    }

    public void confirm() {
        setTextClicks(countTimes + 1);
        countClicks = 0;
    }

    public void setTextClicks(int num){

        switch (num){
            case 1:
                click01.setText("A");
                click02.setText("A");
                click03.setText("A");
                click01.setBackgroundResource(R.drawable.circle_unverified);
                click02.setBackgroundResource(R.drawable.circle_unverified);
                click03.setBackgroundResource(R.drawable.circle_unverified);
                description.setText(R.string.description_register_button_first);
                break;
            case 2:
                click01.setText("B");
                click02.setText("B");
                click03.setText("B");
                click01.setBackgroundResource(R.drawable.circle_unverified);
                click02.setBackgroundResource(R.drawable.circle_unverified);
                click03.setBackgroundResource(R.drawable.circle_unverified);
                description.setText(R.string.description_register_button_second);
                break;
            case 3:
                click01.setText("C");
                click02.setText("C");
                click03.setText("C");
                click01.setBackgroundResource(R.drawable.circle_unverified);
                click02.setBackgroundResource(R.drawable.circle_unverified);
                click03.setBackgroundResource(R.drawable.circle_unverified);
                description.setText(R.string.description_register_button_third);
                break;
            case 4:
                click01.setText("D");
                click02.setText("D");
                click03.setText("D");
                click01.setBackgroundResource(R.drawable.circle_unverified);
                click02.setBackgroundResource(R.drawable.circle_unverified);
                click03.setBackgroundResource(R.drawable.circle_unverified);
                description.setText(R.string.description_register_button_fourth);
                break;
        }
    }

    public void clean(){
        countTimes = 0;
    }

    public void insertControlDB(){
       dbHelper.insertControl(this.name, getDate(), this.idControl, this.listIdButtons);
    }

    /**
     * Get the current date
     * @return Return a string that is the current date
     */
    public String getDate(){
        return new SimpleDateFormat("dd-MM-yyyy").format(new Date());
    }

}
