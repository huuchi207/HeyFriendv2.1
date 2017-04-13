package object;


import common.CommonMethod;

/**
 * Created by huuchi207 on 12/10/2016.
 */

public class Message {
    private String message;
    private long time;
    private String senderUid;
    //for push message
    public Message(String message, String senderUid){
        this.message = message;
        this.time= CommonMethod.getCurrentTime();
        this.senderUid = senderUid;
    }
    //for use message


    public String getSenderUid() {
        return senderUid;
    }

    public void setSenderUid(String senderUid) {
        this.senderUid = senderUid;
    }

    //default contructor
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
