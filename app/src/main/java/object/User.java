package object;

import android.content.Context;


import com.chi.heyfriendv21.R;

import java.io.Serializable;

import common.Constant;

/**
 * Created by huuchi207 on 12/10/2016.
 */

public class User implements Serializable {
    private String uid;
    private String email;
    private String name;
    private String photoURL;
    private long lastOnline;
    private boolean connection;
    private String dateOfBirth;

    private int gender;


    public int getGender() {
        return gender;
    }
    public String getStringGender(Context context){
        return gender== Constant.MALE ? context.getString(R.string.txt_male) :
                context.getString(R.string.txt_female);
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    //for use user
    public User(String uid, String email, String name, String photoURL, long lastOnline, boolean connection) {
        this.uid = uid;
        this.email = email;

        this.name = name;
        this.photoURL = photoURL;
        this.lastOnline = lastOnline;
        this.connection = connection;
    }
    //for push user
    public User(String uid, String email, String name, String photoURL, boolean connection) {
        this.uid = uid;
        this.email = email;
        this.name = name;
        this.photoURL = photoURL;
        this.lastOnline = System.currentTimeMillis();
        this.connection = connection;
    }
    //default contructor

    public User(String uid, String email, String name, String photoURL, long lastOnline,
                boolean connection, String dateOfBirth, int gender) {
        this.uid = uid;
        this.email = email;
        this.name = name;
        this.photoURL = photoURL;
        this.lastOnline = lastOnline;
        this.connection = connection;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
    }

    public User(){

    }
    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isConnection() {
        return connection;
    }

    public void setConnection(boolean connection) {
        this.connection = connection;
    }

    public String getPhotoURL() {
        return photoURL;
    }

    public long getLastOnline() {
        return lastOnline;
    }

    public void setLastOnline(long lastOnline) {
        this.lastOnline = lastOnline;
    }

    public void setPhotoURL(String photoURL) {
        this.photoURL = photoURL;
    }

}
