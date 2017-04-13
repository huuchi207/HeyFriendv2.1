package object;



import java.io.Serializable;

import common.CommonMethod;

/**
 * Created by root on 20/10/2016.
 */

public class LastMessage implements Serializable {
    private String content;
    private long time;
    private boolean status;
    private String senderUid;
    public LastMessage(){

    }
    //default
    public LastMessage(String content, long time, boolean status, String senderUid) {
        this.content = content;
        this.time = time;
        this.status = status;
        this.senderUid = senderUid;
    }
    public LastMessage(String content, String senderUid) {
        this.content = content;
        this.time = CommonMethod.getCurrentTime();
        this.status = false;
        this.senderUid = senderUid;
    }

    public LastMessage(String content, String senderUid, boolean status) {
        this.content = content;
        this.senderUid = senderUid;
        this.status = status;
        this.time= CommonMethod.getCurrentTime();
    }


    public String getSenderUid() {
        return senderUid;
    }

    public void setSenderUid(String senderUid) {
        this.senderUid = senderUid;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }



}