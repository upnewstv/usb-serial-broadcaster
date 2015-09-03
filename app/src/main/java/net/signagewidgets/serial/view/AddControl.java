package net.signagewidgets.serial.view;

import android.content.Context;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import net.signagewidgets.serial.R;
import net.signagewidgets.serial.util.Logging;

/**
 * Created by lenoirzamboni on 8/27/15.
 */
public class AddControl extends LinearLayout {

    private static Logging sLogging = new Logging(AddControl.class);

    private AlertDialog.Builder alertDialogBuilder;
    private AlertDialog alertDialog;
    private LayoutInflater li;
    private Context context;

    private RadioGroup radioGroupButtons;
    private RadioButton radioButton_1;
    private RadioButton radioButton_2;
    private RadioButton radioButton_3;
    private RadioButton radioButton_4;

    private TextView cancel;
    private TextView next;

    private int qtdButtons = 2;
    private String name;
    private EditText nameControl;

    public AddControl(Context context) {
        super(context);

        li = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context = context;

        createDialog();

        radioGroupButtons = (RadioGroup) alertDialog.findViewById(R.id.radioGroup);

        radioButton_1 = (RadioButton) alertDialog.findViewById(R.id.radioButton_1);
        radioButton_2 = (RadioButton) alertDialog.findViewById(R.id.radioButton_2);
        radioButton_3 = (RadioButton) alertDialog.findViewById(R.id.radioButton_3);
        radioButton_4 = (RadioButton) alertDialog.findViewById(R.id.radioButton_4);

        cancel = (TextView) alertDialog.findViewById(R.id.textView_cancel_add);
        next = (TextView) alertDialog.findViewById(R.id.textView_next);

        nameControl = (EditText) alertDialog.findViewById(R.id.EditText_name_control);

        setNumberButtons();
        next();
        cancel();
        setBGRadioButtons();

        alertDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
    }

    public void createDialog(){

        alertDialogBuilder = new AlertDialog.Builder(context);

        // create alert dialog
        alertDialog = alertDialogBuilder.create();

        alertDialog.getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        alertDialogBuilder.setView(li.inflate(R.layout.add_control, null));


        // show it
        alertDialog.show();

        alertDialog.setContentView(R.layout.add_control);
    }

    public void dismissPopup(){
        alertDialog.dismiss();
    }

    public void setNumberButtons() {

        radioGroupButtons.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {

                // find which radio button is selected
                if (checkedId == radioButton_1.getId()) {
                    qtdButtons = 1;
                } else if (checkedId == radioButton_2.getId()) {
                    qtdButtons = 2;
                } else if (checkedId == radioButton_3.getId()) {
                    qtdButtons = 3;
                } else {
                    qtdButtons = 4;
                }
            }
        });
    }

    public void next(){
        next.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                setNameControl();

                if(name.isEmpty()){
                    Toast.makeText(context, "Por favor, digite um nome", Toast.LENGTH_SHORT).show();
                }else{
                    dismissPopup();
                    VerifyButtons verifyButtons = new VerifyButtons(context, name, qtdButtons);
                }

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

    public void setBGRadioButtons(){
        if (Build.VERSION.SDK_INT < 21){
            radioButton_1.setBackgroundResource(R.drawable.radio_button_layout);
            radioButton_2.setBackgroundResource(R.drawable.radio_button_layout);
            radioButton_3.setBackgroundResource(R.drawable.radio_button_layout);
            radioButton_4.setBackgroundResource(R.drawable.radio_button_layout);
        }
    }

    public void setNameControl(){
        name = nameControl.getText().toString();
    }
}
