package net.signagewidgets.serial.view;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.signagewidgets.serial.R;
import net.signagewidgets.serial.util.Logging;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lenoirzamboni on 8/27/15.
 */
public class VerifyButtons extends LinearLayout{

    Logging logging = new Logging(VerifyButtons.class);

    private AlertDialog.Builder alertDialogBuilder;
    private AlertDialog alertDialog;
    private LayoutInflater li;
    private Context context;
    private int numberButtons;
    private int countTimes = 0;
    private String name;
    private TextView restart;
    private TextView nextButton;
    private TextView cancel;
    private TextView click01;
    private TextView click02;
    private TextView click03;
    private Long idControl;
    private List<Long> listIdButtons;
    private TextView description;
    private int countClicks = 0;
    private BroadcastReceiver receiver;

    public VerifyButtons(Context context, String name, int numberButtons) {
        super(context);

        IntentFilter filter = new IntentFilter("net.signagewidgets.serial.BUTTON");

        context.registerReceiver(receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Long idControl = intent.getExtras().getLong("id");
                Long idButton = intent.getExtras().getLong("button");

                addButton(idControl, idButton);

                logging.error("Receiving");
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
        nextButton = (TextView) alertDialog.findViewById(R.id.textView_next_button);

        click01 = (TextView) alertDialog.findViewById(R.id.click_1);
        click02 = (TextView) alertDialog.findViewById(R.id.click_2);
        click03 = (TextView) alertDialog.findViewById(R.id.click_3);

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
    }

    public void cancel(){
        cancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                clean();
                numberButtons = 0;

                if (receiver != null) {
                    context.unregisterReceiver(receiver);
                    receiver = null;
                }

                dismissPopup();
            }
        });
    }

    public void restart(){
        restart.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                dismissPopup();
                AddControl addControl = new AddControl(context);
            }
        });
    }

    public void addButton(Long idControl, Long idButton){

        logging.error("ID Control" , idControl);
        logging.error("Calling method addButton + countClicks" , countClicks);


        if(countClicks == 0){
            this.idControl = idControl;
        }

        countClicks++;

        if(this.idControl.equals(idControl)){

            switch (countClicks){
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

                    enableMessageNextButton();
                    break;

                case 4:
                    if(countTimes == numberButtons){
                        AddedControl addedControl = new AddedControl(context);
                        dismissPopup();
                    }
                    confirm();
                    break;
            }
        }
    }

    public void confirm() {
        setTextClicks(countTimes + 1);
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
                disableNextButton();
                break;
            case 2:
                click01.setText("B");
                click02.setText("B");
                click03.setText("B");
                click01.setBackgroundResource(R.drawable.circle_unverified);
                click02.setBackgroundResource(R.drawable.circle_unverified);
                click03.setBackgroundResource(R.drawable.circle_unverified);
                description.setText(R.string.description_register_button_second);
                disableNextButton();
                break;
            case 3:
                click01.setText("C");
                click02.setText("C");
                click03.setText("C");
                click01.setBackgroundResource(R.drawable.circle_unverified);
                click02.setBackgroundResource(R.drawable.circle_unverified);
                click03.setBackgroundResource(R.drawable.circle_unverified);
                description.setText(R.string.description_register_button_third);
                disableNextButton();
                break;
            case 4:
                click01.setText("D");
                click02.setText("D");
                click03.setText("D");
                click01.setBackgroundResource(R.drawable.circle_unverified);
                click02.setBackgroundResource(R.drawable.circle_unverified);
                click03.setBackgroundResource(R.drawable.circle_unverified);
                description.setText(R.string.description_register_button_fourth);
                disableNextButton();
                break;
        }
    }

    public void enableMessageNextButton(){
        nextButton.setVisibility(VISIBLE);
    }

    public void disableNextButton(){
        nextButton.setVisibility(INVISIBLE);
    }

    public void clean(){
        countTimes = 0;
        countClicks = 0;
    }
}
