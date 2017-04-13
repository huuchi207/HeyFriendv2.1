package object;

import java.io.Serializable;

/**
 * Created by root on 12/11/2016.
 */

public  class Tag implements Serializable {
    String uid;
    String name;
    String photoUrl;

    public Tag(String name) {
        this.name= name;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Tag(String uid, String name, String photoUrl) {
        this.uid = uid;
        this.name = name;
        this.photoUrl = photoUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }
    public String toString(){
        return name;
    }
}