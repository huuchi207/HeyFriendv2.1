package object;

import java.io.Serializable;
import java.util.ArrayList;



public class GroupChatData implements Comparable<GroupChatData>, Serializable {
    private LastMessage lastMessage;
    private String uid;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    private String name;
    private ArrayList<Participant> participants;

    public ArrayList<Participant> getParticipants() {
        return participants;
    }

    public void setParticipants(ArrayList<Participant> participants) {
        this.participants = participants;
    }

    public GroupChatData() {
    }

    public GroupChatData(LastMessage lastMessage, String name, ArrayList<Participant> participants) {
        this.lastMessage = lastMessage;
        this.name = name;
        this.participants = participants;
    }

    public LastMessage getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(LastMessage lastMessage) {
        this.lastMessage = lastMessage;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public boolean isAvailable(){
        return (name!=null && lastMessage!= null && uid!=null);
    }


    @Override
    public int compareTo(GroupChatData another) {
        if (this.getLastMessage()== null || another.getLastMessage()== null)
            return 0;
        if (this.getLastMessage().getTime()> another.getLastMessage().getTime()){
            return -1;
        }
        if (this.getLastMessage().getTime()< another.getLastMessage().getTime()){
            return 1;
        }
        return 0;
    }

    public static class Participant implements Serializable {
        String uid;
        String photoUrl;
        String name;
        boolean status;
        public Participant() {
        }


        public Participant(String uid, String photoUrl, String name, boolean status) {
            this.uid = uid;
            this.photoUrl = photoUrl;
            this.name = name;
            this.status = status;
        }

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public boolean isStatus() {
            return status;
        }

        public void setStatus(boolean status) {
            this.status = status;
        }

        public String getName() {

            return name;
        }

        public String getPhotoUrl() {
            return photoUrl;
        }

        public void setPhotoUrl(String photoUrl) {
            this.photoUrl = photoUrl;
        }

        public void setName(String name) {
            this.name = name;

        }


    }
}
