package com.chi.heyfriendv21.activity;


import android.app.Dialog;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.chi.heyfriendv21.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import common.Constant;
import dialog.DateDialog;
import object.User;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView tvName, tvGender, tvBirthday;
    private Button btnEdit1, btnEdit2, btnEdit3, btnSave;
    private EditText edtName;
    private AlertDialog dialogName, dialogGender;
    private DateDialog dialogBirthday;

    private User user;
    private DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        user = (User) getUserFromIntent().getSerializableExtra(Constant.USER);
        databaseReference = FirebaseDatabase.getInstance().getReference();

        tvName = (TextView) findViewById(R.id.tv_name);
        tvGender = (TextView) findViewById(R.id.tv_gender);
        tvBirthday = (TextView) findViewById(R.id.tv_birthday);
        btnEdit1 = (Button) findViewById(R.id.btn_edit_info1);
        btnEdit2 = (Button) findViewById(R.id.btn_edit_info2);
        btnEdit3 = (Button) findViewById(R.id.btn_edit_info3);
        btnSave = (Button) findViewById(R.id.btn_save_info);
        edtName = new EditText(this);

        btnEdit1.setOnClickListener(this);
        btnEdit2.setOnClickListener(this);
        btnEdit3.setOnClickListener(this);
        btnSave.setOnClickListener(this);

//        getUserInfo();

        dialogName = new AlertDialog.Builder(this).create();
        dialogName.setTitle(getString(R.string.txt_name));
        dialogName.setView(edtName);
        dialogName.setButton(DialogInterface.BUTTON_POSITIVE, getString(R.string.txt_save_info), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                tvName.setText(edtName.getText());
//                user.setName(edtName.getText().toString());
                hideSoftKeyboard(edtName);
            }
        });
        dialogName.setButton(DialogInterface.BUTTON_NEUTRAL, getString(R.string.txt_cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                hideSoftKeyboard(edtName);
                dialog.dismiss();

            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final CharSequence[] items = {getString(R.string.txt_male), getString(R.string.txt_female)};
        builder.setTitle(getString(R.string.txt_gender_selection));
        builder.setSingleChoiceItems(items, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
//                        user.setGender(Constant.MALE);
                        tvGender.setText(getString(R.string.txt_male));
                        break;
                    case 1:
//                        user.setGender(Constant.FEMALE);
                        tvGender.setText(getString(R.string.txt_female));
                        break;

                    default:
                        break;
                }
                dialogGender.dismiss();
            }
        });

        dialogGender = builder.create();

        dialogBirthday = new DateDialog(tvBirthday);

    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.btn_edit_info1:
                edtName.setText(tvName.getText());
                dialogName.show();
                edtName.requestFocus();
                showSoftKeyboard();
                break;

            case R.id.btn_edit_info2:
                dialogGender.show();
                break;

            case R.id.btn_edit_info3:
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                dialogBirthday.show(ft, "Date Picker");
                break;

            case R.id.btn_save_info:

                break;

            default:
                break;
        }
    }

    private void hideSoftKeyboard(EditText edt) {
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(edt.getWindowToken(), 0);
    }

    private void showSoftKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }

    private Intent getUserFromIntent(){
        Intent intent;
        return intent = getIntent();
    }

    private void getUserInfo() {
        tvName.setText(user.getName());
        tvGender.setText(user.getStringGender(this));
        tvBirthday.setText(user.getDateOfBirth());
    }
}
