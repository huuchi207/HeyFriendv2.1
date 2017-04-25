package com.chi.heyfriendv21.activity;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.chi.heyfriendv21.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import common.CommonMethod;
import common.Constant;
import dialog.DateDialog;
import object.User;

public class EditInforActivity extends AppCompatActivity implements View.OnClickListener {


    private RelativeLayout myLayout = null;
    private EditText etDate = null;
    private EditText etName = null;
    DatabaseReference databaseReference;
    Button btUpdate;
    RadioButton rbMale, rbFemale;
    User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_information);
        myLayout = (RelativeLayout)findViewById(R.id.layout);
        etDate = (EditText)findViewById(R.id.editDate);
        etName = (EditText)findViewById(R.id.editName);

        btUpdate = (Button) findViewById(R.id.btUpdate);
        btUpdate.setOnClickListener(this);
        rbFemale = (RadioButton) findViewById(R.id.rbFemale);
        rbMale = (RadioButton) findViewById(R.id.rbMale);
        getDataIntent();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        if (user.getName()!= null)
            etName.setText(user.getName());
        myLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
//                Toast.makeText(getApplicationContext(),"Touched", Toast.LENGTH_SHORT).show();
                etDate.setFocusable(false);
                etName.setFocusable(false);
                etName.setFocusableInTouchMode(true);
                hideSoftKeyboard(EditInforActivity.this);
                etDate.setFocusableInTouchMode(true);
                return false;
            }
        });

        myLayout.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                etDate.setFocusable(false);
                etDate.setFocusableInTouchMode(true);
            }
        });
        etName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                InputMethodManager keyboard = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                keyboard.showSoftInput(etName, 0);
                etName.setFocusableInTouchMode(true);
                etName.setFocusable(true);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        etDate = (EditText)findViewById(R.id.editDate);
        etDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    DateDialog dialog = new DateDialog(v);
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    dialog.show(ft, "DatePicker");
                    hideSoftKeyboard(EditInforActivity.this);
                }
            }
        });
    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                activity.getCurrentFocus().getWindowToken(), 0);
    }
    void getDataIntent(){
        Intent intent = getIntent();
        user = (User)intent.getSerializableExtra(Constant.USER);
    }
    @Override
    public void onClick(View view) {
        CommonMethod.showAnimation(view, EditInforActivity.this );
        if (view== btUpdate){
            //TODO: push data to firebase

            String uid ="";
            final String name =etName.getText().toString().trim();
            String dateOfBirth= etDate.getText().toString().trim();
            int gender= rbFemale.isChecked() ? Constant.FEMALE : Constant.MALE;
            if (name.equals("")){
                Toast.makeText(this, R.string.announce_fill_in_the_blank, Toast.LENGTH_SHORT).show();
                return;
            }
            if (isValidDate(dateOfBirth)){
                user.setDateOfBirth(dateOfBirth);
            }
            else {
                Toast.makeText(this, R.string.announce_invalid_date_of_birth, Toast.LENGTH_SHORT).show();
                return;
            }
            user.setName(name);
            if (user.getUid()!=null){
                uid = user.getUid();
            }
            user.setGender(gender);

            databaseReference.child(Constant.CHILD_USERS).child(uid).setValue(user, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                    if(databaseError!= null){
                        Toast.makeText(EditInforActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    else{
                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                .setDisplayName(name)
                                .build();

                        if (CommonMethod.getCurrentFirebaseUser()!=null)
                            CommonMethod.getCurrentFirebaseUser().updateProfile(profileUpdates)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            if (EditInforActivity.this!= null){
                                                Toast.makeText(EditInforActivity.this, getString(R.string.announce_welcome)+" " + name, Toast.LENGTH_SHORT).show();
                                            }

                                            Intent mainActivity= new Intent(EditInforActivity.this, MainActivity.class);
                                            startActivity(mainActivity);
                                            finish();
                                        }
                                    }
                                });

                    }
                }
            });

        }
    }
    boolean isValidDate(String dateString){
        Date date = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            date = sdf.parse(dateString);
            if (!dateString.equals(sdf.format(date))) {
                date = null;
            }
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
        if (date == null) {
            // Invalid date format
            return true;
        } else {
            // Valid date format
            return false;
        }
    }
}
