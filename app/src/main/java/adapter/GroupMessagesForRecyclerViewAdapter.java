package adapter;

import android.app.ActionBar;
import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;

import com.chi.heyfriendv21.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;


import java.util.HashMap;
import java.util.Map;

import common.CommonMethod;
import common.Constant;
import common.MessageViewHolder;
import dialog.ImageDialogFragment;
import object.GroupChatData;
import object.Message;



public class GroupMessagesForRecyclerViewAdapter extends FirebaseRecyclerAdapter<Message,MessageViewHolder> {

    private Activity activity;
    private int position;
    private String myUid;
    private DatabaseReference ref;
    private GroupChatData groupChatData;
    private Map<String, String> mapPhotoUrl;
    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public GroupMessagesForRecyclerViewAdapter(Class<Message> modelClass, int modelLayout,
                                               Class<MessageViewHolder> viewHolderClass, DatabaseReference ref,Activity activity, GroupChatData groupChatData, String myUid) {
        super(modelClass, modelLayout, viewHolderClass, ref);
        this.ref = ref;
        this.activity = activity;
        this.myUid = myUid;
        this.groupChatData = groupChatData;
        mapPhotoUrl = new HashMap<>();
        for (GroupChatData.Participant participant : groupChatData.getParticipants()){
            String uid= participant.getUid();
            String photoUrl= participant.getPhotoUrl();
            mapPhotoUrl.put(uid, photoUrl);
        }
    }

    @Override
    protected void populateViewHolder(MessageViewHolder viewHolder, final Message message, final int position) {
        //dont reuse view
        viewHolder.setIsRecyclable(false);
        //get senderUid to present data to recyclerview
        String senderUid= message.getSenderUid();
        // update status of lastmessage
        if (position==getItemCount()-1){
            //TODO: update state of last message(useless)
            ref.getRoot().child(Constant.CHILD_GROUPCHAT).child(groupChatData.getUid())
                    .child(Constant.CHILD_USERS).child(myUid).child(Constant.KEY_STATUS).setValue(true);
            NotificationManager notificationManager = (NotificationManager)activity.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.cancel(groupChatData.getUid().hashCode());
        }

        //show msg
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(RecyclerView.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT);
        lp.leftMargin= 10;
        lp.rightMargin= 10;

        if (position>0){
            long diffTime =( message.getTime()- getItem(position-1).getTime());
            if (diffTime<600000){
                viewHolder.tvDateSending.setVisibility(View.GONE);
            }
            else viewHolder.tvDateSending.setText(CommonMethod.convertLongToTime(message.getTime()));
//                    Log.e(message.getTime()+ "--"+ CommonMethod.convertLongToTime(message.getTime())+ "--" +CommonMethod.convertLongToTime(getItem(position-1).getTime())+"//"+ getItem(position-1).getTime(), diffTime+"");
        }
        else viewHolder.tvDateSending.setText(CommonMethod.convertLongToTime(message.getTime()));

        if (message.getMessage().equals("Image")) {
            viewHolder.tvMessage.setVisibility(View.GONE);
        } else viewHolder.tvMessage.setText(message.getMessage());

        if (!message.getPhotoUrl().equals("")) {
            Glide.with(activity).load(message.getPhotoUrl()).into(viewHolder.ivPhoto);
        } else viewHolder.ivPhoto.setVisibility(View.GONE);

        if (!senderUid.equals(myUid)){
            lp.gravity= Gravity.LEFT;
            String clientPhotoURL= mapPhotoUrl.get(senderUid);

            viewHolder.linearLayout.setLayoutParams(lp);
            if (position>0 && !getItem(position-1).getSenderUid().equals(myUid)){
                viewHolder.ivMessenger.setImageResource(android.R.color.transparent);
                long diffTime =( message.getTime()- getItem(position-1).getTime());
                if (diffTime < 600000) {
//                    viewHolder.tvMessage.setBackgroundResource(R.drawable.receive_second);
                    viewHolder.llContentMsg.setBackgroundResource(R.drawable.receive_second);
                } else {
                    viewHolder.llContentMsg.setBackgroundResource(R.drawable.receive_first);
                }
            }
            else {
                viewHolder.tvMessage.setBackgroundResource(R.drawable.receive_first);
                Glide.with(activity).load(clientPhotoURL).into(viewHolder.ivMessenger);
            }

//                viewHolder.tvDateSending.setText(clientName);

            viewHolder.tvMessage.setTextColor(Color.WHITE);
            viewHolder.tvMessage.setLayoutParams(lp);
        }
        else {
            lp.gravity= Gravity.RIGHT;
            viewHolder.linearLayout.setLayoutParams(lp);
//                    viewHolder.linearLayout.setGravity(Gravity.RIGHT);
            viewHolder.ivMessenger.setImageResource(android.R.color.transparent);
            if (position>0 && getItem(position-1).getSenderUid().equals(myUid)){
                long diffTime =( message.getTime()- getItem(position-1).getTime());
                if (diffTime < 600000) {
//                    viewHolder.tvMessage.setBackgroundResource(R.drawable.sending_second);
                    viewHolder.llContentMsg.setBackgroundResource(R.drawable.sending_second);
                } else{
//                    viewHolder.tvMessage.setBackgroundResource(R.drawable.sending_first);
                    viewHolder.llContentMsg.setBackgroundResource(R.drawable.sending_first);
                }
            }
            else viewHolder.tvMessage.setBackgroundResource(R.drawable.sending_first);
// viewHolder.tvMessage.setBackgroundResource(R.drawable.bubble_out);
            viewHolder.tvMessage.setLayoutParams(lp);

        }
        //for context menu
        viewHolder.tvMessage.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                setPosition(position);
                return false;
            }

        });
        viewHolder.llAllComponent.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                setPosition(position);
                return false;
            }

        });
        viewHolder.ivPhoto.setOnLongClickListener(new View.OnLongClickListener(){
            @Override
            public boolean onLongClick(View view) {
                setPosition(position);
                return false;
            }
        });
        viewHolder.ivPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImageDialogFragment dialog = new ImageDialogFragment(message.getPhotoUrl());
                dialog.show(activity.getFragmentManager(), "");
            }
        });
    }

}
