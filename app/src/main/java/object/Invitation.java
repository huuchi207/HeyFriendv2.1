package object;

/**
 * Created by huuchi207 on 12/10/2016.
 */

public class Invitation {
    private String invitationMessage;
    private long time;
    private Friend sender;

    public Friend getSender() {
        return sender;
    }

    public void setSender(Friend sender) {
        this.sender = sender;
    }



    public Invitation(String invitationMessage, long time, Friend sender) {
        this.invitationMessage = invitationMessage;
        this.time = time;
        this.sender = sender;


    }


    public Invitation() {
    }


    public String getInvitationMessage() {
        return invitationMessage;
    }

    public void setInvitationMessage(String invitationMessage) {
        this.invitationMessage = invitationMessage;

    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
