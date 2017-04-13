package object;



/**
 * Created by huuchi207 on 17/10/2016.
 */

public class Friend implements Comparable<Friend> {
    private String uid;
    private String photoURL;
    private String name;
    private LastMessage lastMessage;
    private Boolean connection;
    public Friend() {

    }

    public Boolean getConnection() {
        return connection;
    }

    public void setConnection(Boolean connection) {
        this.connection = connection;
    }

    public String getUid() {
        return uid;
    }

    public Friend(String uid, String photoURL, String name, LastMessage lastMessage, Boolean connection) {
        this.uid = uid;
        this.photoURL = photoURL;
        this.name = name;
        this.lastMessage = lastMessage;
        this.connection = connection;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }


    public LastMessage getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(LastMessage lastMessage) {
        this.lastMessage = lastMessage;
    }

    public String getPhotoURL() {
        if(photoURL!= null)
            return photoURL;
        else return "null";
    }

    public void setPhotoURL(String photoURL) {
        this.photoURL = photoURL;
    }

    public String getName() {
        if(name!= null)
            return name;
        else return "null";
    }

    public void setName(String name) {
        this.name = name;
    }


    @Override
    public int compareTo(Friend o) {
        if (this.getLastMessage().getTime()> o.getLastMessage().getTime()){
            return -1;
        }
        if (this.getLastMessage().getTime()< o.getLastMessage().getTime()){
            return 1;
        }
        return 0;
    }
}
