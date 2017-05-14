package com.chi.heyfriendv21.activity;


import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.chi.heyfriendv21.R;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView tvNumber1;
    private Button btnEdit1;
    private EditText edt;
    private AlertDialog alertDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        tvNumber1 = (TextView) findViewById(R.id.tvNumber1);
        tvNumber1.setSelected(true);
        btnEdit1 = (Button) findViewById(R.id.btn_edit_info1);
        edt = new EditText(this);
        alertDialog = new AlertDialog.Builder(this).create();

//        edt.setFocusable(true);
//        edt.setFocusableInTouchMode(true);
//        edt.requestFocus();

        alertDialog.setTitle("Chỉnh sửa");
        alertDialog.setView(edt);
        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Lưu", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                tvNumber1.setText(edt.getText());
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(edt.getWindowToken(), 0);
            }
        });

        alertDialog.setButton(DialogInterface.BUTTON_NEUTRAL, "Hủy", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(edt.getWindowToken(), 0);
                dialog.dismiss();

            }
        });
;
        btnEdit1.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.btn_edit_info1:
                edt.setText(tvNumber1.getText());
                alertDialog.show();
                edt.requestFocus();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                break;

            default:
                break;
        }
    }
}
