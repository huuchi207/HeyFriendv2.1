package object;


import common.CommonMethod;


public class Message {
    private String message;
    private long time;
    private String senderUid;
    private String photoUrl;

    //for push message
    public Message(String message, String senderUid){
        this.message = message;
        this.time= CommonMethod.getCurrentTime();
        this.senderUid = senderUid;
        this.photoUrl ="";
    }
    //for push msg with photo

    public Message(String message, String senderUid, String photoUrl) {
        this.message = message;
        this.time= CommonMethod.getCurrentTime();
        this.senderUid = senderUid;
        this.photoUrl = photoUrl;
    }

    //for use message


    public String getSenderUid() {
        return senderUid;
    }

    public void setSenderUid(String senderUid) {
        this.senderUid = senderUid;
    }

    //default contructor

    public String getPhotoUrl() {
        if (photoUrl!= null)
        return photoUrl;
        else return "";
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public Message(){

    }
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return message + "-------" + time+ "-----"+ senderUid;
    }
}
