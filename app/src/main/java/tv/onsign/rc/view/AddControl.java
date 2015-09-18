package tv.onsign.rc.view;

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

import tv.onsign.rc.R;

/**
 * Created by lenoirzamboni on 8/27/15.
 */
public class AddControl extends LinearLayout {
    
    protected AlertDialog.Builder mAlertDialogBuilder;
    protected AlertDialog mAlertDialog;
    protected LayoutInflater mLayoutInflater;
    protected Context mContext;

    protected RadioGroup mRadioGroupButtons;
    protected RadioButton mRadioButton_1;
    protected RadioButton mRadioButton_2;
    protected RadioButton mRadioButton_3;
    protected RadioButton mRadioButton_4;

    protected TextView mCancel;
    protected TextView mNext;

    protected int mNumberButtons = 2;
    protected String mName;
    protected EditText mNameControl;

    public AddControl(Context context) {
        super(context);

        mLayoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.mContext = context;

        createDialog();

        mRadioGroupButtons = (RadioGroup) mAlertDialog.findViewById(R.id.radioGroup);

        mRadioButton_1 = (RadioButton) mAlertDialog.findViewById(R.id.radioButton_1);
        mRadioButton_2 = (RadioButton) mAlertDialog.findViewById(R.id.radioButton_2);
        mRadioButton_3 = (RadioButton) mAlertDialog.findViewById(R.id.radioButton_3);
        mRadioButton_4 = (RadioButton) mAlertDialog.findViewById(R.id.radioButton_4);

        mCancel = (TextView) mAlertDialog.findViewById(R.id.textView_cancel_add);
        mNext = (TextView) mAlertDialog.findViewById(R.id.textView_next);

        mNameControl = (EditText) mAlertDialog.findViewById(R.id.EditText_name_control);

        setNumberButtons();
        next();
        cancel();
        setBGRadioButtons();

        mAlertDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
    }

    public void createDialog() {

        mAlertDialogBuilder = new AlertDialog.Builder(mContext);

        // create alert dialog
        mAlertDialog = mAlertDialogBuilder.create();

        mAlertDialog.getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        mAlertDialogBuilder.setView(mLayoutInflater.inflate(R.layout.add_control, null));


        // show it
        mAlertDialog.show();

        mAlertDialog.setContentView(R.layout.add_control);
    }

    public void dismissPopup() {
        mAlertDialog.dismiss();
    }

    public void setNumberButtons() {

        mRadioGroupButtons.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {

                // find which radio button is selected
                if (checkedId == mRadioButton_1.getId()) {
                    mNumberButtons = 1;
                } else if (checkedId == mRadioButton_2.getId()) {
                    mNumberButtons = 2;
                } else if (checkedId == mRadioButton_3.getId()) {
                    mNumberButtons = 3;
                } else {
                    mNumberButtons = 4;
                }
            }
        });
    }

    public void next() {
        mNext.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                setNameControl();

                if (mName.isEmpty()) {
                    Toast.makeText(mContext, mContext.getString(R.string.no_name_toast), Toast.LENGTH_SHORT).show();
                } else {
                    dismissPopup();
                    VerifyButtons verifyButtons = new VerifyButtons(mContext, mName, mNumberButtons);
                }
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

    public void setBGRadioButtons() {
        if (Build.VERSION.SDK_INT < 21){
            mRadioButton_1.setBackgroundResource(R.drawable.radio_button_layout);
            mRadioButton_2.setBackgroundResource(R.drawable.radio_button_layout);
            mRadioButton_3.setBackgroundResource(R.drawable.radio_button_layout);
            mRadioButton_4.setBackgroundResource(R.drawable.radio_button_layout);
        }
    }

    public void setNameControl() {
        mName = mNameControl.getText().toString();
    }
}
