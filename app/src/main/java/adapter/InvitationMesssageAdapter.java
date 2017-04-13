package adapter;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.chi.heyfriendv21.R;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import java.util.ArrayList;

import common.CommonMethod;
import common.Constant;
import de.hdodenhof.circleimageview.CircleImageView;
import object.Friend;
import object.Invitation;
import object.LastMessage;


/**
 * Created by root on 01/11/2016.
 */

public class InvitationMesssageAdapter extends BaseAdapter {
    private Activity context;
    private int layout;
    ArrayList<Invitation> data;

    private DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference();
    private FirebaseUser currentFirebaseUser = CommonMethod.getCurrentFirebaseUser();
    public InvitationMesssageAdapter(Activity context, int layout, ArrayList<Invitation> data) {
        this.context = context;
        this.layout = layout;
        this.data = data;

    }

    public ArrayList<Invitation> getData() {
        return data;
    }

    public void setData(ArrayList<Invitation> data) {
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final viewHolder viewHolder;
        if (convertView == null) {
            convertView = context.getLayoutInflater().inflate(layout, null);
            viewHolder = new viewHolder(convertView);
            convertView.setTag(viewHolder);
        } else viewHolder = (viewHolder) convertView.getTag();

        //put data
        final Invitation invitationMessage= data.get(position);
        viewHolder.tvInvitationMessage.setText(invitationMessage.getInvitationMessage());
        viewHolder.tvName.setText(invitationMessage.getSender().getName());
        Glide.with(context).load(invitationMessage.getSender().getPhotoURL()).into(viewHolder.civAvatar);
        viewHolder.btRefuse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(Constant.buttonClick);
                if(currentFirebaseUser!=null){
                    databaseReference.child(Constant.CHILD_INVITATIONS).child(currentFirebaseUser.getUid()).
                            child(invitationMessage.getSender().getUid()).removeValue( new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                            if(databaseError!= null){
                                Log.e("completion error", databaseError.getDetails());
                                Toast.makeText(context, R.string.announce_cant_delete_invitation_message, Toast.LENGTH_SHORT).show();
                            }
                            else{
//                            Toast.makeText(context, R.string.announce_invitation_msg_is_deleted, Toast.LENGTH_SHORT).show();
                                if (data.size()>position)
                                    data.remove(position);
                                notifyDataSetChanged();
                            }
                        }
                    });
                }


            }
        });
        viewHolder.btApprove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(Constant.buttonClick);

                final String senderUid= invitationMessage.getSender().getUid();
                String receiverUid= "";
                Friend receiver= new Friend("", "","", new LastMessage("",""),false);
                if (currentFirebaseUser!=null)
                    receiverUid= currentFirebaseUser.getUid();
                final String finalReceiverUid= receiverUid;
                if (currentFirebaseUser!=null){
                    receiver= new Friend(currentFirebaseUser.getUid(),
                            currentFirebaseUser.getPhotoUrl().toString(),
                            currentFirebaseUser.getDisplayName(),
                            null, false);
                }
                final Friend finalReceiver= receiver;
                //put data into receiverUid of CHILD_FRIENDLISTS
                databaseReference.child(Constant.CHILD_FRIENDLISTS).
                        child(finalReceiverUid).child(senderUid).
                        setValue(invitationMessage.getSender(), new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                        if(databaseError!= null){
                            Log.e("completion error", databaseError.getDetails());
                            Toast.makeText(context, R.string.announce_cant_delete_invitation_message, Toast.LENGTH_SHORT).show();
                        }
                        else{
                            //put data into senderUid of CHILD_FRIENDLISTS
                            databaseReference.getRoot().child(Constant.CHILD_FRIENDLISTS).
                                    child(senderUid).child(finalReceiverUid).
                                    setValue(finalReceiver, new DatabaseReference.CompletionListener() {
                                        @Override
                                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                            if(databaseError!= null){
                                                Log.e("completion error", databaseError.getDetails());
                                                Toast.makeText(context, R.string.announce_cant_delete_invitation_message, Toast.LENGTH_SHORT).show();
                                            }
                                            else{
                                                //put uid to CHILD_USERS
                                                databaseReference.getRoot().child(Constant.CHILD_USERS).
                                                        child(senderUid).child(Constant.KEY_FRIENDS).child(finalReceiverUid).
                                                        setValue(true, new DatabaseReference.CompletionListener() {
                                                            @Override
                                                            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                                                if(databaseError!= null){
                                                                    Log.e("completion error", databaseError.getDetails());
                                                                    Toast.makeText(context, R.string.announce_cant_delete_invitation_message, Toast.LENGTH_SHORT).show();
                                                                }
                                                                else{
                                                                    //put uid to CHILD_USERS
                                                                    databaseReference.getRoot().child(Constant.CHILD_USERS).
                                                                            child(finalReceiverUid).child(Constant.KEY_FRIENDS)
                                                                            .child(senderUid).
                                                                            setValue(true, new DatabaseReference.CompletionListener() {
                                                                                @Override
                                                                                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                                                                    if(databaseError!= null){
                                                                                        Log.e("completion error", databaseError.getDetails());
                                                                                        Toast.makeText(context, R.string.announce_cant_delete_invitation_message, Toast.LENGTH_SHORT).show();
                                                                                    }
                                                                                    else{
                                                                                        Toast.makeText(context, R.string.announce_add_friend_successfully, Toast.LENGTH_SHORT).show();
                                                                                        viewHolder.btRefuse.performClick();
                                                                                    }
                                                                                }
                                                                            });
                                                                }
                                                            }
                                                        });
                                            }
                                        }
                                    });
                        }
                    }
                });




            }
        });
        return convertView;
    }

    static class viewHolder {
        View convertView;
        CircleImageView civAvatar;
        TextView tvName, tvInvitationMessage;
        Button btApprove, btRefuse;
        public viewHolder(View convertView) {
            this.convertView = convertView;
            civAvatar = (CircleImageView) convertView.findViewById(R.id.ivAvatar);
            tvName= (TextView) convertView.findViewById(R.id.tvSenderName);
            tvInvitationMessage = (TextView) convertView.findViewById(R.id.tvLastMessage);
            btApprove= (Button) convertView.findViewById(R.id.btApprove);
            btRefuse = (Button) convertView.findViewById(R.id.btRefuse);
        }
    }
}
